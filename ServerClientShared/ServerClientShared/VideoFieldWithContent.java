package ServerClientShared;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by wsv759 on 12/03/15.
 */
public class VideoFieldWithContent extends FileFieldWithContent{
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    public VideoFieldWithContent(VideoFieldWithoutContent fieldWithoutContent) {
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
        File newFile = new File(folderPath + ".mp4");
        FileOutputStream fileOut = new FileOutputStream(newFile);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] contentArray = (byte[]) content;
        out.write(contentArray, 0, contentArray.length);
        out.writeTo(fileOut);
        fileOut.flush();
        fileOut.close();
        out.flush();
        out.close();
    }
    
}
