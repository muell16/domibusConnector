package eu.ecodex.connector.common.message;

/**
 * This is a container object that contains the content of the message in XML
 * format as well as the PDF document itself. It is mandatory to build a
 * {@link Message} object.
 * 
 * @author riederb
 * 
 */
public class MessageContent {

    private byte[] nationalXmlContent;
    private byte[] eCodexContent;
    private byte[] pdfDocument;

    public byte[] getNationalXmlContent() {
        return nationalXmlContent;
    }

    public void setNationalXmlContent(byte[] nationalXmlContent) {
        this.nationalXmlContent = nationalXmlContent;
    }

    public byte[] getECodexContent() {
        return eCodexContent;
    }

    public void setECodexContent(byte[] eCodexContent) {
        this.eCodexContent = eCodexContent;
    }

    public byte[] getPdfDocument() {
        return pdfDocument;
    }

    public void setPdfDocument(byte[] pdfDocument) {
        this.pdfDocument = pdfDocument;
    }

}
