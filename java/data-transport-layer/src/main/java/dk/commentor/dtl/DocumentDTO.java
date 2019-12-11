package dk.commentor.dtl;

import java.util.Calendar;
import java.util.UUID;

public class DocumentDTO {
    public UUID GUID;
    public String Title;
    public String Author;
    public String MIMEType;
    public Calendar LastUpdated;
    public byte[] Contents;
}
