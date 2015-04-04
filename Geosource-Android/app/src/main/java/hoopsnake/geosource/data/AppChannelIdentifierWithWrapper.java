package hoopsnake.geosource.data;

import android.os.Parcel;
import android.os.Parcelable;

import ServerClientShared.Channel;
import ServerClientShared.ChannelIdentifier;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 04/04/15.
 */
public class AppChannelIdentifierWithWrapper implements AppChannelIdentifier {
    private ChannelIdentifier wrappedChannelIdentifier;

    public AppChannelIdentifierWithWrapper(ChannelIdentifier channelIdentifierToWrap)
    {
        assertNotNull(channelIdentifierToWrap);
        this.wrappedChannelIdentifier = channelIdentifierToWrap;
    }

    @Override
    public String getChannelName() {
        return wrappedChannelIdentifier.getChannelName();
    }

    @Override
    public String getChannelOwner() {
        return wrappedChannelIdentifier.getChannelOwner();
    }

    @Override
    public boolean matchesChannel(Channel channel) {
        return wrappedChannelIdentifier.equals(channel.getIdentifier());
    }

    // Parcelling part
    public AppChannelIdentifierWithWrapper(Parcel in){
        String[] s = new String[2];
        in.readStringArray(s);

        wrappedChannelIdentifier = new ChannelIdentifier(s[0], s[1]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {wrappedChannelIdentifier.getChannelName(), wrappedChannelIdentifier.getChannelOwner()});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AppChannelIdentifierWithWrapper createFromParcel(Parcel in) {
            return new AppChannelIdentifierWithWrapper(in);
        }

        public AppChannelIdentifierWithWrapper[] newArray(int size) {
            return new AppChannelIdentifierWithWrapper[size];
        }
    };
}
