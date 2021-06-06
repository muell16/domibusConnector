package eu.ecodex.signature;

import eu.europa.esig.dss.*;
import eu.europa.esig.dss.client.http.DataLoader;
import eu.europa.esig.dss.client.http.commons.CommonsDataLoader;
import eu.europa.esig.dss.client.http.commons.FileCacheDataLoader;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import eu.europa.esig.dss.token.KSPrivateKeyEntry;
import eu.europa.esig.dss.validation.CertificateVerifier;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import eu.europa.esig.dss.xades.signature.XAdESService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

import java.io.*;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.*;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;


public class EvidenceUtilsXades extends EvidenceUtils {

    public EvidenceUtilsXades(Resource javaKeyStorePath, String javaKeyStorePassword, String alias, String keyPassword) {
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

        java.security.cert.Certificate[] certArray = {cert};

        PrivateKeyEntry privKeyEntry = new PrivateKeyEntry(privKey, certArray);
        //KSPrivateKeyEntry pke = new KSPrivateKeyEntry("", privKeyEntry);

        // Signature creation
        // Create and configure the signature creation service
        XAdESService service = new XAdESService(new CommonCertificateVerifier(true));

        XAdESSignatureParameters sigParam = new XAdESSignatureParameters();
        sigParam.setSignatureLevel(SignatureLevel.XAdES_BASELINE_B);
        sigParam.setSignaturePackaging(SignaturePackaging.ENVELOPED);

        CertificateToken tkn = new CertificateToken(cert);
        sigParam.setSigningCertificate(tkn);

        final List<CertificateToken> x509Certs = new ArrayList<CertificateToken>();
        final List<X509Certificate> certs = keyInfos.getCertChain();
        for (final Certificate certificate : certs) {
            if (certificate instanceof X509Certificate) {
                //ChainCertificate chainCert = new ChainCertificate(new CertificateToken((X509Certificate) certificate));
                x509Certs.add(new CertificateToken((X509Certificate) certificate));
            } else {
                LOG.warn("the alias {} has a certificate chain item that does not represent an X509Certificate; it is ignored");
            }
        }
        sigParam.setCertificateChain(x509Certs);

        sigParam.setDigestAlgorithm(DigestAlgorithm.SHA1);
        sigParam.setEncryptionAlgorithm(sigParam.getEncryptionAlgorithm());

        InMemoryDocument docum = new InMemoryDocument(xmlData);

        ToBeSigned bytesToSign = service.getDataToSign(docum, sigParam);

        final String jceSignatureAlgorithm = sigParam.getSignatureAlgorithm().getJCEId();

        //final byte[] signature = DSSUtils.encrypt(jceSignatureAlgorithm, pke.getPrivateKey(), bytesToSign);

        //InMemoryDocument signedDocument = (InMemoryDocument) service.signDocument(docum, sigParam, signature);
        final Signature signature = Signature.getInstance(jceSignatureAlgorithm);
        signature.initSign(privKeyEntry.getPrivateKey());
        signature.update(bytesToSign.getBytes());
        final byte[] signatureValue = signature.sign();
        final SignatureValue signedData = new SignatureValue(sigParam.getSignatureAlgorithm(), signatureValue);

        DocumentSignatureService<XAdESSignatureParameters> signatureService = new XAdESService(getCompleteCertificateVerifier());
        DSSDocument toBeSigned = new InMemoryDocument(xmlData);
        final DSSDocument signedDocument = signatureService.signDocument(toBeSigned, sigParam, signedData);

        // Verification
        SignedDocumentValidator val = SignedDocumentValidator.fromDocument(signedDocument);
        CommonCertificateVerifier certVeri = new CommonCertificateVerifier();
        val.setCertificateVerifier(certVeri);

        Reports test = val.validateDocument();
        boolean sigValid = test.getDiagnosticData().getSignatureById(test.getDiagnosticData().getFirstSignatureId()).isSignatureIntact();
        boolean sigIntact = test.getDiagnosticData().getSignatureById(test.getDiagnosticData().getFirstSignatureId()).isSignatureIntact();

        LOG.info("Signature applied to document. Validationresult: Signature Valid: "
                + sigValid
                + " / Signature Intact: "
                + sigIntact);

        return getBytes(signedDocument.openStream());
    }

    @Override
    public boolean verifySignature(byte[] xmlData) {
        InMemoryDocument signedDocument = new InMemoryDocument(xmlData);

        SignedDocumentValidator val;

        val = SignedDocumentValidator.fromDocument(signedDocument);
        CommonCertificateVerifier certVeri = new CommonCertificateVerifier(true);
        val.setCertificateVerifier(certVeri);
        Reports test = val.validateDocument();

        boolean sigValid = test.getDiagnosticData().getSignatureById(test.getDiagnosticData().getFirstSignatureId()).isSignatureIntact();
        boolean sigIntact = test.getDiagnosticData().getSignatureById(test.getDiagnosticData().getFirstSignatureId()).isSignatureIntact();

        LOG.info("Signature applied to document. Validationresult: Signature Valid: "
                + sigValid
                + " / Signature Intact: "
                + sigIntact);

        if (sigValid && sigIntact)
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

    protected synchronized static KeyInfos getKeyInfosFromKeyStore(Resource store, String storePass, String alias, String keyPass) throws MalformedURLException {
        LOG.debug("Loading KeyPair from Java KeyStore(" + store + ")");
        KeyStore ks;
        InputStream kfis = null;
        KeyInfos keyInfos = new KeyInfos();


        Key key = null;
        PrivateKey privateKey = null;
        try {
            ks = KeyStore.getInstance("JKS");

            kfis = store.getInputStream();
            ks.load(kfis, (storePass == null) ? null : storePass.toCharArray());

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
                } catch (ClassCastException e) {
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

    protected CertificateVerifier getCompleteCertificateVerifier() {
        CertificateVerifier cv = new CommonCertificateVerifier();
        cv.setDataLoader(getFileCacheDataLoader());
        //cv.setCrlSource(onlineCrlSource());
        //cv.setOcspSource(onlineOcspSource());
        //cv.setTrustedCertSource(getTrustedCertificateSource());
        return cv;
    }

    private DataLoader getFileCacheDataLoader() {
        FileCacheDataLoader cacheDataLoader = new FileCacheDataLoader();
        CommonsDataLoader dataLoader = new CommonsDataLoader();
        //dataLoader.setProxyConfig(getProxyConfig());
        cacheDataLoader.setDataLoader(dataLoader);
        //cacheDataLoader.setFileCacheDirectory(new File("target"));
        cacheDataLoader.setCacheExpirationTime(3600000L);
        return cacheDataLoader;
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