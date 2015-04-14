package hoopsnake.geosource;

import android.app.Activity;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by wsv759 on 06/04/15.
 * Abstract class that should be extended by all background tasks for the app that are not directly
 * related to the UI. Any extensions of this should be run in a Thread for proper behaviour.
 * Any UI-related code should go in onPostExecute(), and is NOT guaranteed to ever run.
 */
public abstract class BackgroundRunnable<Result> implements Runnable {

    public static final String LOG_TAG = "geosource background";
    /**
     * Keeps track of the ui, while still allowing the ui to be destroyed.
     */
    protected WeakReference<Activity> activityRef;

    public BackgroundRunnable(WeakReference<Activity> activityRef)
    {
        this.activityRef = activityRef;
    }

    /**
     * All background code should go here.
     * @return the result of the background code, to be dealt with in onPostExecute.
     */
    protected abstract Result doInBackground();

    /**
     * All code that needs to run on the ui thread should go here. This is not guaranteed to execute.
     * @param result the result of doInBackground.
     * @param activity the ui on which to run the thread.
     */
    protected abstract void onPostExecute(Result result, Activity activity);

    /**
     * Runs onPostExecute() on the UI thread, if the UI thread still exists (if the activity reference still exists).
     * @param result
     */
    private void onPostExecuteWrapper(final Result result)
    {
        if (activityRef != null) {
            final Activity activity = activityRef.get();
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onPostExecute(result, activity);
                    }
                });

                return;
            }
        }

        Log.i(LOG_TAG,"result without ui: " + this.getClass() + result.toString());
    }

    @Override
    public void run() {
        Result result = doInBackground();
        onPostExecuteWrapper(result);
    }
}
