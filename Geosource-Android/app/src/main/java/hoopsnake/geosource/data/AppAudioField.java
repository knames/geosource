package hoopsnake.geosource.data;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;

import ServerClientShared.AudioFieldWithContent;
import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.R;
import hoopsnake.geosource.media.MediaManagement;


/**
 * Created by wsv759 on 07/03/15.
 *
 * Implementation of an app field with type Audio.
 */
public class AppAudioField extends AbstractAppFieldWithFile{

    /**
     * The button used to start/stop audio recording.
     */
    private ToggleButton recordButton;

    public AppAudioField(AudioFieldWithContent fieldToWrap, IncidentActivity activity) {
        super(fieldToWrap, activity);
    }

    @Override
    public boolean usesFilesOfType(Uri contentFileUri) {
        //TODO implement this.
        return true;
    }

    /**
     * make sure that the content file Uri is set AND it isn't currently being recorded to.
     * @return
     */
    @Override
    public boolean contentIsFilled()
    {
        return super.contentIsFilled() && recordButton != null && !recordButton.isChecked();
    }

    /**
     * TODO cause the filled content view representation to actually happen.
     * Currently all I have is a hacky solution that enables recording but not playback. and it just records a new file each time, keeping only
     * the latest one. Baaad!
     */

    @Override
    View getFilledContentViewRepresentation() {


        return getEmptyContentViewRepresentation(-1);
    }

    @Override
    View getEmptyContentViewRepresentation(final int requestCodeForIntent) {
        recordButton = (ToggleButton) activity.getLayoutInflater().inflate(R.layout.field_audio_toggle_button, null);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recordButton.isChecked()) {
                    Uri fileUriForNewAudio = MediaManagement.getOutputImageFileUri();
                    if (fileUriForNewAudio == null) {
                        Toast.makeText(activity, "Cannot record audio; new audio file could not be created on external storage device.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    MediaManagement.getAudioRecording(fileUriForNewAudio);

                    setContentFileUri(fileUriForNewAudio);
                } else {
                    MediaManagement.stopAudioRecording();
                }
            }
        });

        return recordButton;
    }

    @Override
    public void onResultFromSelection(int resultCode, Intent data) {}

    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    private void writeObject(ObjectOutputStream out) throws IOException
    {

    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {

    }

    private void readObjectNoData() throws ObjectStreamException
    {

    }
}
