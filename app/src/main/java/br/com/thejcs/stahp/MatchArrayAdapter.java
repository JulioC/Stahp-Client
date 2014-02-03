package br.com.thejcs.stahp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.thejcs.stahp.api.entity.MatchEntity;

public class MatchArrayAdapter extends ArrayAdapter<MatchEntity> {
    public MatchArrayAdapter(Context context, int resourceId, List<MatchEntity> items) {
        super(context, resourceId, items);
    }

    private class ViewHolder {
        ImageView imageView;
        TextView matchStatus;
        TextView matchCreator;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        MatchEntity item = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.match_list_item, null);
            holder = new ViewHolder();
            holder.matchCreator = (TextView) convertView.findViewById(R.id.match_creator);
            holder.matchStatus = (TextView) convertView.findViewById(R.id.match_status);
//            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.matchCreator.setText(String.format(
                getContext().getString(R.string.created_by),
                item.getCreator().getName()));
        holder.matchStatus.setText(item.getStatus().toString());
//        holder.imageView.setImageResource(item.getImageId());

        return convertView;
    }
}
