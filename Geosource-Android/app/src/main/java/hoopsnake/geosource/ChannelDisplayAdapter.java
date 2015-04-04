package hoopsnake.geosource;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import hoopsnake.geosource.data.AppChannel;

/**
 * Created by wsv759 on 02/04/15.
 */
public class ChannelDisplayAdapter extends ArrayAdapter<AppChannel> {

    int layoutResourceId;

    public ChannelDisplayAdapter(Activity activity, int resource, AppChannel[] channels) {
        super(activity, resource, channels);

        layoutResourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ChannelHolder holder;

        Log.d(MainActivity.APP_LOG_TAG, "5");
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)super.getContext()).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ChannelHolder();
            holder.viewChannelName = (TextView) row.findViewById(R.id.channel_name_view);
            holder.viewChannelOwner = (TextView)row.findViewById(R.id.channel_owner_view);

            row.setTag(holder);
        }
        else
        {
            holder = (ChannelHolder)row.getTag();
        }

        AppChannel channel = super.getItem(position);
        holder.viewChannelName.setText(channel.getChannelName());
        holder.viewChannelOwner.setText(channel.getChannelOwner());

        Log.d(MainActivity.APP_LOG_TAG, "6");
        return row;
    }

    private static class ChannelHolder
    {
        TextView viewChannelName;
        TextView viewChannelOwner;
    }
}
