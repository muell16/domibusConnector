package eu.domibus.connector.persistence.service.impl;

import java.sql.Blob;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.domibus.connector.domain.model.DomibusConnectorKeystore;
import eu.domibus.connector.persistence.dao.DomibusConnectorKeystoreDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorKeystore;
import eu.domibus.connector.persistence.service.DomibusConnectorKeystorePersistenceService;

@Service
public class DomibusConnectorKeystorePersistenceServiceImpl implements DomibusConnectorKeystorePersistenceService {

	//Entity manager is required to access LobCreator
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
    DomibusConnectorKeystoreDao keystoreDao;

	@Override
	@Transactional
	public DomibusConnectorKeystore persistNewKeystore(DomibusConnectorKeystore pKeystore) {
		PDomibusConnectorKeystore dbKeystore = new PDomibusConnectorKeystore();
		
		String uuid = pKeystore.getUuid();

		if(StringUtils.isEmpty(uuid)) {
			uuid = String.format("%s@%s", UUID.randomUUID(), "dc.keystore.eu");
		}

		dbKeystore.setUuid(uuid);

		Session hibernateSession = entityManager.unwrap(Session.class);
		Blob blob = Hibernate.getLobCreator(hibernateSession).createBlob(pKeystore.getKeystoreBytes());
		dbKeystore.setKeystore(blob);

		dbKeystore.setPassword(pKeystore.getPasswordPlain());		
		dbKeystore.setDescription(pKeystore.getDescription());
		dbKeystore.setType(pKeystore.getType());
		
		dbKeystore = keystoreDao.save(dbKeystore);
		
		pKeystore.setUuid(dbKeystore.getUuid());
		pKeystore.setUploaded(dbKeystore.getUploaded());
		
		return pKeystore;
	}

	@Override
	public DomibusConnectorKeystore getKeystoreByUUID(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}



}
