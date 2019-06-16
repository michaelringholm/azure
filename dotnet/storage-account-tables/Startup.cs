using Microsoft.Extensions.DependencyInjection;

namespace storage_account_tables
{
    public static class AppConfig
    {

        public static ServiceProvider BuildServiceProvider()
        {
            var serviceCollection = new ServiceCollection();
            //serviceCollection.AddSingleton<IDocumentService,DocuwareService>();
            return serviceCollection.BuildServiceProvider();
        }
    }
}