using System.Collections.Generic;
using Microsoft.Azure.Cosmos.Table;
using Microsoft.Extensions.Configuration;

namespace storage_account_tables
{
    public class InvoiceDAO : IInvoiceDAO
    {
        private CloudTable cloudTable;

        public InvoiceDAO(IConfiguration configuration) {
            cloudTable = new StorageAccountTable(configuration).CreateTableAsync("invoice").ConfigureAwait(false).GetAwaiter().GetResult();
        }
        public List<InvoiceDTO> GetAll()
        {
            //cloudTable.Execute(TableOperation.Retrieve, null, null);
            return null;
        }

        public void Insert(InvoiceDTO invoice)
        {
            //cloudTable.Execute(TableOperation.Insert);
        }

        public void TruncateTable()
        {
            cloudTable.Delete();
        }
    }
}