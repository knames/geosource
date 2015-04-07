package hoopsnake.geosource;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import org.xwalk.core.XWalkView;

import hoopsnake.geosource.comm.RunnableGetSubscribedChannels;
import hoopsnake.geosource.comm.RunnableSendAnyStoredIncidents;
import hoopsnake.geosource.data.AppChannelIdentifier;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * @author wsv759
 *
 * This activity is the entry point for the Geosource Android app. It displays a webview,
 * as well as a button to add an incident. If the add incident button is clicked, this class
 * delegates to IncidentActivity to fill out a new incident. When that incident has been filled out
 * and sent, IncidentActivity returns here.
 */
public class MainActivity extends Activity {
    public static final String APP_LOG_TAG = "geosource";
    public static final String FILENAME_SUBSCRIBED_CHANNELS = "subscribed_channels";
    public static final String PARAM_CHOSEN_CHANNEL = "chosenChannel";

    private XWalkView xWalkWebView;

    //TODO make this something other than a hard-coded string.
    private String userName = "frank";
    private String gid = null;


    /**
     * The set of all request codes that are used by this activity when starting new activities or fragments.
     */
    private enum RequestCode {
        CREATE_INCIDENT_ACTIVITY,
        GET_CHANNEL_ACTIVITY
    }

    /**
     * Start up, and load the webview for the website.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO check if isInternetConnected() first.
        xWalkWebView=(XWalkView)findViewById(R.id.xwalkWebView);
        xWalkWebView.load("http://okenso.com/", null);
        xWalkWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

        setIncidentButtonTextBasedOnSharedPref();

        //If folder is not empty, and we are connected to the internet, send those files!
        Thread sendIncidentsThread = new Thread(new RunnableSendAnyStoredIncidents(this));
        sendIncidentsThread.setPriority(Thread.MIN_PRIORITY);
        sendIncidentsThread.start();

        //Get all the subscribed channels for this user.
        Thread subscribedChannelsThread = new Thread(new RunnableGetSubscribedChannels(this, userName));
        subscribedChannelsThread.setPriority(Thread.MIN_PRIORITY);
        subscribedChannelsThread.start();
    }

    /**
     * Go to the incident creation activity.
     */
    public void onCreateIncidentButtonClicked(View v)
    {
        if (IncidentActivity.curIncidentExistsInFileSystem(this))
            startIncidentActivity(false, null);
        else {
            Intent intent = new Intent(MainActivity.this, ChannelSelectionActivity.class);
            startActivityForResult(intent, RequestCode.GET_CHANNEL_ACTIVITY.ordinal());
        }
    }

    private void startIncidentActivity(boolean createNewIncident, AppChannelIdentifier channel)
    {
        Intent intent = new Intent(MainActivity.this, IncidentActivity.class);

        //If there isn't currently an incident being worked on, give the necessary parameters to ask for a new one.
        if (createNewIncident) {
            assertTrue(!IncidentActivity.curIncidentExistsInFileSystem(this));
            assertNotNull(channel);

            intent.putExtra(PARAM_CHOSEN_CHANNEL, (Parcelable) channel);
            intent.putExtra(IncidentActivity.PARAM_STRING_POSTER, userName);
        }
        else
        {
            assertNull(channel);
        }

        startActivityForResult(intent, RequestCode.CREATE_INCIDENT_ACTIVITY.ordinal());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        RequestCode request = RequestCode.values()[requestCode];
        switch(request)
        {
            case CREATE_INCIDENT_ACTIVITY:
                if (resultCode == RESULT_OK)
                {
                    Button incidentButton = (Button) findViewById(R.id.create_incident_button);
                    incidentButton.setText(getString(R.string.create_incident));
                }
                else //RESULT_CANCELLED
                {
                    setIncidentButtonTextBasedOnSharedPref();
                }
                break;
            case GET_CHANNEL_ACTIVITY:
                if (resultCode == RESULT_OK)
                {
                    AppChannelIdentifier channel = data.getParcelableExtra(PARAM_CHOSEN_CHANNEL);

                    assertNotNull(channel);

                    startIncidentActivity(true, channel);
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (xWalkWebView != null) {
            xWalkWebView.pauseTimers();
            xWalkWebView.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (xWalkWebView != null) {
            xWalkWebView.resumeTimers();
            xWalkWebView.onShow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (xWalkWebView != null) {
            xWalkWebView.onDestroy();
        }
    }

    public void login(String gid) {
        this.gid = gid;
    }

    public void logout() {
        this.gid = null;
    }

    private void setIncidentButtonTextBasedOnSharedPref()
    {
        Button incidentButton = (Button) findViewById(R.id.create_incident_button);

        if (IncidentActivity.curIncidentExistsInFileSystem(this))
            incidentButton.setText(getString(R.string.resume_incident_creation));
        else
            incidentButton.setText(getString(R.string.create_incident));
    }
}
