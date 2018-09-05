package eu.domibus.connector.security.validation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.annotation.Resource;

import eu.domibus.connector.security.spring.SecurityToolkitConfigurationProperties;
import eu.ecodex.dss.util.ECodexDataLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.domibus.connector.security.proxy.DomibusConnectorProxyConfig;
import eu.europa.esig.dss.client.crl.OnlineCRLSource;
import eu.europa.esig.dss.client.http.commons.CommonsDataLoader;
import eu.europa.esig.dss.client.ocsp.OnlineOCSPSource;
import eu.europa.esig.dss.tsl.TrustedListsCertificateSource;
import eu.europa.esig.dss.tsl.service.TSLRepository;
import eu.europa.esig.dss.tsl.service.TSLValidationJob;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.x509.KeyStoreCertificateSource;


@Component("domibusConnectorCertificateVerifier")
public class DomibusConnectorCertificateVerifier extends CommonCertificateVerifier implements InitializingBean {

//	private static final String OJ_STORE_PASSWORD = "ecodex";
//
//	private static final String OJ_STORE_JKS = "/keys/ojStore.jks";

	static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorCertificateVerifier.class);

	@Autowired
	SecurityToolkitConfigurationProperties securityToolkitConfigurationProperties;

	@Value("${security.lotl.scheme.uri:null}")
	String lotlSchemeUri;

	@Value("${security.lotl.url:null}")
	String lotlUrl;

	@Value("${security.oj.url:null}")
	String ojUrl;

	@Resource(name="domibusConnectorProxyConfig")
	DomibusConnectorProxyConfig proxyPreferenceManager;

	public DomibusConnectorCertificateVerifier() {


	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (!securityToolkitConfigurationProperties.getKeyStore().getPath().exists()) {
			createStore(securityToolkitConfigurationProperties.getKeyStore().getPath());
		}

		TrustedListsCertificateSource certSource = new TrustedListsCertificateSource();
		OnlineCRLSource crlSource = new OnlineCRLSource();
		OnlineOCSPSource ocspSource = new OnlineOCSPSource();
		ECodexDataLoader normalLoader = new ECodexDataLoader();
		normalLoader.setAllowLDAP(false);
		normalLoader.setProxyConfig(proxyPreferenceManager);

		TSLRepository tslRepository = new TSLRepository();
		tslRepository.setTrustedListsCertificateSource(certSource);

		KeyStoreCertificateSource keyStoreCertificateSource = null;
		InputStream res = securityToolkitConfigurationProperties.getKeyStore().getPath().getInputStream();
		keyStoreCertificateSource = new KeyStoreCertificateSource(res, "JKS", securityToolkitConfigurationProperties.getKeyStore().getPassword());

		TSLValidationJob job = new TSLValidationJob();
		job.setDataLoader(normalLoader);
		job.setLotlRootSchemeInfoUri(lotlSchemeUri);
		job.setLotlUrl(lotlUrl);
		job.setOjContentKeyStore(keyStoreCertificateSource);
		job.setOjUrl(ojUrl);
		job.setLotlCode("EU");
		job.setRepository(tslRepository);
		//job.refresh();
		job.initRepository();

		crlSource.setDataLoader(normalLoader);

		CommonsDataLoader ocspDataLoader = new CommonsDataLoader();
		ocspDataLoader.setProxyConfig(proxyPreferenceManager);
		ocspDataLoader.setContentType("application/ocsp-request");

		ocspSource.setDataLoader(ocspDataLoader);

		setTrustedCertSource(certSource);
		setCrlSource(crlSource);
		setOcspSource(ocspSource);

	}

	private void createStore(org.springframework.core.io.Resource resource) {
        String file = resource.getFilename();
	    try (FileOutputStream fos = new FileOutputStream(file)){
            KeyStore ks = KeyStore.getInstance("JKS");
            LOGGER.debug("Creating key store with file [{}] and password [{}]", file, 
                    securityToolkitConfigurationProperties.getKeyStore().getPassword().toCharArray());
            ks.store(fos, securityToolkitConfigurationProperties.getKeyStore().getPassword().toCharArray());            
        } catch (IOException ioe) {
	        LOGGER.error("Cannot create java key store with resource: [{}]", resource);
        } catch (KeyStoreException e) {
            LOGGER.error("Cannot create java key store", e);
        } catch (CertificateException e) {
            LOGGER.error("Cannot create java key store", e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Cannot create java key store", e);
        }
    }

}
