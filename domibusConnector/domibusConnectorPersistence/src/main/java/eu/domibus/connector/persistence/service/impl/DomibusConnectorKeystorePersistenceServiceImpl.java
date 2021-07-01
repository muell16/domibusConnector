package eu.domibus.connector.persistence.service.impl;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Blob;
import java.util.UUID;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.domibus.connector.domain.model.DomibusConnectorKeystore;
import eu.domibus.connector.persistence.dao.DomibusConnectorKeystoreDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorKeystore;
import eu.domibus.connector.persistence.model.enums.KeystoreType;
import eu.domibus.connector.persistence.service.DomibusConnectorKeystorePersistenceService;

@Service
public class DomibusConnectorKeystorePersistenceServiceImpl implements DomibusConnectorKeystorePersistenceService {

	//Entity manager is required to access LobCreator
	@PersistenceContext
	EntityManager entityManager;
	
	DomibusConnectorKeystoreDao keystoreDao;

	@Override
	@Transactional
	public DomibusConnectorKeystore persistNewKeystore(String uuid, byte[] keystoreBytes, String password,
			String description, DomibusConnectorKeystore.KeystoreType type) {
		PDomibusConnectorKeystore dbKeystore = new PDomibusConnectorKeystore();

		if(StringUtils.isEmpty(uuid)) {
			uuid = String.format("%s@%s", UUID.randomUUID(), "dc.keystore.eu");
		}

		dbKeystore.setUuid(uuid);

		Session hibernateSession = entityManager.unwrap(Session.class);
		Blob blob = Hibernate.getLobCreator(hibernateSession).createBlob(keystoreBytes);
		dbKeystore.setKeystore(blob);

		dbKeystore.setPassword(password);		
		dbKeystore.setDescription(description);
		dbKeystore.setType(KeystoreType.valueOf(type.name()));
		
		dbKeystore = keystoreDao.save(dbKeystore);
		
		DomibusConnectorKeystore keystore = new DomibusConnectorKeystore(
				dbKeystore.getUuid(), 
				keystoreBytes, 
				password, 
				dbKeystore.getUploaded(), 
				dbKeystore.getDescription(), 
				type);
		
		return keystore;
	}

	@Override
	public DomibusConnectorKeystore getKeystoreByUUID(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}


	private static String generatePasswordHashWithSaltOnlyPW(String password, String saltParam)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		int iterations = 1000;
		char[] chars = password.toCharArray();
		byte[] salt = DatatypeConverter.parseHexBinary(saltParam);
		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = skf.generateSecret(spec).getEncoded();
		return toHex(hash);
	}

	private static String getHexSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return toHex(salt);
	}

	private static String toHex(byte[] array) throws NoSuchAlgorithmException {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		} else {
			return hex;
		}
	}
}
