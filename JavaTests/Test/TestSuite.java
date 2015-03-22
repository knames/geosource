package Test;

import Test.IncidentTest;
import Test.FieldTest;
import java.util.List;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Runs all the tests we have developed.
 * @author wwf594
 */
public class TestSuite 
{
    public static void main (String args[])
    {
        JUnitCore junit = new JUnitCore();
        Result result = junit.run(FieldTest.class);
        System.out.println(parseResults(result,"FieldTest"));
        result= junit.run(IncidentTest.class);
        System.out.println(parseResults(result,"IncidentTest"));
        result= junit.run(ControllerTest.class);
        System.out.println(parseResults(result,"ControllerTest"));
        

        
    }
    
    
    /**
     * This parses the Results we get from our tests, and turns them into a readable format.
     * @param testResults The Results we get from running a JUnit test
     * @param testName The name of the test
     * @return A human readable String containing the results of the given test.
     */
    private static String parseResults (Result testResults, String testName)
    {
        String slate="Test "+testName+" was ";
        if(testResults.wasSuccessful())
        {
            slate=slate+"successful. \n";
        }
        else
        {
            slate=slate+"a failure. \n";
            List<Failure> failureList=testResults.getFailures();
            while(!(failureList.isEmpty()))
            {
                Failure temp=failureList.remove(0);
                slate=slate+temp.getMessage()+"\n"+temp.getTrace()+"\n \n";
            }
        }
        return slate;
    }
}
