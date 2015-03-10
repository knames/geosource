package hoopsnake.geosource.data;

import java.util.ArrayList;

/**
 * Created by wsv759 on 08/03/15.
 *
 * Interface for Incidents to be accessed by UI and control level of the android app.
 * This is as opposed to the vanilla java Incidents that are passed between the client and server.
 * Implementations of this will probably be constructed out of and deconstructed into regular Incidents,
 * when it's time to send over the network.
 */
public interface AppIncident
{
    /**
     * @precond the underlying FieldList is not null.
     * @postcond see return.
     * @return true if this incident's required fields are all filled in
     *  AND its channel name has been properly set, or false otherwise.
     */
    public boolean isCompletelyFilledIn();

    /**
     * @precond the underlying FieldList is not null.
     * @postcond see return.
     * @return an Incident object equivalent to this AppIncident, with the same underlying fields and content,
     *  formatted for client-server interaction.
     */
    public Incident toIncident();

    /**
     * @precond none.
     * @postcond see return.
     * @return the channel name associated with this incident.This is not guaranteed to be non-null.
     */
    public String getChannelName();

    /**
     * @precond newChannelName is a not null, and corresponds to an existing channel name.
     * @param newChannelName a String representation of the name of the channel corresponding to this incident.
     * @postcond the channel name of this incident is set to the new name.
     */
    public void setChannelName(String newChannelName);

    /**
     * @precond none.
     * @postcond see return.
     * @return the field list underlying this incident. May be null.
     */
    public ArrayList<AppFieldWithContent> getFieldList();
}
