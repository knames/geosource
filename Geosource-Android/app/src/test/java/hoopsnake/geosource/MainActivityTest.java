package hoopsnake.geosource;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {


    private final ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class);
    MainActivity activity;


    @Before
    public void setup()
    {
        activity = controller.create().start().resume().get();
    }

}
