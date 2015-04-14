package ServerClientShared;

/**
 * Created by wsv759 on 18/02/15.
 * A single field within an incident.
 * Includes title, type, and a content field. The content field may be null.
 */
public abstract class FieldWithoutContent extends Field{
    FieldWithoutContent(String title, String type, boolean isRequired)
    {
       super(title, type, isRequired);
    }
}
