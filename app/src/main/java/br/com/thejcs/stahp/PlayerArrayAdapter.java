package br.com.thejcs.stahp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.thejcs.stahp.api.entity.PlayerEntity;

public class PlayerArrayAdapter extends ArrayAdapter<PlayerEntity> {
    public PlayerArrayAdapter(Context context, int resourceId, List<PlayerEntity> items) {
        super(context, resourceId, items);
    }

    private class ViewHolder {
        TextView playerName;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        PlayerEntity item = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.player_list_item, null);
            holder = new ViewHolder();
            holder.playerName = (TextView) convertView.findViewById(R.id.player_name);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.playerName.setText(item.getName());

        return convertView;
    }
}