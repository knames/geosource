package hoopsnake.geosource.data;

import android.app.Activity;

import java.io.File;
import java.util.ArrayList;

import ServerClientShared.Incident;
import hoopsnake.geosource.AppGeotagWrapper;

/**
 * Created by wsv759 on 08/03/15.
 *
 * Interface for Incidents to be accessed by UI and control level of the android app.
 * This is as opposed to the vanilla java Incidents that are passed between the client and server.
 * Implementations of this will probably be constructed out of and deconstructed into regular Incidents,
 * when it's time to send over the network.
 *
 * Any implementation of this must implement Serializable.
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
     * @return the channel name associated with this incident.This is guaranteed to be non-null.
     */
    public String getChannelName();

    /**
     * @precond none.
     * @postcond see return.
     * @return the channel owner associated with this incident.This is guaranteed to be non-null.
     */
    public String getChannelOwner();

    /**
     * @precond none.
     * @postcond see return.
     * @return the author associated with this incident. (i.e. the current user logged into the app.)
     * This is guaranteed to be non-null.
     */
    public String getIncidentAuthor();

    /**
     * @precond none.
     * @postcond see return.
     * @return the field list underlying this incident. May be null.
     */
    public ArrayList<AppField> getFieldList();

    /**
     * @precond geotag not null.
     * @postcond this incident's geotag is updated to the new one.
     * @param geotag
     */
    public void setGeotag(AppGeotagWrapper geotag);

    /**
     *
     * @return the file associated with this incident, for storage purposes. Not to be confused with IncidentActivity.FILENAME_CUR_INCIDENT,
     * which contains a rotating copy of the current unfinished incident.
     *
     * If this incident had no file prior to this call, a new file is returned. Otherwise the associated file is returned.
     *
     * If this Incident is not completely filled in, null is returned.
     *
     * @param activity needed to resolve the absolute path name.
     */
    public File getFile(Activity activity);
}
