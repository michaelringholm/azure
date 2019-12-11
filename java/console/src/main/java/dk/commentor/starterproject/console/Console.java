package dk.commentor.starterproject.console;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
        logger.info("Starter Project Console - Started...");
        String imoNumber = "100200";
        CertificateDTO shipCertificate = caseWorkerBO.GetShipCertificate(imoNumber).get();
        List<DocumentDTO> shipDocuments = caseWorkerBO.GetShipDocuments(imoNumber);
        OrderDTO order = processOrderCommand.Execute(new ProcessOrderInputModel()).get();            
        logger.info("Starter Project Console - Ended!");        
	}
}