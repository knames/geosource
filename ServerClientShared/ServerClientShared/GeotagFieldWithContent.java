package ServerClientShared;

import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    @Override
    public void write(String folderPath) throws IOException {
        File newFile = new File(folderPath + ".json");
        FileWriter fileOut = new FileWriter(newFile);
        JsonWriter out = new JsonWriter(fileOut);
        Geotag geotag = (Geotag)content;
        out.beginObject();
        out.name("location");
        out.beginObject();
        out.name("lat");
        out.value(geotag.getLatitude());
        out.name("lng");
        out.value(geotag.getLongitude());
        out.endObject();
        out.name("time");
        out.value(geotag.getTimestamp());
        out.endObject();
    }

}
