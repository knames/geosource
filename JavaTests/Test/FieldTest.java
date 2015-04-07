package Test;

import ServerClientShared.AudioFieldWithContent;
import ServerClientShared.AudioFieldWithoutContent;
import ServerClientShared.Geotag;
import ServerClientShared.GeotagFieldWithContent;
import ServerClientShared.GeotagFieldWithoutContent;
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
    
    //Tests constructors for StringFieldWithContent
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
    
    //Tests constructors for ImageFieldWithContent
    @Test
    public void imageFieldTest()
    {
        byte[] image = new byte[]{1,2,4};
        //Check if the given  field type can be built.
        ImageFieldWithoutContent t1 = new ImageFieldWithoutContent("Description", true);
        
        ImageFieldWithContent imageCon = new ImageFieldWithContent(t1);
        
        assertTrue(imageCon.getContent()==null);
        
        //Check to see if it allows/disallows certain types to be added
        
        assertFalse(imageCon.contentMatchesType("TROLOLOL"));
        assertTrue(imageCon.contentMatchesType(image));
        
        try
        {
            imageCon.setContent("LOLOLOL");
            fail("Accepted String->Image");
        }
        catch(RuntimeException e){};//Expected
        
        assertTrue(imageCon.getContent()==null);
        
        imageCon.setContent(image);
        
        assertTrue(imageCon.getContent()==image);
        
    }
    
    //Tests constructors for VideoFieldWithContent
    @Test
    public void videoFieldTest()
    {
        byte[] video = new byte[]{1,2,4};
        //Check if the given  field type can be built.
        VideoFieldWithoutContent t1 = new VideoFieldWithoutContent("Description", true);
        
        VideoFieldWithContent videoCon = new VideoFieldWithContent(t1);
        
        assertTrue(videoCon.getContent()==null);
        
        //Check to see if it allows/disallows certain types to be added
        
        assertFalse(videoCon.contentMatchesType("TROLOLOL"));
        assertTrue(videoCon.contentMatchesType(video));
        
        try
        {
            videoCon.setContent("LOLOLOL");
            fail("Accepted String->Video");
        }
        catch(RuntimeException e){};//Expected
        
        assertTrue(videoCon.getContent()==null);
        
        videoCon.setContent(video);
        
        assertTrue(videoCon.getContent()==video);
        
    }
    
    //Tests constructors for AudioFieldWithContent
    @Test
    public void audioFieldTest()
    {
        byte[] audio = new byte[]{1,2,4};
        //Check if the given  field type can be built.
        AudioFieldWithoutContent t1 = new AudioFieldWithoutContent("Description", true);
        
        AudioFieldWithContent audioCon = new AudioFieldWithContent(t1);
        
        assertTrue(audioCon.getContent()==null);
        
        //Check to see if it allows/disallows certain types to be added
        
        assertFalse(audioCon.contentMatchesType("TROLOLOL"));
        assertTrue(audioCon.contentMatchesType(audio));
        
        try
        {
            audioCon.setContent("LOLOLOL");
            fail("Accepted String->Audio");
        }
        catch(RuntimeException e){};//Expected
        
        assertTrue(audioCon.getContent()==null);
        
        audioCon.setContent(audio);
        
        assertTrue(audioCon.getContent()==audio);
        
    }
    
     //Tests constructors for AudioFieldWithContent
    @Test
    public void geotagFieldTest()
    {
        
        Geotag geotag = new Geotag();
        geotag.setLatitude(11.555);
        geotag.setLongitude(31.789);
        geotag.setTimestamp(5);
        
        //Check if the given  field type can be built.
        GeotagFieldWithoutContent t1 = new GeotagFieldWithoutContent("Description", true);
        
        GeotagFieldWithContent geotagCon = new GeotagFieldWithContent(t1);
        
        assertTrue(geotagCon.getContent()==null);
        
        //Check to see if it allows/disallows certain types to be added
        
        assertFalse(geotagCon.contentMatchesType("TROLOLOL"));
        assertTrue(geotagCon.contentMatchesType(geotag));
        
        try
        {
            geotagCon.setContent("LOLOLOL");
            fail("Accepted String->Geotag");
        }
        catch(RuntimeException e){};//Expected
        
        assertTrue(geotagCon.getContent()==null);
        
        geotagCon.setContent(geotag);
        
        assertTrue(geotagCon.getContent()==geotag);
        
    }
    
}
