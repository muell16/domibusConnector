package eu.ecodex.signature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.bouncycastle.asn1.x509.DigestInfo;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


import eu.europa.ec.markt.dss.Digest;
import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.signature.Document;
import eu.europa.ec.markt.dss.signature.InMemoryDocument;
import eu.europa.ec.markt.dss.signature.SignatureFormat;
import eu.europa.ec.markt.dss.signature.SignaturePackaging;
import eu.europa.ec.markt.dss.signature.SignatureParameters;
import eu.europa.ec.markt.dss.signature.token.KSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.xades.XAdESService;

public class SignatureUtil {
	public static void signDocumentDetached() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, SAXException, ParserConfigurationException {
		KeyStore ks = KeyStore.getInstance("JKS");
		
		FileInputStream kfis = new FileInputStream("/Users/muell16/git/ecodex_evidences/EvidencesModel/src/main/resources/evidenceBuilderStore.jks");
		
		ks.load(kfis, "123456".toCharArray());
		
		PrivateKey privKey = (PrivateKey)ks.getKey("evidenceBuilderKey", "123456".toCharArray());

		X509Certificate cert = (X509Certificate)ks.getCertificate("evidenceBuilderKey");
		
		XAdESService service = new XAdESService();
		
		SignatureParameters sigParam = new SignatureParameters();
		
		sigParam.setSignatureFormat(SignatureFormat.XAdES_BES);
		sigParam.setSignaturePackaging(SignaturePackaging.DETACHED);
		sigParam.setSigningDate(new Date());
		sigParam.setSigningCertificate(cert);
		
		InMemoryDocument doc = getNewInMemoryDocument("/Users/muell16/git/ecodex_evidences/EvidencesModel/src/main/resources/MyASICContainer.scs");
		
		Digest digest = service.digest(doc, sigParam);
		
		Certificate[] certArray = {cert};
		
		PrivateKeyEntry privKeyEntry = new PrivateKeyEntry(privKey, certArray);
		
		KSPrivateKeyEntry pke = new KSPrivateKeyEntry(privKeyEntry);
		
		DigestInfo digestInfo = new DigestInfo(DigestAlgorithm.SHA1.getAlgorithmIdentifier(), digest.getValue());
		
		Cipher cipher = Cipher.getInstance(pke.getSignatureAlgorithm().getPadding());
		
		cipher.init(Cipher.ENCRYPT_MODE, pke.getPrivateKey());
		
		byte[] signature = cipher.doFinal(digestInfo.getDEREncoded());
		
		Document XAdESSignature = service.signDocument(doc, sigParam, signature);
		
		
		//ab hier weiterverarbeitung
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		dbf.setNamespaceAware(true);
//		
//		org.w3c.dom.Document signed  = dbf.newDocumentBuilder().parse(XAdESSignature.openStream());
//		
//		
//		Element s = (Element)signed.removeChild(signed.getDocumentElement());
//		
//		org.w3c.dom.Document newdoc = dbf.newDocumentBuilder().newDocument();
//		Element sigs = newdoc.createElementNS("http://uri.etsi.org/2918/v.1.1.1#", "XAdESSignatures");
		
		//Ausgabe
		InputStream is = XAdESSignature.openStream();
		OutputStream out = new FileOutputStream(new File("mydetachedSig.xml"));
		
		int read = 0;
		byte[] bytes = new byte[1024];
		
		while((read = is.read(bytes))!= 1)
			out.write(bytes, 0 , read);
	}
	
	private static InMemoryDocument getNewInMemoryDocument(String file) {
		InMemoryDocument result = null;
		
		try {
			byte[] document = getBytesFromFile(new File(file));
			
			result = new InMemoryDocument(document);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	// Returns the contents of the file in a byte array.
	private static byte[] getBytesFromFile(File file) throws IOException {
	    InputStream is = new FileInputStream(file);

	    // Get the size of the file
	    long length = file.length();

	    // You cannot create an array using a long type.
	    // It needs to be an int type.
	    // Before converting to an int type, check
	    // to ensure that file is not larger than Integer.MAX_VALUE.
	    if (length > Integer.MAX_VALUE) {
	        // File is too large
	    }

	    // Create the byte array to hold the data
	    byte[] bytes = new byte[(int)length];

	    // Read in the bytes
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length
	           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }

	    // Ensure all the bytes have been read in
	    if (offset < bytes.length) {
	        throw new IOException("Could not completely read file "+file.getName());
	    }

	    // Close the input stream and return bytes
	    is.close();
	    return bytes;
	}

	public static void main(String[] args) {
		try {
			SignatureUtil.signDocumentDetached();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
