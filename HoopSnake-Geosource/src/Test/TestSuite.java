package Test;

import ServerClientShared.Test.FieldTest;
import ServerClientShared.Test.IncidentTest;
import java.util.List;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 *
 * @author wwf594
 */
public class TestSuite 
{
    public static void main (String args[])
    {
        JUnitCore junit = new JUnitCore();
        //Result result = junit.run(FieldTest.class);
        //System.out.println(parseResults(result,"FieldTest"));
        

        
    }
    
    
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
