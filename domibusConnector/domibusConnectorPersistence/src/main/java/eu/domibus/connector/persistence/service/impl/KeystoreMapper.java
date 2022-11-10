package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorKeystore;
import eu.domibus.connector.persistence.model.PDomibusConnectorKeystore;
import org.springframework.beans.BeanUtils;

import javax.annotation.Nullable;

public class KeystoreMapper {

	static @Nullable DomibusConnectorKeystore mapKeystoreToDomain(@Nullable PDomibusConnectorKeystore persistenceKeystore) {
        if (persistenceKeystore != null) {
            eu.domibus.connector.domain.model.DomibusConnectorKeystore keystore
                    = new eu.domibus.connector.domain.model.DomibusConnectorKeystore(
                    persistenceKeystore.getUuid(),
                    persistenceKeystore.getKeystore(),
                    persistenceKeystore.getUploaded(),
                    persistenceKeystore.getDescription()
            );
            return keystore;
        }
        return null;
    }


    static @Nullable PDomibusConnectorKeystore mapKeystoreToPersistence(@Nullable DomibusConnectorKeystore keystore) {
        if (keystore != null) {
        	PDomibusConnectorKeystore persistenceKeystore = new PDomibusConnectorKeystore();
            BeanUtils.copyProperties(keystore, persistenceKeystore);
            return persistenceKeystore;
        }
        return null;
    }

}
