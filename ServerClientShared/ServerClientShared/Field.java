package ServerClientShared;

/**
 * Created by wsv759 on 19/02/15.
 *
 * A single field within an incident. Does not include a content field by default.
 */
public abstract class Field {
    protected String title;
    protected String type;
    protected boolean isRequired;

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }


    public boolean isRequired() {
        return isRequired;
    }

    public Field(String title, String type, boolean isRequired)
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
