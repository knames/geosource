package hoopsnake.geosource;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

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

    //TODO make this something other than a hard-coded string.
    String curChannelName = "mushrooms";

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

        WebView webview = (WebView) findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(getString(R.string.website_url));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Go to the incident creation activity.
     */
    public void onCreateIncidentButtonClicked(View v)
    {
        Intent intent = new Intent(MainActivity.this, IncidentActivity.class);
        //TODO determine the current channel.

        intent.putExtra(IncidentActivity.CHANNEL_NAME_PARAM_STRING, curChannelName);
        startActivityForResult(intent, RequestCode.CREATE_INCIDENT_ACTIVITY_REQUEST_CODE.ordinal());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.CREATE_INCIDENT_ACTIVITY_REQUEST_CODE.ordinal())
        {
            if (resultCode == RESULT_OK)
            {
                //TODO Potentially react to the OK result.
            }
            else
            {
                //TODO Potentially react to the unsuccessful result.
            }
        }
    }
}
