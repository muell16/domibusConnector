package eu.ecodex.signature;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.x509.DigestInfo;

import eu.europa.ec.markt.dss.DSSUtils;
import eu.europa.ec.markt.dss.Digest;
import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.parameter.SignatureParameters;
import eu.europa.ec.markt.dss.signature.InMemoryDocument;
import eu.europa.ec.markt.dss.signature.SignatureLevel;
import eu.europa.ec.markt.dss.signature.SignaturePackaging;
import eu.europa.ec.markt.dss.signature.token.KSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.xades.XAdESService;
import eu.europa.ec.markt.dss.validation102853.SignedDocumentValidator;
import eu.europa.ec.markt.dss.validation102853.CommonCertificateVerifier;
import eu.europa.ec.markt.dss.validation102853.report.Conclusion;
import eu.europa.ec.markt.dss.validation102853.report.DetailedReport;
import eu.europa.ec.markt.dss.validation102853.report.SimpleReport;

public class EvidenceUtilsXades extends EvidenceUtils {

    public EvidenceUtilsXades(String javaKeyStorePath, String javaKeyStorePassword, String alias, String keyPassword) {
	super(javaKeyStorePath, javaKeyStorePassword, alias, keyPassword);
    }

    @Override
    public byte[] signByteArray(byte[] xmlData) {

	byte[] signedData = null;
	try {
	    signedData = createAndVerifySignature(xmlData);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	return signedData;
    }

    private byte[] createAndVerifySignature(byte[] xmlData) throws Exception {
	LOG.info("Xades Signer started");
	//
	KeyInfos keyInfos = getKeyInfosFromKeyStore(javaKeyStorePath, javaKeyStorePassword, alias, keyPassword);

	PrivateKey privKey = keyInfos.getPrivKey();

	java.security.cert.X509Certificate cert = keyInfos.getCert();

	java.security.cert.Certificate[] certArray = { cert };
	
	PrivateKeyEntry privKeyEntry = new PrivateKeyEntry(privKey, certArray);
	KSPrivateKeyEntry pke = new KSPrivateKeyEntry(privKeyEntry);
	
	// Signature creation
	// Create and configure the signature creation service
	XAdESService service = new XAdESService(new CommonCertificateVerifier(true));

	SignatureParameters sigParam = new SignatureParameters();
	sigParam.setSignatureLevel(SignatureLevel.XAdES_BASELINE_B);
	sigParam.setSignaturePackaging(SignaturePackaging.ENVELOPED);
	sigParam.setSigningCertificate(cert);
	sigParam.setCertificateChain(keyInfos.getCertChain());
	sigParam.setDigestAlgorithm(DigestAlgorithm.SHA1);
	sigParam.setEncryptionAlgorithm(pke.getEncryptionAlgorithm());
	
	InMemoryDocument docum = new InMemoryDocument(xmlData);
	
	byte[] bytesToSign = service.getDataToSign(docum, sigParam);
	
	final String jceSignatureAlgorithm = sigParam.getSignatureAlgorithm().getJCEId();
	
	final byte[] signature = DSSUtils.encrypt(jceSignatureAlgorithm, pke.getPrivateKey(), bytesToSign);
	
	InMemoryDocument signedDocument = (InMemoryDocument) service.signDocument(docum, sigParam, signature);

	// Verification
	SignedDocumentValidator val = SignedDocumentValidator.fromDocument(signedDocument);
	CommonCertificateVerifier certVeri = new CommonCertificateVerifier();
	val.setCertificateVerifier(certVeri);

	val.validateDocument();
	
	LOG.info("Signature applied to document. Validationresult: Signature Valid: "
		+ val.getSignatures().get(0).checkIntegrity(signedDocument).isSignatureValid()
		+ " / Signature Intact: " 
		+ val.getSignatures().get(0).checkIntegrity(signedDocument).isSignatureIntact());

	return getBytes(signedDocument.openStream());
    }

    @Override
    public boolean verifySignature(byte[] xmlData) {
	InMemoryDocument signedDocument = new InMemoryDocument(xmlData);
	
	SignedDocumentValidator val;
	
    val = SignedDocumentValidator.fromDocument(signedDocument);
    CommonCertificateVerifier certVeri = new CommonCertificateVerifier(true);
    val.setCertificateVerifier(certVeri);
    val.validateDocument();
    
    boolean sigValid = val.getSignatures().get(0).checkIntegrity(signedDocument).isSignatureValid();
    boolean sigIntact = val.getSignatures().get(0).checkIntegrity(signedDocument).isSignatureIntact();
    
	LOG.info("Signature applied to document. Validationresult: Signature Valid: "
			+ sigValid
			+ " / Signature Intact: " 
			+ sigIntact);

	if(sigValid && sigIntact)
		return true;
	else
		return false;
    }

    private synchronized static byte[] getBytes(InputStream is) throws IOException {

	int len;
	int size = 1024;
	byte[] buf;

	if (is instanceof ByteArrayInputStream) {
	    size = is.available();
	    buf = new byte[size];
	    len = is.read(buf, 0, size);
	} else {
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    buf = new byte[size];
	    while ((len = is.read(buf, 0, size)) != -1)
		bos.write(buf, 0, len);
	    buf = bos.toByteArray();
	}
	return buf;
    }

    protected synchronized static KeyInfos getKeyInfosFromKeyStore(String store, String storePass, String alias, String keyPass) throws MalformedURLException {
	LOG.debug("Loading KeyPair from Java KeyStore(" + store + ")");
	KeyStore ks;
	InputStream kfis = null;
	KeyInfos keyInfos = new KeyInfos();
	
	
	Key key = null;
	PrivateKey privateKey = null;
	try {
	    ks = KeyStore.getInstance("JKS");
	    
	    final URL ksLocation = new URL(store);
	    
	    kfis = ksLocation.openStream();
	    ks.load(kfis, (storePass == null) ? null : storePass.toCharArray() );

	    if (ks.containsAlias(alias)) {
		key = ks.getKey(alias, keyPass.toCharArray());
		if (key instanceof PrivateKey) {
		    X509Certificate cert = (X509Certificate) ks.getCertificate(alias);

		    keyInfos.setCert(cert);
		    privateKey = (PrivateKey) key;
		    keyInfos.setPrivKey(privateKey);
		} else {
		    keyInfos = null;
		}

		try {
		    keyInfos.setCertChain(ks.getCertificateChain(alias));
		}catch(ClassCastException e) {
		    LOG.error(e.getMessage());
		}

	    } else {
		keyInfos = null;
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
	} finally {
	    IOUtils.closeQuietly(kfis);
	}

	return keyInfos;
    }

}

class KeyInfos {
    private PrivateKey privKey;
    private X509Certificate cert;
    private ArrayList<X509Certificate> certChain;

    public PrivateKey getPrivKey() {
	return privKey;
    }

    public void setPrivKey(PrivateKey privKey) {
	this.privKey = privKey;
    }

    public X509Certificate getCert() {
	return cert;
    }

    public void setCert(X509Certificate cert) {
	this.cert = cert;
    }

    public ArrayList<X509Certificate> getCertChain() {
	return certChain;
    }

    public void setCertChain(ArrayList<X509Certificate> certChain) {
	this.certChain = certChain;
    }

    public boolean addToCertificateChain(X509Certificate cert) {
	if (certChain == null) {
	    certChain = new ArrayList<X509Certificate>();
	}
	return certChain.add(cert);
    }

    public boolean setCertChain(Certificate[] certs) throws ClassCastException {
	
	X509Certificate[] x509Certs = KeyInfos.convert(certs, X509Certificate.class);

	for (X509Certificate cert : x509Certs) {
	    if (addToCertificateChain(cert) == false)
		return false;
	}
	return true;
    }

    public static <T> T[] convert(Object[] objects, Class type) throws ClassCastException {
	T[] convertedObjects = (T[]) Array.newInstance(type, objects.length);

	try {
	    for (int i = 0; i < objects.length; i++) {
		convertedObjects[i] = (T) objects[i];
	    }
	} catch (ClassCastException e) {
	    throw new ClassCastException("Exception on convert() : " + e.getMessage());
	}

	return convertedObjects;
    }

}

class DigestUtil {

    private DigestUtil() {
    }

    public static byte[] digestSHA256(final byte[] bytes) {
        return digest(bytes, DigestAlgorithm.SHA256);
    }

    public static byte[] digest(final byte[] bytes, final DigestAlgorithm algorithm) {
        return digest(bytes, algorithm == null ? null : algorithm.getName());
    }

    public static byte[] digest(final byte[] bytes, final String algorithm) {
        try {
            final MessageDigest digest = MessageDigest.getInstance(algorithm);
            final byte[] digestValue = digest.digest(bytes);
            return Base64.encodeBase64(digestValue);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}