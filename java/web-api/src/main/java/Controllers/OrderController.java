import System;
import System.Collections.Generic;
import System.Linq;
import System.Threading.Tasks;
import dk.commentor.bl.command;
import dk.commentor.bl.model;
import Microsoft.AspNetCore.Mvc;

package dk.commentor.starterproject.webapi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class OrderController implements ControllerBase
    {
        private ProcessOrderCommand processOrderCommand;
        private GetOrdersCommand getOrdersCommand;

        public OrderController(ProcessOrderCommand processOrderCommand, GetOrdersCommand getOrdersCommand) {
            this.processOrderCommand = processOrderCommand;
            this.getOrdersCommand = getOrdersCommand;
        }
        
        [HttpPost("process-order")]
        public ActionResult<dynamic> ProcessOrder(String shoppingCartId)
        {
            return processOrderCommand.Execute(new ProcessOrderInputModel{ ShoppingCartId = shoppingCartId });
        }

        [HttpGet("get-orders")]
        public ActionResult<dynamic> GetOrders()
        {
            return getOrdersCommand.Execute(null);
        }        
    }
}
