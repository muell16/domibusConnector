package eu.ecodex.signature;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.crypto.Cipher;

import org.bouncycastle.asn1.x509.DigestInfo;

import eu.europa.ec.markt.dss.Digest;
import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.signature.InMemoryDocument;
import eu.europa.ec.markt.dss.signature.SignatureFormat;
import eu.europa.ec.markt.dss.signature.SignaturePackaging;
import eu.europa.ec.markt.dss.signature.SignatureParameters;
import eu.europa.ec.markt.dss.signature.token.KSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.xades.XAdESService;
import eu.europa.ec.markt.dss.validation.SignedDocumentValidator;
import eu.europa.ec.markt.dss.validation.TrustedListCertificateVerifier;
import eu.europa.ec.markt.dss.validation.report.Result.ResultStatus;
import eu.europa.ec.markt.dss.validation.report.ValidationReport;

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

	// PrivateKey privKey = (PrivateKey) ks.getKey("myKeyName",
	// "123456".toCharArray());
	PrivateKey privKey = keyInfos.getPrivKey();

	// java.security.cert.X509Certificate cert =
	// (java.security.cert.X509Certificate) ks.getCertificate("myKeyName");
	java.security.cert.X509Certificate cert = keyInfos.getCert();

	// Signature creation
	// Create and configure the signature creation service
	XAdESService service = new XAdESService();

	SignatureParameters sigParam = new SignatureParameters();
	sigParam.setSignatureFormat(SignatureFormat.XAdES_BES);
	sigParam.setSignaturePackaging(SignaturePackaging.ENVELOPED);
	sigParam.setSigningDate(new Date());
	sigParam.setSigningCertificate(cert);

	InMemoryDocument docum = new InMemoryDocument(xmlData);

	// Create the digest of the data to be signed
	Digest dgst = service.digest(docum, sigParam);

	// Sign the digest
	java.security.cert.Certificate[] certArray = { cert };

	PrivateKeyEntry privKeyEntry = new PrivateKeyEntry(privKey, certArray);
	KSPrivateKeyEntry pke = new KSPrivateKeyEntry(privKeyEntry);

	DigestInfo digestInfo = new DigestInfo(DigestAlgorithm.SHA1.getAlgorithmIdentifier(), dgst.getValue());

	Cipher cipher = Cipher.getInstance(pke.getSignatureAlgorithm().getPadding());
	cipher.init(Cipher.ENCRYPT_MODE, pke.getPrivateKey());

	byte[] signature = cipher.doFinal(digestInfo.getDEREncoded());

	InMemoryDocument signedDocument = (InMemoryDocument) service.signDocument(docum, sigParam, signature);

	// Verification
	SignedDocumentValidator val = SignedDocumentValidator.fromDocument(signedDocument);
	TrustedListCertificateVerifier certVeri = new TrustedListCertificateVerifier();
	val.setCertificateVerifier(certVeri);

	ValidationReport valRep = val.validateDocument();
	LOG.info("Signature applied to document. Validationresult: "
		+ valRep.getSignatureInformationList().get(0).getSignatureVerification().getSignatureVerificationResult());

	return getBytes(signedDocument.openStream());
    }

    @Override
    public boolean verifySignature(byte[] xmlData) {
	InMemoryDocument signedDocument = new InMemoryDocument(xmlData);
	String validationResult = "false";
	SignedDocumentValidator val;
	try {
	    val = SignedDocumentValidator.fromDocument(signedDocument);
	    TrustedListCertificateVerifier certVeri = new TrustedListCertificateVerifier();
	    val.setCertificateVerifier(certVeri);
	    ValidationReport valRep = val.validateDocument();
	    validationResult = valRep.getSignatureInformationList().get(0).getSignatureVerification().getSignatureVerificationResult().getStatus().toString();
	    LOG.info("Verify Signature, Result: " + validationResult);
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return isValid(validationResult);
    }
    

    private boolean isValid(String validationResult) {

	if (ResultStatus.valueOf(validationResult) == ResultStatus.VALID)
	    return true;

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

    protected synchronized static KeyInfos getKeyInfosFromKeyStore(String store, String storePass, String alias, String keyPass) {
	LOG.debug("Loading KeyPair from Java KeyStore(" + store + ")");
	KeyStore ks;
	FileInputStream kfis;
	KeyInfos keyInfos = new KeyInfos();

	Key key = null;
	PrivateKey privateKey = null;
	try {
	    ks = KeyStore.getInstance("JKS");
	    kfis = new FileInputStream(store);
	    ks.load(kfis, storePass.toCharArray());
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
	}

	return keyInfos;
    }

}

class KeyInfos {
    private PrivateKey privKey;
    private X509Certificate cert;

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
}
