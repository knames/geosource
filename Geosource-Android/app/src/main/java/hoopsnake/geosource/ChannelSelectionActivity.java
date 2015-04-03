package hoopsnake.geosource;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import hoopsnake.geosource.media.ChannelDisplayAdapter;

import static junit.framework.Assert.assertNotNull;


public class ChannelSelectionActivity extends ListActivity {
    public static final String PARAM_BOOLEAN_PICTURE_CHANNELS_ONLY = "pictureChannelsOnly";
    public static final String PARAM_CHOSEN_CHANNEL = "chosenChannel";

    private EditText channelSearchBar;


    /**
     * the channels to choose from.
     */
    private Channel[] channels = {new Channel("Dummy for testing.", "Please wait while I try and get your channels...")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_selection);
        Bundle extras = getIntent().getExtras();

        Log.d(MainActivity.APP_LOG_TAG, "1");
        ChannelDisplayAdapter channelAdapter = new ChannelDisplayAdapter(
                this, R.layout.channel_name_and_owner_view, channels);
        setListAdapter(channelAdapter);
        Log.d(MainActivity.APP_LOG_TAG, "2");
//      //TODO uncomment this once channel sending from socket is implemented.
//        if (extras == null) {
//            new TaskGetChannels(this).execute(false);
//        }
//        else
//        {
//            new TaskGetChannels(this).execute(extras.getBoolean(PARAM_BOOLEAN_PICTURE_CHANNELS_ONLY, false));
//        }

        channelSearchBar = (EditText) findViewById(R.id.channel_search_bar);

        /**
         * Enabling Search Filter
         *
         */
        channelSearchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                String text = cs.toString().trim();

                ((ChannelDisplayAdapter) getListAdapter()).getFilter().filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
            @Override
            public void afterTextChanged(Editable arg0) {}
        });

        Log.d(MainActivity.APP_LOG_TAG, "3");
    }

    public void setChannels(Channel[] channels)
    {
        this.channels = channels;
        assertNotNull(channels);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //TODO fix all this.
        Intent intent = new Intent();

        Log.d(MainActivity.APP_LOG_TAG, "4");
        LinearLayout ll = (LinearLayout) v;
        TextView channelNameView = (TextView) ll.findViewById(R.id.channel_name_view);
        TextView channelOwnerView = (TextView) ll.findViewById(R.id.channel_owner_view);

        intent.putExtra(PARAM_CHOSEN_CHANNEL, (android.os.Parcelable) new Channel(
                channelNameView.getText().toString(),
                channelOwnerView.getText().toString()));

        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Call this if the data set has been updated.
     */
    public void onUpdated()
    {
        ChannelDisplayAdapter channelAdapter = (ChannelDisplayAdapter) getListAdapter();

        channelAdapter.notifyDataSetChanged();

        String searchString = channelSearchBar.getText().toString().trim();
        if (!searchString.isEmpty())
            channelAdapter.getFilter().filter(searchString);
    }
}


