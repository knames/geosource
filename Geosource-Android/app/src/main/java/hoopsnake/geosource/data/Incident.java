package hoopsnake.geosource.data;

import java.util.ArrayList;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 19/02/15.
 */
public class Incident {

    /** The fields for this incident, including their content. Their content may be null. */
    ArrayList<FieldWithContent> fieldWithContentList;

    /** Create a new incident by populating its fieldWithContentList by means of a
     * fieldWithoutContentList, and adding null content. */
    public Incident(ArrayList<FieldWithoutContent> fieldWithoutContentList)
    {
        fieldWithContentList = new ArrayList<FieldWithContent>();

        for (FieldWithoutContent fieldWithoutContent : fieldWithoutContentList)
        {
            FieldWithContent newFieldWithContent = new FieldWithContent(
                    fieldWithoutContent.getTitle(), fieldWithoutContent.getType(), fieldWithoutContent.isRequired(), null);
            fieldWithContentList.add(newFieldWithContent);
        }
    }

    public ArrayList<FieldWithContent> getFieldWithContentList() {
        return fieldWithContentList;
    }

    /** Is the incident ready to be shipped off? That is, has it been completely filled out? */
    public boolean isCompletelyFilledIn()
    {
        assertNotNull(fieldWithContentList);

        for (FieldWithContent fieldWithContent : fieldWithContentList)
        {
            if (fieldWithContent.getContent() == null)
                return false;
        }

        return true;
    }
}
