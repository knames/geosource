package ServerClientShared;

/**
 * Created by wsv759 on 12/03/15.
 */
public class AudioFieldWithoutContent extends FieldWithoutContent{
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    public static final String TYPE = "audio";

    public AudioFieldWithoutContent(String title, boolean isRequired) {
        super(title, TYPE, isRequired);
    }
}
