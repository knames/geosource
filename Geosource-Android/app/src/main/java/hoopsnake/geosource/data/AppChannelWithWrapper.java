package hoopsnake.geosource.data;

import android.os.Parcel;
import android.os.Parcelable;

import ServerClientShared.Channel;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 04/04/15.
 */
public class AppChannelWithWrapper implements AppChannel {
    private Channel wrappedChannel;

    public AppChannelWithWrapper(Channel channelToWrap)
    {
        assertNotNull(channelToWrap);
        this.wrappedChannel = channelToWrap;
    }

    @Override
    public String getChannelName() {
        return wrappedChannel.getChannelName();
    }

    @Override
    public String getChannelOwner() {
        return wrappedChannel.getChannelOwner();
    }

    // Parcelling part
    public AppChannelWithWrapper(Parcel in){
        String[] s = new String[2];
        in.readStringArray(s);

        wrappedChannel = new Channel(s[0], s[1]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {wrappedChannel.getChannelName(), wrappedChannel.getChannelOwner()});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AppChannelWithWrapper createFromParcel(Parcel in) {
            return new AppChannelWithWrapper(in);
        }

        public AppChannelWithWrapper[] newArray(int size) {
            return new AppChannelWithWrapper[size];
        }
    };
}
