package ServerClientShared;

import java.io.Serializable;

/**
 * Created by wsv759 on 12/03/15.
 */
public class StringFieldWithContent extends FieldWithContent{
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    public StringFieldWithContent(StringFieldWithoutContent fieldWithoutContent) {
        super(fieldWithoutContent);
    }

    @Override
    public boolean contentMatchesType(Serializable content) {
        return content instanceof String;
    }
}
