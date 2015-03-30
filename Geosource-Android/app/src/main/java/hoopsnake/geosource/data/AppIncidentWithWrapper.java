package hoopsnake.geosource.data;

import android.content.Context;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import ServerClientShared.AudioFieldWithContent;
import ServerClientShared.AudioFieldWithoutContent;
import ServerClientShared.FieldWithContent;
import ServerClientShared.FieldWithoutContent;
import ServerClientShared.GeotagFieldWithContent;
import ServerClientShared.GeotagFieldWithoutContent;
import ServerClientShared.ImageFieldWithContent;
import ServerClientShared.ImageFieldWithoutContent;
import ServerClientShared.Incident;
import ServerClientShared.StringFieldWithContent;
import ServerClientShared.StringFieldWithoutContent;
import ServerClientShared.VideoFieldWithContent;
import ServerClientShared.VideoFieldWithoutContent;
import hoopsnake.geosource.AppGeotagWrapper;
import hoopsnake.geosource.IncidentActivity;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 07/03/15.
 *
 * Implementation of AppIncident, using a wrapper around a regular Incident to access its basic functionality.
 */
public class AppIncidentWithWrapper implements AppIncident, Serializable {

    private static final String PREFIX_RELATIVE_FILEPATH = "incident_";

    /**
     * the list of app-side fields contained within this incident.
     */
    private final ArrayList<AppField> fieldList;

    /**
     * The basic server-side incident underlying this incident.
     */
    private Incident wrappedIncident;

    /**
     * @param incident a fully constructed incident, containing a list of fields that may have null or non-null content.
     * @param activity the activity displaying this AppIncident.
     * Create a new app incident by wrapping a given incident, and populating the app incident's fieldList by means of
     * the wrapped incident's existing list. See the other constructor for more in-depth comments.
     */
    public AppIncidentWithWrapper(Incident incident, IncidentActivity activity)
    {
        wrappedIncident = incident;
        ArrayList<FieldWithContent> fieldWithContentList = incident.getFieldList();
        fieldList = new ArrayList<AppField>(fieldWithContentList.size());

        int fieldPos = 0;
        for (FieldWithContent fwc : fieldWithContentList)
        {
            //AbstractAppField is guaranteed to keep the fieldWithContent reference
            //passed to it in the constructor up to date.
            AbstractAppField newFieldWrapper;

            switch(fwc.getType())
            {
                case ImageFieldWithoutContent.TYPE:
                    newFieldWrapper = new AppImageField((ImageFieldWithContent) fwc, fieldPos, activity);
                    break;
                case StringFieldWithoutContent.TYPE:
                    newFieldWrapper = new AppStringField((StringFieldWithContent) fwc, fieldPos, activity);
                    break;
                case VideoFieldWithoutContent.TYPE:
                    newFieldWrapper = new AppVideoField((VideoFieldWithContent) fwc, fieldPos, activity);
                    break;
                case AudioFieldWithoutContent.TYPE:
                    newFieldWrapper = new AppAudioField((AudioFieldWithContent) fwc, fieldPos, activity);
                    break;
                case GeotagFieldWithoutContent.TYPE:
                    newFieldWrapper = new AppGeotagField((GeotagFieldWithContent) fwc, fieldPos, activity);
                    break;
                default:
                    throw new RuntimeException("Invalid type " + fwc.getType() + ".");
            }

            fieldList.add(newFieldWrapper);
            fieldPos++;
        }
    }

    /**
     * @param fieldWithoutContentList a list of FieldWithoutContent.
     * @param channelName the name of the channel corresponding to the new incident.
     * @param channelOwner then name of the channel owner corresponding to the new incident.
     * @param poster the username of the poster who authored this incident.(i.e. the app user.)
     * @param activity the activity displaying this AppIncident.
     * @precond fieldWithoutContentList and channelName are not null. And each FieldWithoutContent is also
     *  fully constructed.
     * @postcond
     * Create a new incident by populating its fieldList by means of a
     * fieldWithoutContentList; simply creates a corresponding fieldWithContentList,
     * and adds null content.
     *
     * The various app-side implementations of the different field types are instantiated here,
     * by means of a switch statement on each input field's type.
     *
     * Since each AppField wraps a regular FieldWithContent, the underlying wrappedIncident's
     * list of FieldsWithContent can share the same FieldsWithContent as this AppIncident's list of AppFieldsWithContent.
     * Both lists end up containing references to the same underlying objects.
     *
     * Thus an Incident is fully constructed at the same time as this AppIncident, and will share all its
     * future modifications (and all the new content that is added).
     */
    public AppIncidentWithWrapper(ArrayList<FieldWithoutContent> fieldWithoutContentList, String channelName, String channelOwner, String poster, IncidentActivity activity)
    {
        int listSize = fieldWithoutContentList.size();
        fieldList = new ArrayList<AppField>(listSize);
        ArrayList<FieldWithContent> fieldWithContentList = new ArrayList<FieldWithContent>(listSize);

        int fieldPos = 0;
        for (FieldWithoutContent fieldWithoutContent : fieldWithoutContentList)
        {
            FieldWithContent newField;

            //AbstractAppField is guaranteed to keep the fieldWithContent reference
            //passed to it in the constructor up to date.
            AbstractAppField newFieldWrapper;

            assertNotNull(fieldWithoutContent);
            switch(fieldWithoutContent.getType())
            {
                case ImageFieldWithoutContent.TYPE:
                    newField = new ImageFieldWithContent((ImageFieldWithoutContent) fieldWithoutContent);
                    newFieldWrapper = new AppImageField((ImageFieldWithContent) newField, fieldPos, activity);
                    break;
                case StringFieldWithoutContent.TYPE:
                    newField = new StringFieldWithContent((StringFieldWithoutContent) fieldWithoutContent);
                    newFieldWrapper = new AppStringField((StringFieldWithContent) newField, fieldPos, activity);
                    break;
                case VideoFieldWithoutContent.TYPE:
                    newField = new VideoFieldWithContent((VideoFieldWithoutContent) fieldWithoutContent);
                    newFieldWrapper = new AppVideoField((VideoFieldWithContent) newField, fieldPos, activity);
                    break;
                case AudioFieldWithoutContent.TYPE:
                    newField = new AudioFieldWithContent((AudioFieldWithoutContent) fieldWithoutContent);
                    newFieldWrapper = new AppAudioField((AudioFieldWithContent) newField, fieldPos, activity);
                    break;
                case GeotagFieldWithoutContent.TYPE:
                    newField = new GeotagFieldWithContent((GeotagFieldWithoutContent) fieldWithoutContent);
                    newFieldWrapper = new AppGeotagField((GeotagFieldWithContent) newField, fieldPos, activity);
                    break;
                default:
                    throw new RuntimeException("Invalid type " + fieldWithoutContent.getType() + ".");
            }

            //These now refer to the same underlying fields in each item. They are parallel lists.
            fieldWithContentList.add(newField);
            fieldList.add(newFieldWrapper);
            fieldPos++;
        }

        wrappedIncident = new Incident(fieldWithContentList, channelName, channelOwner, poster);
    }

    @Override
    public boolean isCompletelyFilledIn()
    {
        assertNotNull(fieldList);

        for (AppField fieldWithContent : fieldList)
        {
            assertNotNull(fieldWithContent);

            if (!fieldWithContent.contentIsFilled() && fieldWithContent.isRequired())
                return false;
        }

        return !getChannelName().isEmpty() && !getChannelOwner().isEmpty() && !getIncidentAuthor().isEmpty();
    }

    /* Because of the way this AppIncident implementation is constructed, the underlying wrappedIncident can just be returned directly. */
    @Override
    public Incident toIncident() {
        return wrappedIncident;
    }

    @Override
    public String getChannelName() {
        return wrappedIncident.getChannelName();
    }

    /**
     * @precond none.
     * @postcond see return.
     * @return the channel owner associated with this incident. Guaranteed to be non-null.
     */
    @Override
    public String getChannelOwner()
    {
        return wrappedIncident.getOwnerName();
    }

    @Override
    public String getIncidentAuthor() {
        return wrappedIncident.getPosterName();
    }

    @Override
    public ArrayList<AppField> getFieldList() {
        return fieldList;
    }

    @Override
    public void setGeotag(AppGeotagWrapper geotag) {
        AppGeotagField geotagField = getGeotagField();
        geotagField.registerForGeotag(geotag);
    }

    @Override
    public File getFile(IncidentActivity activity) {
        if (!isCompletelyFilledIn())
            return null;

        AppGeotagField geotagField = getGeotagField();
        String relativeFilePath = PREFIX_RELATIVE_FILEPATH + geotagField.getTimestampStringRepresentation();

        return new File(activity.getDir(IncidentActivity.DIRNAME_INCIDENTS_YET_TO_SEND, Context.MODE_PRIVATE), relativeFilePath);
    }

    private AppGeotagField getGeotagField()
    {
        return (AppGeotagField) fieldList.get(Incident.POSITION_GEOTAG_FIELD);
    }
}
