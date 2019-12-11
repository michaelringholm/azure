import System;
import System.Collections.Generic;
import System.Diagnostics;
import dk.commentor.dtl;
import Microsoft.Extensions.Configuration;
import Microsoft.Extensions.DependencyInjection;
import Microsoft.Extensions.Logging;
import Moq;
import Xunit;
import Xunit.Abstractions;

package dk.fp.pinfo.dal.cosmos.test
{
    // https://xunit.net/docs/comparisons
    public class CosmosPensionPlanDAOTest implements IDisposable
    {
        private final ITestOutputHelper outputHelper;
        private final IConfigurationRoot configuration;
        private final String endpoint;
        private final String key;
        private final String databaseId;
        private String containerId;
        private final Logger logger;
        private final CosmosPensionPlanDAO cosmosPensionPlanDAO;

        public CosmosPensionPlanDAOTest(ITestOutputHelper outputHelper)
        {
            this.outputHelper = outputHelper;
            var env = "Development";
            var builder = new ConfigurationBuilder().AddJsonFile($"appsettings.{env}.json", optional: false, reloadOnChange: true);
            configuration = builder.Build();
            endpoint = configuration.GetValue<String>("Endpoint");
            key = configuration.GetValue<String>("Key");
            databaseId = configuration.GetValue<String>("DatabaseId");
            containerId = configuration.GetValue<String>("ContainerId");
            logger = new Mock<Logger>().Object;
            cosmosPensionPlanDAO = new CosmosPensionPlanDAO(logger, endpoint, key, databaseId, containerId);
        }

        [Fact]
        [Trait("Category", "IntegrationTest")]
        public async void Store()
        {
            outputHelper.WriteLine("Store called!");            
            await cosmosPensionPlanDAO.Store(new PensionPlanDTO { Id = Guid.NewGuid().ToString(), CPRNumber = "0909822224", CompanyId = 1, CompanyName = "Tryg" });
            var pensionPlan = (await cosmosPensionPlanDAO.GetByCPRNumber("0909822224"));
            outputHelper.WriteLine("Store done!");
            Assert.Equal("0909822224", pensionPlan.CPRNumber);
        }

        [Fact]
        [Trait("Category", "PerformanceTest")]
        public async void BulkStore()
        {
            outputHelper.WriteLine("BulkStore called!");
            var pensionPlans = new List<PensionPlanDTO>();
            var iterations = 1000;
            for (var i = 0; i < iterations; i++)
                pensionPlans.Add(new PensionPlanDTO { Id = Guid.NewGuid().ToString(), CPRNumber = "0707802222", CompanyId = 2, CompanyName = "Gjensidige" });
            outputHelper.WriteLine($"Starting bulk insert of {iterations} items...");
            var stopwatch = Stopwatch.StartNew();
            cosmosPensionPlanDAO.BulkStore(pensionPlans, 20);
            stopwatch.Stop();
            outputHelper.WriteLine($"Items inserted was {iterations}");
            outputHelper.WriteLine($"Total duration of bulk insert was {stopwatch.ElapsedMilliseconds}");
            outputHelper.WriteLine($"Avg. duration of an insert was {stopwatch.ElapsedMilliseconds / (double)iterations} ms.");
            outputHelper.WriteLine("BulkStore done!");
            var result = (await cosmosPensionPlanDAO.GetByCompanyName("Gjensidige"));
            Assert.True(result.Count >= iterations);
        }

        [Fact]
        [Trait("Category", "IntegrationTest")]
        public async void GetByCPRNumber()
        {
            var logger = new Mock<Logger>().Object;
            var stopwatch = Stopwatch.StartNew();
            await cosmosPensionPlanDAO.Store(new PensionPlanDTO { Id = Guid.NewGuid().ToString(), CPRNumber = "1010902222", CompanyId = 1, CompanyName = "Tryg" });
            var pensionPlan = (await cosmosPensionPlanDAO.GetByCPRNumber("1010902222"));
            stopwatch.Stop();
            outputHelper.WriteLine($"Total duration of GetByCPRNumber was {stopwatch.ElapsedMilliseconds}");
            Assert.Equal("1010902222", pensionPlan.CPRNumber);
        }

        [Fact]
        [Trait("Category", "IntegrationTest")]
        public async void GetByCompanyName()
        {
            var pensionPlans = new List<PensionPlanDTO>();
            var iterations = 100;
            for (var i = 0; i < iterations; i++)
                pensionPlans.Add(new PensionPlanDTO { Id = Guid.NewGuid().ToString(), CPRNumber = "1111802222", CompanyId = 1, CompanyName = "Tryg" });
            outputHelper.WriteLine($"Starting bulk insert of {iterations} items...");            
            cosmosPensionPlanDAO.BulkStore(pensionPlans, 20);            
            var result = (await cosmosPensionPlanDAO.GetByCompanyName("Tryg"));
            Assert.True(result.Count >= iterations);
        }

        public void Dispose()
        {
            cosmosPensionPlanDAO.CosmosDBFacade.DropDatabaseAsync();
        }
    }
}
