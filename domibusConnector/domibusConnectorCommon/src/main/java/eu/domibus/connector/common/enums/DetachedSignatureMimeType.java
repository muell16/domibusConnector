package eu.domibus.connector.common.enums;

public enum DetachedSignatureMimeType {
    /**
     * application/octet-stream
     */
    BINARY("application/octet-stream"),
    /**
     * text/xml
     */
    XML("text/xml"),
    /**
     * application/pkcs7-signature
     */
    PKCS7("application/pkcs7-signature");

    private final String code;

    /**
     * constructor
     * 
     * @param code
     *            the value of the mime-type's code
     */
    private DetachedSignatureMimeType(final String code) {
        this.code = code;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }
}
