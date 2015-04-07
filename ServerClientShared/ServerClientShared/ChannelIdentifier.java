package ServerClientShared;

import java.io.Serializable;

/**
 * Created by wsv759 on 02/04/15.
 *
 * Just strings representing and owner. A simple pair.
 */
public class ChannelIdentifier implements Serializable, Comparable<ChannelIdentifier> {
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

    @Override
    public int compareTo(ChannelIdentifier another) {
        int nameComparisonResult = channelName.compareTo(another.channelName);
        if (nameComparisonResult == 0)
            return channelOwner.compareTo(another.channelOwner);
        else
            return nameComparisonResult;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof ChannelIdentifier))
            return false;
        else if (obj == this)
            return true;

        ChannelIdentifier other = (ChannelIdentifier) obj;

        return channelName.equals(other.channelName) && channelOwner.equals(other.channelOwner);
    }
}
