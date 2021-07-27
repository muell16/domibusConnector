package eu.domibus.connector.security.validation;

import java.io.IOException;
import java.io.InputStream;

import eu.domibus.connector.common.annotations.BusinessDomainScoped;
import eu.domibus.connector.common.service.DCKeyStoreService;
import eu.domibus.connector.security.proxy.DomibusConnectorProxyConfig;
import eu.domibus.connector.security.spring.SecurityToolkitConfigurationProperties;
import eu.domibus.connector.tools.logging.LoggingUtils;
import eu.ecodex.dss.util.ECodexDataLoader;

import eu.europa.esig.dss.x509.CommonTrustedCertificateSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import eu.europa.esig.dss.client.crl.OnlineCRLSource;
import eu.europa.esig.dss.client.http.commons.CommonsDataLoader;
import eu.europa.esig.dss.client.ocsp.OnlineOCSPSource;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.x509.KeyStoreCertificateSource;


@BusinessDomainScoped
@Component
public class DomibusConnectorCertificateVerifier extends CommonCertificateVerifier {

	static Logger LOGGER = LogManager.getLogger(DomibusConnectorCertificateVerifier.class);

	private final SecurityToolkitConfigurationProperties securityToolkitConfigurationProperties;
	private final DomibusConnectorProxyConfig proxyPreferenceManager;
	private final DCKeyStoreService dcKeyStoreService;

	public DomibusConnectorCertificateVerifier(SecurityToolkitConfigurationProperties securityToolkitConfigurationProperties,
											   DomibusConnectorProxyConfig proxyPreferenceManager,
											   DCKeyStoreService dcKeyStoreService) {
		this.securityToolkitConfigurationProperties = securityToolkitConfigurationProperties;
		this.proxyPreferenceManager = proxyPreferenceManager;
		this.dcKeyStoreService = dcKeyStoreService;
		init();
	}

//	@Override
	public void init() {
//		if (!securityToolkitConfigurationProperties.getKeyStore().getPath().exists()) {
//			createStore(securityToolkitConfigurationProperties.getKeyStore().getPath());
//		}

//		TrustedListsCertificateSource certSource = new TrustedListsCertificateSource();
		OnlineCRLSource crlSource = new OnlineCRLSource();
		OnlineOCSPSource ocspSource = new OnlineOCSPSource();
		ECodexDataLoader normalLoader = new ECodexDataLoader();
		normalLoader.setAllowLDAP(false);
		normalLoader.setProxyConfig(proxyPreferenceManager);

		//completely ignore TSL
//		TSLRepository tslRepository = new TSLRepository();
//		tslRepository.setTrustedListsCertificateSource(certSource);

		LOGGER.debug("Using truststore location [{}], password [{}], type [{}]", securityToolkitConfigurationProperties.getTrustStore().getPath(),
				LoggingUtils.logPassword(LOGGER, securityToolkitConfigurationProperties.getTrustStore().getPassword()),
				securityToolkitConfigurationProperties.getTrustStore().getType());
		KeyStoreCertificateSource keyStoreCertificateSource = null;
		InputStream res = null;
		try {
			res = dcKeyStoreService.loadKeyStoreAsResource(securityToolkitConfigurationProperties.getTrustStore()).getInputStream();
		} catch (IOException e) {
			throw new RuntimeException("Unable to load trust store", e);
		}
		keyStoreCertificateSource = new KeyStoreCertificateSource(res, securityToolkitConfigurationProperties.getTrustStore().getType(), securityToolkitConfigurationProperties.getTrustStore().getPassword());



		CommonTrustedCertificateSource trustedCertSource = new CommonTrustedCertificateSource();
		trustedCertSource.importAsTrusted(keyStoreCertificateSource);

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

		crlSource.setDataLoader(normalLoader);

		CommonsDataLoader ocspDataLoader = new CommonsDataLoader();
		ocspDataLoader.setProxyConfig(proxyPreferenceManager);
		ocspDataLoader.setContentType("application/ocsp-request");

		ocspSource.setDataLoader(ocspDataLoader);

		setTrustedCertSource(trustedCertSource);
		setCrlSource(crlSource);
		setOcspSource(ocspSource);

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
