package Test;

import Control.Controller;
import DataBase.DBAccess;
import FileSystem.FileAccess;
import ServerClientShared.FieldWithContent;
import ServerClientShared.FieldWithoutContent;
import ServerClientShared.Incident;
import java.net.URISyntaxException;
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
   private static String testChannel="march13";
   
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

    
    //Checks whether we can grab formSpecs inside the controller properly, using a test channel currently on the server.
    @Test
    public void getFormSpecTest()
    {
        try
        {
            //This channel is static and rebuilt automatically with the database, so it is always the same
            DBAccess dbTest = new DBAccess();
            assertTrue(dbTest.getFormSpecLocation(testChannel, testOwner).equals("okenso.4"));
            
        }
        catch (SQLException SQLe)
        {
            System.out.println("Database connection failed");
        }
        
        //TODO
        //ArrayList<FieldWithoutContent> testSpec=SocketStuff.doStuff(testChannel, testOwner);
        //int i=5;
        
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
            DBAccess dbAccess= new DBAccess();
            //FileAccess fAccess= new FileAccess();
            
            //Safety Test so we can send a valid post. If something changes in the testchannel, this should make 
            //the test fail before it can submit invalid posts.
            //String fileName = dbAccess.getFormSpecLocation(testChannel, testOwner); //get spec's file name in filesystem
            //ArrayList<FieldWithoutContent> specList= fAccess.getFormSpec("okenso.4"); //retreive form spec
            //int postNum = dbAccess.newPost(testChannel, testOwner, "xxLegolasxxYoloxx");
            
            
            int i=5;
        
        }
        catch(SQLException SQLe)
        {
            throw new RuntimeException("Database initialization failed");
            
        }
        /*catch (URISyntaxException URISe)
        {
            throw new RuntimeException("Filesystem not consistent, error initializing path");
        }*/
        
        

        //ArrayList<FieldWithoutContent> spec=controller.getForm(testChannel, testOwner);
        
            

    }
    
    
}
