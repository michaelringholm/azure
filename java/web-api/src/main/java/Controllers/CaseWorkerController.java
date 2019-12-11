import System;
import System.Collections.Generic;
import System.Linq;
import System.Threading.Tasks;
import dk.commentor.bl.bo;
import Microsoft.AspNetCore.Mvc;

package dk.commentor.starterproject.webapi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CaseWorkerController implements ControllerBase
    {
        private CaseWorkerBO caseWorkerBO;

        public CaseWorkerController(CaseWorkerBO caseWorkerBO) {
            this.caseWorkerBO = caseWorkerBO;
        }

        [HttpGet("ship-certificate")]
        public ActionResult<dynamic> GetShipCertificate(String imoNumber)
        {
            return caseWorkerBO.GetShipCertificate(imoNumber);
        }

        [HttpGet("ship-documents")]
        public ActionResult<dynamic> GetShipDocuments(String imoNumber)
        {
            return caseWorkerBO.GetShipDocuments(imoNumber);
        }        
    }
}
