package eu.domibus.connector.persistence.service.impl;

import java.util.Iterator;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.domibus.connector.persistence.dao.DomibusConnectorPropertiesDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorProperties;
import eu.domibus.connector.persistence.service.DomibusConnectorPropertiesPersistenceService;

@Service
public class DomibusConnectorPropertiesPersistenceServiceImpl implements DomibusConnectorPropertiesPersistenceService {

	DomibusConnectorPropertiesDao propertiesDao;

	@Autowired
	public void setPropertiesDao(DomibusConnectorPropertiesDao propertiesDao) {
		this.propertiesDao = propertiesDao;
	}

	@Override
	public Properties loadProperties() {
		Iterable<PDomibusConnectorProperties> allProperties = propertiesDao.findAll();
		Properties props = new Properties();
		Iterator<PDomibusConnectorProperties> it = allProperties.iterator();
		while(it.hasNext()) {
			PDomibusConnectorProperties property = it.next();
			props.setProperty(property.getPropertyName(), property.getPropertyValue());
		}
		return props;
	}

	@Override
	@Transactional
	public void saveProperties(Properties properties) {
		Set<String> stringPropertyNames = properties.stringPropertyNames();
		for(String propertyName:stringPropertyNames) {
			Optional<PDomibusConnectorProperties> property = propertiesDao.findByPropertyName(propertyName);
			PDomibusConnectorProperties prop = null;
			if(property.isPresent()){
				if(!property.get().getPropertyValue().equals(properties.getProperty(propertyName))) {
					prop = property.get();
					prop.setPropertyValue(properties.getProperty(propertyName));
					propertiesDao.save(prop);
				}
			}else {
				prop = new PDomibusConnectorProperties();
				prop.setPropertyName(propertyName);
				prop.setPropertyValue(properties.getProperty(propertyName));
				propertiesDao.save(prop);
			}
		}
	}
	
	@Override
	@Transactional
	public void resetProperties(Properties properties) {
		Iterable<PDomibusConnectorProperties> allDbProperties = propertiesDao.findAll();
		Iterator<PDomibusConnectorProperties> it = allDbProperties.iterator();
		while(it.hasNext()) {
			PDomibusConnectorProperties dbProperty = it.next();
			String propertyValue = properties.getProperty(dbProperty.getPropertyName());
			if(propertyValue!=null && dbProperty!=null && !dbProperty.getPropertyValue().equals(propertyValue)) {
				dbProperty.setPropertyValue(propertyValue);
				propertiesDao.save(dbProperty);
			}
		}
		
	}

}
