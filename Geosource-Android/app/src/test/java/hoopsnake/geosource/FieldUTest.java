package hoopsnake.geosource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

//New System broke all of the tests, none of these work properly now. Will rework later.

/**
 * Created by wwf594 on 07/03/15.
 */
public class FieldUTest
{
    public FieldUTest()
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

    @Test
    public void constructionTest()
    {

    }

    //Tests the ImageField functionality
    @Test
    public void imageFieldTest()
    {

    }

    //Tests the StringField functionality
    @Test
    public void stringFieldTest()
    {

    }

    //Tests the VideoField functionality
    @Test
    public void videoFieldTest()
    {

    }

    //Tests the audioField functionality
    @Test
    public void AudioFieldTest()
    {

    }

// TODO add test methods here.
// The methods must be annotated with annotation @Test. For example:
//
// @Test
// public void hello() {}

}
