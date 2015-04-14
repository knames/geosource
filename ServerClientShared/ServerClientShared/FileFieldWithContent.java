package ServerClientShared;

import java.io.Serializable;

/**
 * Created by wsv759 on 12/03/15.
 */
public abstract class FileFieldWithContent extends FieldWithContent{
    FileFieldWithContent(FieldWithoutContent fieldWithoutContent) {
        super(fieldWithoutContent);
    }

    @Override
    public boolean contentMatchesType(Serializable content)
    {
        return content instanceof byte[];
    }
}
