package eu.domibus.connector.persistence.liquibase;

import javax.sql.DataSource;

public interface TestDatabase extends AutoCloseable {

    DataSource getDataSource();

}
