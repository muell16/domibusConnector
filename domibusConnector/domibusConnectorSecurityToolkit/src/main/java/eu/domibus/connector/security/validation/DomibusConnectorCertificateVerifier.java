package eu.domibus.connector.security.validation;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.EnableMBeanExport;
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
		TrustedListsCertificateSource certSource = new TrustedListsCertificateSource();
		OnlineCRLSource crlSource = new OnlineCRLSource();
		OnlineOCSPSource ocspSource = new OnlineOCSPSource();
		CommonsDataLoader normalLoader = new CommonsDataLoader();
		normalLoader.setProxyConfig(proxyPreferenceManager);
		               
		TSLRepository tslRepository = new TSLRepository();
		tslRepository.setTrustedListsCertificateSource(certSource);
		                    
		KeyStoreCertificateSource keyStoreCertificateSource = null;
		try {
			keyStoreCertificateSource = new KeyStoreCertificateSource(new File("src/test/resources/keys/connector-ojStore-empty.jks"), "JKS", "connector");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		               
		TSLValidationJob job = new TSLValidationJob();
		job.setDataLoader(normalLoader);
		job.setLotlRootSchemeInfoUri(lotlSchemeUri);
		job.setLotlUrl(lotlUrl);
		job.setOjContentKeyStore(keyStoreCertificateSource);
		job.setOjUrl(ojUrl);
		job.setLotlCode("EU");
		job.setRepository(tslRepository);
		job.refresh();
		                    
		crlSource.setDataLoader(normalLoader);
		       
		CommonsDataLoader ocspDataLoader = new CommonsDataLoader();
		ocspDataLoader.setProxyConfig(proxyPreferenceManager);
		ocspDataLoader.setContentType("application/ocsp-request");
		                    
		ocspSource.setDataLoader(ocspDataLoader);
		       
		setTrustedCertSource(certSource);
		setCrlSource(crlSource);
		setOcspSource(ocspSource);
		
	}

}
