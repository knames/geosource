package hoopsnake.geosource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ServerClientShared.FieldWithContent;
import ServerClientShared.FieldType;
import hoopsnake.geosource.data.ImageField;
import hoopsnake.geosource.data.StringField;

import static org.junit.Assert.*;

//New System broke all of the tests, none of these work properly now. Will rework later.

/**
 * Created by wwf594 on 07/03/15.
 */
public class FieldUTest
{
    public FieldUTest()
    {
    }

    private static int testCount=0;

//Use @BeforeClass for stuff you want to run once before all the tests start. This includes things like
//database connections.

    @BeforeClass
    public static void setUpClass()
    {

    }

//Use @AfterClass for stuff you want to run once after all the tests end. This includes stuff like
//closing database connections, and other clean-up operations.

    @AfterClass
    public static void tearDownClass()
    {

    }

//Use @Before for things you want to run before every single test. This can cover setting up all the
//objects you need for the tests, or reseting them to a certain state.

    @Before
    public void setUp()
    {
        testCount=testCount+1;
        System.out.println("Starting Test "+testCount);
    }

//Use @After for things you want to run after every single test. You can use this to reset the items
//to a base state.

    @After
    public void tearDown()
    {
        System.out.println("Finishing Test "+testCount);
    }

// All tests are prefixed with @Test, which marks them as a test to be run when you run this a test file.
// The basic type of test fails if an assertion fails, or if use fail(String), which contains an error
// message.

    //FieldWithContent and FieldWithoutContent have unit tests ServerclientShared. These tests for the android-only fields.




    //Does the Field properly determine whether the input given is valid or not?
    @Test
    public void typeCheckerTest()
    {

        String inputString= "This is a test line.";
        String inputString2="THIS IS A DIFFERENT LINE";
        //Technically not an image,but as long we get the images in this format, this should be valid enough to test.
        //Will probably need to test the conversion in some other class.
        //If not given a value, bytes will default to 0. We won't have to check for nulls because of this.
        byte[] testImage= new byte[3];
        testImage[0]=4;
        testImage[1]=7;
        testImage[2]=3;


        StringField stringContentTest= new StringField(new FieldWithContent("Testing Strings!",FieldType.STRING,true));
        ImageField imageContentTest= new ImageField(new FieldWithContent("Testing Pictures!",FieldType.IMAGE,true));
        boolean comparisonSuccess;


        //Test on type STRING
        comparisonSuccess=stringContentTest.contentIsSuitable(inputString);
        assertTrue("Comparison Failed, String<->String",comparisonSuccess);
        stringContentTest.setContent(inputString);
        assertTrue("Didn't add String to Content!", stringContentTest.getContentStringRepresentation()==inputString);


        //We allow null fields, so this should be good.
        comparisonSuccess=stringContentTest.contentIsSuitable(null);
        assertTrue("Comparison Failed, null<->String",comparisonSuccess);
        stringContentTest.setContent(null);
        assertTrue("Didn't add null to Content!", stringContentTest.getContentStringRepresentation()==null);


        comparisonSuccess=stringContentTest.contentIsSuitable(101);
        assertFalse("Comparison Failed Int<->String", comparisonSuccess);
        try
        {
            stringContentTest.setContent(101);
            fail("Illegaly added Integer to a String field!");
        }
        catch(RuntimeException e){};//Expected



        //No easy way to check setContent for Images, will check later.

        //Test on type IMAGE

//        comparisonSuccess=imageContentTest.contentIsSuitable(testImage);
//        assertTrue("Comparison Failed, Image<->Image",comparisonSuccess);
//        imageContentTest.setContent(testImage);
//        //assertTrue("Didn't add String to Content!", imageContentTest.wrappedField==testImage);//How to check if assigned properly?
//
//
//        //We allow null fields, so this should be good.
//        comparisonSuccess=imageContentTest.contentIsSuitable(null,FieldType.IMAGE);
//        assertTrue("Comparison Failed, null<->Image",comparisonSuccess);
//        imageField.setContent(null);
//        assertTrue("Didn't add null to Content!(2)", imageField.getContent()==null);
//
//
//        comparisonSuccess=FieldWithContent.contentMatchesType(101,FieldType.IMAGE);
//        assertFalse("Comparison Failed Int<->Image", comparisonSuccess);
//        try
//        {
//            imageField.setContent(101);
//            fail("Illegaly added Integer to a Image field!");
//        }
//        catch(RuntimeException e){};//Expected



        //TODO When AUDIO and VIDEO have been implemented, build tests for them here.


    }










    //Tests the ImageField functionality
    @Test
    public void imageFieldTest()
    {

    }

    //Tests the StringField functionality
    @Test
    public void stringFieldTest()
    {

    }

    //Tests the VideoField functionality
    @Test
    public void videoFieldTest()
    {

    }

    //Tests the audioField functionality
    @Test
    public void AudioFieldTest()
    {

    }


    //Field tests
    //TODO test getType()
    //TODO test getTitle()
    //TODO test isRequired()

// TODO add test methods here.
// The methods must be annotated with annotation @Test. For example:
//
// @Test
// public void hello() {}

}
