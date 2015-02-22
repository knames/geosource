package hoopsnake.geosource;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import junit.framework.Assert;

public class MainActivityIT extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public MainActivityIT()
    {
        super(MainActivity.class);
    }

    //Just a basic test I wrote to look at things.
    /*public void testIfTextAppears() throws Exception
    {
        //Check to see if we are in the right activity
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

        solo.clickOnButton(solo.getString(R.string.add_audio));

        //Cool fact: It will accept partial matches: i.e if Hello World is on the screen, looking for Hello asserts to true.
        assertTrue(solo.searchText("Hello"));
    }*/

    /* Currently tests these things:
    *  Checks to see if Audio Button can be pressed and outputs proper error message.
    *  Checks to see if Camera can successfully save photo
    *  Checks to see if Camera can successfully save video
     */
    //Checks to see if basic functionality is working.
    public void testSanity() throws Exception
    {
        //Check to see if we are in the right activity
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        this.testAudio();
        assertTrue(1==2);
        //cameraTest();
        //VideoTest();

    }

    //For now, tests to see if the button can be clicked and gives the proper error message.
    //Audio recordings have not been implemented yet.
    public void testAudio() throws Exception
    {
        //Check to see if we are in the right activity
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

        solo.clickOnButton(solo.getString(R.string.add_audio));

        //Cool fact: It will accept partial matches: i.e if Hello World is on the screen, looking for Hello asserts to true.
        assertTrue(solo.searchText("audio recording not yet implemented"));
    }





    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testSuccess() {
        Assert.assertTrue(true);
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}