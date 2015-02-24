package hoopsnake.geosource.data;

import java.util.ArrayList;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 19/02/15.
 */
public class Incident {

    /** The fields for this incident, including their content. Their content may be null. */
    ArrayList<FieldWithContent> fieldList;

    /** Create a new incident by populating its fieldList by means of a
     * fieldWithoutContentList, and adding null content. */
    public Incident(ArrayList<FieldWithoutContent> fieldWithoutContentList)
    {
        fieldList = new ArrayList<FieldWithContent>();

        for (FieldWithoutContent fieldWithoutContent : fieldWithoutContentList)
        {
            FieldWithContent newFieldWithContent = new FieldWithContent(
                    fieldWithoutContent.getTitle(), fieldWithoutContent.getType(), fieldWithoutContent.isRequired());
            fieldList.add(newFieldWithContent);
        }
    }

    public ArrayList<FieldWithContent> getFieldList() {
        return fieldList;
    }

    /** Is the incident ready to be shipped off? That is, has it been completely filled out? */
    public boolean isCompletelyFilledIn()
    {
        assertNotNull(fieldList);

        for (FieldWithContent fieldWithContent : fieldList)
        {
            assertNotNull(fieldWithContent);

            if (!fieldWithContent.contentIsFilled() && fieldWithContent.isRequired())
                return false;
        }

        return true;
    }
}
