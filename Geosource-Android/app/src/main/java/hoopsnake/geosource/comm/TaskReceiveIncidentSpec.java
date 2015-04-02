package hoopsnake.geosource.comm;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
public class TaskReceiveIncidentSpec extends IncidentActivityCommTask<String, Void, SocketResult> {
    String channelName, channelOwner, poster;
    IncidentActivity activity;
    public TaskReceiveIncidentSpec(IncidentActivity activity)
    {
        this.activity = activity;
    }

    protected SocketResult doInBackground(String... params) {

        channelName = params[0];
        channelOwner = params[1];
        poster = params[2];
        assertNotNull(channelName);
        assertNotNull(channelOwner);
        assertNotNull(poster);

        SocketWrapper socketWrapper;
        ObjectOutputStream outStream; //wrapped stream to client
        ObjectInputStream inStream; //stream from client
        try //create socket
        {
            socketWrapper = new SocketWrapper();
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
        }
        catch (IOException e)
        {
            e.printStackTrace();

            return SocketResult.UNKNOWN_ERROR;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

            return SocketResult.CLASS_NOT_FOUND;
        } finally {
            socketWrapper.closeAll();
        }

        //TODO some of this code should maybe go on the ui thread.
        activity.setIncident(new AppIncidentWithWrapper(fieldsToBeFilled, channelName, channelOwner, poster, activity));

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
            activity.renderIncidentFromScratch(true);
        }
    }
}
