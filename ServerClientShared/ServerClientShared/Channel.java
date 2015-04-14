package ServerClientShared;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wsv759 on 04/04/15.
 */
public class Channel implements Serializable {
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    public ChannelIdentifier getIdentifier() {
        return identifier;
    }

    public ArrayList<FieldWithoutContent> getIncidentSpec() {
        return incidentSpec;
    }

    private ChannelIdentifier identifier;

    private ArrayList<FieldWithoutContent> incidentSpec;

    public Channel(ChannelIdentifier identifier, ArrayList<FieldWithoutContent> incidentSpec)
    {
        if (identifier == null || incidentSpec == null)
            throw new RuntimeException("parameters to channel constructor cannot be null.");

        this.identifier = identifier;
        this.incidentSpec = incidentSpec;
    }
}
