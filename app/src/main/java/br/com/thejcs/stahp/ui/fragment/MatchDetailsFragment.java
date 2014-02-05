package br.com.thejcs.stahp.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import br.com.thejcs.stahp.ui.adapter.EntryArrayAdapter;
import br.com.thejcs.stahp.ui.MatchActivity;
import br.com.thejcs.stahp.R;
import br.com.thejcs.stahp.StahpApplication;
import br.com.thejcs.stahp.api.StahpAPI;
import br.com.thejcs.stahp.api.entity.MatchEntity;
import br.com.thejcs.stahp.util.CroutonErrorListener;
import br.com.thejcs.stahp.util.MatchUtils;

public class MatchDetailsFragment extends Fragment {

    enum ButtonAction {
        ACTION_START,
        ACTION_JOIN,
        ACTION_PLAY,
        ACTION_NONE
    };

    private MatchEntity match;

    private ButtonAction action = ButtonAction.ACTION_NONE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_match_details, container, false);

        Button buttonAction = (Button) rootView.findViewById(R.id.action_button);
        buttonAction.setOnClickListener(createButtonClickListener());

        return rootView;
    }

    public void setMatch(MatchEntity match) {
        this.match = match;

        TextView textId = (TextView) getView().findViewById(R.id.match_creator);
        textId.setText(String.format(
                getString(R.string.created_by),
                match.getCreator().getName()));

        String playerId = StahpApplication.getInstance().getPlayerId();
        MatchUtils.ConsolidatedStatus status =
                MatchUtils.getConsolidatedStatus(playerId, match);

        boolean showScores = false;
        String friendlyStatus = "";
        switch (status) {
            case READY_TO_START:
                friendlyStatus = getString(R.string.ready_to_start);
                action = ButtonAction.ACTION_START;
                showScores = false;
                break;
            case WAITING_START:
                friendlyStatus = getString(R.string.waiting_start);
                action = ButtonAction.ACTION_NONE;
                showScores = false;
                break;
            case OPEN_TO_JOIN:
                friendlyStatus = getString(R.string.open_to_join);
                action = ButtonAction.ACTION_JOIN;
                showScores = false;
                break;
            case PLAYER_TURN:
                friendlyStatus = getString(R.string.player_turn);
                action = ButtonAction.ACTION_PLAY;
                showScores = false;
                break;
            case STARTED:
                friendlyStatus = getString(R.string.started);
                action = ButtonAction.ACTION_NONE;
                showScores = true;
                break;
            case WAITING_FINISH:
                friendlyStatus = getString(R.string.waiting_finish);
                action = ButtonAction.ACTION_NONE;
                showScores = true;
                break;
            case FINISHED:
                friendlyStatus = getString(R.string.finished);
                action = ButtonAction.ACTION_NONE;
                showScores = true;
                break;
        }

        TextView textStatus = (TextView) getView().findViewById(R.id.match_status);
        textStatus.setText(friendlyStatus);

        ListView listPlayers = (ListView) getView().findViewById(R.id.match_players);
        listPlayers.setAdapter(new EntryArrayAdapter(
                getActivity(),
                android.R.layout.simple_list_item_1,
                match.getEntries(),
                showScores));

        updateButtonAction();
    }

    private void updateButtonAction() {
        Button buttonAction = (Button) getView().findViewById(R.id.action_button);

        switch (action) {
            case ACTION_JOIN:
                buttonAction.setText(getString(R.string.match_join));
                break;
            case ACTION_PLAY:
                buttonAction.setText(getString(R.string.match_play));
                break;
            case ACTION_START:
                buttonAction.setText(getString(R.string.match_start));
                break;
        }

        if(action == ButtonAction.ACTION_NONE) {
            buttonAction.setVisibility(View.GONE);
        }
        else {
            buttonAction.setEnabled(true);
            buttonAction.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener createButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button buttonAction = (Button) view;
                buttonAction.setEnabled(false);

                switch (action) {
                    case ACTION_JOIN:
                        joinMatch();
                        break;
                    case ACTION_START:
                        startMatch();
                        break;
                    case ACTION_PLAY:
                        playMatch();
                        break;
                }
            }
        };
    }

    private void joinMatch() {
        final Activity activity = getActivity();
        activity.setProgressBarVisibility(true);

        Response.Listener<MatchEntity> listener = new Response.Listener<MatchEntity>() {
            @Override
            public void onResponse(MatchEntity match) {
                activity.setProgressBarVisibility(false);
                setMatch(match);
            }
        };
        Response.ErrorListener errorListener = createErrorListener();

        StahpAPI api = StahpApplication.getInstance().getAPI();
        api.joinMatch(match.getId(), listener, errorListener);
    }

    private void startMatch() {
        final Activity activity = getActivity();
        activity.setProgressBarVisibility(true);

        Response.Listener<MatchEntity> listener = new Response.Listener<MatchEntity>() {
            @Override
            public void onResponse(MatchEntity match) {
                activity.setProgressBarVisibility(false);
                setMatch(match);
            }
        };
        Response.ErrorListener errorListener = createErrorListener();

        StahpAPI api = StahpApplication.getInstance().getAPI();
        api.startMatch(match.getId(), listener, errorListener);
    }

    private void playMatch() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), MatchActivity.class);
        intent.putExtra("matchId", match.getId());
        startActivity(intent);
    }

    private Response.ErrorListener createErrorListener() {
        final Activity activity = getActivity();
        return new CroutonErrorListener(activity) {
            @Override
            public void onErrorResponse(VolleyError error) {
                activity.setProgressBarVisibility(false);
                super.onErrorResponse(error);
            }
        };
    }
}
