package ServerClientShared;

import java.io.Serializable;

/**
 * Created by wsv759 on 21/03/15.
 */
public class GeotagFieldWithContent extends FieldWithContent {
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    public GeotagFieldWithContent(GeotagFieldWithoutContent fieldWithoutContent) {
        super(fieldWithoutContent);
    }

    @Override
    public boolean contentMatchesType(Serializable content) {
        return content instanceof Geotag;
    }
}
