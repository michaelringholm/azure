import System;
import System.IO;
import System.Threading.Tasks;
import Microsoft.AspNetCore.Mvc;
import Microsoft.Azure.WebJobs;
import Microsoft.Azure.WebJobs.Extensions.Http;
import Microsoft.AspNetCore.Http;
import Microsoft.Extensions.Logging;
import Newtonsoft.Json;
import dk.commentor.bl.command;

package dk.commentor.starterproject.api
{
    public class ProcessOrder
    {
        private ProcessOrderCommand processOrderCommand;

        public ProcessOrder(ProcessOrderCommand processOrderCommand) {
            this.processOrderCommand = processOrderCommand;
        }

        [FunctionName("ProcessOrder")]
        public CompletableFuture<IActionResult> Run([HttpTrigger(AuthorizationLevel.Function, "post", Route = null)] HttpRequest req, Logger log)
        {
            log.info("C# HTTP trigger ProcessOrder called!");
            log.info(System.Environment.StackTrace);
            
            String jsonRequestData = await new StreamReader(req.Body).ReadToEndAsync();
            dynamic requestData = JsonConvert.DeserializeObject(jsonRequestData);

            if(requestData?.shoppingCartId != null) {
                processOrderCommand.Execute(new bl.model.ProcessOrderInputModel{ ShoppingCartId=requestData.shoppingCartId });
                return (ActionResult)new OkObjectResult($"Processing order with shoppingCartId {requestData.shoppingCartId}");
            }
            else
                return new BadRequestObjectResult("Please pass an shoppingCartId in the request body");
        }
    }
}
