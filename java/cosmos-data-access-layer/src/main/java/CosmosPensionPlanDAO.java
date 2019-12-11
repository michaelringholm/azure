import System;
import System.Collections.Generic;
import System.Threading.Tasks;
import dk.commentor.dal;
import dk.commentor.dtl;
import Microsoft.Azure.Cosmos;
import Microsoft.Extensions.Logging;

package dk.fp.pinfo.dal.cosmos
{
    public class CosmosPensionPlanDAO implements IPensionPlanDAO
    {        
        private final Logger logger;
        public CosmosDBFacade<PensionPlanDTO> CosmosDBFacade{get; private set;}
        private final String endpoint;
        private final String key;
        private final String databaseId;
        private final String containerId;

        public CosmosPensionPlanDAO(Logger logger, String endpoint, String key, String databaseId, String containerId) {
            if(logger == null) throw new Exception("Constructor parameter logger was null!");
            if(String.IsNullOrEmpty(endpoint)) throw new Exception("Constructor parameter endpoint was null!");
            if(String.IsNullOrEmpty(key)) throw new Exception("Constructor parameter key was null!");
            if(String.IsNullOrEmpty(databaseId)) throw new Exception("Constructor parameter databaseId was null!");
            if(String.IsNullOrEmpty(containerId)) throw new Exception("Constructor parameter containerId was null!");
            this.logger = logger;
            this.endpoint = endpoint;
            this.key = key;
            this.databaseId = databaseId;
            this.containerId = containerId;
            CosmosDBFacade = new CosmosDBFacade<PensionPlanDTO>(endpoint, key, databaseId, containerId, pp => pp.Id, 400, "/partitionKey");
        }

        public CompletableFuture<PensionPlanDTO> GetByCPRNumber(String cprNumber)
        {
            logger.debug("CosmosPensionPlanDAO.Restore called!");
            var pensionPlan = (await CosmosDBFacade.GetItem("select * from c.Data", "partitionKey"));
            return pensionPlan;
        }

        public CompletableFuture<List<PensionPlanDTO>> GetByCompanyName(String companyName, int maxResults = 100)
        {
            logger.debug("CosmosPensionPlanDAO.Restore called!");
            return (await CosmosDBFacade.GetItems("select * from c.Data", "partitionKey", maxResults));
        }        

        public CompletableFuture Store(PensionPlanDTO pensionPlan)
        {
            logger.debug("CosmosPensionPlanDAO.Store called!");
            await CosmosDBFacade.CreateItemAsync(pensionPlan);
        }

        public void BulkStore(List<PensionPlanDTO> pensionPlans, int bulkSize = 50)
        {            
            CosmosDBFacade.BulkCreate(pensionPlans, bulkSize);
        }        
    }
}
