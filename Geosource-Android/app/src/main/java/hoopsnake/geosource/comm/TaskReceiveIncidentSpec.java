package hoopsnake.geosource.comm;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import ServerClientShared.Commands;
import ServerClientShared.FieldWithoutContent;
import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.R;
import hoopsnake.geosource.data.AppIncidentWithWrapper;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 19/02/15.
 * Task to receive a new incident spec from the server, detailing what the fields are that need to be filled out.
 * @precond when calling execute(), must use precisely params as shown: execute(String channelName, String channelOwner, String poster). (No null args).
 * @postcond when execute() is called, receive a new incident spec from the server, detailing what the fields are that need to be filled out.
 */
public class TaskReceiveIncidentSpec extends IncidentActivitySocketTask<String, Void, SocketResult> {

    String channelName, channelOwner, poster;
    public static final String LOG_TAG = "geosource comm";

    public TaskReceiveIncidentSpec(IncidentActivity activity)
    {
        super(activity);
    }

    protected SocketResult doInBackground(String... params) {
        ObjectOutputStream outStream; //wrapped stream to client

        ObjectInputStream inStream; //stream from client
        Socket outSocket;

        channelName = params[0];
        channelOwner = params[1];
        poster = params[2];
        assertNotNull(channelName);
        assertNotNull(channelOwner);
        assertNotNull(poster);

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


        ArrayList<FieldWithoutContent> fieldsToBeFilled;
        try
        {
            //TODO identify with the server whether I am asking for an incident spec or sending an incident.
            Log.i(LOG_TAG, "Attempting to send incident.");
            outStream.writeObject(Commands.IOCommand.GET_FORM);
            outStream.writeUTF(channelName);
            outStream.flush();
            outStream.writeUTF(channelOwner);
            outStream.flush();
            outStream.writeUTF(poster);
            outStream.flush();

            Log.i(LOG_TAG, "Retrieving reply...");
            fieldsToBeFilled = (ArrayList<FieldWithoutContent>) inStream.readObject();
            if (fieldsToBeFilled == null)
                return SocketResult.FAILED_FORMATTING;

            inStream.close();
            outStream.close();
            outSocket.close();
            Log.i(LOG_TAG, "Connection Closed");
        }
        catch (IOException e)
        {
            e.printStackTrace();

            return SocketResult.UNKNOWN_ERROR;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

            return SocketResult.CLASS_NOT_FOUND;

        }

        //TODO some of this code should maybe go on the ui thread.
        activity.setIncident(new AppIncidentWithWrapper(fieldsToBeFilled, channelName, channelOwner,poster, activity));

        return SocketResult.SUCCESS;
    }

    protected void onPostExecute(SocketResult result) {
        makeToastAndLogOnSocketResult(
                activity.getString(R.string.downloaded_incident_spec_for_channel) + " " + channelName + ".",
                activity.getString(R.string.failed_to_download_incident_spec_for_channel) + " " + channelName + ".",
                result,
                activity,
                LOG_TAG);

        if (result.equals(SocketResult.SUCCESS)) {
            activity.renderIncidentFromScratch();
        }
    }
}
