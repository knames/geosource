/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClientShared.Test;

import java.util.List;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class TestSuite 
{
    public static void main (String args[])
    {
        JUnitCore junit = new JUnitCore();
        Result result = junit.run(FieldTest.class);
        System.out.println(parseResults(result,"FieldTest"));
        result= junit.run(IncidentTest.class);
        System.out.println(parseResults(result,"IncidentTest"));
        

        
    }
    
    public static String parseResults (Result testResults, String testName)
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
