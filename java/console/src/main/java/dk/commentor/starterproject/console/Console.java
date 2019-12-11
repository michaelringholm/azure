package dk.commentor.starterproject.console;

import java.lang.reflect.Type;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dk.commentor.bl.bo.CaseWorkerBO;
import dk.commentor.bl.command.ProcessOrderCommand;
import dk.commentor.bl.model.ProcessOrderInputModel;
import dk.commentor.dtl.CertificateDTO;
import dk.commentor.dtl.DocumentDTO;
import dk.commentor.dtl.OrderDTO;

@SpringBootApplication
public class Console implements CommandLineRunner {
    public static void main(String[] args) {
        System.out.println("Dependency injection demo");
        SpringApplication.run(Console.class, args);
    }

    @Autowired private Logger logger;
    @Autowired private ProcessOrderCommand processOrderCommand;
    @Autowired private CaseWorkerBO caseWorkerBO;

	@Override
	public void run(String... args) throws Exception {
		logger.info("Running v1.0...");
        //createCustomerCommand.Execute();
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		
        Type listType = new TypeToken<List<CertificateDTO>>(){}.getType();
        //List<CertificateDTO> certificates = new Gson().fromJson("[{\"IMONumber\":\"100200\",\"Issued\":\"2019-07-24T13:09:04.2742868+02:00\"},{\"IMONumber\":\"300500\",\"Issued\":\"2019-05-24T13:09:04.4803459+02:00\"}]", listType);
        List<CertificateDTO> certificates = gson.fromJson("[{\"IMONumber\":\"100200\"},{\"IMONumber\":\"300500\"}]", listType);
		
        logger.info("Starter Project Console - Started...");
        String imoNumber = "100200";
        CertificateDTO shipCertificate = caseWorkerBO.GetShipCertificate(imoNumber).get();
        List<DocumentDTO> shipDocuments = caseWorkerBO.GetShipDocuments(imoNumber);
        OrderDTO order = processOrderCommand.Execute(new ProcessOrderInputModel()).get();            
        logger.info("Starter Project Console - Ended!");        
	}
}