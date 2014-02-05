package br.com.thejcs.stahp.ui.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Response;

import java.util.Arrays;

import br.com.thejcs.stahp.StahpApplication;
import br.com.thejcs.stahp.api.StahpAPI;
import br.com.thejcs.stahp.api.entity.MatchEntity;
import br.com.thejcs.stahp.ui.adapter.MatchArrayAdapter;
import br.com.thejcs.stahp.ui.MatchDetailsActivity;
import br.com.thejcs.stahp.util.CroutonErrorListener;

public class MatchListFragment extends ListFragment {

    private MatchEntity[] matches;

    public void fetchMatches() {
        fetchMatches(null);
    }

    public void fetchMatches(String playerId) {
        setListShown(false);

        matches = null;

        Response.Listener<MatchEntity[]> listener = createResponseListener();
        Response.ErrorListener errorListener = new CroutonErrorListener(getActivity());

        StahpAPI api = StahpApplication.getInstance().getAPI();
        if(playerId != null) {
            api.getMatches(playerId, listener, errorListener);
        }
        else {
            api.getMatches(listener, errorListener);
        }
    }

    public void fetchAllMatches() {
        setListShown(false);

        matches = null;

        Response.Listener<MatchEntity[]> listener = createResponseListener();
        Response.ErrorListener errorListener = new CroutonErrorListener(getActivity());

        StahpAPI api = StahpApplication.getInstance().getAPI();
        api.getAllMatches(listener, errorListener);
    }

    private Response.Listener<MatchEntity[]> createResponseListener() {
        return new Response.Listener<MatchEntity[]>() {
            @Override
            public void onResponse(MatchEntity[] matches) {
                setMatches(matches);
            }
        };
    }

    private void setMatches(MatchEntity[] matches) {
        this.matches = matches;
        setListAdapter(new MatchArrayAdapter(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                Arrays.asList(matches)));
        setListShown(true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    void showDetails(int index) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), MatchDetailsActivity.class);
        intent.putExtra("matchId", matches[index].getId());
        startActivity(intent);
    }

}
