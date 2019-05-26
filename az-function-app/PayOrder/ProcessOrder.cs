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
    public class ProcessOrder
    {
        private ProcessOrderCommand command;

        public ProcessOrder(ProcessOrderCommand command) {
            this.command = command;
        }

        [FunctionName("ProcessOrder")]
        public async Task<IActionResult> Run([HttpTrigger(AuthorizationLevel.Function, "get", "post", Route = null)] HttpRequest req, ILogger log)
        {
            log.LogInformation("C# HTTP trigger ProcessOrder called!");
            log.LogInformation(System.Environment.StackTrace);
            
            string jsonRequestData = await new StreamReader(req.Body).ReadToEndAsync();
            dynamic requestData = JsonConvert.DeserializeObject(jsonRequestData);

            if(requestData?.orderId != null) {
                command.Execute(requestData);
                return (ActionResult)new OkObjectResult($"Started processing order with id {requestData.orderId}");
            }
            else
                return new BadRequestObjectResult("Please pass an orderId in the request body");
        }
    }
}
