package eu.domibus.connector.domain.enums;

import java.util.stream.Stream;

public enum MessageTargetSource {

    GATEWAY("GATEWAY"),
    BACKEND("BACKEND");

    public static MessageTargetSource ofOfDbName(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Stream.of(MessageTargetSource.values())
                .filter(t -> t.getDbName().equals(dbData))
                .findFirst()
                .get();
    }

    MessageTargetSource(String dbName) {
        this.dbName = dbName;
    }

    /**
     * This name is used within the database
     */
    private String dbName;


    public String getDbName() {
        return dbName;
    }
}
