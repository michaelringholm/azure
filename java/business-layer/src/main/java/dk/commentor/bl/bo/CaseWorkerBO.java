package dk.commentor.bl.bo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import dk.commentor.dal.ICertificateDAO;
import dk.commentor.dtl.CertificateDTO;
import dk.commentor.dtl.DocumentDTO;
import dk.commentor.sl.IDocumentService;

public class CaseWorkerBO
{
    private ICertificateDAO certificateDAO;
    private IDocumentService documentService;
    public CaseWorkerBO(ICertificateDAO certificateDAO, IDocumentService documentService) {
        this.certificateDAO = certificateDAO;
        this.documentService = documentService;
    }

    public CompletableFuture<CertificateDTO> GetShipCertificate(String imoNumber) throws Exception
    {
        CompletableFuture<CertificateDTO> certificates = certificateDAO.Restore(imoNumber);
        return certificates;
    }

    public List<DocumentDTO> GetShipDocuments(String imoNumber) {
        List<DocumentDTO> documents = documentService.GetDocuments("IMONumber", imoNumber);
        return documents;
    }        
}
