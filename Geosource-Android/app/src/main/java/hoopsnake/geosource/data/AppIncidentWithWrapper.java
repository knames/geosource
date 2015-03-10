package hoopsnake.geosource.data;

import java.util.ArrayList;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 07/03/15.
 *
 * Implementation of AppIncident, using a wrapper around a regular Incident to access its basic functionality.
 */
public class AppIncidentWithWrapper implements AppIncident {

    /**
     * the list of app-side fields contained within this incident.
     */
    protected ArrayList<AppFieldWithContent> fieldList;

    /**
     * The basic server-side incident underlying this incident.
     */
    protected Incident wrappedIncident;

    /**
     * Create a new incident by populating its fieldList by means of a
     * fieldWithoutContentList; simply creates a corresponding fieldWithContentList,
     * and adds null content.
     *
     * The various app-side implementations of the different field types are instantiated here,
     * by means of a switch statement on each input field's type.
     *
     * Since each AppFieldWithContent wraps a regular FieldWithContent, the underlying wrappedIncident's
     * list of FieldsWithContent can share the same FieldsWithContent as this AppIncident's list of AppFieldsWithContent.
     * Both lists end up containing references to the same underlying objects.
     *
     * Thus an Incident is fully constructed at the same time as this AppIncident, and will share all its
     * future modifications (and all the new content that is added).
     */
    public AppIncidentWithWrapper(ArrayList<FieldWithoutContent> fieldWithoutContentList)
    {
        wrappedIncident = new Incident();

        int listSize = fieldWithoutContentList.size();
        fieldList = new ArrayList<AppFieldWithContent>(listSize);
        ArrayList<FieldWithContent> fieldWithContentList = new ArrayList<FieldWithContent>(listSize);

        for (FieldWithoutContent fieldWithoutContent : fieldWithoutContentList)
        {
            FieldWithContent newField = new FieldWithContent(fieldWithoutContent);

            //AbstractAppFieldWithContent is guaranteed to keep the fieldWithContent reference
            //passed to it in the constructor up to date.
            AbstractAppFieldWithContent newFieldWithContentWrapper;
            switch(fieldWithoutContent.getType())
            {
                case IMAGE:
                    newFieldWithContentWrapper = new ImageField(newField);
                    break;
                case STRING:
                    newFieldWithContentWrapper = new StringField(newField);
                    break;
                case VIDEO:
                    newFieldWithContentWrapper = new VideoField(newField);
                    break;
                case AUDIO:
                    newFieldWithContentWrapper = new AudioField(newField);
                    break;
                default:
                    throw new RuntimeException("Invalid type " + fieldWithoutContent.getType() + ".");
            }

            //These now refer to the same underlying fields in each item. They are parallel lists.
            fieldWithContentList.add(newField);
            fieldList.add(newFieldWithContentWrapper);
        }

        //TODO test to see if this works. Do the references in the fieldWithContentList refer to the same fields as the references in fieldList?
        wrappedIncident.setFieldList(fieldWithContentList);
    }

    @Override
    public boolean isCompletelyFilledIn()
    {
        assertNotNull(fieldList);

        for (AppFieldWithContent fieldWithContent : fieldList)
        {
            assertNotNull(fieldWithContent);

            if (!fieldWithContent.contentIsFilled() && fieldWithContent.isRequired())
                return false;
        }

        return true;
    }

    /* Because of the way this AppIncident implementation is constructed, the underlying wrappedIncident can just be returned directly. */
    @Override
    public Incident toIncident() {
        //TODO delete this if the below todo works.
//        ArrayList<FieldWithContent> fieldWithContentList = new ArrayList<FieldWithContent>(fieldList.size());
//
//        for (AppFieldWithContent appField : fieldList)
//        {
//            fieldWithContentList.add(appField.toFieldWithContent());
//        }
//
//        return new Incident(fieldWithContentList);

        //TODO test to see if this works. Do the references in the fieldWithContentList refer to the same fields as the references in fieldList?
        return wrappedIncident;
    }

    @Override
    public String getChannelName() {
        return wrappedIncident.getChannelName();
    }

    @Override
    public void setChannelName(String newChannelName) {
        wrappedIncident.setChannelName(newChannelName);
    }

    @Override
    public ArrayList<AppFieldWithContent> getFieldList() {
        return fieldList;
    }
}