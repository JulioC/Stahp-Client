package br.com.thejcs.stahp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.Window;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Arrays;

import br.com.thejcs.stahp.api.StahpAPI;
import br.com.thejcs.stahp.api.entity.MatchEntity;
import br.com.thejcs.stahp.util.CroutonErrorListener;

public class MatchDetailsActivity extends ActionBarActivity {

    private MatchEntity match;

    private MatchDetailsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_match_details);

        if (savedInstanceState == null) {
            fragment = new MatchDetailsFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .hide(fragment)
                    .commit();
        }

        Intent intent = getIntent();
        String matchId = intent.getStringExtra("matchId");

        loadMatchDetails(matchId);
    }

    public void loadMatchDetails(String matchId) {
        setProgressBarIndeterminateVisibility(true);

        getFragmentManager()
                .beginTransaction()
                .hide(fragment)
                .commit();

        Response.Listener<MatchEntity> listener = createResponseListener();
        Response.ErrorListener errorListener = createErrorListener();

        StahpAPI api = StahpApplication.getInstance().getAPI();
        api.getMatch(matchId, listener, errorListener);
    }

    private Response.Listener<MatchEntity> createResponseListener() {
        return new Response.Listener<MatchEntity>() {
            @Override
            public void onResponse(MatchEntity match) {
                setProgressBarIndeterminateVisibility(false);

                setMatch(match);
            }
        };
    }

    private Response.ErrorListener createErrorListener() {
        return new CroutonErrorListener(this) {
            @Override
            public void onErrorResponse(VolleyError error) {
                setProgressBarIndeterminateVisibility(false);

                super.onErrorResponse(error);
            }
        };
    }

    private void setMatch(MatchEntity match) {
        this.match = match;
        fragment.setMatch(match);

        getFragmentManager()
                .beginTransaction()
                .show(fragment)
                .commit();
    }

}
