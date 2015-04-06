package hoopsnake.geosource.comm;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.LinkedList;

import ServerClientShared.Channel;
import ServerClientShared.Commands;
import hoopsnake.geosource.FileIO;
import hoopsnake.geosource.MainActivity;
import hoopsnake.geosource.R;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 06/04/15.
 */
public class RunnableGetSubscribedChannels implements Runnable {

        private static final String LOG_TAG = "geosource comm";
        private WeakReference<Activity> activityRef;
        private String userId;
        private String absFileName;
        public RunnableGetSubscribedChannels(Activity activity, String userId) {
            assertNotNull(activity);
            assertNotNull(userId);

            absFileName = activity.getFilesDir().getAbsolutePath() + "/" + MainActivity.FILENAME_SUBSCRIBED_CHANNELS;
            this.activityRef = new WeakReference<Activity>(activity);
            this.userId = userId;

        }

        protected SocketResult doInBackground() {

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
                LinkedList<Channel> listSubscribedChannels = (LinkedList<Channel>) inStream.readObject();
                if (listSubscribedChannels == null)
                    return SocketResult.FAILED_FORMATTING;

                subscribedChannels  = listSubscribedChannels.toArray(new Channel[listSubscribedChannels.size()]);
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

            if (FileIO.writeObjectToFileNoContext(subscribedChannels, absFileName))
                return SocketResult.SUCCESS;
            else
                return SocketResult.FAILED_FORMATTING; //TODO this should not be a socket result, as it is local, after the socket succeeded.
        }

    protected void onPostExecute(SocketResult result)
    {
        if (activityRef != null) {
            Activity activity = activityRef.get();
            if (activity != null)
                result.makeToastAndLog(
                        activity.getString(R.string.downloaded_subscribed_channels),
                        activity.getString(R.string.failed_to_download_subscribed_channels),
                        activity,
                        LOG_TAG);
        }
    }


    @Override
    public void run() {
        SocketResult result = doInBackground();
        onPostExecute(result);
    }
}
