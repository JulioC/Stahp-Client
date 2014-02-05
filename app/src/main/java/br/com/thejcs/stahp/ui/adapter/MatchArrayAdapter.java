package br.com.thejcs.stahp.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.thejcs.stahp.R;
import br.com.thejcs.stahp.StahpApplication;
import br.com.thejcs.stahp.api.entity.MatchEntity;
import br.com.thejcs.stahp.util.MatchUtils;

public class MatchArrayAdapter extends ArrayAdapter<MatchEntity> {
    public MatchArrayAdapter(Context context, int resourceId, List<MatchEntity> items) {
        super(context, resourceId, items);
    }

    private class ViewHolder {
        TextView matchStatus;
        TextView matchCreator;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        MatchEntity match = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.match_list_item, null);
            holder = new ViewHolder();
            holder.matchCreator = (TextView) convertView.findViewById(R.id.match_creator);
            holder.matchStatus = (TextView) convertView.findViewById(R.id.match_status);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.matchCreator.setText(String.format(
                getContext().getString(R.string.created_by),
                match.getCreator().getName()));


        String playerId = StahpApplication.getInstance().getPlayerId();

        MatchUtils.ConsolidatedStatus status =
                MatchUtils.getConsolidatedStatus(playerId, match);

        String friendlyStatus = "";
        switch (status) {
            case READY_TO_START:
                friendlyStatus = getContext().getString(R.string.ready_to_start);
                break;
            case WAITING_START:
                friendlyStatus = getContext().getString(R.string.waiting_start);
                break;
            case OPEN_TO_JOIN:
                friendlyStatus = getContext().getString(R.string.open_to_join);
                break;
            case PLAYER_TURN:
                friendlyStatus = getContext().getString(R.string.player_turn);
                break;
            case STARTED:
                friendlyStatus = getContext().getString(R.string.started);
                break;
            case WAITING_FINISH:
                friendlyStatus = getContext().getString(R.string.waiting_finish);
                break;
            case FINISHED:
                friendlyStatus = getContext().getString(R.string.finished);
                break;
        }
        holder.matchStatus.setText(friendlyStatus);

        return convertView;
    }
}
