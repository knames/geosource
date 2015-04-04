package hoopsnake.geosource.data;

import android.os.Parcelable;

import java.io.Serializable;

import ServerClientShared.Channel;

/**
 * Created by wsv759 on 04/04/15.
 */
public interface AppChannelIdentifier extends Parcelable, Serializable {
    public String getChannelName();
    public String getChannelOwner();

    public boolean matchesChannel(Channel channel);
}
