package eu.dc5.domain.model;

public enum DC5BusinessDocumentStates {
    CREATED((byte) 1), SUBMITTED((byte) 2), RELAYED((byte) 3), REJECTED((byte) 4), CONFIRMED((byte) 5), DELIVERED((byte) 6), RETRIEVED((byte) 7);

    private final byte code;

    private DC5BusinessDocumentStates(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

}
