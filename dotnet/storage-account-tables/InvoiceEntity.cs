using System;
using Microsoft.Azure.Cosmos.Table;

namespace storage_account_tables
{    
    public class InvoiceEntity : TableEntity
    {
        public Guid InvoiceID { get; set; }
        public int InvoiceNumber { get; set; }
        public int InvoiceYear { get; set; }
        public int InvoiceMonth { get; set; }
        public int InvoiceDay { get; set; }
        
        public InvoiceEntity()
        {
        }

        public InvoiceEntity(Guid invoiceID, int invoiceNumber, int invoiceYear, int invoiceMonth, int invoiceDay)
        {            
            RowKey = invoiceID.ToString();
            PartitionKey = invoiceYear.ToString();
            InvoiceID = invoiceID;
            InvoiceNumber = invoiceNumber;
            InvoiceYear = invoiceYear;
            InvoiceMonth = invoiceMonth;
            InvoiceDay = invoiceDay;
        }

        public InvoiceEntity(InvoiceDTO invoice)
        {
            RowKey = invoice.InvoiceID.ToString();
            PartitionKey = invoice.InvoiceYear.ToString();
            InvoiceID = invoice.InvoiceID;
            InvoiceNumber = invoice.InvoiceNumber;
            InvoiceYear = invoice.InvoiceYear;
            InvoiceMonth = invoice.InvoiceMonth;
            InvoiceDay = invoice.InvoiceDay;
        }
    }
}