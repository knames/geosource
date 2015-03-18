package hoopsnake.geosource.comm;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ServerClientShared.Commands;
import ServerClientShared.Incident;
import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.R;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 19/02/15.
 * Task to send a new completed incident to the server.
 */
public class TaskSendIncident extends IncidentActivitySocketTask<Incident, Void, SocketResult> {
    public static final String LOG_TAG = "geosource comm";

    public TaskSendIncident(IncidentActivity activity)
    {
        super(activity);
    }

    protected SocketResult doInBackground(Incident... params) {
        try {
            activity.getContentCountDownLatch().await();
        } catch (InterruptedException e) {
            e.printStackTrace();

            return SocketResult.FAILED_PREFORMATTING;
        }

        ObjectOutputStream outStream; //wrapped stream to client

        ObjectInputStream inStream; //stream from client
        Socket outSocket;

        Incident incidentToSend = params[0];
        assertNotNull(incidentToSend);

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
            outStream.writeObject(incidentToSend);

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
