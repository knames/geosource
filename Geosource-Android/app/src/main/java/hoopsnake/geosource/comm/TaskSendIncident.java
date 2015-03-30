package hoopsnake.geosource.comm;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import ServerClientShared.Commands;
import hoopsnake.geosource.FileIO;
import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.R;
import hoopsnake.geosource.data.AbstractAppFieldWithFile;
import hoopsnake.geosource.data.AppField;
import hoopsnake.geosource.data.AppIncident;
import hoopsnake.geosource.data.AppIncidentWithWrapper;
import hoopsnake.geosource.data.TaskSetContentBasedOnFileUri;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by wsv759 on 19/02/15.
 * Task to send a new completed incident to the server.
 */
public class TaskSendIncident extends IncidentActivityCommTask<AppIncident, Void, SocketResult> {
    private static final int MINUTES_TO_WAIT_FOR_FORMATTING = 2;

    public CountDownLatch getContentCountDownLatch() {
        return contentCountDownLatch;
    }

    private CountDownLatch contentCountDownLatch;

    public TaskSendIncident(IncidentActivity activity)
    {
        super(activity);
    }

    protected SocketResult doInBackground(AppIncident... params) {
        //TODO ping the server before serializing everything.
        AppIncident appIncidentToSend = params[0];
        LinkedList<AbstractAppFieldWithFile> l = new LinkedList<>();
        for (AppField field : appIncidentToSend.getFieldList())
        {
            assertFalse(!field.contentIsFilled() && field.isRequired());
            if (field instanceof AbstractAppFieldWithFile && field.contentIsFilled())
            {
                l.add((AbstractAppFieldWithFile) field);
            }
        }

        contentCountDownLatch = new CountDownLatch(l.size());

        for (AbstractAppFieldWithFile field : l)
            new TaskSetContentBasedOnFileUri(this).execute(field);

        try {
            contentCountDownLatch.await(MINUTES_TO_WAIT_FOR_FORMATTING, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();

            saveUnsentIncidentToFileSystemIfNecessary(appIncidentToSend);
            return SocketResult.FAILED_FORMATTING;
        }

        ObjectOutputStream outStream; //wrapped stream to client

        ObjectInputStream inStream; //stream from client
        Socket outSocket;


        assertNotNull(appIncidentToSend);

        try //create socket
        {
            SocketWrapper socketWrapper = new SocketWrapper();
            outSocket = socketWrapper.getOutSocket();
            outStream = socketWrapper.getOut();
            inStream = socketWrapper.getIn();
        }
        catch(IOException e)
        {
            e.printStackTrace();

            saveUnsentIncidentToFileSystemIfNecessary(appIncidentToSend);
            return SocketResult.FAILED_CONNECTION;
        }


        try
        {
            //TODO identify with the server whether I am asking for an incident spec or sending an incident.
            Log.i(LOG_TAG, "Attempting to send incident.");
            outStream.writeObject(Commands.IOCommand.SEND_INCIDENT);
            outStream.writeObject(appIncidentToSend.toIncident());
            outStream.flush();
            //TODO is a reply really not necessary?

            inStream.close();
            outStream.close();
            outSocket.close();
            Log.i(LOG_TAG, "Connection Closed");
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

        return SocketResult.SUCCESS;
    }

    protected void onPostExecute(SocketResult result) {
        makeToastAndLogOnSocketResult(activity.getString(R.string.uploaded_incident),
                activity.getString(R.string.failed_to_upload_incident),
                result,
                activity,
                LOG_TAG);
    }

    private void saveUnsentIncidentToFileSystemIfNecessary(AppIncident unsentIncident)
    {
        assertTrue(unsentIncident.isCompletelyFilledIn());

        //Don't serialize the big file content byte arrays!
        for (AppField field : unsentIncident.getFieldList())
        {
            if (field instanceof AbstractAppFieldWithFile)
                field.setContent(null);
        }

        File unsentIncidentFile = unsentIncident.getFile(activity);

        if (unsentIncidentFile.length() == 0)
            FileIO.writeObjectToFileNoContext((AppIncidentWithWrapper) unsentIncident, unsentIncidentFile.getAbsolutePath());
    }
}
