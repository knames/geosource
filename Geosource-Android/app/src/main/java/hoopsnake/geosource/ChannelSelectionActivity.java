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

import java.util.ArrayList;
import java.util.List;

import ServerClientShared.ChannelIdentifier;
import hoopsnake.geosource.comm.TaskGetChannelIdentifiers;
import hoopsnake.geosource.data.AppChannelIdentifier;
import hoopsnake.geosource.data.AppChannelIdentifierWithWrapper;

import static junit.framework.Assert.assertNotNull;


public class ChannelSelectionActivity extends ListActivity {
    public static final String PARAM_BOOLEAN_PICTURE_CHANNELS_ONLY = "pictureChannelsOnly";

    private EditText channelSearchBar;

    /**
     * the channelIdentifiers to choose from.
     */
    private ArrayList<AppChannelIdentifier> channelIdentifiers = new ArrayList<AppChannelIdentifier>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_selection);
        Bundle extras = getIntent().getExtras();

        Log.d(MainActivity.APP_LOG_TAG, "1");
        ChannelDisplayAdapter channelAdapter = new ChannelDisplayAdapter(
                this, R.layout.channel_name_and_owner_view, channelIdentifiers);
        setListAdapter(channelAdapter);
        channelAdapter.notifyDataSetChanged();

        Log.d(MainActivity.APP_LOG_TAG, "2");

		//TODO uncomment this once channel sending from socket is implemented.
        if (extras == null) {
            new TaskGetChannelIdentifiers(this).execute(false);
        }
        else
        {
            new TaskGetChannelIdentifiers(this).execute(extras.getBoolean(PARAM_BOOLEAN_PICTURE_CHANNELS_ONLY, false));
        }

        channelSearchBar = (EditText) findViewById(R.id.channel_search_bar);

        /**
         * Enabling Search Filter
         *
         */
        channelSearchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                String text = cs.toString().trim();

                //Filter the list to display only items matching the edit text.
                ((ChannelDisplayAdapter) getListAdapter()).getFilter().filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
            @Override
            public void afterTextChanged(Editable arg0) {}
        });

        Log.d(MainActivity.APP_LOG_TAG, "3");
    }

    public void setChannelIdentifiers(List<AppChannelIdentifier> channelIdentifiers)
    {
        this.channelIdentifiers.clear();
        this.channelIdentifiers.addAll(channelIdentifiers);

        assertNotNull(channelIdentifiers);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //TODO fix all this.
        Log.d(MainActivity.APP_LOG_TAG, "4");
        Intent intent = new Intent();

        LinearLayout ll = (LinearLayout) v;
        TextView channelNameView = (TextView) v.findViewById(R.id.channel_name_view);
        TextView channelOwnerView = (TextView) v.findViewById(R.id.channel_owner_view);

        AppChannelIdentifier selectedAci = new AppChannelIdentifierWithWrapper(
                new ChannelIdentifier(channelNameView.getText().toString(), channelOwnerView.getText().toString()));
        intent.putExtra(MainActivity.PARAM_CHOSEN_CHANNEL, (android.os.Parcelable) selectedAci);

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

        getListView().invalidateViews();
    }
}


