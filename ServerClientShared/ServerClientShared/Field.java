package ServerClientShared;

import java.io.Serializable;

/**
 * Created by wsv759 on 19/02/15.
 *
 * A single field within an incident. Does not include a content field by default.
 */
public abstract class Field implements Serializable {
    private String title;
    private String type;
    private boolean isRequired;

    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }


    public boolean isRequired() {
        return isRequired;
    }

    Field(String title, String type, boolean isRequired)
    {
        this.title = title;
        this.type = type;
        this.isRequired = isRequired;

        if (title == null)
		    throw new RuntimeException("title is null.");
        if (type == null)
		    throw new RuntimeException("type is null.");
    }
}
