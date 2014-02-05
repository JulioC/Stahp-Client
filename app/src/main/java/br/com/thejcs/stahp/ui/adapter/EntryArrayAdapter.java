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
import br.com.thejcs.stahp.api.entity.EntryEntity;

public class EntryArrayAdapter extends ArrayAdapter<EntryEntity> {
    private boolean showScores;

    public EntryArrayAdapter(Context context, int resourceId, List<EntryEntity> items,
                             boolean showScores) {
        super(context, resourceId, items);

        this.showScores = showScores;
    }

    private class ViewHolder {
        TextView playerName;
        TextView entryInfo;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        EntryEntity item = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.player_list_item, null);
            holder = new ViewHolder();
            holder.playerName = (TextView) convertView.findViewById(R.id.player_name);
            holder.entryInfo = (TextView) convertView.findViewById(R.id.entry_info);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.playerName.setText(item.getPlayer().getName());
        if(showScores) {
            holder.entryInfo.setVisibility(View.VISIBLE);

            Integer score = item.getScore();
            if(score == null) {
                holder.entryInfo.setText(getContext().getString(R.string.waiting_response));
            }
            else {
                holder.entryInfo.setText(String.format(
                        getContext().getString(R.string.score_number),
                        score));
            }
        }
        else {
            holder.entryInfo.setVisibility(View.GONE);
        }

        return convertView;
    }
}