using System.Collections.Generic;

namespace storage_account_tables
{
    public interface IInvoiceDAO
    {
        void Drop();
        void InsertOrMerge(InvoiceDTO invoice);
        List<InvoiceDTO> GetAll();
    }
}