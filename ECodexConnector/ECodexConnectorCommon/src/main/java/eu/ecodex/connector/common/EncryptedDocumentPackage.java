package eu.ecodex.connector.common;

public class EncryptedDocumentPackage {

    byte[] trustOkTokenXml;
    Object asicsContainer;
    byte[] content;

    public byte[] getTrustOkTokenXml() {
        return trustOkTokenXml;
    }

    public void setTrustOkTokenXml(byte[] trustOkTokenXml) {
        this.trustOkTokenXml = trustOkTokenXml;
    }

    public Object getAsicsContainer() {
        return asicsContainer;
    }

    public void setAsicsContainer(Object asicsContainer) {
        this.asicsContainer = asicsContainer;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}
