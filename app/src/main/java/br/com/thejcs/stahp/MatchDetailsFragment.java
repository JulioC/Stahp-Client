package br.com.thejcs.stahp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.thejcs.stahp.api.entity.MatchEntity;
import br.com.thejcs.stahp.api.entity.PlayerEntity;

public class MatchDetailsFragment extends Fragment {

    private MatchEntity match;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_match_details, container, false);
        return rootView;
    }

    public void setMatch(MatchEntity match) {
        this.match = match;

        TextView textId = (TextView) getView().findViewById(R.id.match_creator);
        textId.setText(match.getId());

        TextView textStatus = (TextView) getView().findViewById(R.id.match_status);
        textStatus.setText(match.getStatus().toString());

        try {
            ListView listPlayers = (ListView) getView().findViewById(R.id.match_players);
            listPlayers.setAdapter(new PlayerArrayAdapter(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    new ArrayList<PlayerEntity>(match.getPlayers())));
        }
        catch (Exception e) {
            e.getMessage();
        }

        String playerId = StahpApplication.getInstance().getPlayerId();

        // TODO: this button is doing too much, poor guy :(
        Button buttonAction = (Button) getView().findViewById(R.id.action_button);
        switch(match.getStatus()) {
            case CREATED:
                if(match.isCreator(playerId)) {
                    buttonAction.setText(getString(R.string.match_start));
                }
                else if(!match.isPlayerIn(playerId)) {
                    buttonAction.setText(getString(R.string.match_join));
                }
                else {
                    buttonAction.setVisibility(View.GONE);
                }
                break;
            case STARTED:
                if(match.isPlayerIn(playerId)) {
                    buttonAction.setText(getString(R.string.match_play));
                }
                break;
            case FINISHED:
                buttonAction.setVisibility(View.GONE);
                break;
        }
    }
}
