package hoopsnake.geosource.comm;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by wsv759 on 06/04/15.
 */
public abstract class BackgroundRunnable<Result> implements Runnable {
    protected WeakReference<Activity> activityRef;

    public BackgroundRunnable(WeakReference<Activity> activityRef)
    {
        this.activityRef = activityRef;
    }

    protected abstract Result doInBackground();
    protected abstract void onPostExecute(Result result, Activity activity);

    protected void onPostExecuteWrapper(final Result result)
    {
        if (activityRef != null) {
            final Activity activity = activityRef.get();
            if (activity != null)
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onPostExecute(result, activity);
                    }
                });
        }
    }

    @Override
    public void run() {
        Result result = doInBackground();
        onPostExecuteWrapper(result);
    }
}
