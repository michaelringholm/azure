using System.Collections.Generic;
using System.Linq;
//using Microsoft.Azure.Storage;
//using Microsoft.Azure.Storage;
using Microsoft.Azure.Cosmos.Table;
using Microsoft.Extensions.Configuration;

namespace storage_account_tables
{
    public class InvoiceDAO : IInvoiceDAO
    {
        private CloudTable table;

        public InvoiceDAO(IConfiguration configuration)
        {
            CloudStorageAccount storageAccount = CloudStorageAccount.Parse(configuration.GetValue<string>("StorageConnectionString"));
            CloudTableClient tableClient = storageAccount.CreateCloudTableClient();
            table = tableClient.GetTableReference("invoice");
            table.CreateIfNotExists();
            //cloudTable = new StorageAccountTable(configuration).CreateTableAsync("invoice").ConfigureAwait(false).GetAwaiter().GetResult();
        }
        
        public List<InvoiceDTO> GetAll()
        {
            //cloudTable.Execute(TableOperation.Retrieve, null, null);
            TableQuery<InvoiceEntity> query = new TableQuery<InvoiceEntity>().Where(TableQuery.GenerateFilterCondition("PartitionKey", QueryComparisons.Equal, "2019"));
            var invoices = table.ExecuteQuery(query);
            var invoiceList = invoices.Select(i => new InvoiceDTO{InvoiceID = i.InvoiceID, InvoiceYear = i.InvoiceYear}).ToList();
            return invoiceList;
        }

        public void InsertOrMerge(InvoiceDTO invoice)
        {
            var insertOperation = TableOperation.Insert(new InvoiceEntity(invoice));
            table.Execute(insertOperation);
            //table.Execute(TableOperation.InsertOrMerge(new InvoiceEntity(invoice)));
        }

        public void Drop()
        {
            table.Delete();
        }
    }
}