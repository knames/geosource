package hoopsnake.geosource;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.xwalk.core.XWalkView;

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

    private XWalkView xWalkWebView;

    //TODO make these something other than a hard-coded string.
    private String curChannelName = "march13";
    private String curChannelOwner = "okenso";
    private String userName = "frank";


    /**
     * The set of all request codes that are used by this activity when starting new activities or fragments.
     */
    private enum RequestCode {
        CREATE_INCIDENT_ACTIVITY_REQUEST_CODE
    }

    /**
     * Start up, and load the webview for the website.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xWalkWebView=(XWalkView)findViewById(R.id.xwalkWebView);
        xWalkWebView.load("http://okenso.com", null);

        setIncidentButtonTextBasedOnSharedPref();
    }

    /**
     * Go to the incident creation activity.
     */
    public void onCreateIncidentButtonClicked(View v)
    {
        Intent intent = new Intent(MainActivity.this, IncidentActivity.class);
        //TODO determine the current channel.
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.app_sharedpref_file_key), Context.MODE_PRIVATE);

        //If there isn't currently an incident being worked on, give the necessary parameters to ask for a new one.
        if (!sharedPref.contains(IncidentActivity.SHAREDPREF_CUR_INCIDENT_EXISTS)) {
            intent.putExtra(IncidentActivity.PARAM_STRING_CHANNEL_NAME, curChannelName);
            intent.putExtra(IncidentActivity.PARAM_STRING_CHANNEL_OWNER, curChannelOwner);
            intent.putExtra(IncidentActivity.PARAM_STRING_POSTER, userName);
        }

        startActivityForResult(intent, RequestCode.CREATE_INCIDENT_ACTIVITY_REQUEST_CODE.ordinal());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.CREATE_INCIDENT_ACTIVITY_REQUEST_CODE.ordinal())
        {
            if (resultCode == RESULT_OK)
            {
                Button incidentButton = (Button) findViewById(R.id.create_incident_button);
                incidentButton.setText(getString(R.string.create_incident));
            }
            else //RESULT_CANCELLED
            {
                setIncidentButtonTextBasedOnSharedPref();
            }
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

    private void setIncidentButtonTextBasedOnSharedPref()
    {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.app_sharedpref_file_key), Context.MODE_PRIVATE);
        Button incidentButton = (Button) findViewById(R.id.create_incident_button);

        if (sharedPref.contains(IncidentActivity.SHAREDPREF_CUR_INCIDENT_EXISTS) && sharedPref.getBoolean(IncidentActivity.SHAREDPREF_CUR_INCIDENT_EXISTS, false))
            incidentButton.setText(getString(R.string.resume_incident_creation));
        else
            incidentButton.setText(getString(R.string.create_incident));
    }
}
