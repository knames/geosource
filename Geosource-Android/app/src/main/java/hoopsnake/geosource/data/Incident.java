package hoopsnake.geosource.data;

import java.util.LinkedList;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 19/02/15.
 */
public class Incident {

    public LinkedList<FullField> getFullFieldList() {
        return fullFieldList;
    }

    LinkedList<FullField> fullFieldList;

    public Incident(LinkedList<EmptyField> emptyFieldList)
    {
        fullFieldList = new LinkedList<FullField>();

        for (EmptyField emptyField : emptyFieldList)
        {
            FullField newFullField = new FullField(emptyField.getTitle(), emptyField.getType(), null);
            fullFieldList.add(newFullField);
        }
    }

    /** Is the incident ready to be shipped off? That is, has it been completely filled out? */
    public boolean isCompletelyFilledIn()
    {
        assertNotNull(fullFieldList);

        for (FullField fullField : fullFieldList)
        {
            if (fullField.getContent() == null)
                return false;
        }

        return true;
    }
}
