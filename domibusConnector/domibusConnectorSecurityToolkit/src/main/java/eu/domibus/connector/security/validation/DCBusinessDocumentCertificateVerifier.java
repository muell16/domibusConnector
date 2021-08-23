package eu.domibus.connector.security.validation;

import eu.domibus.connector.common.annotations.BusinessDomainScoped;
import eu.domibus.connector.common.service.DCKeyStoreService;
import eu.domibus.connector.dss.configuration.TrustListSourceConfigurationProperties;
import eu.domibus.connector.dss.service.DSSTrustedListsManager;
import eu.domibus.connector.security.spring.DocumentValidationConfigurationProperties;
import eu.domibus.connector.tools.logging.LoggingUtils;
import eu.europa.esig.dss.service.crl.OnlineCRLSource;
import eu.europa.esig.dss.service.ocsp.OnlineOCSPSource;
import eu.europa.esig.dss.spi.tsl.TrustedListsCertificateSource;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.spi.x509.CommonTrustedCertificateSource;
import eu.europa.esig.dss.spi.x509.KeyStoreCertificateSource;
import eu.europa.esig.dss.spi.x509.ListCertificateSource;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Certificate Verifier for the
 * BusinessDocument
 *
 */
@BusinessDomainScoped
@Component
public class DCBusinessDocumentCertificateVerifier extends CommonCertificateVerifier {

    static Logger LOGGER = LogManager.getLogger(DomibusConnectorCertificateVerifier.class);

    private final DocumentValidationConfigurationProperties documentValidationConfigurationProperties;
    private final OnlineCRLSource onlineCRLSource;
    private final OnlineOCSPSource onlineOCSPSource;
    private final DCKeyStoreService dcKeyStoreService;
    private final DSSTrustedListsManager trustedListsManager;

    public DCBusinessDocumentCertificateVerifier(DocumentValidationConfigurationProperties documentValidationConfigurationProperties,

                                                 OnlineCRLSource onlineCRLSource, OnlineOCSPSource onlineOCSPSource, DCKeyStoreService dcKeyStoreService, DSSTrustedListsManager trustedListsManager) {
        this.documentValidationConfigurationProperties = documentValidationConfigurationProperties;
        this.onlineCRLSource = onlineCRLSource;
        this.onlineOCSPSource = onlineOCSPSource;
        this.dcKeyStoreService = dcKeyStoreService;
        this.trustedListsManager = trustedListsManager;
        init();
    }
    
    public void init() {
        ListCertificateSource listCertificateSource = new ListCertificateSource();


        if (documentValidationConfigurationProperties.isOcspEnabled()) {
            setOcspSource(onlineOCSPSource);
            LOGGER.info("OCSP checking is enabled");
        } else {
            LOGGER.info("OCSP checking is NOT enabled");
        }
        if (documentValidationConfigurationProperties.isCrlEnabled()) {
            LOGGER.info("CRL checking is enabled");
            setCrlSource(onlineCRLSource);
        } else {
            LOGGER.info("CRL checking is NOT enabled");
        }

        documentValidationConfigurationProperties
                .getTrustedListSources()
                .forEach( name -> {
                    Optional<TrustedListsCertificateSource> certificateSource = trustedListsManager.getCertificateSource(name);
                    if (certificateSource.isPresent()) {
                        listCertificateSource.add(certificateSource.get());
                    } else {
                        LOGGER.warn("There is no TrustedListsCertificateSource with key [{}] configured. Available are [{}]", name, trustedListsManager.getAllSourceNames());
                    }
                });

        LOGGER.debug("Using truststore location [{}], password [{}], type [{}]", documentValidationConfigurationProperties.getTrustStore().getPath(),
                LoggingUtils.logPassword(LOGGER, documentValidationConfigurationProperties.getTrustStore().getPassword()),
                documentValidationConfigurationProperties.getTrustStore().getType());
        KeyStoreCertificateSource keyStoreCertificateSource = null;
        InputStream res = null;
        try {
            res = dcKeyStoreService.loadKeyStoreAsResource(documentValidationConfigurationProperties.getTrustStore()).getInputStream();
        } catch (IOException e) {
            throw new RuntimeException("Unable to load trust store", e);
        }
        keyStoreCertificateSource = new KeyStoreCertificateSource(res, documentValidationConfigurationProperties.getTrustStore().getType(), documentValidationConfigurationProperties.getTrustStore().getPassword());

        CommonTrustedCertificateSource trustedCertSource = new CommonTrustedCertificateSource();
        trustedCertSource.importAsTrusted(keyStoreCertificateSource);
        listCertificateSource.add(trustedCertSource);

        setTrustedCertSources(listCertificateSource);

    }


}
