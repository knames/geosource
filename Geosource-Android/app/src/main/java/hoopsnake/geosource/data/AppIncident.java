package hoopsnake.geosource.data;

import java.util.ArrayList;

/**
 * Created by wsv759 on 08/03/15.
 */
public interface AppIncident
{
    /** Is the incident ready to be shipped off? That is, has it been completely filled out? */
    public boolean isCompletelyFilledIn();

    /** Convert it to a basic incident, so that it can be sent via socket connection. */
    public Incident toIncident();

    /** Get the channel name associated with this incident. */
    public String getChannelName();

    /** Set the channel name associated with this incident. */
    public void setChannelName(String newChannelName);

    /** Get the field list underlying this incident. */
    public ArrayList<AppFieldWithContent> getFieldList();
}
