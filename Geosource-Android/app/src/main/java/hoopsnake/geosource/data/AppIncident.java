package hoopsnake.geosource.data;

import java.util.ArrayList;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 07/03/15.
 */
public class AppIncident extends Incident {


    /** Create a new incident by populating its fieldList by means of a
     * fieldWithoutContentList, and adding null content. */
    public AppIncident(ArrayList<FieldWithoutContent> fieldWithoutContentList)
    {
        for (FieldWithoutContent fieldWithoutContent : fieldWithoutContentList)
        {
            FieldWithContent newFieldWithContent = new FieldWithContent(
                    fieldWithoutContent.getTitle(), fieldWithoutContent.getType(), fieldWithoutContent.isRequired());
            fieldList.add(newFieldWithContent);
        }
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
