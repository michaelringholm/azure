using System.Collections.Generic;

namespace storage_account_tables
{
    public interface IInvoiceDAO
    {
        void TruncateTable();
        void Insert(InvoiceDTO invoice);
        List<InvoiceDTO> GetAll();
    }
}