package com.docuware.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;

import dk.commentor.dtl.DocumentDTO;
import dk.commentor.sl.IDocumentService;

public class DocuwareService implements IDocumentService {
    private final Logger logger;

    public DocuwareService(Logger logger) {
        this.logger = logger;
    }

    public List<DocumentDTO> GetDocuments(String attrName, String attrValue)
        {
            logger.debug("DocuwareService.GetDocuments called!");
            List<DocumentDTO> documents = new ArrayList<DocumentDTO>();
            DocumentDTO document = new DocumentDTO();
            document.GUID = UUID.randomUUID(); document.Author = "Scott Hanselman"; document.Title = "Coding C#"; document.LastUpdated = Calendar.getInstance();
            documents.add(document);
            document = new DocumentDTO();
            document.GUID = UUID.randomUUID(); document.Author = "Scott Hanselman"; document.Title = ".Net Framework"; document.LastUpdated = Calendar.getInstance();
            documents.add(document);
            document = new DocumentDTO();
            document.GUID = UUID.randomUUID(); document.Author = "Scott Hanselman"; document.Title = "Microsoft Asp.Net"; document.LastUpdated = Calendar.getInstance();
            documents.add(document);
            return documents;
        }
}
