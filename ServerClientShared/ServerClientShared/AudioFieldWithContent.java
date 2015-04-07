package ServerClientShared;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by wsv759 on 12/03/15.
 */
public class AudioFieldWithContent extends FileFieldWithContent{
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    public AudioFieldWithContent(AudioFieldWithoutContent fieldWithoutContent) {
        super(fieldWithoutContent);
    }

    @Override
    public boolean contentMatchesType(Serializable content)
    {
        if (!super.contentMatchesType(content))
            return false;

        //TODO add extra tests?
        return true;
    }

    @Override
    public void write(String folderPath) throws IOException {
        File newFile = new File(folderPath + ".mp3");
        FileOutputStream fileOut = new FileOutputStream(newFile);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(content);
        out.flush();
        out.close();
    }

}
