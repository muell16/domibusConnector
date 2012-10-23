package eu.ecodex.signature;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.etsi.uri._02640.v2.REMEvidenceType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import eu.spocseu.edeliverygw.JaxbContextHolder;

public class EvidenceUtilsImpl implements EvidenceUtils {
	private static Logger LOG = Logger.getLogger(EvidenceUtilsImpl.class);

	private String javaKeyStorePath, javaKeyStorePassword, alias, keyPassword;

	public EvidenceUtilsImpl(String javaKeyStorePath,
							 String javaKeyStorePassword, 
							 String alias, 
							 String keyPassword) {
		this.javaKeyStorePath = javaKeyStorePath;
		this.javaKeyStorePassword = javaKeyStorePassword;
		this.alias = alias;
		this.keyPassword = keyPassword;
	}

	@Override
	public byte[] signByteArray(byte[] xmlData) {
		LOG.debug("Start Signprocess");
		byte[] signedByteArray = null;

		// Create a DOM XMLSignatureFactory that will be used to generate the
		// enveloped signature

		try {
			XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

			// Create a Reference to the enveloped document (in this case we are
			// signing the whole document, so a URI of "" signifies that) and
			// also specify the SHA1 digest algorithm and the ENVELOPED
			// Transform.
			Reference ref = fac.newReference("", fac.newDigestMethod(
					DigestMethod.SHA1, null), Collections.singletonList(fac
					.newTransform(Transform.ENVELOPED,
							(TransformParameterSpec) null)), null, null);

			// Create the SignedInfo
			SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(
					CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS,
					(C14NMethodParameterSpec) null), fac.newSignatureMethod(
					SignatureMethod.RSA_SHA1, null), Collections
					.singletonList(ref));

			// Load KeyPair from Java Key Store
			KeyPair kp = getKeyPairFromKeyStore(javaKeyStorePath, javaKeyStorePassword, alias, keyPassword);

			// Create a KeyValue containing the PublicKey that was generated
			KeyInfoFactory kif = fac.getKeyInfoFactory();
			KeyValue kv;

			kv = kif.newKeyValue(kp.getPublic());
			// Create a KeyInfo and add the KeyValue to it
			KeyInfo ki = kif.newKeyInfo(Collections.singletonList(kv));

			// Instantiate the document to be signed
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			Document doc;
			doc = dbf.newDocumentBuilder().parse(
					new ByteArrayInputStream(xmlData));

			// Create a DOMSignContext and specify the PrivateKey and
			// location of the resulting XMLSignature's parent element
			DOMSignContext dsc = new DOMSignContext(kp.getPrivate(),
					doc.getDocumentElement());

			// Create the XMLSignature (but don't sign it yet)
			XMLSignature signature = fac.newXMLSignature(si, ki);

			// Marshal, generate (and sign) the enveloped signature
			signature.sign(dsc);


			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer trans = tf.newTransformer();
			trans.transform(new DOMSource(doc), new StreamResult(bos));

			signedByteArray = bos.toByteArray();

		} catch (KeyException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (XMLSignatureException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}

		return signedByteArray;
	}

	private synchronized static KeyPair getKeyPairFromKeyStore(String store,
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

	@Override
	public REMEvidenceType convertIntoEvidenceType(byte[] xmlData) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		REMEvidenceType convertedEvidence = null;
		Document doc;
		
		LOG.debug("Convert byte-array into Evidence");
		try {
			doc = dbf.newDocumentBuilder().parse(
					new ByteArrayInputStream(xmlData));
			
			convertedEvidence = convertIntoREMEvidenceType(doc).getValue();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		
		return convertedEvidence;
	}
	
	private JAXBElement<REMEvidenceType> convertIntoREMEvidenceType(
			Document domDocument) {
		JAXBElement<REMEvidenceType> jaxbObj = null;

		try {
			jaxbObj = JaxbContextHolder.getSpocsJaxBContext()
					.createUnmarshaller()
					.unmarshal(domDocument, REMEvidenceType.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return jaxbObj;
	}

}
