package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.LargeFileReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.largefiles.provider.LargeFilePersistenceProvider;
import eu.domibus.connector.persistence.service.LargeFilePersistenceService;
import eu.domibus.connector.persistence.service.exceptions.LargeFileDeletionException;
import eu.domibus.connector.persistence.spring.DomibusConnectorPersistenceProperties;
import liquibase.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LargeFilePersistenceServiceImpl implements LargeFilePersistenceService {

    private static final Logger LOGGER = LogManager.getLogger(LargeFilePersistenceServiceImpl.class);

    @Autowired
    DomibusConnectorPersistenceProperties domibusConnectorPersistenceProperties;

    LargeFilePersistenceProvider defaultLargeFilePersistenceProvider;

    @Autowired(required = false)
    List<LargeFilePersistenceProvider> availableLargeFilePersistenceProvider = new ArrayList<>();

    @PostConstruct
    public void init() {
        final Class<? extends LargeFilePersistenceProvider> defaultLargeFileProviderClass = domibusConnectorPersistenceProperties.getDefaultLargeFileProviderClass();
        LargeFilePersistenceProvider p = availableLargeFilePersistenceProvider
                .stream()
                .filter(largeFilePersistenceProvider -> defaultLargeFileProviderClass.isAssignableFrom(largeFilePersistenceProvider.getClass()))
                .findFirst()
                .orElse(null);
        if (p == null) {
            throw new RuntimeException(String.format("No LargeFilePersistenceProvider provider with Class [%s] is registered as spring bean!\n" +
                    "The following LargeFilePersistenceProvider are available:\n[%s]",
                    defaultLargeFileProviderClass,
                    getAvailableStorageProviderAsStringWithNewLine()
            ));
        } else {
            defaultLargeFilePersistenceProvider = p;
            LOGGER.info("Setting LargeFilePersistenceProvider [{}] as default provider", defaultLargeFilePersistenceProvider);
        }
    }


    @Override
    public LargeFileReference getReadableDataSource(LargeFileReference bigDataReference) {
        return getProviderByLargeFileReference(bigDataReference)
                .getReadableDataSource(bigDataReference);
    }

    @Override
    public LargeFileReference createDomibusConnectorBigDataReference(InputStream input, String connectorMessageId, String documentName, String documentContentType) {
        return defaultLargeFilePersistenceProvider.createDomibusConnectorBigDataReference(input, connectorMessageId, documentName, documentContentType);
    }

    @Override
    public LargeFileReference createDomibusConnectorBigDataReference(String connectorMessageId, String documentName, String documentContentType) {
        return defaultLargeFilePersistenceProvider.createDomibusConnectorBigDataReference(connectorMessageId, documentName, documentContentType);
    }

    @Override
    public void deleteDomibusConnectorBigDataReference(LargeFileReference bigDataReference) throws LargeFileDeletionException {
        getProviderByLargeFileReference(bigDataReference)
                .deleteDomibusConnectorBigDataReference(bigDataReference);
    }

    @Override
    public Map<DomibusConnectorMessage.DomibusConnectorMessageId, List<LargeFileReference>> getAllAvailableReferences() {
        Map<DomibusConnectorMessage.DomibusConnectorMessageId, List<LargeFileReference>> collect = availableLargeFilePersistenceProvider
                .stream()
                .map(provider -> provider.getAllAvailableReferences())
                .flatMap(refmap -> refmap.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return collect;
    }

    @Override
    public boolean isAvailable(LargeFileReference toCopy) {
        return getProviderByName(toCopy.getStorageProviderName()).isPresent();
    }

    @Override
    public LargeFilePersistenceProvider getDefaultProvider() {
        return defaultLargeFilePersistenceProvider;
    }

    private LargeFilePersistenceProvider getProviderByLargeFileReference(LargeFileReference bigDataReference) {
        String storageProviderName = bigDataReference.getStorageProviderName();
        LOGGER.debug("Looking up Storage provider for largeFileReference [{}] with name [{}]",
                bigDataReference.getStorageIdReference(),
                storageProviderName);
        LargeFilePersistenceProvider largeFilePersistenceProvider = this.getProviderByName(storageProviderName)
                .orElseThrow(() -> new RuntimeException(String.format("No  LargeFilePersistenceProvider with name %s is available.\n" +
                                "The following LargeFilePersistenceProvider are available:\n[%s]",
                        storageProviderName,
                        getAvailableStorageProviderAsStringWithNewLine()
                )));
        return largeFilePersistenceProvider;
    }

    private Optional<LargeFilePersistenceProvider> getProviderByName(String providerName) {
        if (StringUtils.isEmpty(providerName)) {
            throw new IllegalArgumentException("largeFilePersistenceProviderName is not allowed to be empty!");
        }
        return availableLargeFilePersistenceProvider
                .stream()
                .filter(largeFilePersistenceProvider -> providerName.equals(largeFilePersistenceProvider.getProviderName()))
                .findFirst();
    }

    private String getAvailableStorageProviderAsStringWithNewLine() {
        return availableLargeFilePersistenceProvider
                .stream()
                .map(l -> l.getProviderName()  + ":" + l.getClass())
                .collect(Collectors.joining("\n"));
    }

}
