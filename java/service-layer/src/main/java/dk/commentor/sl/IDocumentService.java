package dk.commentor.sl;

import java.util.List;
import dk.commentor.dtl.DocumentDTO;

public interface IDocumentService
{
    List<DocumentDTO> GetDocuments(String attrName, String attrValue);
}
