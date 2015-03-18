package Test;

import java.util.ArrayList;
import ServerClientShared.Incident;
import ServerClientShared.FieldWithContent;
import ServerClientShared.FieldWithoutContent;
import ServerClientShared.StringFieldWithContent;
import ServerClientShared.StringFieldWithoutContent;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author add118 & wwf594
 */
public class IncidentTest 
{
    private static int testCount=0;
    
    public IncidentTest() {
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
    
    @Test
    public void constructionTest() {
    	ArrayList<FieldWithContent> basicSpec = new ArrayList<FieldWithContent>(1);
        ArrayList<FieldWithContent> emptySpec = new ArrayList<FieldWithContent>(0);
    	basicSpec.add(new StringFieldWithContent(new StringFieldWithoutContent("Hello World", true)));
    	Incident testIncident1 = new Incident(basicSpec,"daChannel","Randy");
    	
    	//Test getFieldList returns proper content
    	assertEquals(testIncident1.getFieldList().get(0), basicSpec.get(0));
    	assertEquals(testIncident1.getFieldList(), basicSpec);
    	
    	//Null ArrayList<FieldWithContent> test
        try
        {
            Incident testIncident2 = new Incident(null,"testChannel5","Randy");
            fail("Null fieldList not allowed.");
        }
        catch(RuntimeException e){};//Expected
    	
    	//Empty ArrayList
        try
        {
            Incident testIncident2 = new Incident(emptySpec,"testChannel5","Randy");
            fail("Null fieldList not allowed.");
        }
        catch(RuntimeException e){};//expected
        
        //Test for null channel names
        try
        {
            Incident testIncident3 = new Incident(basicSpec,null,"Randy");
            fail("Failed to throw exception on null channel name");
        }
        catch(RuntimeException e){};//expected
        
        //Test for null owner names
        try
        {
            Incident testIncident3 = new Incident(basicSpec,"anotherTestChannel",null);
            fail("Failed to throw exception on null channel name");
        }
        catch(RuntimeException e){};//expected
    	
        //try to set Null on owner name
        try
        {
            testIncident1.setChannelName(null);
            fail("Failed to throw exception on null owner name");
        }
        catch(RuntimeException e){};//expected
    }
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
}
