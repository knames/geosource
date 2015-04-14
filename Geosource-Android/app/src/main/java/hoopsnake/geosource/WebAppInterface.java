package hoopsnake.geosource;

import android.content.Context;
import android.widget.Toast;

import org.xwalk.core.JavascriptInterface;

public class WebAppInterface {
    MainActivity mContext;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = (MainActivity) c;
    }

    @JavascriptInterface
    public void login(String gid) {
        mContext.login(gid);
        Toast.makeText(mContext, "android logged in", Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void logout() {
        mContext.logout();
    }
}