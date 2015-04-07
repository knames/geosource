package Test;


import ServerClientShared.Geotag;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author wwf594
 */
public class GeotagTest
{
    private static int testCount=0;
    
    public GeotagTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testCount=testCount+1;
        System.out.println("Starting Test "+testCount);
    }
    
    @After
    public void tearDown() {
    	System.out.println("Finishing Test "+testCount);
    }
    
    /**
     * A basic test of geotag functionality
     */
    @Test
    public void constructionTest() 
    {
        //These all should pass.
        //Basic Geotag.
    	Geotag test1= new Geotag();
        test1.setTimestamp(5);
        test1.setLatitude(155.2222);
        test1.setLongitude(27.8888);
        assertTrue(test1.exists());
        
        //Geotag with negatives.
        Geotag test2= new Geotag();
        test2.setTimestamp(5);
        test2.setLatitude(-42.11);
        test2.setLongitude(-32.555);
        assertTrue(test2.exists());
        
        Geotag test3= new Geotag();
        test3.setTimestamp(5);
        test3.setLatitude(-42.11);
        test3.setLongitude(32.555);
        assertTrue(test3.exists());
        
        Geotag test4= new Geotag();
        test4.setTimestamp(5);
        test4.setLatitude(0);
        test4.setLongitude(-32.555);
        assertTrue(test4.exists());
        
        //Invalid Geotags
        Geotag test5= new Geotag();
        test5.setTimestamp(5);
        test5.setLatitude(0);
        assertFalse(test5.exists());
        
        Geotag test6= new Geotag();
        test6.setTimestamp(5);
        test6.setLongitude(-32.555);
        assertFalse(test6.exists());
        
        Geotag test7= new Geotag();
        test7.setLatitude(0);
        test7.setLongitude(-32.555);
        assertFalse(test7.exists());
        
        Geotag test8= new Geotag();
        assertFalse(test8.exists());

    }
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
}
