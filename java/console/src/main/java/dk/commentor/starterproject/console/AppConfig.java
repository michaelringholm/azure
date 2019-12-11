package dk.commentor.starterproject.console;

import static org.mockito.Mockito.mock;

import com.docuware.service.DocuwareService;
import com.paypal.PayPalService;

import org.mockito.Mock;
import org.openerp.OpenERPService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import dk.commentor.bl.bo.CaseWorkerBO;
import dk.commentor.bl.command.ProcessOrderCommand;
import dk.commentor.dal.ICertificateDAO;
import dk.commentor.dal.blobstore.AZBlobstoreCertificateDAO;
import dk.commentor.logging.CommentorLogger;
import dk.commentor.sl.IDocumentService;
import dk.commentor.sl.IOrderService;
import dk.commentor.sl.IPaymentService;

@Configuration
@PropertySource({"classpath:application.properties"})
public class AppConfig {
    @Autowired Environment env;

    @Bean
    public Logger logger() {
        return new CommentorLogger();
    }

    @Bean
    IOrderService orderService() {
        return mock(OpenERPService.class);
    }

    @Bean IPaymentService paymentService() {
        return new PayPalService(logger());
    }

    @Bean ICertificateDAO certificateDAO() throws Exception {
        String blobContainerConnectionString = env.getProperty("BlobConnectionString");
        String blobName = env.getProperty("BlobName");
        return new AZBlobstoreCertificateDAO(logger(), blobContainerConnectionString, blobName);
    }

    @Bean IDocumentService documentService() throws Exception {
        return new DocuwareService(logger());
    }      

    @Bean public ProcessOrderCommand processOrderCommand() throws Exception {
        return new ProcessOrderCommand(orderService(), paymentService(), logger());
    }

    @Bean public CaseWorkerBO caseWorkerBO() throws Exception{
        return new CaseWorkerBO(certificateDAO(), documentService());
    }
}