package eu.domibus.connector.security.validation;

import eu.domibus.connector.common.annotations.BusinessDomainScoped;
import eu.domibus.connector.common.service.DCKeyStoreService;
import eu.domibus.connector.dss.configuration.TrustListSourceConfigurationProperties;
import eu.domibus.connector.security.spring.DocumentValidationConfigurationProperties;
import eu.domibus.connector.tools.logging.LoggingUtils;
import eu.europa.esig.dss.service.crl.OnlineCRLSource;
import eu.europa.esig.dss.service.ocsp.OnlineOCSPSource;
import eu.europa.esig.dss.spi.x509.CommonTrustedCertificateSource;
import eu.europa.esig.dss.spi.x509.KeyStoreCertificateSource;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

    public DCBusinessDocumentCertificateVerifier(DocumentValidationConfigurationProperties documentValidationConfigurationProperties,

                                                 OnlineCRLSource onlineCRLSource, OnlineOCSPSource onlineOCSPSource, DCKeyStoreService dcKeyStoreService) {
        this.documentValidationConfigurationProperties = documentValidationConfigurationProperties;
        this.onlineCRLSource = onlineCRLSource;
        this.onlineOCSPSource = onlineOCSPSource;
        this.dcKeyStoreService = dcKeyStoreService;
        init();
    }

    //	@Override
    public void init() {

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

        if (documentValidationConfigurationProperties.getTrustedLists() != null) {
            configureTrustedListsVerification(documentValidationConfigurationProperties.getTrustedLists());
        }

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

//        setTrustedCertSource(trustedCertSource);
        setTrustedCertSources(trustedCertSource);


//		TSLValidationJob job = new TSLValidationJob();
//		job.setDataLoader(normalLoader);
//		job.setLotlRootSchemeInfoUri(lotlSchemeUri);
//		job.setLotlUrl(lotlUrl);
////		job.setOjContentKeyStore(keyStoreCertificateSource);
//		job.setOjUrl(ojUrl);
//		job.setLotlCode("EU");
//		job.setRepository(tslRepository);
////		job.refresh();
//		job.initRepository();

//        crlSource.setDataLoader(normalLoader);
//
//        CommonsDataLoader ocspDataLoader = new CommonsDataLoader();
//        ocspDataLoader.setProxyConfig(proxyPreferenceManager);
//        ocspDataLoader.setContentType("application/ocsp-request");
//
//
//        ocspSource.setDataLoader(ocspDataLoader);
//
//        setTrustedCertSource(trustedCertSource);
//        setCrlSource(crlSource);
//        setOcspSource(ocspSource);

    }

    private void configureTrustedListsVerification(TrustListSourceConfigurationProperties trustedLists) {
        List<TrustListSourceConfigurationProperties.LotlSource> lotlSources = trustedLists.getLotlSources();
        lotlSources.stream().forEach(this::configureLotlSource);
    }

    private void configureLotlSource(TrustListSourceConfigurationProperties.LotlSource lotlSource) {
//        LOTLSource lotlSource = new LOTLSource();
//        TSLValidationJob job = new TSLValidationJob();


    }

//	private void createStore(org.springframework.core.io.Resource resource) {
//        String file = resource.getFilename();
//	    try (FileOutputStream fos = new FileOutputStream(file)){
//            KeyStore ks = KeyStore.getInstance("JKS");
//            LOGGER.debug("Creating key store with file [{}] and password [{}]", file,
//                    securityToolkitConfigurationProperties.getKeyStore().getPassword().toCharArray());
//            ks.store(fos, securityToolkitConfigurationProperties.getKeyStore().getPassword().toCharArray());
//        } catch (IOException ioe) {
//	        LOGGER.error("Cannot create java key store with resource: [{}]", resource);
//        } catch (KeyStoreException e) {
//            LOGGER.error("Cannot create java key store", e);
//        } catch (CertificateException e) {
//            LOGGER.error("Cannot create java key store", e);
//        } catch (NoSuchAlgorithmException e) {
//            LOGGER.error("Cannot create java key store", e);
//        }
//    }

}
