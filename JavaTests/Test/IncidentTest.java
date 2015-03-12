package Test;

import java.util.ArrayList;

import ServerClientShared.FieldType;
import ServerClientShared.Incident;
import ServerClientShared.FieldWithContent;
import ServerClientShared.FieldWithoutContent;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author add118
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
    	basicSpec.add(new FieldWithContent("Image", FieldType.IMAGE, true));
    	Incident testIncident1 = new Incident(basicSpec,"daChannel","Randy");
    	
    	//Test getFieldList returns proper content
    	assertEquals(testIncident1.getFieldList().get(0), basicSpec.get(0));
    	assertEquals(testIncident1.getFieldList(), basicSpec);
    	
    	//Null ArrayList<FieldWithContent> test
    	Incident testIncident2 = new Incident(null,"testChannel5","Randy");
    	assertEquals(null, testIncident2.getFieldList());
    	testIncident2.setFieldList(basicSpec); 
    	assertEquals(testIncident1.getFieldList(), basicSpec);
        
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
            Incident testIncident4 = new Incident(basicSpec,"anotherTestChannel",null);
            fail("Failed to throw exception on null channel name");
        }
        catch(RuntimeException e){};//expected
    	 
    }
    // add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
}
