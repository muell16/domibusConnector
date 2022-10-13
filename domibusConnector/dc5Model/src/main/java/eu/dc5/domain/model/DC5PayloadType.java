package eu.dc5.domain.model;

public enum DC5PayloadType {
    BUSINESS_XML((byte) 1), BUSINESS_DOC((byte) 2), DETACHED_SIGNATURE((byte) 3), ATTACHMENT((byte) 4), ASICS((byte) 5), TOKEN_XM((byte) 6);

    private final byte code;

    DC5PayloadType(byte x) {
        this.code = x;
    }

    public byte getCode() {
        return code;
    }
}
