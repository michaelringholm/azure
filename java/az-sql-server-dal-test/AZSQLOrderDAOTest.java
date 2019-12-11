import System;
import System.Collections.Generic;
import System.Diagnostics;
import dk.commentor.dal;
import dk.commentor.dal.azsql;
import dk.commentor.dtl;
import dk.commentor.security.keyvault;
import Microsoft.Extensions.Configuration;
import Microsoft.Extensions.DependencyInjection;
import Microsoft.Extensions.Logging;
import Moq;
import Xunit;
import Xunit.Abstractions;

package dk.fp.pinfo.dal.cosmos.test;

    // https://xunit.net/docs/comparisons
    public class AZSQLOrderDAOTest implements IDisposable
    {
        private final ITestOutputHelper outputHelper;
        private final IConfigurationRoot configuration;
        private final Logger logger;
        private final IOrderDAO orderDAO;

        public AZSQLOrderDAOTest(ITestOutputHelper outputHelper)
        {
            this.outputHelper = outputHelper;
            var env = "Development";
            var builder = new ConfigurationBuilder().AddJsonFile($"appsettings.{env}.json", optional: false, reloadOnChange: true);
            configuration = builder.Build();
            var keyVault = new AzureKeyVault(configuration);
            logger = new Mock<Logger>().Object;
            orderDAO = new AZSQLOrderDAO(keyVault);
        }

        [Fact]
        [Trait("Category", "IntegrationTest")]
        public async void Store()
        {
            outputHelper.WriteLine("Store called!");            
            await orderDAO.Store(new OrderDTO { ID = Guid.NewGuid(), OrderID = 909822224, OrderDate = DateTime.Now });
            var order = (await orderDAO.Restore("909822224"));
            outputHelper.WriteLine("Store done!");
            Assert.Equal(909822224, order.OrderID);
        }

        public void Dispose()
        {
            //orderDAO.DropDatabaseAsync();
        }
    }
