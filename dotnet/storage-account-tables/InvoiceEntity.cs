using Microsoft.Azure.Cosmos.Table;

namespace storage_account_tables
{    
    public class InvoiceEntity : TableEntity
    {
        public InvoiceEntity()
        {
        }

        public InvoiceEntity(string lastName, string firstName)
        {
            PartitionKey = lastName;
            RowKey = firstName;
        }

        public string Email { get; set; }
        public string PhoneNumber { get; set; }
    }
}