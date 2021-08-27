package eu.domibus.connector.dss.service;

import eu.domibus.connector.dss.configuration.CertificateVerifierConfigurationProperties;
import eu.europa.esig.dss.service.crl.OnlineCRLSource;
import eu.europa.esig.dss.service.ocsp.OnlineOCSPSource;
import eu.europa.esig.dss.spi.tsl.TrustedListsCertificateSource;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.spi.x509.CommonTrustedCertificateSource;
import eu.europa.esig.dss.spi.x509.ListCertificateSource;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommonCertificateVerifierFactory {

    static Logger LOGGER = LogManager.getLogger(CommonCertificateVerifierFactory.class);

    private final OnlineCRLSource onlineCRLSource;
    private final OnlineOCSPSource onlineOCSPSource;
    private final CertificateSourceFromKeyStoreCreator certificateSourceFromKeyStoreCreator;
    private final DSSTrustedListsManager trustedListsManager;

    public CommonCertificateVerifierFactory(OnlineCRLSource onlineCRLSource,
                                            OnlineOCSPSource onlineOCSPSource,
                                            CertificateSourceFromKeyStoreCreator certificateSourceFromKeyStoreCreator,
                                            DSSTrustedListsManager trustedListsManager) {

        this.onlineCRLSource = onlineCRLSource;
        this.onlineOCSPSource = onlineOCSPSource;
        this.certificateSourceFromKeyStoreCreator = certificateSourceFromKeyStoreCreator;
        this.trustedListsManager = trustedListsManager;
    }

    public CommonCertificateVerifier createCommonCertificateVerifier(CertificateVerifierConfigurationProperties certificateVerifierConfig) {
        CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
        ListCertificateSource listCertificateSource = new ListCertificateSource();

        if (certificateVerifierConfig.isOcspEnabled()) {
            commonCertificateVerifier.setOcspSource(onlineOCSPSource);
            LOGGER.debug("OCSP checking is enabled");
        } else {
            LOGGER.debug("OCSP checking is NOT enabled");
        }
        if (certificateVerifierConfig.isCrlEnabled()) {
            LOGGER.debug("CRL checking is enabled");
            commonCertificateVerifier.setCrlSource(onlineCRLSource);
        } else {
            LOGGER.debug("CRL checking is NOT enabled");
        }

        String trustedListSourceName = certificateVerifierConfig.getTrustedListSource();
        Optional<TrustedListsCertificateSource> certificateSource = trustedListsManager.getCertificateSource(trustedListSourceName);
        if (certificateSource.isPresent()) {
            listCertificateSource.add(certificateSource.get());
        } else {
            LOGGER.warn("There is no TrustedListsCertificateSource with key [{}] configured. Available are [{}]", trustedListSourceName, trustedListsManager.getAllSourceNames());
        }


        if (certificateVerifierConfig.getIgnoreStore() != null) {
            CertificateSource ignoreCertificateSourceFromStore = certificateSourceFromKeyStoreCreator.createCertificateSourceFromStore(certificateVerifierConfig.getIgnoreStore());
            commonCertificateVerifier.setAdjunctCertSources(ignoreCertificateSourceFromStore);
        }


        if (certificateVerifierConfig.isTrustStoreEnabled()) {
            CertificateSource certificateSourceFromStore = certificateSourceFromKeyStoreCreator.createCertificateSourceFromStore(certificateVerifierConfig.getTrustStore());
            CommonTrustedCertificateSource trustedCertSource = new CommonTrustedCertificateSource();
            trustedCertSource.importAsTrusted(certificateSourceFromStore);
            listCertificateSource.add(trustedCertSource);
            LOGGER.debug("Setting source [{}] as trusted on [{}]", certificateSourceFromStore, commonCertificateVerifier);
        } else {
            LOGGER.debug("TrustStore is not enabled");
        }

        if (listCertificateSource.isEmpty()) {
            LOGGER.warn("No trusted certificate source has been configured");
        }

        commonCertificateVerifier.setTrustedCertSources(listCertificateSource);
        return commonCertificateVerifier;

    }

}
