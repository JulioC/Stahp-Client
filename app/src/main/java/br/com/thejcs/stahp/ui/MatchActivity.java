package br.com.thejcs.stahp.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Arrays;
import java.util.List;

import br.com.thejcs.stahp.R;
import br.com.thejcs.stahp.StahpApplication;
import br.com.thejcs.stahp.api.StahpAPI;
import br.com.thejcs.stahp.api.entity.ChallengeEntity;
import br.com.thejcs.stahp.api.entity.EntryEntity;
import br.com.thejcs.stahp.api.entity.MatchEntity;
import br.com.thejcs.stahp.ui.fragment.ChallengesFragment;
import br.com.thejcs.stahp.ui.fragment.MatchTimeFragment;
import br.com.thejcs.stahp.util.CroutonErrorListener;

public class MatchActivity extends ActionBarActivity {

    private MatchEntity match;
    private List<ChallengeEntity> challenges;
    private MatchTimeFragment matchTimeFragment;
    private ChallengesFragment challengesFragment;

    // TODO: use a better timer
    private static final int timerRunInterval = 100;
    private long startTime = 0;
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if(running) {
                updateMatchTime();
                timerHandler.postDelayed(this, timerRunInterval);
            }
        }
    };
    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        Intent intent = getIntent();
        String matchId = intent.getStringExtra("matchId");

        loadMatchDetails(matchId);

        matchTimeFragment = new MatchTimeFragment();
        challengesFragment = new ChallengesFragment();

        getFragmentManager().beginTransaction()
                .add(R.id.container_timer, matchTimeFragment)
                .add(R.id.container_challenge, challengesFragment)
                .hide(matchTimeFragment)
                .hide(challengesFragment)
                .commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    private void loadMatchDetails(String matchId) {
        Response.Listener<MatchEntity> listener = new Response.Listener<MatchEntity>() {
            @Override
            public void onResponse(MatchEntity match) {
                setMatch(match);
            }
        };

        Response.ErrorListener errorListener = new CroutonErrorListener(this);

        StahpAPI api = StahpApplication.getInstance().getAPI();
        api.getMatch(matchId, listener, errorListener);
    }

    private void setMatch(MatchEntity match) {
        this.match = match;

        loadChallenges();
    }

    private void loadChallenges() {
        Response.Listener<ChallengeEntity[]> listener = new Response.Listener<ChallengeEntity[]>() {
            @Override
            public void onResponse(ChallengeEntity[] challengeArray) {
                setChallenges(Arrays.asList(challengeArray));
            }
        };

        Response.ErrorListener errorListener = new CroutonErrorListener(this);

        StahpAPI api = StahpApplication.getInstance().getAPI();
        api.getChallenges(match.getId(), listener, errorListener);
    }

    private void setChallenges(List<ChallengeEntity> challenges) {
        this.challenges = challenges;

        prepareMatch();
    }

    private void prepareMatch() {
        matchTimeFragment.setTotalTime(match.getTimeLimit());
        challengesFragment.setChallenges(challenges);

        String dialogTitle = getString(R.string.match_ready_title);
        String dialogText = String.format(
                getString(R.string.match_ready_message),
                match.getTimeLimit());
        String dialogConfirm = getString(R.string.match_ready_yes);

        new AlertDialog.Builder(this)
                .setTitle(dialogTitle)
                .setMessage(dialogText)
                .setPositiveButton(dialogConfirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startMatch();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void startMatch() {
        getFragmentManager()
                .beginTransaction()
                .show(matchTimeFragment)
                .show(challengesFragment)
                .commit();

        challengesFragment.start();

        running = true;
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    private void endMatch() {
        getFragmentManager()
                .beginTransaction()
                .hide(matchTimeFragment)
                .hide(challengesFragment)
                .commit();

        running = false;
        timerHandler.removeCallbacks(timerRunnable);

        String dialogTitle = getString(R.string.match_ended_title);
        String dialogText = getString(R.string.match_ended_message);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(dialogTitle)
                .setMessage(dialogText)
                .setCancelable(false)
                .show();

        sendWords(dialog);
    }

    private void sendWords(final Dialog dialog) {
        Response.Listener<EntryEntity> listener = new Response.Listener<EntryEntity>() {
            @Override
            public void onResponse(EntryEntity result) {
                showResult(result);
            }
        };

        Response.ErrorListener errorListener = new CroutonErrorListener(this) {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                super.onErrorResponse(error);
            }
        };

        List<String> words = challengesFragment.getWords();

        StahpAPI api = StahpApplication.getInstance().getAPI();
        api.sendResult(match.getId(), words, listener, errorListener);
    }

    private void showResult(EntryEntity result) {
        String dialogTitle = getString(R.string.match_score_title);
        String dialogText = String.format(
                getString(R.string.match_score_message),
                result.getScore());
        String dialogOk = String.format(
                getString(R.string.match_score_ok),
                result.getScore());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(dialogTitle)
                .setMessage(dialogText)
                .setPositiveButton(dialogOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void updateMatchTime() {
        long millis = System.currentTimeMillis() - startTime;
        int seconds = (int) (millis / 1000);

        int left = match.getTimeLimit() - seconds;
        if(left <= 0) {
            endMatch();
            return;
        }

        matchTimeFragment.setTimeLeft(left);
    }


}
