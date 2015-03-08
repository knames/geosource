package hoopsnake.geosource.data;
//TODO All the Field classes are now shared. remove them and import instead.
/**
 * Created by wsv759 on 19/02/15.
 *
 * A single field within an incident. Does not include a content field by default.
 */
public abstract class Field {
    protected String title;
    protected FieldType type;
    protected boolean isRequired;

    public FieldType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }


    public boolean isRequired() {
        return isRequired;
    }

    public Field(String title, FieldType type, boolean isRequired)
    {
        this.title = title;
        this.type = type;
        this.isRequired = isRequired;

        if (title == null)
            throw new RuntimeException(title + " is null.");
        if (type == null)
            throw new RuntimeException(type + " is null.");
    }
}

