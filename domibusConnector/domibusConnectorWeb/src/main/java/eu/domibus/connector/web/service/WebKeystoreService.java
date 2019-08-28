package eu.domibus.connector.web.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.security.cert.X509Certificate;

import org.springframework.stereotype.Service;

import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties.CannotLoadKeyStoreException;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties.ValidationException;

@Service("webKeystoreService")
public class WebKeystoreService {

	public WebKeystoreService() {
		// TODO Auto-generated constructor stub
	}
	
	public class CertificateInfo{
		private String alias;
		private String subject;
		private String issuer;
		private Date notBefore;
		private Date notAfter;
		private String algorithm;
		private String type;
		
		/**
		 * @param alias
		 * @param subject
		 */
		public CertificateInfo(String alias, String subject, String issuer, Date notBefore, Date notAfter, String algorithm, String type) {
			super();
			this.alias = alias;
			this.setSubject(subject);
			this.setIssuer(issuer);
			this.notBefore = notBefore;
			this.notAfter = notAfter;
			this.setAlgorithm(algorithm);
			this.setType(type);
		}
		
		public String getAlias() {
			return alias;
		}
		public void setAlias(String alias) {
			this.alias = alias;
		}
		
		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getIssuer() {
			return issuer;
		}

		public void setIssuer(String issuer) {
			this.issuer = issuer;
		}

		public Date getNotBefore() {
			return notBefore;
		}

		public void setNotBefore(Date notBefore) {
			this.notBefore = notBefore;
		}

		public Date getNotAfter() {
			return notAfter;
		}

		public void setNotAfter(Date notAfter) {
			this.notAfter = notAfter;
		}

		public String getAlgorithm() {
			return algorithm;
		}

		public void setAlgorithm(String algorithm) {
			this.algorithm = algorithm;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	
		
		
	}

	public KeyStore loadKeyStore(String path, String password) {
        if (password == null) {
            password = "";
        }
        if(path.startsWith("file:")) {
        	path = path.substring(5);
        }
        char[] pwdArray = password.toCharArray();
        File store = new File(path);
        try (FileInputStream fis = new FileInputStream(store)) {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(fis, pwdArray);
            return keyStore;
        } catch (IOException | KeyStoreException | CertificateException | NoSuchAlgorithmException e) {
            throw new CannotLoadKeyStoreException(String.format("Cannot load key store from path %s", path), e);
        }
    }
	
	public List<CertificateInfo> loadStoreCertificatesInformation(String path, String password) {
		KeyStore keyStore = loadKeyStore(path, password);
		List<CertificateInfo> certsInfo = new ArrayList<CertificateInfo>();
		try {
			Enumeration<String> aliases = keyStore.aliases();
			while(aliases.hasMoreElements()) {
				String alias = aliases.nextElement();
				Certificate certificate = keyStore.getCertificate(alias);
				String subject = null;
				String issuer = null;
				Date notBefore = null;
				Date notAfter = null;
				String algorithm = null;
				String type = "undefined";
				if(certificate instanceof X509Certificate) {
                    X509Certificate x = (X509Certificate ) certificate;
                    subject = x.getSubjectX500Principal().getName();
                    issuer = x.getIssuerX500Principal().getName();
                    notBefore = x.getNotBefore();
                    notAfter = x.getNotAfter();
                    algorithm = x.getSigAlgName();
                    if(keyStore.isKeyEntry(alias) && keyStore.isCertificateEntry(alias)) {
                    	type = "keypair";
                    }else if(keyStore.isCertificateEntry(alias)) {
                    	type = "public";
                    }else if(keyStore.isKeyEntry(alias)) {
                    	type = "private";
                    }
                    
                }
				certsInfo.add(new CertificateInfo(alias, subject, issuer, notBefore, notAfter, algorithm, type));
			}
			return certsInfo;
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	
}
