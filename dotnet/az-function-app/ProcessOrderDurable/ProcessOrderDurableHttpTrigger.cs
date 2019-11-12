using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Azure.WebJobs;
using Microsoft.Azure.WebJobs.Extensions.Http;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using com.opusmagus.bl;

namespace com.opusmagus.api
{
    public class ProcessOrderDurableHttpTrigger
    {
        private ProcessOrderCommand command;
        private ILogger logger;

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
