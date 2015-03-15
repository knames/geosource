package hoopsnake.geosource.comm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.R;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 15/03/15.
 */
public abstract class IncidentActivitySocketTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    IncidentActivity activity;

    public IncidentActivitySocketTask(IncidentActivity activity)
    {
        this.activity = activity;
        assertNotNull(activity);
    }

    /**
     * Helper function. Allows the two socket tasks to react similarly to various results.
     * @param onSuccess the string to toast and log on success.
     * @param onFailure the string to toast and log on failure.
     * @param result the socket result for this task.
     * @param context the context upon which to toast and log.
     * @param logTag the log tag to use when logging.
     */
    void makeToastAndLogOnSocketResult(String onSuccess, String onFailure, SocketResult result, Context context, String logTag)
    {
        String message;
        switch (result) {
            case UNKNOWN_ERROR:
                message = onFailure + " " + activity.getString(R.string.unknown_error);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                Log.e(logTag, message);
                break;
            case FAILED_CONNECTION:
                message = onFailure + " " + activity.getString(R.string.connection_failed);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                Log.e(logTag, message);
                break;
            case CLASS_NOT_FOUND:
                Toast.makeText(context, onFailure + " " + activity.getString(R.string.incomprehensible_server_response), Toast.LENGTH_LONG).show();
                Log.e(logTag, onFailure + "Server response object class not found.");
                break;
            case SUCCESS:
                Toast.makeText(context, onSuccess, Toast.LENGTH_LONG).show();
                Log.i(logTag,onSuccess);
            default:
                throw new RuntimeException("invalid result.");
        }
    }
}
