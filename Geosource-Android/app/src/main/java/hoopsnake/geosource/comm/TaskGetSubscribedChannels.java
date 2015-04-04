package hoopsnake.geosource.comm;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ServerClientShared.Channel;
import ServerClientShared.Commands;
import hoopsnake.geosource.FileIO;
import hoopsnake.geosource.MainActivity;
import hoopsnake.geosource.R;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 04/04/15.
 */
public class TaskGetSubscribedChannels extends AsyncTask<String, Void, SocketResult> {
    private static final String LOG_TAG = "geosource comm";
    private Activity activity;

    public TaskGetSubscribedChannels(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected SocketResult doInBackground(String... params) {
        String userId = params[0];
        assertNotNull(userId);

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


        Channel[] subscribedChannels;
        try
        {
            Log.i(LOG_TAG, "Attempting to get subscribed Channels.");
            outStream.writeObject(Commands.IOCommand.GET_SUBSCRIBED_CHANNELS);
            outStream.writeUTF(userId);
            outStream.flush();

            Log.i(LOG_TAG, "Retrieving reply...");
            subscribedChannels = (Channel[]) inStream.readObject();

            if (subscribedChannels == null)
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

        if (FileIO.writeObjectToFile(activity, subscribedChannels, MainActivity.FILENAME_SUBSCRIBED_CHANNELS))
            return SocketResult.SUCCESS;
        else
            return SocketResult.FAILED_FORMATTING; //TODO this should not be a socket result, as it is local, after the socket succeeded.
    }

    @Override
    protected void onPostExecute(SocketResult result)
    {
        result.makeToastAndLog(
                activity.getString(R.string.downloaded_subscribed_channels),
                activity.getString(R.string.failed_to_download_subscribed_channels),
                activity,
                LOG_TAG);
    }
}
