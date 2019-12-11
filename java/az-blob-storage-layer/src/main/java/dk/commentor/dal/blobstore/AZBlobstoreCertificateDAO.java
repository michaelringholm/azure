package dk.commentor.dal.blobstore;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import dk.commentor.dal.ICertificateDAO;
import dk.commentor.dtl.CertificateDTO;

public class AZBlobstoreCertificateDAO implements ICertificateDAO {
    private final Logger logger;
    private final AzureBlobService azureBlobService;
    private final String blobName;
    private final Object lockObj="";

    public AZBlobstoreCertificateDAO(Logger logger, String blobContainerConnString, String blobName) throws Exception {
        if (logger == null)
            throw new Exception("parameter logger was null!");
        if (blobContainerConnString == null)
            throw new Exception("parameter blobContainerConnString was null!");
        if (blobName == null)
            throw new Exception("parameter blobName was null!");
        this.logger = logger;
        azureBlobService = new AzureBlobService(blobContainerConnString);
        this.blobName = blobName;
    }

    public CompletableFuture<List<CertificateDTO>> GetCertificates() throws Exception
    {
        logger.debug("AZBlobstoreCertificateDAO.GetTechProfileNames called!");
        return azureBlobService.getBlobContentsAsString("certificates", blobName).thenApplyAsync( result -> {
            logger.debug(result);
            Type listType = new TypeToken<List<CertificateDTO>>(){}.getType();
            List<CertificateDTO> certificates = new Gson().fromJson(result, listType);
            return certificates;
        });
        //});
        /*return CompletableFuture.supplyAsync( () -> {
            logger.debug("AZBlobstoreCertificateDAO.GetTechProfileNames called!");
            String certificateDataJSON = azureBlobService.getBlobContentsAsString("certificates", blobName).get();
            Type listType = new TypeToken<List<CertificateDTO>>(){}.getType();
            List<CertificateDTO> certificates = new Gson().fromJson(certificateDataJSON, listType);
            return certificates;
        });*/
    }

    public CompletableFuture<CertificateDTO> Restore(String imoNumber)
    {
        //GetCertificates()).Where(c => c.IMONumber.Equals(imoNumber)).FirstOrDefault();
        return null;
    }

    public CompletableFuture Store(CertificateDTO certificate)
    {
        /*var certificates = GetCertificates().get();
        var existingCertificate = certificates.Where(c => c.IMONumber.Equals(certificate.IMONumber)).FirstOrDefault();
        if(existingCertificate != null) {
            // Update
            existingCertificate.Issued = certificate.Issued;
        }
        else {
            // Insert
            certificates.Add(certificate);
        }
        String jsonData = Newtonsoft.Json.JsonConvert.SerializeObject(certificates);
        azureBlobService.putBlobContents("certificates", blobName, jsonData);*/
        return null;
    }
}
