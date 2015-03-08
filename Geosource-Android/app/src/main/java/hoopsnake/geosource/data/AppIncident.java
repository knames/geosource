package hoopsnake.geosource.data;

import java.util.ArrayList;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 07/03/15.
 */
public class AppIncident extends Incident {
    ArrayList<AppFieldWithContent> fieldList;

    /** Create a new incident by populating its fieldList by means of a
     * fieldWithoutContentList, and adding null content. */
    public AppIncident(ArrayList<FieldWithoutContent> fieldWithoutContentList)
    {
        fieldList = new ArrayList<AppFieldWithContent>();
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

            fieldList.add(newFieldWithContentWrapper);
        }
    }

    /** Is the incident ready to be shipped off? That is, has it been completely filled out? */
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
}
