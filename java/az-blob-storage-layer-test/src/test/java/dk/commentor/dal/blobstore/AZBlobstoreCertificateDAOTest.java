package dk.commentor.dal.blobstore;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Properties;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import dk.commentor.dal.ICertificateDAO;
import dk.commentor.dtl.CertificateDTO;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class AZBlobstoreCertificateDAOTest {
    @Autowired private Environment env;
    
	@Test
	@Category(IntegrationTest.class)
	public void GetCertificates() throws Exception { //async
        Logger logger = mock(Logger.class);
        /*var env = Environment.GetEnvironmentVariable("JAVA_ENVIRONMENT");
        if(env == null) throw new Exception("Could not find JAVA_ENVIRONMENT!");
        Properties properties = new PropertyLoad
        var configuration = builder.Build();*/        
        
        //String blobStorageConnString = System.getProperty("BlobStorageConnString");
        String blobStorageConnString = env.getProperty("BlobStorageConnString");
        ICertificateDAO azBlobstoreCertificateDAO = new AZBlobstoreCertificateDAO(logger, blobStorageConnString, "data.json");
        List<CertificateDTO> certificates = azBlobstoreCertificateDAO.GetCertificates().get(); // await

        assertEquals("100200", certificates.get(0).IMONumber);
        assertEquals("300500", certificates.get(1).IMONumber);
    }

    //@Test
	@Category(IntegrationTest.class)
    public void Store() throws Exception { // async
        Logger logger = mock(Logger.class);
        /*var env = Environment.GetEnvironmentVariable("JAVA_ENVIRONMENT");
        if(env == null) throw new Exception("Could not find JAVA_ENVIRONMENT!");
        var builder = new ConfigurationBuilder().AddJsonFile($"appsettings.{env}.json", optional: false, reloadOnChange: true);
        var configuration = builder.Build();*/
        String blobStorageConnString = System.getProperty("BlobStorageConnString");
        ICertificateDAO azBlobstoreCertificateDAO = new AZBlobstoreCertificateDAO(logger, blobStorageConnString, "data.json");
        //azBlobstoreCertificateDAO.Store(new CertificateDTO { IMONumber = "100200", Issued = DateTime.Now });
        //azBlobstoreCertificateDAO.Store(new CertificateDTO { IMONumber = "300500", Issued = DateTime.Now.AddMonths(-2) });
        List<CertificateDTO> certificates = azBlobstoreCertificateDAO.GetCertificates().get();

        assertEquals("100200", certificates.get(0).IMONumber);
        assertEquals("300500", certificates.get(1).IMONumber);
    }            
}