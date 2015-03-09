package ServerClientShared.Test;

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
 * @author Warren
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
    	Incident testIncident1 = new Incident(basicSpec);
    	
    	//Test getFieldList returns proper content
    	assertEquals(testIncident1.getFieldList().get(0), basicSpec.get(0));
    	assertEquals(testIncident1.getFieldList(), basicSpec);
    	
    	//Null ArrayList<FieldWithContent> test
    	Incident testIncident2 = new Incident(null);
    	assertEquals(null, testIncident2.getFieldList());
    	testIncident2.setFieldList(basicSpec); 
    	assertEquals(testIncident1.getFieldList(), basicSpec);

    	 
    }
    // add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
}
