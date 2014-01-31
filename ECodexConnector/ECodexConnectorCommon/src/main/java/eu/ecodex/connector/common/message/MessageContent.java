package eu.ecodex.connector.common.message;

import eu.ecodex.connector.common.enums.DetachedSignatureMimeType;

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
    private byte[] detachedSignature;
    private DetachedSignatureMimeType detachedSignatureMimeType;

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

    public byte[] getDetachedSignature() {
        return detachedSignature;
    }

    public void setDetachedSignature(byte[] detachedSignature) {
        this.detachedSignature = detachedSignature;
    }

    public DetachedSignatureMimeType getDetachedSignatureMimeType() {
        return detachedSignatureMimeType;
    }

    public void setDetachedSignatureMimeType(DetachedSignatureMimeType detachedSignatureMimeType) {
        this.detachedSignatureMimeType = detachedSignatureMimeType;
    }

}
