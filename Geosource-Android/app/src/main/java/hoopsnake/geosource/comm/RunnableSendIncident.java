package hoopsnake.geosource.comm;

import android.app.Activity;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import ServerClientShared.Commands;
import hoopsnake.geosource.FileIO;
import hoopsnake.geosource.R;
import hoopsnake.geosource.data.AbstractAppFieldWithFile;
import hoopsnake.geosource.data.AppField;
import hoopsnake.geosource.data.AppIncident;
import hoopsnake.geosource.data.AppIncidentWithWrapper;
import hoopsnake.geosource.data.RunnableSetContentBasedOnFileUri;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by wsv759 on 06/04/15.
 * Task to send a new completed incident to the server.
 */
public class RunnableSendIncident implements Runnable {
    private static final int MINUTES_TO_WAIT_FOR_FORMATTING = 2;
    private static final String LOG_TAG = "geosource comm";

    WeakReference<Activity> activityRef;
    AppIncident appIncidentToSend;
    File incidentFile;

    SocketWrapper socketWrapper;
    ObjectOutputStream outStream; //stream to client
    ObjectInputStream inStream; //stream from client

    private CountDownLatch contentSerializationCountDownLatch;

    public CountDownLatch getContentSerializationCountDownLatch() {
        return contentSerializationCountDownLatch;
    }

    public RunnableSendIncident(WeakReference<Activity> activityRef, AppIncident incidentToSend)
    {
        assertNotNull(incidentToSend);

        this.appIncidentToSend = incidentToSend;
        this.activityRef = activityRef;
        if (activityRef != null)
            incidentFile = incidentToSend.getFile(activityRef.get());
    }

    protected SocketResult doInBackground() {
        //Check if there are any fields to serialize.
        LinkedList<AbstractAppFieldWithFile> listFileFieldsToSerialize = new LinkedList<>();
        for (AppField field : appIncidentToSend.getFieldList())
        {
            assertFalse(!field.contentIsFilled() && field.isRequired());

            if (field instanceof AbstractAppFieldWithFile && field.contentIsFilled())
                listFileFieldsToSerialize.add((AbstractAppFieldWithFile) field);
        }

        //If there are any files to serialize, ping the server to make sure serializing isn't a waste of time.
        //If the ping succeeds, go through with serialization.
        if (!listFileFieldsToSerialize.isEmpty()) {
            SocketResult initializeResult = initializeSocketConnection();
            if (!initializeResult.equals(SocketResult.SUCCESS))
            {
                saveUnsentIncidentToFileSystemIfNecessary(appIncidentToSend);
                return initializeResult;
            }


            //Ping the server.
            try {
                Log.v(LOG_TAG, "pinging server.");

                outStream.writeObject(Commands.IOCommand.PING);
                outStream.flush();

                Commands.IOCommand reply = (Commands.IOCommand) inStream.readObject();
                if (!Commands.IOCommand.PING.equals(reply)) {
                    saveUnsentIncidentToFileSystemIfNecessary(appIncidentToSend);
                    return SocketResult.FAILED_CONNECTION;
                }
                Log.v(LOG_TAG, "ping succeeded.");
            } catch (IOException e) {
                e.printStackTrace();
                saveUnsentIncidentToFileSystemIfNecessary(appIncidentToSend);
                return SocketResult.UNKNOWN_ERROR;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                saveUnsentIncidentToFileSystemIfNecessary(appIncidentToSend);
                return SocketResult.CLASS_NOT_FOUND;
            }
            finally{
                socketWrapper.closeAll();
            }

            //Serialize all the necessary files.
            contentSerializationCountDownLatch = new CountDownLatch(listFileFieldsToSerialize.size());
            for (AbstractAppFieldWithFile field : listFileFieldsToSerialize) {
                Thread threadSetContent = new Thread(new RunnableSetContentBasedOnFileUri(this, field));
                threadSetContent.setPriority(Thread.currentThread().getPriority());
                threadSetContent.run();

                //TODO do this sequentially if necessary.
//                new RunnableSetContentBasedOnFileUri(this, field).run();
            }
            try {
                contentSerializationCountDownLatch.await(MINUTES_TO_WAIT_FOR_FORMATTING, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();

                saveUnsentIncidentToFileSystemIfNecessary(appIncidentToSend);
                return SocketResult.FAILED_FORMATTING;
            }
        }

        //TODO ping the server before serializing everything.

        assertNotNull(appIncidentToSend);

        SocketResult initializeResult = initializeSocketConnection();
        if (!initializeResult.equals(SocketResult.SUCCESS))
            return initializeResult;

        try
        {
            Log.i(LOG_TAG, "Attempting to send incident.");
            outStream.writeObject(Commands.IOCommand.SEND_INCIDENT);
            outStream.writeObject(appIncidentToSend.toIncident());
            outStream.flush();
            //TODO is a reply really not necessary?
        }
        catch (IOException e)
        {
            e.printStackTrace();
            saveUnsentIncidentToFileSystemIfNecessary(appIncidentToSend);
            return SocketResult.UNKNOWN_ERROR;
        }
        //TODO confirm this is not necessary (it is associated with receiving a reply, that currently isn't happening).
//            catch (ClassNotFoundException e) {
//                Log.e(LOG_TAG, "incoming class not found.");
//                e.printStackTrace();
//
//                return SocketResult.CLASS_NOT_FOUND;
//
//            }
        finally {
            socketWrapper.closeAll();
        }


        return SocketResult.SUCCESS;
    }

    protected void onPostExecute(SocketResult result) {
        if (activityRef != null) {
            Activity activity = activityRef.get();
            if (activity != null)
                result.makeToastAndLog(activity.getString(R.string.uploaded_incident),
                        activity.getString(R.string.failed_to_upload_incident),
                        activity,
                        LOG_TAG);
        }
        else
            Log.i(LOG_TAG, "TaskSendIncident had result " + result.toString() );
    }

    private void saveUnsentIncidentToFileSystemIfNecessary(AppIncident unsentIncident)
    {
        String incidentLostMsg;
        if (activityRef != null)
            incidentLostMsg = activityRef.get().getString(R.string.incident_lost_media_saved);
        else
            incidentLostMsg = "Unable to retrieve current incident. Any created media files were saved.";

        //If no file could be created for the incident, fail.
        if (incidentFile == null) {
            Log.e(LOG_TAG, incidentLostMsg);
            return;
        }

        //If the file is empty or does not exist, create it. Otherwise it already exists, with the correct serialized incident inside, and
        // we can return.
        if (incidentFile.length() != 0) {
            Log.i(LOG_TAG, "unsent incident exists at " + incidentFile.getAbsolutePath());
            return;
        }
        assertTrue(unsentIncident.isCompletelyFilledIn());

        //Don't serialize the big file content byte arrays!
        for (AppField field : unsentIncident.getFieldList())
        {
            if (field instanceof AbstractAppFieldWithFile) {
                field.setContent(null);
            }
        }

        boolean fileWasWritten = FileIO.writeObjectToFileNoContext((AppIncidentWithWrapper) unsentIncident, incidentFile.getAbsolutePath());
        if (fileWasWritten)
            Log.i(LOG_TAG, "unsent incident saved to " + incidentFile.getAbsolutePath());
        else
            Log.e(LOG_TAG, incidentLostMsg);
    }

    private SocketResult initializeSocketConnection()
    {
        try //create socket
        {
            socketWrapper = new SocketWrapper();
            outStream = socketWrapper.getOut();
            inStream = socketWrapper.getIn();

            return SocketResult.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();

            saveUnsentIncidentToFileSystemIfNecessary(appIncidentToSend);
            return SocketResult.FAILED_CONNECTION;
        }
    }

    @Override
    public void run() {
        SocketResult result = doInBackground();
        onPostExecute(result);
    }
}
