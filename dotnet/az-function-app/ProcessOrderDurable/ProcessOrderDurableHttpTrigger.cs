using System.Threading.Tasks;
using Microsoft.Azure.WebJobs;
using Microsoft.Extensions.Logging;
using com.opusmagus.bl;
using System.Net.Http;

namespace com.opusmagus.api
{
    public class ProcessOrderDurableHttpTrigger
    {
        public ProcessOrderDurableHttpTrigger() {
        }

        [FunctionName("ProcessOrderDurableHttpTrigger")]
        public static async Task<HttpResponseMessage> Run(HttpRequestMessage req, DurableOrchestrationClient starter, string functionName, ILogger log)
        {
            // Function input comes from the request content.
            dynamic eventData = await req.Content.ReadAsAsync<object>();

            // Pass the function name as part of the route 
            string instanceId = await starter.StartNewAsync(functionName, eventData);

            log.LogInformation($"Started orchestration with ID = '{instanceId}'.");

            var checkStatus = starter.CreateCheckStatusResponse(req, instanceId);
            log.LogInformation(checkStatus.ToString());
            return checkStatus;
        }
    }
}
