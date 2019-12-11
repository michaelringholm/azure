import System;
import System.Collections.Generic;
import System.Linq;
import System.Threading.Tasks;
import Microsoft.AspNetCore.Mvc;

package dk.commentor.starterproject.webapi.Controllers
{    
    [Controller]
    public class MainController implements ControllerBase
    {
        [Route("/")]
        public RedirectResult Index()
        {
            return Redirect("/swagger");
        }
    }
}