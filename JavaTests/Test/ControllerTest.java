package Test;

import DataBase.DBAccess;
import ServerClientShared.AudioFieldWithContent;
import ServerClientShared.AudioFieldWithoutContent;
import ServerClientShared.FieldWithContent;
import ServerClientShared.FieldWithoutContent;
import ServerClientShared.Geotag;
import ServerClientShared.GeotagFieldWithContent;
import ServerClientShared.GeotagFieldWithoutContent;
import ServerClientShared.ImageFieldWithContent;
import ServerClientShared.ImageFieldWithoutContent;
import ServerClientShared.Incident;
import ServerClientShared.StringFieldWithContent;
import ServerClientShared.StringFieldWithoutContent;
import ServerClientShared.VideoFieldWithContent;
import ServerClientShared.VideoFieldWithoutContent;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class ControllerTest 
{

   private static int testCount=0;
   
   //The channel we will be testing on
   private static String testChannel="testing";
   
   //The test channel's owner.
   private static String testOwner="okenso";

    
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

    /**
     * Checks if we can get a ping off the server.
     */
    @Test
    public void pingTest()
    {
        assertTrue(SocketStuff.getPing());
    }
    
    
    /**
     * Checks whether we can grab formSpecs inside the controller properly, using a test channel currently on the server.        
     * */
    @Test
    public void getFormSpecTest()
    {
        try
        {
            //Check if the file spec exists on the server.
            //This channel is static and rebuilt automatically with the database, so it is always the same
            DBAccess dbTest = new DBAccess();
            assertTrue(dbTest.getFormSpecLocation(testChannel, testOwner).equals("okenso.4"));
            
        }
        catch (SQLException SQLe)
        {
            throw new RuntimeException("Database connection failed");
        }
        
        //Using Sockets, actually grab the spec for march13 and check if it is right.
        ArrayList<FieldWithoutContent> testSpec=SocketStuff.getSpec(testChannel, testOwner);
        assertTrue(testSpec.get(0) instanceof StringFieldWithoutContent);
        assertTrue(testSpec.get(1) instanceof GeotagFieldWithoutContent);
        assertTrue(testSpec.get(2) instanceof StringFieldWithoutContent);
        assertTrue(testSpec.get(3) instanceof ImageFieldWithoutContent);
        assertTrue(testSpec.get(4) instanceof VideoFieldWithoutContent);
        assertTrue(testSpec.get(5) instanceof AudioFieldWithoutContent);
        int i=5;
        
    }
    
    
    //Can I post things?
    @Test
    public void postThings()
    {
        //Hit a dead-end with this, due to various issues.
        //Will try again later.
        
        
        //Note: All checking to see if fields are valid is done in the app. This is just to test
        //if we can post.

        
            
       try
        {
            DBAccess dbTest= new DBAccess();
            assertTrue(dbTest.getFormSpecLocation(testChannel, testOwner).equals("okenso.4"));     
        }
        catch(SQLException SQLe)
        {
            throw new RuntimeException("Database initialization failed");   
        }
        //Using Sockets, actually grab the spec for march13 and check if it is right.
        ArrayList<FieldWithoutContent> testSpec=SocketStuff.getSpec(testChannel, testOwner);
        assertTrue(testSpec.get(0) instanceof StringFieldWithoutContent);
        assertTrue(testSpec.get(1) instanceof GeotagFieldWithoutContent);
        assertTrue(testSpec.get(2) instanceof StringFieldWithoutContent);
        assertTrue(testSpec.get(3) instanceof ImageFieldWithoutContent);
        assertTrue(testSpec.get(4) instanceof VideoFieldWithoutContent);
        assertTrue(testSpec.get(5) instanceof AudioFieldWithoutContent);
   
        
        //IN PROGRESS
        //Then, create an Incident, then send it to the server.
        
        StringFieldWithoutContent t1 = new StringFieldWithoutContent("Test Description", true);     
        StringFieldWithContent stringCon = new StringFieldWithContent(t1);
        stringCon.setContent("THIS IS A TEST. DO NOT PANIC");
        
        StringFieldWithoutContent t2 = new StringFieldWithoutContent("More stuff", true);     
        StringFieldWithContent stringCon2 = new StringFieldWithContent(t2);
        stringCon2.setContent("GROUND CONTROL TO MAJOR TOM");
        
        Geotag location = new Geotag() ;
        location.setLatitude(11.111);
        location.setLongitude(22.222);
        location.setTimestamp(555555);
        GeotagFieldWithoutContent g1 = new GeotagFieldWithoutContent("Location", true);
        GeotagFieldWithContent geotagCon = new GeotagFieldWithContent(g1);
        
        
        byte[] image = new byte[]{1,2,4};
        ImageFieldWithoutContent i1 = new ImageFieldWithoutContent("an image?", true);      
        ImageFieldWithContent imageCon = new ImageFieldWithContent(i1);
        imageCon.setContent(image);
        
        VideoFieldWithoutContent v1 = new VideoFieldWithoutContent("a video?", true);      
        VideoFieldWithContent videoCon = new VideoFieldWithContent(v1);
        videoCon.setContent(image);
        
        AudioFieldWithoutContent a1 = new AudioFieldWithoutContent("an audio bite?", true);      
        AudioFieldWithContent audioCon = new AudioFieldWithContent(a1);
        audioCon.setContent(image);
        
        ArrayList<FieldWithContent> filledIncident=new ArrayList();
        filledIncident.add(stringCon);
        filledIncident.add(geotagCon);
        filledIncident.add(stringCon2);
        filledIncident.add(imageCon);
        filledIncident.add(videoCon);
        filledIncident.add(audioCon);
        Incident testIncident = new Incident(filledIncident,testChannel,testOwner,"xx420Blazexx");
        
        SocketStuff.makePost(testIncident);
    }
    
    
}
