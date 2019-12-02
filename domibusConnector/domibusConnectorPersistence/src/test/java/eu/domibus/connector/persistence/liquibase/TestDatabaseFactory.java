package eu.domibus.connector.persistence.liquibase;

public interface TestDatabaseFactory {
    /**
     * @return type of the database
     */
    String getDatabaseType();

    /*
        returns the version of the database
         */
    String getVersion();

    String getName();

    boolean isAvailable(String version);

    /**
     * should create on each call a new fresh database
     */
    TestDatabase createNewDatabase(String version);
}
