using System;

namespace storage_account_tables
{
    public class InvoiceDTO
    {
        public Guid InvoiceID { get; set; }
        public int InvoiceNumber { get; set; }
        public int InvoiceYear { get; set; }
        public int InvoiceMonth { get; set; }
        public int InvoiceDay { get; set; }        
    }
}