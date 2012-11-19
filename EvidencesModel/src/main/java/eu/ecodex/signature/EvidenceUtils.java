package eu.ecodex.signature;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.log4j.Logger;

public abstract class EvidenceUtils {
	
	protected static Logger LOG = Logger.getLogger(EvidenceUtilsImpl.class);
	
	protected String javaKeyStorePath, javaKeyStorePassword, alias, keyPassword;

	public EvidenceUtils(String javaKeyStorePath,
							 String javaKeyStorePassword, 
							 String alias, 
							 String keyPassword) {
		this.javaKeyStorePath = javaKeyStorePath;
		this.javaKeyStorePassword = javaKeyStorePassword;
		this.alias = alias;
		this.keyPassword = keyPassword;
	}
	
	
	public abstract byte[] signByteArray(byte[] xmlData);
	public abstract boolean verifySignature(byte[] xmlData);
	
	
	protected synchronized static KeyPair getKeyPairFromKeyStore(String store,
			String storePass, String alias, String keyPass) {
		LOG.debug("Loading KeyPair from Java KeyStore(" + store + ")" );
		KeyStore ks;
		FileInputStream kfis;
		KeyPair keyPair = null;

		Key key = null;
		PublicKey publicKey = null;
		PrivateKey privateKey = null;
		try {
			ks = KeyStore.getInstance("JKS");
			kfis = new FileInputStream(store);
			ks.load(kfis, storePass.toCharArray());
			if (ks.containsAlias(alias)) {
				key = ks.getKey(alias, keyPass.toCharArray());
				if (key instanceof PrivateKey) {
					X509Certificate cert = (X509Certificate) ks
							.getCertificate(alias);
					publicKey = cert.getPublicKey();
					privateKey = (PrivateKey) key;
					keyPair = new KeyPair(publicKey, privateKey);
				} else {
					keyPair = null;
				}
			} else {
				keyPair = null;
			}
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return keyPair;
	}
	
}
