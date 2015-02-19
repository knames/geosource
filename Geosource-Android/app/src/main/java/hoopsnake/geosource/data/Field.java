package hoopsnake.geosource.data;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 19/02/15.
 */
public abstract class Field {
    String title;

    public FieldTypeManagement.FieldType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    FieldTypeManagement.FieldType type;

    public Field(String title, FieldTypeManagement.FieldType type)
    {
        this.title = title;
        this.type = type;

        assertNotNull(title);
        assertNotNull(type);
    }
}
