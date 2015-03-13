package ServerClientShared;

import java.io.Serializable;

/**
 * Created by wsv759 on 18/02/15.
 * A single field within an incident.
 * Includes title, type, and a content field. The content field may be null.
 */
public abstract class FieldWithoutContent extends Field implements Serializable {

    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    public FieldWithoutContent(String title, String type, boolean isRequired)
    {
       super(title, type, isRequired);
    }
}
