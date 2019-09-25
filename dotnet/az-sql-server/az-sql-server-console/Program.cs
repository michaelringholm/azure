using System;
using System.IO;
using System.Threading.Tasks;
using az_key_vault;
using az_sql_server;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace az_sql_server_console
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Started...");
            var env = Environment.GetEnvironmentVariable("ASPNETCORE_ENVIRONMENT");
            var builder = new ConfigurationBuilder().SetBasePath(Directory.GetCurrentDirectory()).AddJsonFile($"appsettings.{env}.json", optional: true, reloadOnChange: true);
            var diContainer = new ServiceCollection();
            diContainer.AddSingleton<ISecurityVault, AzureKeyVault>();
            diContainer.AddSingleton<IConfigurationRoot>(builder.Build());
            var diProvider = diContainer.BuildServiceProvider();
            var securityVault = diProvider.GetService<ISecurityVault>(); 
            var dbConnString = securityVault.GetSecret("dbConnString");
            DoDBStuff(dbConnString).ConfigureAwait(false).GetAwaiter().GetResult();
            Console.WriteLine("Ended!");
        }

        private async static Task DoDBStuff(string dbConnString)
        {
            ITaskDAO taskDAO = new TaskDAO(dbConnString);
            await taskDAO.SaveExecutionInformation(new TaskExecutionInformation{Id=Guid.NewGuid(), Duration=457, Status = "Success"});
        }
    }
}
