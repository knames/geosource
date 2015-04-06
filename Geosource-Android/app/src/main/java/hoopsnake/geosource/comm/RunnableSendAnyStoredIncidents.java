package hoopsnake.geosource.comm;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.lang.ref.WeakReference;

import hoopsnake.geosource.FileIO;
import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.data.AppIncident;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 06/04/15.
 */
public class RunnableSendAnyStoredIncidents implements Runnable {

    @Override
    public void run() {

    }

    private static final String LOG_TAG = "geosource comm";
    WeakReference<Activity>  activityRef;
    File savedIncidentsDir;
    public RunnableSendAnyStoredIncidents(Activity activity)
    {
        assertNotNull(activity);

        savedIncidentsDir = activity.getDir(IncidentActivity.DIRNAME_INCIDENTS_YET_TO_SEND, Context.MODE_PRIVATE);
        assertNotNull(savedIncidentsDir);

        this.activityRef = new WeakReference<Activity>(activity);
    }

    protected Void doInBackground(Void... params) {
        File[] savedIncidentFiles = savedIncidentsDir.listFiles();

        Log.v(LOG_TAG, Integer.toString(savedIncidentFiles.length) + " incidents to send.");

        assertNotNull(savedIncidentFiles);
        if (savedIncidentFiles.length == 0)
            return null;
//
//        String activityBaseFilesDirPath;
//        try {
//            activityBaseFilesDirPath = activity.getFilesDir().getCanonicalPath();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e(LOG_TAG, "unable to send files. Could not retrieve canonical path.");
//            return null;
//        }

        //Send each of the individual files.
        for (File incidentFileToSend : savedIncidentFiles) {
            String incidentFilePath = incidentFileToSend.getAbsolutePath();
//            try {
//                incidentFilePath = incidentFileToSend.getCanonicalPath();
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e(LOG_TAG, "unable to send files. Could not retrieve canonical path.");
//                return null;
//            }

//            String incidentRelativePath;
//            if (incidentFilePath.startsWith(activityBaseFilesDirPath)) {
//                incidentRelativePath = incidentFilePath.substring(activityBaseFilesDirPath.length() + 1);
//            } else {
//                Log.e(LOG_TAG, "unable to send files. Canonical paths " + incidentFilePath + ", " + activityBaseFilesDirPath + " did not match up.");
//                return null;
//            }

            AppIncident incidentToSend = (AppIncident) FileIO.readObjectFromFileNoContext(incidentFilePath);

            if (incidentToSend == null)
                Log.e(LOG_TAG, "unable to send file " + incidentFilePath +". File IO failed.");
            else {
                Thread threadSendIncident = new Thread(new RunnableSendIncident(activityRef, incidentToSend));
                threadSendIncident.setPriority(Thread.currentThread().getPriority());
                threadSendIncident.run();

                //TODO do this sequentially if necessary.
//                new RunnableSendIncident(activityRef, incidentToSend).run();
            }
        }

        return null;
    }
}
