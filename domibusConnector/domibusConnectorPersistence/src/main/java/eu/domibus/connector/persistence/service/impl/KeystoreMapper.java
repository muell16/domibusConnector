package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorKeystore;
import eu.domibus.connector.persistence.model.DC5ConfigItem;
import org.springframework.beans.BeanUtils;

import javax.annotation.Nullable;

public class KeystoreMapper {

	static @Nullable DomibusConnectorKeystore mapKeystoreToDomain(@Nullable DC5ConfigItem persistenceKeystore) {
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


    static @Nullable DC5ConfigItem mapKeystoreToPersistence(@Nullable DomibusConnectorKeystore keystore) {
        if (keystore != null) {
        	DC5ConfigItem persistenceKeystore = new DC5ConfigItem();
            BeanUtils.copyProperties(keystore, persistenceKeystore);
            return persistenceKeystore;
        }
        return null;
    }

}
