package wp4.testenvironment.configurations;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import eu.ecodex.dss.model.CertificateStoreInfo;
import eu.ecodex.dss.model.SignatureParameters;
import eu.ecodex.dss.util.SignatureParametersFactory;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.EncryptionAlgorithm;
import eu.europa.esig.dss.model.x509.CertificateToken;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * This class can be used to receive valid configurations of Connector Certificates.
 * 
 * The respective test case is SUB_CONF_01
 */
public class ValidConfig_SignatureParameters {
	
	private static final Resource JKS_KEYSTORE_PATH = new ClassPathResource("/keystores/signature_store.jks");
	private static final String JKS_KEYSTORE_PASSWORD = "teststore";
	private static final String JKS_KEY_NAME = "sign_key";
	private static final String JKS_KEY_PASSWORD = "teststore";
	
	private static final Resource PKCS12_KEYSTORE_PATH = new ClassPathResource("keystores/signature_store.p12");
	private static final String PKCS12_KEYSTORE_PASSWORD = "teststore";
	private static final String PKCS12_KEY_NAME = "sign_key";
	private static final String PKCS12_KEY_PASSWORD = "teststore";

	/**
	 * Returns a "SignatureParameters" object containing
	 * - An accessible private key
	 * - The respective certificate
	 * - The respective certificate chain
	 * - The digest algorithm being set to SHA1
	 * - The encryption algorithm being set to RSA
	 * 
	 * The information has been set using standard java classes
	 * 
	 * The respective test case is SUB_CONF_01 - Variant 1
	 */
	public static SignatureParameters getJKSConfiguration() {
		
		InputStream kfis = null;
		
		try{
			SignatureParameters sigParam = new SignatureParameters();
			
			KeyStore ks = KeyStore.getInstance("JKS");
			kfis = JKS_KEYSTORE_PATH.getInputStream();
			ks.load(kfis, JKS_KEYSTORE_PASSWORD.toCharArray());
			
			PrivateKey privKey = (PrivateKey) ks.getKey(JKS_KEY_NAME, JKS_KEYSTORE_PASSWORD.toCharArray());
			sigParam.setPrivateKey(privKey);
			
			X509Certificate cert = (X509Certificate) ks.getCertificate(JKS_KEY_NAME);
			CertificateToken tkn = new CertificateToken(cert);
			
			sigParam.setCertificate(tkn);
	
			Certificate[] certs = ks.getCertificateChain(JKS_KEY_NAME);
			
			final List<CertificateToken> x509Certs = new ArrayList<CertificateToken>();
			
			for (final Certificate certificate : certs) {
				if (certificate instanceof X509Certificate) {
					CertificateToken chainCert = new CertificateToken((X509Certificate) certificate);
			    	x509Certs.add(chainCert);
				}
			}
			
			sigParam.setCertificateChain(x509Certs);
	
			sigParam.setDigestAlgorithm(DigestAlgorithm.SHA1.getName());
			sigParam.setSignatureAlgorithm(EncryptionAlgorithm.RSA.getName());
			
			return sigParam;
			
		} catch(Exception e) {
			
			System.err.println("Exception within the configuration of the signature parameters - Variant 1:");
			e.printStackTrace();
			return null;
			
		} finally {
			IOUtils.closeQuietly(kfis);
		}
	}

	/**
	 * Returns a "SignatureParameters" object containing
	 * - An accessible private key
	 * - The respective certificate
	 * - The respective certificate chain
	 * - The digest algorithm being set to SHA1
	 * - The encryption algorithm being set to RSA
	 * 
	 * The information has been set using standard java classes
	 * 
	 * The respective test case is SUB_CONF_01 - Variant 2
	 */
	public static SignatureParameters getPKCS12Configuration() {
		InputStream kfis = null;
		
		try{
			SignatureParameters sigParam = new SignatureParameters();
			
			KeyStore ks = KeyStore.getInstance("PKCS12");
			kfis = PKCS12_KEYSTORE_PATH.getInputStream();
			ks.load(kfis, PKCS12_KEYSTORE_PASSWORD.toCharArray());
			
			PrivateKey privKey = (PrivateKey) ks.getKey(PKCS12_KEY_NAME, PKCS12_KEYSTORE_PASSWORD.toCharArray());
			sigParam.setPrivateKey(privKey);
			
			X509Certificate cert = (X509Certificate) ks.getCertificate(PKCS12_KEY_NAME);
			CertificateToken tkn = new CertificateToken(cert);
			
			sigParam.setCertificate(tkn);
	
			Certificate[] certs = ks.getCertificateChain(PKCS12_KEY_NAME);
			
			final List<CertificateToken> x509Certs = new ArrayList<CertificateToken>();
			
			for (final Certificate certificate : certs) {
				if (certificate instanceof X509Certificate) {
					CertificateToken chainCert = new CertificateToken((X509Certificate) certificate);
			    	x509Certs.add(chainCert);
				}
			}
			
			sigParam.setCertificateChain(x509Certs);
	
			sigParam.setDigestAlgorithm(DigestAlgorithm.SHA1.getName());
			sigParam.setSignatureAlgorithm(EncryptionAlgorithm.RSA.getName());
			
			return sigParam;
			
		} catch(Exception e) {
			
			System.err.println("Exception within the configuration of the signature parameters - Variant 1:");
			e.printStackTrace();
			return null;
			
		} finally {
			IOUtils.closeQuietly(kfis);
		}
	}
	
	/**
	 * Returns a "SignatureParameters" object containing created by a "SignatureParameterFactory" object
	 * - An accessible private key
	 * - The respective certificate
	 * - The respective certificate chain
	 * - The digest algorithm being set to SHA1
	 * - The encryption algorithm being set to RSA
	 * 
	 * The information has been set using standard java classes
	 * 
	 * The respective test case is SUB_CONF_01 - Variant 3
	 * @throws Exception 
	 */
	public static SignatureParameters getJKSConfig_By_SigParamFactory() throws Exception {
		
		SignatureParameters sigParam = new SignatureParameters();
		
		CertificateStoreInfo certStore = new CertificateStoreInfo();
		certStore.setLocation(JKS_KEYSTORE_PATH);
		certStore.setPassword(JKS_KEYSTORE_PASSWORD);
		
		sigParam = SignatureParametersFactory.create(certStore, JKS_KEY_NAME, JKS_KEY_PASSWORD,EncryptionAlgorithm.RSA, DigestAlgorithm.SHA1);
		
		return sigParam;
	}
	
	/**
	 * Returns a "SignatureParameters" object containing created by a "SignatureParameterFactory" object
	 * - An accessible private key
	 * - The respective certificate
	 * - The respective certificate chain
	 * - The digest algorithm being set to SHA1
	 * - The encryption algorithm being set to RSA
	 * 
	 * The information has been set using standard java classes
	 * 
	 * The respective test case is SUB_CONF_01 - Variant 4
	 * @throws Exception 
	 */
	public static SignatureParameters getPKCS12Config_By_SigParamFactory() throws Exception {
		
		SignatureParameters sigParam = new SignatureParameters();
		
		CertificateStoreInfo certStore = new CertificateStoreInfo();
		certStore.setLocation(PKCS12_KEYSTORE_PATH);
		certStore.setPassword(PKCS12_KEYSTORE_PASSWORD);
		
		sigParam = SignatureParametersFactory.create(certStore, PKCS12_KEY_NAME, PKCS12_KEY_PASSWORD,EncryptionAlgorithm.RSA, DigestAlgorithm.SHA1);
		
		return sigParam;
	}
	
	public static SignatureParameters getJKSConfiguration_SHA256() {
		
		InputStream kfis = null;
		
		try{
			SignatureParameters sigParam = new SignatureParameters();
			
			KeyStore ks = KeyStore.getInstance("JKS");
			kfis = JKS_KEYSTORE_PATH.getInputStream();
			ks.load(kfis, JKS_KEYSTORE_PASSWORD.toCharArray());
			
			PrivateKey privKey = (PrivateKey) ks.getKey(JKS_KEY_NAME, JKS_KEYSTORE_PASSWORD.toCharArray());
			sigParam.setPrivateKey(privKey);
			
			X509Certificate cert = (X509Certificate) ks.getCertificate(JKS_KEY_NAME);
			CertificateToken tkn = new CertificateToken(cert);
			
			sigParam.setCertificate(tkn);
	
			Certificate[] certs = ks.getCertificateChain(JKS_KEY_NAME);
			
			final List<CertificateToken> x509Certs = new ArrayList<CertificateToken>();
			
			for (final Certificate certificate : certs) {
				if (certificate instanceof X509Certificate) {
					CertificateToken chainCert = new CertificateToken((X509Certificate) certificate);
			    	x509Certs.add(chainCert);
				}
			}
			
			sigParam.setCertificateChain(x509Certs);
	
			sigParam.setDigestAlgorithm(DigestAlgorithm.SHA256.getName());
			sigParam.setSignatureAlgorithm(EncryptionAlgorithm.RSA.getName());
			
			return sigParam;
			
		} catch(Exception e) {
			
			System.err.println("Exception within the configuration of the signature parameters - Variant 1:");
			e.printStackTrace();
			return null;
			
		} finally {
			IOUtils.closeQuietly(kfis);
		}
	}
	
	public static SignatureParameters getJKSConfiguration_SHA512() {
		
		InputStream kfis = null;
		
		try{
			SignatureParameters sigParam = new SignatureParameters();
			
			KeyStore ks = KeyStore.getInstance("JKS");
			kfis = JKS_KEYSTORE_PATH.getInputStream();
			ks.load(kfis, JKS_KEYSTORE_PASSWORD.toCharArray());
			
			PrivateKey privKey = (PrivateKey) ks.getKey(JKS_KEY_NAME, JKS_KEYSTORE_PASSWORD.toCharArray());
			sigParam.setPrivateKey(privKey);
			
			X509Certificate cert = (X509Certificate) ks.getCertificate(JKS_KEY_NAME);
			CertificateToken tkn = new CertificateToken(cert);
			
			sigParam.setCertificate(tkn);
	
			Certificate[] certs = ks.getCertificateChain(JKS_KEY_NAME);
			
			final List<CertificateToken> x509Certs = new ArrayList<CertificateToken>();
			
			for (final Certificate certificate : certs) {
				if (certificate instanceof X509Certificate) {
					CertificateToken chainCert = new CertificateToken((X509Certificate) certificate);
			    	x509Certs.add(chainCert);
				}
			}
			
			sigParam.setCertificateChain(x509Certs);
	
			sigParam.setDigestAlgorithm(DigestAlgorithm.SHA512.getName());
			sigParam.setSignatureAlgorithm(EncryptionAlgorithm.RSA.getName());
			
			return sigParam;
			
		} catch(Exception e) {
			
			System.err.println("Exception within the configuration of the signature parameters - Variant 1:");
			e.printStackTrace();
			return null;
			
		} finally {
			IOUtils.closeQuietly(kfis);
		}
	}
}
