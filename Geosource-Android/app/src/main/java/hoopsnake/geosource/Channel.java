package hoopsnake.geosource;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import static junit.framework.Assert.assertTrue;

/**
 * Created by wsv759 on 02/04/15.
 *
 * Just strings representing and owner. A simple pair.
 */
public class Channel implements Serializable, Parcelable {
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    public String getChannelName() {
        return channelName;
    }

    public String getChannelOwner() {
        return channelOwner;
    }

    private String channelName;
    private String channelOwner;

    public Channel(String channelName, String channelOwner)
    {
        this.channelName = channelName;
        this.channelOwner = channelOwner;
    }

    // Parcelling part
    public Channel(Parcel in){
        String[] s = new String[2];
        in.readStringArray(s);

        this.channelName = s[0];
        this.channelOwner = s[1];
    }

    @Override
    public String toString()
    {
        return "channel name: " + channelName + ", channel owner: " + channelOwner;
    }

    public static Channel[] fromStringArray(String[] channelNameChannelOwnerRepeating)
    {
        int numPairs = channelNameChannelOwnerRepeating.length;
        assertTrue(numPairs % 2 == 0);

        Channel[] channels = new Channel[numPairs/2];
        for (int i = 0; i < numPairs; i += 2)
            channels[i] = new Channel(channelNameChannelOwnerRepeating[i], channelNameChannelOwnerRepeating[i + 1]);

        return channels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {channelName, channelOwner});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Channel createFromParcel(Parcel in) {
            return new Channel(in);
        }

        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };
}
