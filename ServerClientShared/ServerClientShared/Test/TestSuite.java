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
        Result result=null;
        JUnitCore junit = new JUnitCore();
        //try
       // {
             result = junit.run(FieldTest.class);
        /*}
        catch(Exception e)
        {
            //The errors will be displayed in the results.
        }
        finally
        {*/
            System.out.println(parseResults(result,"FieldTest"));
       // }
        
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
            slate=slate+"failed. \n";
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
