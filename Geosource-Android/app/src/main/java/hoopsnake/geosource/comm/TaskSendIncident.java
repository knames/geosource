package hoopsnake.geosource.comm;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    SocketWrapper socketWrapper;
    ObjectOutputStream outStream; //stream to client
    ObjectInputStream inStream; //stream from client
    AppIncident appIncidentToSend;

    private CountDownLatch contentSerializationCountDownLatch;

    public CountDownLatch getContentSerializationCountDownLatch() {
        return contentSerializationCountDownLatch;
    }

    public TaskSendIncident(IncidentActivity activity)
    {
        super(activity);
    }

    protected SocketResult doInBackground(AppIncident... params) {
        appIncidentToSend = params[0];
        LinkedList<AbstractAppFieldWithFile> listFileFieldsToSerialize = new LinkedList<>();

        //Check if there are any fields to serialize.
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
                return initializeResult;

            //Ping the server.
            try {
                Log.v(LOG_TAG, "pinging server.");

                outStream.writeObject(Commands.IOCommand.PING);
                outStream.flush();

                Commands.IOCommand reply = (Commands.IOCommand) inStream.readObject();
                if (!Commands.IOCommand.PING.equals(reply))
                    return SocketResult.FAILED_CONNECTION;

                Log.v(LOG_TAG, "ping succeeded.");
            } catch (IOException e) {
                e.printStackTrace();
                return SocketResult.UNKNOWN_ERROR;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return SocketResult.CLASS_NOT_FOUND;
            }
            finally{
                socketWrapper.closeAll();
            }

            //Serialize all the necessary files.
            contentSerializationCountDownLatch = new CountDownLatch(listFileFieldsToSerialize.size());
            for (AbstractAppFieldWithFile field : listFileFieldsToSerialize)
                new TaskSetContentBasedOnFileUri(this).execute(field);

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
        finally {
            socketWrapper.closeAll();
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

        //If the file is empty or does not exist, create it. (Otherwise it already exists, with the correct serialized incident inside.)
        if (unsentIncidentFile.length() == 0)
            FileIO.writeObjectToFileNoContext((AppIncidentWithWrapper) unsentIncident, unsentIncidentFile.getAbsolutePath());
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
}
