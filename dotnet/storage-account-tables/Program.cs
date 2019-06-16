using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.Azure.Cosmos.Table;
//using Microsoft.Azure.Storage;
using Microsoft.Extensions.Configuration;

namespace storage_account_tables
{
    class Program
    {
        private static string _storageConnectionString;

        public string StorageConnectionString { get; set; }
        static void Main(string[] args)
        {
            Console.WriteLine("Started");
            LoadAppSettings();
            Console.WriteLine("Ended!");
        }

        public static void LoadAppSettings()
        {
            IConfiguration configRoot = new ConfigurationBuilder().SetBasePath(Directory.GetCurrentDirectory()).AddJsonFile("appsettings.json").Build();
            _storageConnectionString = configRoot.GetValue<String>("StorageConnectionString");
            var cloudTable = CreateTableAsync("mytable").ConfigureAwait(false).GetAwaiter().GetResult();
        }

        public static CloudStorageAccount CreateStorageAccountFromConnectionString()
        {
            CloudStorageAccount storageAccount;
            try
            {
                storageAccount = CloudStorageAccount.Parse(_storageConnectionString);
            }
            catch (FormatException)
            {
                Console.WriteLine("Invalid storage account information provided. Please confirm the AccountName and AccountKey are valid in the app.config file - then restart the application.");
                throw;
            }
            catch (ArgumentException)
            {
                Console.WriteLine("Invalid storage account information provided. Please confirm the AccountName and AccountKey are valid in the app.config file - then restart the sample.");
                Console.ReadLine();
                throw;
            }

            return storageAccount;
        }

        public static async Task<CloudTable> CreateTableAsync(string tableName)
        {
            // Retrieve storage account information from connection string.
            CloudStorageAccount storageAccount = CreateStorageAccountFromConnectionString();

            // Create a table client for interacting with the table service
            //CloudTableClient tableClient = storageAccount.CreateCloudTableClient(new TableClientConfiguration());
            CloudTableClient tableClient = storageAccount.CreateCloudTableClient();

            Console.WriteLine("Create a Table for the demo");

            // Create a table client for interacting with the table service 
            CloudTable table = tableClient.GetTableReference(tableName);
            if (await table.CreateIfNotExistsAsync())
            {
                Console.WriteLine("Created Table named: {0}", tableName);
            }
            else
            {
                Console.WriteLine("Table {0} already exists", tableName);
            }

            Console.WriteLine();
            return table;
        }

        
    }
}
