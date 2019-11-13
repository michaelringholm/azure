using Microsoft.Azure.WebJobs;

namespace com.opusmagus.api
{    
    public class StartShippingActivity
    {
        [FunctionName("StartShippingActivity")]
        public static string Run(string name)
        {
            System.Threading.Thread.Sleep(8000);
            return $"start shipping of order {name}!";
        }
    }
}
