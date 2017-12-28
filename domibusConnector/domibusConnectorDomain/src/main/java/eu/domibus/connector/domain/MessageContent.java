package eu.domibus.connector.domain;

import eu.domibus.connector.domain.enums.DetachedSignatureMimeType;


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
    private byte[] internationalContent;
    private byte[] pdfDocument;
    private String pdfDocumentName;
    private byte[] detachedSignature;
    private DetachedSignatureMimeType detachedSignatureMimeType;
    private String detachedSignatureName;

    public byte[] getNationalXmlContent() {
        return nationalXmlContent;
    }

    public void setNationalXmlContent(byte[] nationalXmlContent) {
        this.nationalXmlContent = nationalXmlContent;
    }

    public byte[] getInternationalContent() {
        return internationalContent;
    }

    public void setInternationalContent(byte[] internationalContent) {
        this.internationalContent = internationalContent;
    }

    public byte[] getPdfDocument() {
        return pdfDocument;
    }

    public void setPdfDocument(byte[] pdfDocument) {
        this.pdfDocument = pdfDocument;
    }

    public String getPdfDocumentName() {
        return pdfDocumentName;
    }

    public void setPdfDocumentName(String pdfDocumentName) {
        this.pdfDocumentName = pdfDocumentName;
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

    public String getDetachedSignatureName() {
        return detachedSignatureName;
    }

    public void setDetachedSignatureName(String detachedSignatureName) {
        this.detachedSignatureName = detachedSignatureName;
    }

}
