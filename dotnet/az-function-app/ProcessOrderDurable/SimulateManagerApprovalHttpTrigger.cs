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
using System.Net.Http;

namespace com.opusmagus.api
{    
    public class SimulateManagerApprovalHttpTrigger
    {
        [FunctionName("SimulateManagerApprovalHttpTrigger")]        
        public static async Task<HttpResponseMessage> Run(HttpRequestMessage req, DurableOrchestrationClient starter, ILogger log)
        {
            dynamic eventData = await req.Content.ReadAsAsync<object>();
            string instanceId = eventData.instanceId;
            //string instanceId = req["instanceId"];
            log.LogInformation(instanceId);
            await starter.RaiseEventAsync(instanceId: instanceId, eventName: "ManagerApproval");
            log.LogInformation("Manager approved");
            return new HttpResponseMessage{StatusCode=System.Net.HttpStatusCode.OK};
        }
    }
}
