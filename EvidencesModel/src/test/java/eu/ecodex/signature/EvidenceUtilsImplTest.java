package eu.ecodex.signature;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.List;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilderFactory;

import org.etsi.uri._02640.v2.REMEvidenceType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * The class <code>EvidenceUtilsImplTest</code> contains tests for the class <code>{@link EvidenceUtilsImpl}</code>.
 *
 * @generatedBy CodePro at 02.11.12 10:16
 * @author cheny01
 * @version $Revision: 1.0 $
 */
public class EvidenceUtilsImplTest {
	
	XMLSignatureFactory signFactory;

	String javaKeyStorePath = "src/main/resources/evidenceBuilderStore.jks";
	String javaKeyStorePassword = "123456";
	String alias = "evidenceBuilderKey";
	String keyPassword = "123456";
	
	/**
	 * Run the EvidenceUtilsImpl(String,String,String,String) constructor test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 02.11.12 10:16
	 */
	@Test
	public void testEvidenceUtilsImpl()
		throws Exception {
		
		EvidenceUtilsImpl result = new EvidenceUtilsImpl(javaKeyStorePath, javaKeyStorePassword, alias, keyPassword);
		// add additional test code here
		assertNotNull(result);
	}

	//create array bytes from xmlFile
	private byte[] getBytesFromFile(String xmlFilePath) throws Exception {
		 
		byte[] bytes = null;
 		File file = new File(xmlFilePath);
		InputStream is;
		is = new FileInputStream(file);
		// Get the size of the file
		long length = file.length();
 		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// throw new Exception("Too large file");
			System.err.println("Too large file");
		}
		// Create the byte array to hold the data
		bytes = new byte[(int) length];
 		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			System.err.println("Could not completely read file " + file.getName());
		}
 		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	/**
	 * Run the REMEvidenceType convertIntoEvidenceType(byte[]) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 02.11.12 10:16
	 */
	@Test
	public void testConvertIntoEvidenceType()
		throws Exception {
		
		String xmlFile ="src/test/resources/ConvertIntoEvidenceTypetestFileA.xml";
		byte[] xmlData = getBytesFromFile(xmlFile);
		EvidenceUtilsImpl fixture= new EvidenceUtilsImpl(javaKeyStorePath, javaKeyStorePassword, alias, keyPassword);
		
		REMEvidenceType remEvidenceType = null;
		remEvidenceType= fixture.convertIntoEvidenceType(xmlData);
//		System.out.print((remEvidenceType.getEvidenceIssuerDetails().getNamesPostalAddresses()).getNamePostalAddress().get(0).getEntityName().getName());
		assertTrue("failure by runing the method convertIntoEvidenceType", (remEvidenceType.getEvidenceIssuerDetails().getNamesPostalAddresses()).getNamePostalAddress().get(0).getEntityName().getName().toString().equalsIgnoreCase("[gatewayname]"));

	}

	// private method to get the keypair
	private KeyPair getKeyPairFromKeyStore(String store,
			String storePass, String alias, String keyPass) {
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
	/**
	 * Run the byte[] signByteArray(byte[]) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 02.11.12 10:16
	 */
	@Test
	public void testSignByteArray()
		throws Exception {
		
		//create signature with methode signByteArray
		String xmlFile ="src/test/resources/ConvertIntoEvidenceTypetestFileA.xml";
		byte[] xmlData = getBytesFromFile(xmlFile);
		EvidenceUtilsImpl fixture= new EvidenceUtilsImpl(javaKeyStorePath, javaKeyStorePassword, alias, keyPassword);
		
		byte[] signedxmlData=fixture.signByteArray(xmlData);
		File xmloutputfile=new File("src/test/resources/DeliveryNonDelivery.xml");
		FileOutputStream fileoutXML=new FileOutputStream(xmloutputfile);
		fileoutXML.write(signedxmlData);
		fileoutXML.close();
		

		// validate  Signature
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document document;
		document = dbf.newDocumentBuilder().parse(
				new ByteArrayInputStream(signedxmlData));
		
			
		KeyPair keypair = getKeyPairFromKeyStore(javaKeyStorePath, javaKeyStorePassword, alias, keyPassword);

		PublicKey publicKey = keypair.getPublic();
    	
//    	System.out.println(keypair.getPublic().toString());
		try {
			
			assertTrue("Signatrue is wrong",
					signatureValidate(document, publicKey));
		} catch (MarshalException ex) {
			fail("Unmarshal failed: " + ex.getMessage());
			ex.printStackTrace();
		}
	

	
		
		
	}
	//signature validation 
		private boolean signatureValidate(Document doc, PublicKey publicKey)
				throws Exception {
			boolean signStatus= true;
			NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS,
					"Signature");
			if (nl.getLength() == 0) {
				throw new Exception("Cannot find Signature element");
			}
			// Create ValidateContext
			DOMValidateContext valContext = new DOMValidateContext(publicKey,
					nl.item(0));
			XMLSignatureFactory factory = getSignatureFactory();
			XMLSignature signature = factory.unmarshalXMLSignature(valContext);
			// Validate the XMLSignature
			signStatus= signStatus && signature.validate(valContext);
			 // check the validation status of each Reference
			List<?> refs = signature.getSignedInfo().getReferences();
			for(int i=0; i<refs.size(); i++) {
				Reference ref = (Reference)refs.get(i);
				System.out.println("Reference["+i+"] validity status: " + ref.validate(valContext));
				signStatus= signStatus && ref.validate(valContext);
				}
		return signStatus;
		}
		
		private XMLSignatureFactory getSignatureFactory()
				throws InstantiationException, IllegalAccessException,
				ClassNotFoundException {
			if (signFactory == null)
				signFactory = XMLSignatureFactory.getInstance("DOM");
			return signFactory;

		}

	@SuppressWarnings("restriction")
	@Before
	public void setUp()
		throws Exception {
//		String providerName = System.getProperty("jsr105Provider",
//				"org.jcp.xml.dsig.internal.dom.XMLDSigRI");
//		signFactory = XMLSignatureFactory.getInstance("DOM",
//				(Provider) Class.forName(providerName).newInstance());
		
		signFactory = XMLSignatureFactory.getInstance
	            ("DOM", new org.jcp.xml.dsig.internal.dom.XMLDSigRI());	
	}

	/**
	 * Perform post-test clean-up.
	 *
	 * @throws Exception
	 *         if the clean-up fails for some reason
	 *
	 * @generatedBy CodePro at 02.11.12 10:16
	 */
	@After
	public void tearDown()
		throws Exception {
	}

	/**
	 * Launch the test.
	 *
	 * @param args the command line arguments
	 *
	 * @generatedBy CodePro at 02.11.12 10:16
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(EvidenceUtilsImplTest.class);
	}
}