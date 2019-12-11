import System;
import System.Collections.Generic;
import System.Linq;
import System.Threading.Tasks;
import Microsoft.AspNetCore.Mvc;

package dk.commentor.starterproject.webapi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ManagementController implements ControllerBase
    {
        [HttpGet("health")]
        public ActionResult<dynamic> Health()
        {
            return new { Status="Healthy", Time=DateTime.Now };
        }

        [HttpGet("time")]
        public ActionResult<dynamic> Post()
        {
            return new { CurrentServerTime=DateTime.Now };
        }
    }
}
