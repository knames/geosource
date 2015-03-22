package hoopsnake.geosource.data;

import android.content.Intent;
import android.view.View;

import ServerClientShared.GeotagFieldWithContent;
import hoopsnake.geosource.Geotag;
import hoopsnake.geosource.IncidentActivity;

/**
 * Created by wsv759 on 21/03/15.
 */
public class AppGeotagField extends AbstractAppField {

    public AppGeotagField(GeotagFieldWithContent fieldToWrap, IncidentActivity activity) {
        super(fieldToWrap, activity);
    }

    @Override
    public String getContentStringRepresentation() {
        return null;
    }

    @Override
    public View getContentViewRepresentation(int requestCodeForIntent) {
        return null;
    }

    @Override
    public void onResultFromSelection(int resultCode, Intent data) {

    }

    /**
     * @precond activity is not null.
     * @postcond This geotag's location and timestamp are updated to their last known values.
     */
    public void setGeotag(Geotag geotag) {
        ((GeotagFieldWithContent) wrappedField).updateContent(geotag.getTimestamp(), geotag.getLongitude(), geotag.getLatitude());
    }
}
