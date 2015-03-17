package Test;

import ServerClientShared.AudioFieldWithContent;
import ServerClientShared.AudioFieldWithoutContent;
import ServerClientShared.ImageFieldWithContent;
import ServerClientShared.ImageFieldWithoutContent;
import ServerClientShared.StringFieldWithContent;
import ServerClientShared.StringFieldWithoutContent;
import ServerClientShared.VideoFieldWithContent;
import ServerClientShared.VideoFieldWithoutContent;
import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *  Tests all classes in the "Field" family.
 * @author wwf594
 */
public class FieldTest {
    
    public FieldTest() {
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

    //Test that the constructor properly works for FieldWithoutContent, and stops passed in null values.
    @Test
    public void noContentConstructorTest()
    {
        //Check if all the different field types can be built.
        StringFieldWithoutContent goodField = new StringFieldWithoutContent("Description", true);
        ImageFieldWithoutContent goodField2 = new ImageFieldWithoutContent("A Picture", false);
        VideoFieldWithoutContent goodField3 = new VideoFieldWithoutContent("LOLCATS", false);
        AudioFieldWithoutContent goodField4 = new AudioFieldWithoutContent("The 1812 Symphony",true);
        
        //This shouldn't be allowed
        try
        {
            StringFieldWithoutContent badField = new StringFieldWithoutContent(null,true);
            fail("Failed to throw exception at null value.");
        }
        catch(RuntimeException e){};//Expected
        //This shouldn't be allowed
        try
        {
            ImageFieldWithoutContent badField = new ImageFieldWithoutContent(null,true);
            fail("Failed to throw exception at null value.");
        }
        catch(RuntimeException e){};//Expected
        //This shouldn't be allowed
        try
        {
            VideoFieldWithoutContent badField = new VideoFieldWithoutContent(null,true);
            fail("Failed to throw exception at null value.");
        }
        catch(RuntimeException e){};//Expected
        //This shouldn't be allowed
        try
        {
            AudioFieldWithoutContent badField = new AudioFieldWithoutContent(null,true);
            fail("Failed to throw exception at null value.");
        }
        catch(RuntimeException e){};//Expected
        
    }

     //Test that the constructor properly works for FieldWithContent, and stops passed in null values.
    @Test
    public void withContentConstructorTest()
    {
        /*
        //Check if the given  field type can be built.
        StringFieldWithoutContent t1 = new StringFieldWithoutContent("Description", true);
        ImageFieldWithoutContent t2 = new ImageFieldWithoutContent("A Picture", false);
        VideoFieldWithoutContent t3 = new VideoFieldWithoutContent("LOLCATS", false);
        AudioFieldWithoutContent t4 = new AudioFieldWithoutContent("The 1812 Symphony",true);
        
        //Building from withoutContent files
        StringFieldWithContent stringCon = new StringFieldWithContent(t1);
        ImageFieldWithContent imageCon = new ImageFieldWithContent(t2);
        VideoFieldWithContent videoCon = new VideoFieldWithContent(t3);
        AudioFieldWithContent audioCon = new AudioFieldWithContent(t4);
        
        assertTrue(stringCon.getContent()==null);
        assertTrue(imageCon.getContent()==null);
        assertTrue(videoCon.getContent()==null);
        assertTrue(audioCon.getContent()==null);
        
        //This shouldn't be allowed
        try
        {
            FieldWithContent badField = new FieldWithContent(null,FieldType.STRING,true,null);
            fail("Failed to throw exception at null value.");
        }
        catch(RuntimeException e){};//Expected
        
        try
        {
            FieldWithContent badField2 = new FieldWithContent("A Troll",null,true,null);
            fail("Failed to throw exception at null value.");
        }
        catch(RuntimeException e){};//Expected
        */
    }
    
    @Test
    public void stringFieldTest()
    {
        //Check if the given  field type can be built.
        StringFieldWithoutContent t1 = new StringFieldWithoutContent("Description", true);
        
        StringFieldWithContent stringCon = new StringFieldWithContent(t1);
        
        assertTrue(stringCon.getContent()==null);
        
        //Check to see if it allows/disallows certain types to be added
        
        assertFalse(stringCon.contentMatchesType(new LinkedList<Integer>()));
        assertTrue(stringCon.contentMatchesType("Hello"));
        
        //stringCon.setContent((new LinkedList<Integer>()));
        //System.out.println(stringCon.getContent().toString());
        //System.out.println(stringCon.getType());
        try
        {
            stringCon.setContent(new LinkedList<Integer>());
            fail("Accepted Int->String");
        }
        catch(RuntimeException e){};//Expected
        
        assertTrue(stringCon.getContent()==null);
        
        stringCon.setContent("Stuff and Things");
        
        assertTrue(stringCon.getContent()=="Stuff and Things");
        
    }
    
     @Test
    public void imageFieldTest()
    {
        /*//Check if the given  field type can be built.
        ImageFieldWithoutContent t1 = new ImageFieldWithoutContent("Description", true);
        
        ImageFieldWithContent imageCon = new ImageFieldWithContent(t1);
        
        assertTrue(imageCon.getContent()==null);
        
        //Check to see if it allows/disallows certain types to be added
        
        assertFalse(imageCon.contentMatchesType(101));
        assertTrue(imageCon.contentMatchesType("TROLOLOLOL"));
        
        try
        {
            imageCon.setContent("LOLOLOL");
            fail("Accepted String->Image");
        }
        catch(RuntimeException e){};//Expected
        
        assertTrue(imageCon.getContent()==null);
        
        imageCon.setContent("Stuff and Things");
        
        assertTrue(imageCon.getContent()=="Stuff and Things");*/
        
    }
    
    //Does the Field properly determine whether the input given is valid or not?
    @Test
    public void typeCheckerTest()
    {
        
        /*String inputString= "This is a test line.";
        String inputString2="THIS IS A DIFFERENT LINE";
        //Technically not an image,but as long we get the images in this format, this should be valid enough to test.
        //Will probably need to test the conversion in some other class.
        //If not given a value, bytes will default to 0. We won't have to check for nulls because of this.
        byte[] testImage= new byte[3];
        testImage[0]=4;
        testImage[1]=7;
        testImage[2]=3;
        
        
        FieldWithContent stringContentTest= new FieldWithContent("Testing Strings!",FieldType.STRING,true);
        FieldWithContent imageContentTest= new FieldWithContent("Testing Pictures!",FieldType.IMAGE,true);
        boolean comparisonSuccess;
        
        
        
        
        //null checks
        try
        {
            comparisonSuccess=FieldWithContent.contentMatchesType(inputString,null);
            fail("Didn't throw exception!(null type)");
        }
        catch(RuntimeException e){};//Expected
        
        
        //Check for proper FieldType
        try
        {
            comparisonSuccess=FieldWithContent.contentMatchesType(inputString,FieldType.OPTION_LIST);
            fail("Didn't throw exception!(improper fieldtype)");
        }
        catch(RuntimeException e){}; //Expected
        
        
        
       
        
        //Test on type STRING
        FieldWithContent goodField = new FieldWithContent("Description",FieldType.STRING,true);
        
        comparisonSuccess=FieldWithContent.contentMatchesType(inputString,FieldType.STRING);
        assertTrue("Comparison Failed, String<->String",comparisonSuccess);
        stringContentTest.setContent(inputString);
        assertTrue("Didn't add String to Content!", stringContentTest.getContent()==inputString);
        
        
        //We allow null fields, so this should be good.
        comparisonSuccess=FieldWithContent.contentMatchesType(null,FieldType.STRING);
        assertTrue("Comparison Failed, null<->String",comparisonSuccess);
        stringContentTest.setContent(null);
        assertTrue("Didn't add null to Content!", stringContentTest.getContent()==null);
        
        
        comparisonSuccess=FieldWithContent.contentMatchesType(101,FieldType.STRING);
        assertFalse("Comparison Failed Int<->String", comparisonSuccess);
        try
        {
            stringContentTest.setContent(101);
            fail("Illegaly added Integer to a String field!");
        }
        catch(RuntimeException e){};//Expected
        
        
        
        
        
        //Test on type IMAGE
        FieldWithContent imageField = new FieldWithContent("Picture",FieldType.IMAGE,true);
        
        comparisonSuccess=FieldWithContent.contentMatchesType(testImage,FieldType.IMAGE);
        assertTrue("Comparison Failed, Image<->Image",comparisonSuccess);
        imageField.setContent(testImage);
        assertTrue("Didn't add String to Content!", imageField.getContent()==testImage);
        
        
        //We allow null fields, so this should be good.
        comparisonSuccess=FieldWithContent.contentMatchesType(null,FieldType.IMAGE);
        assertTrue("Comparison Failed, null<->Image",comparisonSuccess);
        imageField.setContent(null);
        assertTrue("Didn't add null to Content!(2)", imageField.getContent()==null);
        
        
        comparisonSuccess=FieldWithContent.contentMatchesType(101,FieldType.IMAGE);
        assertFalse("Comparison Failed Int<->Image", comparisonSuccess);
        try
        {
            imageField.setContent(101);
            fail("Illegaly added Integer to a Image field!");
        }
        catch(RuntimeException e){};//Expected
        
        
        
        //TODO When AUDIO and VIDEO have been implemented, build tests for them here.*/
        
        
    }
  
}
