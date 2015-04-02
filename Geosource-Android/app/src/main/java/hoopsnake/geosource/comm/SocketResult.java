package hoopsnake.geosource.comm;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import hoopsnake.geosource.R;

/**
 * Created by wsv759 on 19/02/15. All the different results that could occur from the socket connection.
 * This should probably be internal somewhere.
 */
public enum SocketResult {
    SUCCESS {
        @Override
        public void makeToastAndLog(String onSuccess, String onFailure, Context context, String logTag) {
            Toast.makeText(context, onSuccess, Toast.LENGTH_LONG).show();
            Log.i(logTag,onSuccess);
        }
    }, FAILED_CONNECTION {
        @Override
        public void makeToastAndLog(String onSuccess, String onFailure, Context context, String logTag) {
            String message = onFailure + " " + context.getString(R.string.connection_failed);
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            Log.e(logTag, message);
        }
    }, CLASS_NOT_FOUND {
        @Override
        public void makeToastAndLog(String onSuccess, String onFailure, Context context, String logTag) {
            Toast.makeText(context, onFailure + " " + context.getString(R.string.incomprehensible_server_response), Toast.LENGTH_LONG).show();
            Log.e(logTag, onFailure + "Server response object class not found.");
        }
    }, FAILED_FORMATTING {
        @Override
        public void makeToastAndLog(String onSuccess, String onFailure, Context context, String logTag) {
            String message = onFailure + " " + context.getString(R.string.failed_to_format_content);
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            Log.e(logTag, message);
        }
    }, UNKNOWN_ERROR {
        @Override
        public void makeToastAndLog(String onSuccess, String onFailure, Context context, String logTag) {
            String message = onFailure + " " + context.getString(R.string.unknown_error);
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            Log.e(logTag, message);
        }
    };

    /**
     * Helper function. Allows the two socket tasks to react similarly to various results.
     * @param onSuccess the string to toast and log on success.
     * @param onFailure the string to toast and log on failure.
     * @param context the context upon which to toast and log.
     * @param logTag the log tag to use when logging.
     */
    public abstract void makeToastAndLog(String onSuccess, String onFailure, Context context, String logTag);
}

