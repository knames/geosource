package hoopsnake.geosource.comm;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import ServerClientShared.Commands;
import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.R;
import hoopsnake.geosource.data.AbstractAppFieldWithContentAndFile;
import hoopsnake.geosource.data.AppFieldWithContent;
import hoopsnake.geosource.data.AppIncident;
import hoopsnake.geosource.data.TaskSetContentBasedOnFileUri;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 19/02/15.
 * Task to send a new completed incident to the server.
 */
public class TaskSendIncident extends IncidentActivitySocketTask<AppIncident, Void, SocketResult> {
    public static final String LOG_TAG = "geosource comm";

    public CountDownLatch getContentCountDownLatch() {
        return contentCountDownLatch;
    }

    private CountDownLatch contentCountDownLatch;

    public TaskSendIncident(IncidentActivity activity)
    {
        super(activity);
    }

    protected SocketResult doInBackground(AppIncident... params) {
        //TODO serialize everything before sending everything.
        AppIncident appIncidentToSend = params[0];
        LinkedList<AbstractAppFieldWithContentAndFile> l = new LinkedList<>();
        for (AppFieldWithContent field : appIncidentToSend.getFieldList())
        {
            assertFalse(!field.contentIsFilled() && field.isRequired());
            if (field instanceof AbstractAppFieldWithContentAndFile && field.contentIsFilled())
            {
                l.add((AbstractAppFieldWithContentAndFile) field);
            }
        }

        contentCountDownLatch = new CountDownLatch(l.size());

        for (AbstractAppFieldWithContentAndFile field : l)
        {
            new TaskSetContentBasedOnFileUri(this).execute(field);
        }

        try {
            contentCountDownLatch.await(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();

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

        if (result.equals(SocketResult.SUCCESS))
            activity.finish();
    }
}
