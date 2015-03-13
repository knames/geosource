package Test;

import ServerClientShared.FieldType;
import ServerClientShared.FieldWithContent;
import ServerClientShared.FieldWithoutContent;
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
        FieldWithoutContent goodField = new FieldWithoutContent("Description",FieldType.STRING,true);
        FieldWithoutContent goodField2 = new FieldWithoutContent("A Picture",FieldType.IMAGE,false);
        FieldWithoutContent goodField3 = new FieldWithoutContent("LOLCATS",FieldType.VIDEO,false);
        FieldWithoutContent goodField4 = new FieldWithoutContent("The 1812 Symphony",FieldType.AUDIO,true);
        
        //This shouldn't be allowed
        try
        {
            FieldWithoutContent badField = new FieldWithoutContent(null,FieldType.STRING,true);
            fail("Failed to throw exception at null value.");
        }
        catch(RuntimeException e){};//Expected
        
        try
        {
            FieldWithoutContent badField2 = new FieldWithoutContent("A Troll",null,true);
            fail("Failed to throw exception at null value.");
        }
        catch(RuntimeException e){};//Expected
    }
    
     //Test that the constructor properly works for FieldWithContent, and stops passed in null values.
    @Test
    public void withContentConstructorTest()
    {
        //First, let's test with null content. This is essentialy the same as the previous test.
        FieldWithContent goodField = new FieldWithContent("Description",FieldType.STRING,true);
        FieldWithContent goodField2 = new FieldWithContent("A Picture",FieldType.IMAGE,false);
        FieldWithContent goodField3 = new FieldWithContent("LOLCATS",FieldType.VIDEO,false);
        FieldWithContent goodField4 = new FieldWithContent("The 1812 Symphony",FieldType.AUDIO,true);
        
        //Check if the content is empty:
        assertTrue(goodField.getContent()==null);
        assertTrue(goodField2.getContent()==null);
        assertTrue(goodField3.getContent()==null);
        assertTrue(goodField4.getContent()==null);
        
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
        
    }
    
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
        
        
        
        //TODO When AUDIO and VIDEO have been implemented, build tests for them here.
        
        
    }
  
}
