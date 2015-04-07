package ServerClientShared;

import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    @Override
    public void write(String folderPath) throws IOException {
        File newFile = new File(folderPath + ".json");
        FileWriter fileOut = new FileWriter(newFile);
        JsonWriter out = new JsonWriter(fileOut);
        out.beginObject();
        out.name("string");
        out.value((String)content);
        out.endObject();
    }

}
