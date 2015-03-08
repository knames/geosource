package hoopsnake.geosource.data;

import java.util.ArrayList;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 07/03/15.
 */
public class AppIncidentWithWrapper implements AppIncident {

    protected ArrayList<AppFieldWithContent> fieldList;

    protected Incident wrappedIncident;

    /** Create a new incident by populating its fieldList by means of a
     * fieldWithoutContentList, and adding null content.
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

            AppFieldWithContent newFieldWithContentWrapper;
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

            //Ideally, these should now refer to the same underlying fields in each item.
            fieldWithContentList.add(newField);
            fieldList.add(newFieldWithContentWrapper);
        }

        //TODO test to see if this works. Do the references in the fieldWithContentList refer to the same fields as the references in fieldList?
        wrappedIncident.setFieldList(fieldWithContentList);
    }

    /** Is the incident ready to be shipped off? That is, has it been completely filled out? */
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
