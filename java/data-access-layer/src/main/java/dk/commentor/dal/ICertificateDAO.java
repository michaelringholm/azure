package dk.commentor.dal;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import dk.commentor.dtl.CertificateDTO;

public interface ICertificateDAO
{
    CompletableFuture<List<CertificateDTO>> GetCertificates() throws Exception;
    CompletableFuture<Void> Store(CertificateDTO certificate) throws Exception;
    CompletableFuture<CertificateDTO> Restore(String imoNumber) throws Exception;
}