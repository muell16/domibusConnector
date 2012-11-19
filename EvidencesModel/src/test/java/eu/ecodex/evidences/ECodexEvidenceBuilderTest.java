package eu.ecodex.evidences;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.ecodex.evidences.exception.ECodexEvidenceBuilderException;
import eu.ecodex.evidences.types.ECodexMessageDetails;
import eu.ecodex.signature.EvidenceUtils;
import eu.ecodex.signature.EvidenceUtilsImpl;
import eu.spocseu.edeliverygw.REMErrorEvent;
import eu.spocseu.edeliverygw.configuration.EDeliveryDetails;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail.PostalAdress;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail.Server;

/**
 * The class <code>ECodexEvidenceBuilderTest</code> contains tests for the class
 * <code>{@link ECodexEvidenceBuilder}</code>.
 * 
 * @generatedBy CodePro at 02.11.12 10:26
 * @author cheny01
 * @version $Revision: 1.0 $
 */
public class ECodexEvidenceBuilderTest {

	String javaKeyStorePath = "src/main/resources/evidenceBuilderStore.jks";
	String javaKeyStorePassword = "123456";
	String alias = "evidenceBuilderKey";
	String keyPassword = "123456";
	XMLSignatureFactory sigFactory = null;
	DocumentBuilderFactory dbf;

	/**
	 * Run the ECodexEvidenceBuilder(String,String,String,String) constructor
	 * test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 02.11.12 10:26
	 */
	@Test
	public void testECodexEvidenceBuilder() throws Exception {
		ECodexEvidenceBuilder result = new ECodexEvidenceBuilder(
				javaKeyStorePath, javaKeyStorePassword, alias, keyPassword);
		assertNotNull(result);
	}

	// create EDliveryDetails
	private EDeliveryDetails createEntityDetailsObject() {

		PostalAdress address = new PostalAdress();
		address.setCountry("testCountry");
		address.setLocality("testLocality");
		address.setPostalCode("testPostalcode");
		address.setStreetAddress("testStreetaddress");

		Server server = new Server();
		server.setDefaultCitizenQAAlevel(1);
		server.setGatewayAddress("testGatewayaddress");
		server.setGatewayDomain("testGatewaydomain");
		server.setGatewayName("testGatewayname");

		EDeliveryDetail detail = new EDeliveryDetail();

		detail.setPostalAdress(address);
		detail.setServer(server);

		return new EDeliveryDetails(detail);
	}

	// create MessageDetails
	private ECodexMessageDetails createMessageDetailsObject() {

		ECodexMessageDetails messageDetails = new ECodexMessageDetails();
		messageDetails.setEbmsMessageId("testEbms3MsgId");
		messageDetails.setHashAlgorithm("sha1");
		messageDetails.setHashValue(new byte[] { 0x000A, 0x000A });
		messageDetails.setNationalMessageId("testNationalMsgId");
		messageDetails.setRecipientAddress("testRecipientAddress");
		messageDetails.setSenderAddress("testSenderAddress");

		return messageDetails;
	}

	/**
	 * Run the byte[]
	 * createDeliveryNonDeliveryToRecipient(boolean,REMErrorEvent,
	 * EDeliveryDetails,REMEvidenceType) method test.
	 * 
	 * Case: Eventreason = UNKNOWN_ORIGINATOR_ADDRESS;
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 02.11.12 10:26
	 */
	@Test
	public void testCreateDeliveryNonDeliveryToRecipient() throws Exception {

		byte[] signedxmlData;
		PublicKey publicKey;
		ECodexEvidenceBuilder ecodexEvidenceBuilder = new ECodexEvidenceBuilder(
				javaKeyStorePath, javaKeyStorePassword, alias, keyPassword);
		boolean isDelivery = true;
		REMErrorEvent eventReason = REMErrorEvent.UNKNOWN_ORIGINATOR_ADDRESS;
		EDeliveryDetails evidenceIssuerDetails = createEntityDetailsObject();
		REMEvidenceType previousEvidence = createREMEvidenceType1();
		
		signedxmlData = ecodexEvidenceBuilder
				.createDeliveryNonDeliveryToRecipient(isDelivery, eventReason,
						evidenceIssuerDetails, previousEvidence);
		// output the signed Xmlfile
		File xmloutputfile = new File(
				"src/test/resources/outputfileDeliveryNonDeliveryToRecipientUnknownAdress.xml");
		FileOutputStream fileoutXML = new FileOutputStream(xmloutputfile);
		fileoutXML.write(signedxmlData);
		fileoutXML.close();

		Document document;
		document = dbf.newDocumentBuilder().parse(
				new ByteArrayInputStream(signedxmlData));

		KeyPair keypair = getKeyPairFromKeyStore(javaKeyStorePath,
				javaKeyStorePassword, alias, keyPassword);
		publicKey = keypair.getPublic();

		assertTrue("Signature failed", signatureValidate(document, publicKey));

	}

	/**
	 * Run the byte[]
	 * createDeliveryNonDeliveryToRecipient(boolean,REMErrorEvent,
	 * EDeliveryDetails,REMEvidenceType) method test.
	 * 
	 * Case: Eventreason = null
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 02.11.12 10:26
	 */
	@Test
	public void testCreateDeliveryNonDeliveryToRecipient_1() throws Exception {

		byte[] signedxmlData;
		PublicKey publicKey;
		ECodexEvidenceBuilder ecodexEvidenceBuilder = new ECodexEvidenceBuilder(
				javaKeyStorePath, javaKeyStorePassword, alias, keyPassword);
		boolean isDelivery = true;
		REMErrorEvent eventReason = null;
		EDeliveryDetails evidenceIssuerDetails = createEntityDetailsObject();
		REMEvidenceType previousEvidence = createREMEvidenceType1();

		signedxmlData = ecodexEvidenceBuilder
				.createDeliveryNonDeliveryToRecipient(isDelivery, eventReason,
						evidenceIssuerDetails, previousEvidence);
		// output the signed Xmlfile
		File xmloutputfile = new File(
				"src/test/resources/outputfileDeliveryNonDeliveryToRecipientNoreason.xml");
		FileOutputStream fileoutXML = new FileOutputStream(xmloutputfile);
		fileoutXML.write(signedxmlData);
		fileoutXML.close();

		Document document;
		document = dbf.newDocumentBuilder().parse(
				new ByteArrayInputStream(signedxmlData));

		KeyPair keypair = getKeyPairFromKeyStore(javaKeyStorePath,
				javaKeyStorePassword, alias, keyPassword);
		publicKey = keypair.getPublic();

		assertTrue("Signature failed", signatureValidate(document, publicKey));

	}

	// create a REMEvidenceType "SubmissionAcceptanceRejection"
	private REMEvidenceType createREMEvidenceType1()
			throws ECodexEvidenceBuilderException {
		REMEvidenceType evidenceType;

		byte[] evidenceAsByteArray;
		ECodexEvidenceBuilder builder = new ECodexEvidenceBuilder(
				javaKeyStorePath, javaKeyStorePassword, alias, keyPassword);

		evidenceAsByteArray = builder.createSubmissionAcceptanceRejection(true,
				REMErrorEvent.OTHER, createEntityDetailsObject(),
				createMessageDetailsObject());

		EvidenceUtils utils = new EvidenceUtilsImpl(javaKeyStorePath,
				javaKeyStorePassword, alias, keyPassword);
		evidenceType = utils.convertIntoEvidenceType(evidenceAsByteArray);

		return evidenceType;
	}

	/**
	 * Run the byte[]
	 * createRetrievalNonRetrievalByRecipient(boolean,REMErrorEvent
	 * ,EDeliveryDetails,REMEvidenceType) method test.
	 * 
	 * Case: Eventreason = null
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 02.11.12 10:26
	 */
	@Test
	public void testCreateRetrievalNonRetrievalByRecipient() throws Exception {
		byte[] signedxmlData;
		PublicKey publicKey;
		ECodexEvidenceBuilder ecodexEvidenceBuilder = new ECodexEvidenceBuilder(
				javaKeyStorePath, javaKeyStorePassword, alias, keyPassword);
		boolean isDelivery = true;
		REMErrorEvent eventReason = null;
		EDeliveryDetails evidenceIssuerDetails = createEntityDetailsObject();
		REMEvidenceType previousEvidence = createREMEvidenceType2();

		signedxmlData = ecodexEvidenceBuilder
				.createDeliveryNonDeliveryToRecipient(isDelivery, eventReason,
						evidenceIssuerDetails, previousEvidence);
		// output the signed Xmlfile
		File xmloutputfile = new File(
				"src/test/resources/outputfileRetrievalNonRetrievalByRecipientNoreason.xml");
		FileOutputStream fileoutXML = new FileOutputStream(xmloutputfile);
		fileoutXML.write(signedxmlData);
		fileoutXML.close();

		Document document;
		document = dbf.newDocumentBuilder().parse(
				new ByteArrayInputStream(signedxmlData));

		KeyPair keypair = getKeyPairFromKeyStore(javaKeyStorePath,
				javaKeyStorePassword, alias, keyPassword);
		publicKey = keypair.getPublic();

		assertTrue("Signature failed", signatureValidate(document, publicKey));
	}

	// create a REMEvidenceType "DeliveryNonDeliveryToRecipient"

	private REMEvidenceType createREMEvidenceType2()
			throws ECodexEvidenceBuilderException {
		REMEvidenceType evidenceType;
		byte[] evidenceAsByteArray;
		byte[] evidenceAsByteArray1;
		ECodexEvidenceBuilder builder = new ECodexEvidenceBuilder(
				javaKeyStorePath, javaKeyStorePassword, alias, keyPassword);

		evidenceAsByteArray = builder.createSubmissionAcceptanceRejection(true,
				REMErrorEvent.OTHER, createEntityDetailsObject(),
				createMessageDetailsObject());
		EvidenceUtils utils = new EvidenceUtilsImpl(javaKeyStorePath,
				javaKeyStorePassword, alias, keyPassword);

		evidenceAsByteArray1 = builder.createDeliveryNonDeliveryToRecipient(
				true, REMErrorEvent.OTHER, createEntityDetailsObject(),
				utils.convertIntoEvidenceType(evidenceAsByteArray));

		evidenceType = utils.convertIntoEvidenceType(evidenceAsByteArray1);

		return evidenceType;

	}

	/**
	 * Run the byte[]
	 * createRetrievalNonRetrievalByRecipient(boolean,REMErrorEvent
	 * ,EDeliveryDetails,REMEvidenceType) method test.
	 * 
	 * Case: Eventreason = UNKNOWN_ORIGINATOR_ADDRESS;
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 02.11.12 10:26
	 */
	@Test
	public void testCreateRetrievalNonRetrievalByRecipient_1() throws Exception {
		byte[] signedxmlData;
		PublicKey publicKey;
		ECodexEvidenceBuilder ecodexEvidenceBuilder = new ECodexEvidenceBuilder(
				javaKeyStorePath, javaKeyStorePassword, alias, keyPassword);
		boolean isDelivery = true;
		REMErrorEvent eventReason = REMErrorEvent.UNKNOWN_ORIGINATOR_ADDRESS;
		EDeliveryDetails evidenceIssuerDetails = createEntityDetailsObject();
		REMEvidenceType previousEvidence = createREMEvidenceType2();

		signedxmlData = ecodexEvidenceBuilder
				.createDeliveryNonDeliveryToRecipient(isDelivery, eventReason,
						evidenceIssuerDetails, previousEvidence);
		// output the signed Xmlfile
		File xmloutputfile = new File(
				"src/test/resources/outputfileRetrievalNonRetrievalByRecipientUnkownadress.xml");
		FileOutputStream fileoutXML = new FileOutputStream(xmloutputfile);
		fileoutXML.write(signedxmlData);
		fileoutXML.close();

		Document document;
		document = dbf.newDocumentBuilder().parse(
				new ByteArrayInputStream(signedxmlData));

		KeyPair keypair = getKeyPairFromKeyStore(javaKeyStorePath,
				javaKeyStorePassword, alias, keyPassword);
		publicKey = keypair.getPublic();

		assertTrue("Signature failed", signatureValidate(document, publicKey));
	}

	/**
	 * Run the byte[] createSubmissionAcceptanceRejection(boolean,REMErrorEvent,
	 * EDeliveryDetails,ECodexMessageDetails) method test.
	 * 
	 * Case: isAcceptance=true
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 02.11.12 10:26
	 */
	@Test
	public void testCreateSubmissionAcceptanceRejection() throws Exception {
		byte[] signedxmlData;
		PublicKey publicKey;
		ECodexEvidenceBuilder ecodexEvidenceBuilder = new ECodexEvidenceBuilder(
				javaKeyStorePath, javaKeyStorePassword, alias, keyPassword);
		boolean isAcceptance = true;
		REMErrorEvent eventReason = null;
		EDeliveryDetails evidenceIssuerDetails = createEntityDetailsObject();
		ECodexMessageDetails messageDetails = createMessageDetailsObject();

		// run methode createSubmissionAcceptanceRejection
		signedxmlData = ecodexEvidenceBuilder
				.createSubmissionAcceptanceRejection(isAcceptance, eventReason,
						evidenceIssuerDetails, messageDetails);
		// output the signed Xmlfile
		File xmloutputfile = new File(
				"src/test/resources/outputfileSubmissionAcceptanceNORejection.xml");
		FileOutputStream fileoutXML = new FileOutputStream(xmloutputfile);
		fileoutXML.write(signedxmlData);
		fileoutXML.close();

		Document document;
		document = dbf.newDocumentBuilder().parse(
				new ByteArrayInputStream(signedxmlData));

		KeyPair keypair = getKeyPairFromKeyStore(javaKeyStorePath,
				javaKeyStorePassword, alias, keyPassword);
		publicKey = keypair.getPublic();

		assertTrue("Signature failed", signatureValidate(document, publicKey));
	}

	/**
	 * Run the byte[] createSubmissionAcceptanceRejection(boolean,REMErrorEvent,
	 * EDeliveryDetails,ECodexMessageDetails) method test.
	 * 
	 * Case: isAcceptance=false
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 02.11.12 10:26
	 */
	@Test
	public void testCreateSubmissionAcceptanceRejection_1() throws Exception {
		byte[] signedxmlData;
		PublicKey publicKey;
		ECodexEvidenceBuilder ecodexEvidenceBuilder = new ECodexEvidenceBuilder(
				javaKeyStorePath, javaKeyStorePassword, alias, keyPassword);
		boolean isAcceptance = false;
		REMErrorEvent eventReason = REMErrorEvent.UNKNOWN_ORIGINATOR_ADDRESS;
		EDeliveryDetails evidenceIssuerDetails = createEntityDetailsObject();
		ECodexMessageDetails messageDetails = createMessageDetailsObject();

		// run methode createSubmissionAcceptanceRejection
		signedxmlData = ecodexEvidenceBuilder
				.createSubmissionAcceptanceRejection(isAcceptance, eventReason,
						evidenceIssuerDetails, messageDetails);
		// output the signed Xmlfile
		File xmloutputfile = new File(
				"src/test/resources/outputfileSubmissionAcceptanceYESRejection.xml");
		FileOutputStream fileoutXML = new FileOutputStream(xmloutputfile);
		fileoutXML.write(signedxmlData);
		fileoutXML.close();
//		to test: if file A.xml is changed.
		signedxmlData=getBytesFromFile("src/test/resources/signatureTestbysourceInfochange.xml");
		Document document;
		document = dbf.newDocumentBuilder().parse(
				new ByteArrayInputStream(signedxmlData));
		// KeyPair keypair= generateNewRandomKeyPair();
		KeyPair keypair = getKeyPairFromKeyStore(javaKeyStorePath,
				javaKeyStorePassword, alias, keyPassword);
		publicKey = keypair.getPublic();
		
//		System.out.println(publicKey.toString());

		assertTrue("Signature failed", signatureValidate(document, publicKey));
	}

	// Signature validation
	private boolean signatureValidate(Document doc, PublicKey publicKey)
			throws Exception {
		boolean signStatus = true;
		NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS,
				"Signature");
		if (nl.getLength() == 0) {
			throw new Exception("Cannot find Signature element");
		}
		Node signatureNode = nl.item(0);
		XMLSignatureFactory factory = getSignatureFactory();
		// to test: CASE 1
//		XMLSignature signature = factory
//				.unmarshalXMLSignature(new DOMStructure(signatureNode));
		
		// Create ValidateContext
		DOMValidateContext valContext = new DOMValidateContext(publicKey,
				signatureNode);
		
		//to test: CASE 2
		XMLSignature signature = 
	            factory.unmarshalXMLSignature(valContext);
		
		
		// Validate the XMLSignature
		signStatus = signStatus && signature.validate(valContext);
		// check the validation status of each Reference
		List<?> refs = signature.getSignedInfo().getReferences();
		for (int i = 0; i < refs.size(); i++) {
			Reference ref = (Reference) refs.get(i);
			
//			System.out.println("Reference[" + i + "] validity status: "
//					+ ref.validate(valContext));
			signStatus = signStatus && ref.validate(valContext);
		}
		return signStatus;
	}

	private XMLSignatureFactory getSignatureFactory()
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		if (sigFactory == null)
			sigFactory = XMLSignatureFactory.getInstance("DOM");
		return sigFactory;

	}

	// private method to get the keypair
	private KeyPair getKeyPairFromKeyStore(String store, String storePass,
			String alias, String keyPass) {
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

	// create random KeyPair with algorithm "RSA"
//	private static KeyPair generateNewRandomKeyPair()
//			throws NoSuchAlgorithmException {
//		final int KEY_SIZE = 1024;
//		final String ALGORITHM = "RSA";
//		final String RANDOM_ALG = "SHA1PRNG";
//		KeyPairGenerator kg = null;
//		SecureRandom random = null;
//		kg = KeyPairGenerator.getInstance(ALGORITHM);
//		random = SecureRandom.getInstance(RANDOM_ALG);
//		kg.initialize(KEY_SIZE, random);
//		return kg.generateKeyPair();
//	}

	/**
	 * Perform pre-test initialization.
	 * 
	 * @throws Exception
	 *             if the initialization fails for some reason
	 * 
	 * @generatedBy CodePro at 02.11.12 10:26
	 */
	@Before
	public void setUp() throws Exception {
		dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
	}

	/**
	 * Perform post-test clean-up.
	 * 
	 * @throws Exception
	 *             if the clean-up fails for some reason
	 * 
	 * @generatedBy CodePro at 02.11.12 10:26
	 */
	@After
	public void tearDown() throws Exception {
		// Add additional tear down code here
	}

	/**
	 * Launch the test.
	 * 
	 * @param args
	 *            the command line arguments
	 * 
	 * @generatedBy CodePro at 02.11.12 10:26
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(ECodexEvidenceBuilderTest.class);
	}
}