package eu.domibus.connector.dss.service;

import eu.domibus.connector.common.service.DCKeyStoreService;
import eu.domibus.connector.dss.configuration.CertificateVerifierConfigurationProperties;
import eu.domibus.connector.security.spring.DocumentValidationConfigurationProperties;
import eu.domibus.connector.security.validation.DomibusConnectorCertificateVerifier;
import eu.domibus.connector.tools.logging.LoggingUtils;
import eu.europa.esig.dss.service.crl.OnlineCRLSource;
import eu.europa.esig.dss.service.ocsp.OnlineOCSPSource;
import eu.europa.esig.dss.spi.tsl.TrustedListsCertificateSource;
import eu.europa.esig.dss.spi.x509.CommonTrustedCertificateSource;
import eu.europa.esig.dss.spi.x509.KeyStoreCertificateSource;
import eu.europa.esig.dss.spi.x509.ListCertificateSource;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class CommonCertificateVerifierFactory {

    static Logger LOGGER = LogManager.getLogger(DomibusConnectorCertificateVerifier.class);

    private final OnlineCRLSource onlineCRLSource;
    private final OnlineOCSPSource onlineOCSPSource;
    private final DCKeyStoreService dcKeyStoreService;
    private final DSSTrustedListsManager trustedListsManager;

    public CommonCertificateVerifierFactory(OnlineCRLSource onlineCRLSource,
                                            OnlineOCSPSource onlineOCSPSource,
                                            DCKeyStoreService dcKeyStoreService,
                                            DSSTrustedListsManager trustedListsManager) {

        this.onlineCRLSource = onlineCRLSource;
        this.onlineOCSPSource = onlineOCSPSource;
        this.dcKeyStoreService = dcKeyStoreService;
        this.trustedListsManager = trustedListsManager;
    }

    public CommonCertificateVerifier createCommonCertificateVerifier(CertificateVerifierConfigurationProperties certificateVerifierConfig) {
        CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
        ListCertificateSource listCertificateSource = new ListCertificateSource();


        if (certificateVerifierConfig.isOcspEnabled()) {
            commonCertificateVerifier.setOcspSource(onlineOCSPSource);
            LOGGER.info("OCSP checking is enabled");
        } else {
            LOGGER.info("OCSP checking is NOT enabled");
        }
        if (certificateVerifierConfig.isCrlEnabled()) {
            LOGGER.info("CRL checking is enabled");
            commonCertificateVerifier.setCrlSource(onlineCRLSource);
        } else {
            LOGGER.info("CRL checking is NOT enabled");
        }

        certificateVerifierConfig
                .getTrustedListSources()
                .forEach( name -> {
                    Optional<TrustedListsCertificateSource> certificateSource = trustedListsManager.getCertificateSource(name);
                    if (certificateSource.isPresent()) {
                        listCertificateSource.add(certificateSource.get());
                    } else {
                        LOGGER.warn("There is no TrustedListsCertificateSource with key [{}] configured. Available are [{}]", name, trustedListsManager.getAllSourceNames());
                    }
                });

        if (certificateVerifierConfig.getIgnoreStore() != null) {

        }


        if (certificateVerifierConfig.isTrustStoreEnabled()) {
            LOGGER.debug("Using truststore location [{}], password [{}], type [{}]", certificateVerifierConfig.getTrustStore().getPath(),
                    LoggingUtils.logPassword(LOGGER, certificateVerifierConfig.getTrustStore().getPassword()),
                    certificateVerifierConfig.getTrustStore().getType());
            KeyStoreCertificateSource keyStoreCertificateSource = null;
            InputStream res = null;
            try {
                res = dcKeyStoreService.loadKeyStoreAsResource(certificateVerifierConfig.getTrustStore()).getInputStream();
            } catch (IOException e) {
                throw new RuntimeException("Unable to load trust store", e);
            }
            keyStoreCertificateSource = new KeyStoreCertificateSource(res, certificateVerifierConfig.getTrustStore().getType(), certificateVerifierConfig.getTrustStore().getPassword());

            CommonTrustedCertificateSource trustedCertSource = new CommonTrustedCertificateSource();
            trustedCertSource.importAsTrusted(keyStoreCertificateSource);
            listCertificateSource.add(trustedCertSource);
        } else {
            LOGGER.info("TrustStore is not enabled");
        }

        if (listCertificateSource.isEmpty()) {
            LOGGER.warn("No trusted certificate source has been configured within [{}]", DocumentValidationConfigurationProperties.CONFIG_PREFIX);
        }

        commonCertificateVerifier.setTrustedCertSources(listCertificateSource);
        return commonCertificateVerifier;

    }

}
