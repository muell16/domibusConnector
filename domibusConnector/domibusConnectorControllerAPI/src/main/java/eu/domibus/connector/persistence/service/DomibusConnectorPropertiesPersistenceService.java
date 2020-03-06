package eu.domibus.connector.persistence.service;

import java.util.Properties;

public interface DomibusConnectorPropertiesPersistenceService {

	Properties loadProperties();
	
	void saveProperties(Properties properties);

	void resetProperties(Properties properties);
}
