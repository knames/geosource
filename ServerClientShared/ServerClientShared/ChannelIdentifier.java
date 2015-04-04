package ServerClientShared;

import java.io.Serializable;

/**
 * Created by wsv759 on 02/04/15.
 *
 * Just strings representing and owner. A simple pair.
 */
public class ChannelIdentifier implements Serializable {
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

    public ChannelIdentifier(String channelName, String channelOwner)
    {
        this.channelName = channelName;
        this.channelOwner = channelOwner;
    }

    @Override
    public String toString()
    {
        return "channel name: " + channelName + ", channel owner: " + channelOwner;
    }

    public static ChannelIdentifier[] fromStringArray(String[] channelNameChannelOwnerRepeating)
    {
        int numPairs = channelNameChannelOwnerRepeating.length;
        if(numPairs % 2 != 0)
            throw new RuntimeException("channel string array formatted incorrectly.");

        ChannelIdentifier[] channelIdentifiers = new ChannelIdentifier[numPairs/2];
        for (int i = 0; i < numPairs; i += 2)
            channelIdentifiers[i] = new ChannelIdentifier(channelNameChannelOwnerRepeating[i], channelNameChannelOwnerRepeating[i + 1]);

        return channelIdentifiers;
    }
}
