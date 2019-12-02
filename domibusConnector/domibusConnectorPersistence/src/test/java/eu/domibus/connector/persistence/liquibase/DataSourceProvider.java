package eu.domibus.connector.persistence.liquibase;

import javax.sql.DataSource;

public interface DataSourceProvider {
    /**
     * @return type of the database
     */
    String getDatabaseType();

    /*
        returns the version of the database
         */
    String getVersion();

    String getName();

    /**
     * should create on each call a new fresh database
     */
    DataSource createNewDataSource();
}
