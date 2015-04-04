package hoopsnake.geosource.data;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by wsv759 on 04/04/15.
 */
public interface AppChannel extends Parcelable, Serializable {
    public String getChannelName();
    public String getChannelOwner();
}
