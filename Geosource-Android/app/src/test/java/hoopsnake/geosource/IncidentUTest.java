package hoopsnake.geosource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import hoopsnake.geosource.data.AppIncident;
import hoopsnake.geosource.data.FieldType;
import hoopsnake.geosource.data.FieldWithContent;
import hoopsnake.geosource.data.FieldWithoutContent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

//New System broke all of the tests, none of these work properly now. Will rework later.



/**
 * Created by wwf594 on 07/03/15.
 */
public class IncidentUTest
{
    public IncidentUTest()
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

    //Checks to see if an empty Incident can be constructed properly.
    @Test
    public void constructionTest()
    {
        ArrayList<FieldWithContent> arrayCheck= new ArrayList(3);

        //Let's try a typical channel setup first:
        ArrayList<FieldWithoutContent> basicSpec= new ArrayList(3);
        basicSpec.add(new FieldWithoutContent("Image", FieldType.IMAGE, true));
        basicSpec.add(new FieldWithoutContent("Video", FieldType.VIDEO, false));
        basicSpec.add(new FieldWithoutContent("Description", FieldType.STRING, true));
        AppIncident testIncident= new AppIncident(basicSpec);
        arrayCheck=testIncident.getFieldList();

        //Check to see if everything is empty
        assertFalse(testIncident.isCompletelyFilledIn());
        assertFalse(arrayCheck.get(0).contentIsFilled());
        assertFalse(arrayCheck.get(1).contentIsFilled());
        assertFalse(arrayCheck.get(2).contentIsFilled());

    }

    //Checks to see if Incident can handle nulls properly.
    @Test
    public void nullCheck()
    {
        ArrayList<FieldWithContent> arrayCheck= new ArrayList(3);

        ArrayList<FieldWithoutContent> basicSpec= new ArrayList(3);
        basicSpec.add(null);
        basicSpec.add(null);
        AppIncident testIncident= new AppIncident(basicSpec);
        arrayCheck=testIncident.getFieldList();

        //Check to see if everything is empty

        //We have no fields, so technically all fields are filled in!
        assertTrue(testIncident.isCompletelyFilledIn());

        assertFalse(arrayCheck.get(0).contentIsFilled());
        assertFalse(arrayCheck.get(1).contentIsFilled());
        assertFalse(arrayCheck.get(2).contentIsFilled());
        //assertFalse(arrayCheck.get(3).contentIsFilled());

    }

    //Can we fill an incident with values successfully?
    @Test
    public void fillingTest()
    {
        //To make this simple, we'll use three text fields, and make one of them optional
        ArrayList<FieldWithoutContent> basicSpec = new ArrayList(3);
        basicSpec.add(new FieldWithoutContent("Description", FieldType.STRING, true));
        basicSpec.add(new FieldWithoutContent("Description", FieldType.STRING, true));
        basicSpec.add(new FieldWithoutContent("Description", FieldType.STRING, false));
        AppIncident newIncident= new AppIncident(basicSpec);

        //The program would use IncidentDisplayAdapter to fill the list. However, we can just take the content fields
        //out and use them directly:

        ArrayList<FieldWithContent> incidentList=newIncident.getFieldList();
        FieldWithContent fieldWeModify=incidentList.get(0);
        fieldWeModify.setContent("This is what we want to say.");
        fieldWeModify=incidentList.get(1);
        fieldWeModify.setContent("Q$^%ARd%r&thjddghjsdfgDFGSD%YE&*%#*-+*%^&$+__:><");

        assertTrue(newIncident.isCompletelyFilledIn());

        //Let's try one with pictures, now:
        ArrayList<FieldWithoutContent> basicSpec2 = new ArrayList(4);
        basicSpec2.add(new FieldWithoutContent("Description", FieldType.STRING, true));
        basicSpec2.add(new FieldWithoutContent("Video", FieldType.STRING, true));
        basicSpec2.add(new FieldWithoutContent("Video", FieldType.STRING, false));
        basicSpec2.add(new FieldWithoutContent("Description", FieldType.STRING, false));
        AppIncident newIncident2= new AppIncident(basicSpec);

        incidentList=newIncident2.getFieldList();
        fieldWeModify=incidentList.get(0);
        fieldWeModify.setContent("This is what we want to say.");


    }



// TODO add test methods here.
// The methods must be annotated with annotation @Test. For example:
//
// @Test
// public void hello() {}

}
