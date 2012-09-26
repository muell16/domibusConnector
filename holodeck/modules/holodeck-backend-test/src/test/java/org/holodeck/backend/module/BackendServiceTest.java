/*
 *
 */
package org.holodeck.backend.module;

import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.Arrays;
import org.codehaus.cargo.container.ContainerType;
import org.codehaus.cargo.container.InstalledLocalContainer;
import org.codehaus.cargo.container.configuration.ConfigurationType;
import org.codehaus.cargo.container.configuration.LocalConfiguration;
import org.codehaus.cargo.container.deployable.WAR;
import org.codehaus.cargo.container.property.GeneralPropertySet;
import org.codehaus.cargo.generic.DefaultContainerFactory;
import org.codehaus.cargo.generic.configuration.DefaultConfigurationFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;


/**
 * The Class BackendServiceTest.
 */
public class BackendServiceTest{

	/** The container. */
	private static InstalledLocalContainer container;

	/** The Constant BACKEND_WS_URL. */
	private final static String BACKEND_WS_URL = "http://localhost:8080/holodeck/services/BackendService";

	private final static String PERSISTENCE_UNIT_REG_EXP = ".*<parameter name=\"PersistenceUnit\">([^<]*)</parameter>.*";

	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception{
		if (container == null) {
			System.out.println("Init Setup BackendServiceTest");

			File tomcatDirectory = new File("../../target/holodeck/server");
			File actualDirectory = new File(".");

			FileUtils.copyFileToDirectory(new File(tomcatDirectory.getAbsoluteFile() + "/../msh/WEB-INF/classes/META-INF/persistence.xml"), new File(actualDirectory.getAbsoluteFile() + "/target/test-classes/META-INF"));

			System.out.println("copying directory[" + tomcatDirectory.getAbsolutePath() +"] to directory[" + tomcatDirectory.getParentFile().getAbsoluteFile() + "/server_test" + "]");

			FileUtils.copyDirectory(tomcatDirectory.getAbsoluteFile(), (new File(tomcatDirectory.getParentFile().getAbsoluteFile(), "server_test")).getAbsoluteFile() );

			tomcatDirectory = new File("../../target/holodeck/server_test");

			(new File(tomcatDirectory.getAbsolutePath() + "/conf/Catalina/localhost", "holodeck.xml")).delete();

			LocalConfiguration configuration = (LocalConfiguration) new DefaultConfigurationFactory()
					.createConfiguration("tomcat6x", ContainerType.INSTALLED, ConfigurationType.STANDALONE);

			configuration.setProperty(GeneralPropertySet.JVMARGS, "-Xmx512m -XX:MaxPermSize=256m");

			File mshDirectory = new File("../../target/holodeck/msh");

			WAR war = new WAR(mshDirectory.getAbsolutePath());
			war.setContext("holodeck");

			configuration.addDeployable(war);
			container = (InstalledLocalContainer) new DefaultContainerFactory().createContainer("tomcat6x",
					ContainerType.INSTALLED, configuration);

			container.setHome(tomcatDirectory.getAbsolutePath());

			File confDirectory = new File("../../target/holodeck/config");
			configuration.addDeployable(new WAR(confDirectory.getAbsolutePath()));

			System.out.println("Starting tomcat server for BackendServiceTest");
			container.start();

			System.out.println("Finished Setup BackendServiceTest with server state[" + container.getState() + "]");
		}

		deleteDBData();
	}

	private void deleteDBData() throws Exception{
		System.out.println("Deleting DB data");

		setupPersistence();
		deleteData();

		System.out.println("Deleted DB data");
	}

    /**
     * Setup the the {@code EntityManager} and {@code EntityTransaction} for
     * local junit testing.
     */
    private void setupPersistence() throws Exception{
    	String persistenceUnit = getPersistenceUnit();

    	entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);

    	entityManager = entityManagerFactory.createEntityManager();
    }

    private void deleteData() throws Exception{
    	entityManager.getTransaction().begin();

    	{
    		Query query = entityManager.createNativeQuery("delete from Payload");
    		query.executeUpdate();
    	}
    	{
    		Query query = entityManager.createNativeQuery("delete from Message");
    		query.executeUpdate();
    	}

    	entityManager.getTransaction().commit();
    }

	private String getPersistenceUnit() throws Exception{
		File backendModuleConfigFile = new File("../../target/holodeck/msh/WEB-INF/modules/holodeck-backend/META-INF/module.xml");

		String backendModuleConfigContent =  FileUtils.readFileToString(new File(backendModuleConfigFile.getAbsolutePath()));

		backendModuleConfigContent = backendModuleConfigContent.replace("\n", "");
		backendModuleConfigContent = backendModuleConfigContent.replace("\r", "");

		Pattern p = Pattern.compile(PERSISTENCE_UNIT_REG_EXP);
	    Matcher m = p.matcher(backendModuleConfigContent);

	    m.find();

	    return m.group(1);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	public void tearDown() {
	}

	/**
	 * Test wsdl.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testWSDL() throws Exception {
		System.out.println("Init testWSDL");

		URL url = new URL(BACKEND_WS_URL + "?wsdl");
		InputStream is = url.openStream();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		IOUtils.copy(is, byteArrayOutputStream);

		System.out.println("Backend WSDL:");
		System.out.println(new String(byteArrayOutputStream.toByteArray()));
	}

	/**
	 * Test send message with reference.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSendMessageWithReference() throws java.lang.Exception {
		System.out.println("Init testSendMessageWithReference");

		org.holodeck.backend.client.BackendServiceStub backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
		org.holodeck.backend.client.types.SendRequestURL sendRequestURL24 = (org.holodeck.backend.client.types.SendRequestURL) getTestObject(org.holodeck.backend.client.types.SendRequestURL.class);

		InputStream inputStream = BackendServiceTest.class.getResourceAsStream("/send/messaging_1.xml");

		org.holodeck.backend.client.types.MessagingE messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);

		sendRequestURL24.setPayload(new String[]{BACKEND_WS_URL + "?wsdl"});

		backendServiceStub.sendMessageWithReference(sendRequestURL24, messaging);

		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = (org.holodeck.backend.client.types.ListPendingMessagesRequest) getTestObject(org.holodeck.backend.client.types.ListPendingMessagesRequest.class);

		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = backendServiceStub.listPendingMessages(listPendingMessagesRequest30);

		Assert.assertNotNull(listPendingMessagesResponse);
		Assert.assertNotNull(listPendingMessagesResponse.getMessageID());

		Assert.assertEquals(1, listPendingMessagesResponse.getMessageID().length);
	}

	/**
	 * Test send message.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSendMessage() throws java.lang.Exception {
		System.out.println("Init testSendMessage");

		org.holodeck.backend.client.BackendServiceStub backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
		org.holodeck.backend.client.types.SendRequest sendRequest24 = (org.holodeck.backend.client.types.SendRequest) getTestObject(org.holodeck.backend.client.types.SendRequest.class);

		InputStream inputStream = BackendServiceTest.class.getResourceAsStream("/send/messaging_1.xml");

		org.holodeck.backend.client.types.MessagingE messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);

		sendRequest24.setPayload(new DataHandler[]{new DataHandler(BackendServiceTest.class.getResource("/send/jira_logo_small.png"))});

		backendServiceStub.sendMessage(sendRequest24, messaging);

		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = (org.holodeck.backend.client.types.ListPendingMessagesRequest) getTestObject(org.holodeck.backend.client.types.ListPendingMessagesRequest.class);

		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = backendServiceStub.listPendingMessages(listPendingMessagesRequest30);

		Assert.assertNotNull(listPendingMessagesResponse);
		Assert.assertNotNull(listPendingMessagesResponse.getMessageID());

		Assert.assertEquals(1, listPendingMessagesResponse.getMessageID().length);
	}

	/**
	 * Testlist pending messages.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testListPendingMessages() throws java.lang.Exception {
		
		System.out.println("Init testListPendingMessages");

		for ( int i = 0; i < 5; i++){
			org.holodeck.backend.client.BackendServiceStub backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
			org.holodeck.backend.client.types.SendRequestURL sendRequestURL24 = (org.holodeck.backend.client.types.SendRequestURL) getTestObject(org.holodeck.backend.client.types.SendRequestURL.class);

			InputStream inputStream = BackendServiceTest.class.getResourceAsStream("/send/messaging_1.xml");

			org.holodeck.backend.client.types.MessagingE messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);

			sendRequestURL24.setPayload(new String[]{BACKEND_WS_URL + "?wsdl"});

			backendServiceStub.sendMessageWithReference(sendRequestURL24, messaging);
		}

		org.holodeck.backend.client.BackendServiceStub backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = (org.holodeck.backend.client.types.ListPendingMessagesRequest) getTestObject(org.holodeck.backend.client.types.ListPendingMessagesRequest.class);

		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = backendServiceStub.listPendingMessages(listPendingMessagesRequest30);

		Assert.assertNotNull(listPendingMessagesResponse);
		Assert.assertNotNull(listPendingMessagesResponse.getMessageID());

		Assert.assertEquals(5, listPendingMessagesResponse.getMessageID().length);
	}


	/**
	 * Test Download message.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testDownloadMessage() throws java.lang.Exception {
		System.out.println("Init testDownloadMessage");

		org.holodeck.backend.client.BackendServiceStub backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
		org.holodeck.backend.client.types.SendRequest sendRequest24 = (org.holodeck.backend.client.types.SendRequest) getTestObject(org.holodeck.backend.client.types.SendRequest.class);

		InputStream inputStream = BackendServiceTest.class.getResourceAsStream("/send/messaging_1.xml");

		org.holodeck.backend.client.types.MessagingE messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);

		sendRequest24.setPayload(new DataHandler[]{new DataHandler(BackendServiceTest.class.getResource("/send/jira_logo_small.png"))});

		backendServiceStub.sendMessage(sendRequest24, messaging);

		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = (org.holodeck.backend.client.types.ListPendingMessagesRequest) getTestObject(org.holodeck.backend.client.types.ListPendingMessagesRequest.class);

		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = backendServiceStub.listPendingMessages(listPendingMessagesRequest30);

		Assert.assertNotNull(listPendingMessagesResponse);
		Assert.assertNotNull(listPendingMessagesResponse.getMessageID());

		Assert.assertEquals(1, listPendingMessagesResponse.getMessageID().length);

		org.holodeck.backend.client.BackendServiceStub stub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
		org.holodeck.backend.client.types.DownloadMessageRequest downloadMessageRequest32 = (org.holodeck.backend.client.types.DownloadMessageRequest) getTestObject(org.holodeck.backend.client.types.DownloadMessageRequest.class);
		downloadMessageRequest32.setMessageID(listPendingMessagesResponse.getMessageID()[0]);

		org.holodeck.backend.client.types.DownloadMessageResponse downloadMessageResponse = stub.downloadMessage(downloadMessageRequest32);

		Assert.assertNotNull(downloadMessageResponse);
		Assert.assertNotNull(downloadMessageResponse.getPayload());
		Assert.assertNotNull(downloadMessageResponse.getPayload()[0]);

		byte[] originalData = IOUtils.toByteArray(BackendServiceTest.class.getResourceAsStream("/send/jira_logo_small.png")) ;
		byte[] receivedData = IOUtils.toByteArray(downloadMessageResponse.getPayload()[0].getInputStream()) ;

		Assert.assertTrue(Arrays.areEqual(originalData, receivedData));
	}

	//Create an ADBBean and provide it as the test object
	/**
	 * Gets the test object.
	 *
	 * @param type the type
	 * @return the test object
	 * @throws Exception the exception
	 */
	public org.apache.axis2.databinding.ADBBean getTestObject(java.lang.Class type) throws java.lang.Exception {
		return (org.apache.axis2.databinding.ADBBean) type.newInstance();
	}

	
	/**
	 * Test: Send a message that does not exist
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSendMessageWithReferenceNoExist() throws java.lang.Exception {

		String urlMessage = "/send/messageNoExist.xml";
		String messageStartTest = "Init testSendMessageWithReferenceNotExist"; 
		String messageEndTest = "End testSendMessageWithReferenceNotExist";
		org.holodeck.backend.client.BackendServiceStub backendServiceStub = null;		
		org.holodeck.backend.client.types.SendRequestURL sendRequestURL24 = null;
		InputStream inputStream = null;
		
		System.out.println(messageStartTest);

		backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
		sendRequestURL24 = (org.holodeck.backend.client.types.SendRequestURL)getTestObject(org.holodeck.backend.client.types.SendRequestURL.class);

		inputStream = BackendServiceTest.class.getResourceAsStream(urlMessage);
		Assert.assertNull(inputStream);
	
		System.out.println(messageEndTest);
	}

	/**
	 * Test: Send a message that has a format error in its nodes.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSendErrorFormatMessage() throws java.lang.Exception {
		String urlMessage = "/send/messageErrorFormat.xml";
		String messageStartTest = "Init testSendMessageWithReferenceNotExist"; 
		String messageEndTest = "End testSendMessageWithReferenceNotExist";
		org.holodeck.backend.client.BackendServiceStub backendServiceStub = null;		
		org.holodeck.backend.client.types.SendRequestURL sendRequestURL24 = null;
		InputStream inputStream = null;
		
		System.out.println(messageStartTest);

		backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
		sendRequestURL24 = (org.holodeck.backend.client.types.SendRequestURL)getTestObject(org.holodeck.backend.client.types.SendRequestURL.class);

		inputStream = BackendServiceTest.class.getResourceAsStream(urlMessage);

		Assert.assertNull(inputStream);
	
		System.out.println(messageEndTest);
	}

	/**
	 * Test: Send a message with a payload that does not exist
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSendMessageNoExistPayload() throws java.lang.Exception {
		
	    String urlMessage = "/send/messaging_1.xml";
		String urlPayload = "/send/fileNoExist.pdf";
		
		String messageStartTest = "Init testSendMessageNoExistPayload"; 
		String messageEndTest = "End testSendMessageNoExistPayload";

		org.holodeck.backend.client.BackendServiceStub backendServiceStub = null;
		org.holodeck.backend.client.types.SendRequest sendRequest24 = null;
		org.holodeck.backend.client.types.MessagingE messaging = null;
		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = null; 
		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = null;
		
		DataHandler dataHandler = null;
		URL urlPay = null;

		InputStream inputStream = null;
		
		System.out.println(messageStartTest);

		backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
		sendRequest24 = (org.holodeck.backend.client.types.SendRequest) getTestObject(org.holodeck.backend.client.types.SendRequest.class);
		inputStream = BackendServiceTest.class.getResourceAsStream(urlMessage);
		messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);
		urlPay = BackendServiceTest.class.getResource(urlPayload);
		Assert.assertNull(urlPay);

		System.out.println(messageEndTest);
		
	}

	/**
	 * Test send a PDF file
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSendPDFMessage() throws java.lang.Exception {
		String messageStartTest = "Init testSendPDFMessage";
		String messageEndTest = "End testSendPDFMessage";
		String urlMessage = "/send/messaging_1.xml";
		String urlPayload = "/send/payloadPDFNormal.pdf";
		org.holodeck.backend.client.BackendServiceStub backendServiceStub = null;
		org.holodeck.backend.client.types.SendRequest sendRequest24 = null;
		org.holodeck.backend.client.types.MessagingE messaging = null;
		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = null;
		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = null;
		InputStream inputStream = null;

		System.out.println(messageStartTest);
		
		backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
		sendRequest24 = (org.holodeck.backend.client.types.SendRequest) getTestObject(org.holodeck.backend.client.types.SendRequest.class);

		inputStream = BackendServiceTest.class.getResourceAsStream(urlMessage);
		System.out.println(inputStream.toString());
		Assert.assertNotNull(inputStream);
		
		messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);
		Assert.assertNotNull(messaging);
		
		sendRequest24.setPayload(new DataHandler[]{new DataHandler(BackendServiceTest.class.getResource(urlPayload))});
		backendServiceStub.sendMessage(sendRequest24, messaging);

		listPendingMessagesRequest30 = (org.holodeck.backend.client.types.ListPendingMessagesRequest) getTestObject(org.holodeck.backend.client.types.ListPendingMessagesRequest.class);
		Assert.assertNotNull(listPendingMessagesRequest30);
		
		listPendingMessagesResponse = backendServiceStub.listPendingMessages(listPendingMessagesRequest30);
		
		Assert.assertNotNull(listPendingMessagesResponse);
		Assert.assertNotNull(listPendingMessagesResponse.getMessageID());
		Assert.assertEquals(1, listPendingMessagesResponse.getMessageID().length);
	
		System.out.println(messageEndTest);
	}
	
	
	/**
	 * Test send a XML file
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSendXMLMessage() throws java.lang.Exception {
		String messageStartTest = "Init testSendXMLMessage";
		String messageEndTest = "End testSendXMLMessage";
		String urlMessage = "/send/messaging_1.xml";
		String urlPayload = "/send/payloadXML.xml";
		org.holodeck.backend.client.BackendServiceStub backendServiceStub = null;
		org.holodeck.backend.client.types.SendRequest sendRequest24 = null;
		org.holodeck.backend.client.types.MessagingE messaging = null;
		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = null;
		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = null;
		InputStream inputStream = null;

		System.out.println(messageStartTest);
		
		backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
		sendRequest24 = (org.holodeck.backend.client.types.SendRequest) getTestObject(org.holodeck.backend.client.types.SendRequest.class);

		inputStream = BackendServiceTest.class.getResourceAsStream(urlMessage);
		System.out.println(inputStream.toString());
		Assert.assertNotNull(inputStream);
		
		messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);
		Assert.assertNotNull(messaging);
		
		sendRequest24.setPayload(new DataHandler[]{new DataHandler(BackendServiceTest.class.getResource(urlPayload))});
		backendServiceStub.sendMessage(sendRequest24, messaging);

		listPendingMessagesRequest30 = (org.holodeck.backend.client.types.ListPendingMessagesRequest) getTestObject(org.holodeck.backend.client.types.ListPendingMessagesRequest.class);
		Assert.assertNotNull(listPendingMessagesRequest30);
		
		listPendingMessagesResponse = backendServiceStub.listPendingMessages(listPendingMessagesRequest30);
		
		Assert.assertNotNull(listPendingMessagesResponse);
		Assert.assertNotNull(listPendingMessagesResponse.getMessageID());
		Assert.assertEquals(1, listPendingMessagesResponse.getMessageID().length);
	
		System.out.println(messageEndTest);
	}
	
	/**
	 * Test send a TXT file
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSendTXTMessage() throws java.lang.Exception {
		String messageStartTest = "Init testSendTXTMessage";
		String messageEndTest = "End testSendTXTMessage";
		String urlMessage = "/send/messaging_1.xml";
		String urlPayload = "/send/payloadTXT.txt";
		org.holodeck.backend.client.BackendServiceStub backendServiceStub = null;
		org.holodeck.backend.client.types.SendRequest sendRequest24 = null;
		org.holodeck.backend.client.types.MessagingE messaging = null;
		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = null;
		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = null;
		InputStream inputStream = null;

		System.out.println(messageStartTest);
		
		backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
		sendRequest24 = (org.holodeck.backend.client.types.SendRequest) getTestObject(org.holodeck.backend.client.types.SendRequest.class);

		inputStream = BackendServiceTest.class.getResourceAsStream(urlMessage);
		System.out.println(inputStream.toString());
		Assert.assertNotNull(inputStream);
		
		messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);
		Assert.assertNotNull(messaging);
		
		sendRequest24.setPayload(new DataHandler[]{new DataHandler(BackendServiceTest.class.getResource(urlPayload))});
		backendServiceStub.sendMessage(sendRequest24, messaging);

		listPendingMessagesRequest30 = (org.holodeck.backend.client.types.ListPendingMessagesRequest) getTestObject(org.holodeck.backend.client.types.ListPendingMessagesRequest.class);
		Assert.assertNotNull(listPendingMessagesRequest30);
		
		listPendingMessagesResponse = backendServiceStub.listPendingMessages(listPendingMessagesRequest30);
		
		Assert.assertNotNull(listPendingMessagesResponse);
		Assert.assertNotNull(listPendingMessagesResponse.getMessageID());
		Assert.assertEquals(1, listPendingMessagesResponse.getMessageID().length);
	
		System.out.println(messageEndTest);
	}

	/**
	 * Test send a large PDF file (29MB).
	 * This test throws an exception because Holodeck can not manage an attachment so large. 
	 *
	 * @throws Exception the exception
	 */
	@Ignore
	@Test (expected = java.lang.Error.class)
	public void testSend30MBPDFMessage() throws java.lang.Throwable {
		String messageStartTest = "Init testSend30MBPDFMessage";
		String messageEndTest = "End testSend30MBPDFMessage";
		String urlMessage = "/send/messaging_1.xml";
		String urlPayload = "/send/PDF30MB.pdf";
		org.holodeck.backend.client.BackendServiceStub backendServiceStub = null;
		org.holodeck.backend.client.types.SendRequest sendRequest24 = null;
		org.holodeck.backend.client.types.MessagingE messaging = null;
		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = null;
		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = null;
		InputStream inputStream = null;

		System.out.println(messageStartTest);
		
//		try
//		{
			backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
			sendRequest24 = (org.holodeck.backend.client.types.SendRequest) getTestObject(org.holodeck.backend.client.types.SendRequest.class);
			inputStream = BackendServiceTest.class.getResourceAsStream(urlMessage);
			System.out.println(inputStream.toString());
			Assert.assertNotNull(inputStream);
			messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);
			Assert.assertNotNull(messaging);
			sendRequest24.setPayload(new DataHandler[]{new DataHandler(BackendServiceTest.class.getResource(urlPayload))});
			backendServiceStub.sendMessage(sendRequest24, messaging);
			
			listPendingMessagesRequest30 = (org.holodeck.backend.client.types.ListPendingMessagesRequest) getTestObject(org.holodeck.backend.client.types.ListPendingMessagesRequest.class);
			Assert.assertNotNull(listPendingMessagesRequest30);
			listPendingMessagesResponse = backendServiceStub.listPendingMessages(listPendingMessagesRequest30);
			Assert.assertNotNull(listPendingMessagesResponse);
			Assert.assertNotNull(listPendingMessagesResponse.getMessageID());
			Assert.assertEquals(1, listPendingMessagesResponse.getMessageID().length);
			fail("There has been an error. The maximum amount of files that can be sent has been exceeded.");
//		}
//		catch(java.lang.Error e)
//		{
//			System.out.println("dentro de la excepcion" + e.toString());
//		}
		System.out.println(messageEndTest);
	}
	
	@Test
	public void stopServer(){
		if(container != null){
			container.stop();
		}
	}
	
	/**
	 * Test that sends nineteen messages with a PDF attachment file (46KB) for monitoring the performance of Holodeck.
	 * This is the maximum amount of files that Holodeck can manage.
	 * This test has to be executed on its own. This is because other tests could interfere in the performance of 
	 * Holodeck, modifying the results.
	 * 
	 * @throws Exception the exception
	 * 
	 */
	@Ignore
	@Test
	public void testSendNineteenSmallPDFMessages() throws java.lang.Exception {
		String messageStartTest = "Init testSendNineteenSmallPDFMessages";
		String messageEndTest = "End testSendNineteenSmallPDFMessages";
		String urlMessage = "/send/messaging_1.xml";
		String urlPayload = "/send/payloadPDFSmall.pdf";
		org.holodeck.backend.client.BackendServiceStub backendServiceStub = null;
		org.holodeck.backend.client.types.SendRequest sendRequest24 = null;
		org.holodeck.backend.client.types.MessagingE messaging = null;
		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = null;
		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = null;
		org.holodeck.backend.client.BackendServiceStub stub = null;
		org.holodeck.backend.client.types.DownloadMessageRequest downloadMessageRequest32 = null;
		org.holodeck.backend.client.types.DownloadMessageResponse downloadMessageResponse = null;
		byte[] originalData = null;
		byte[] receivedData = null;
		int i = 0;
		int max = 19;
		InputStream inputStream = null;

		System.out.println(messageStartTest);
		
		for (i = 0; i < max; i ++)
		{
			backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
			sendRequest24 = (org.holodeck.backend.client.types.SendRequest) getTestObject(org.holodeck.backend.client.types.SendRequest.class);
	
			inputStream = BackendServiceTest.class.getResourceAsStream(urlMessage);
			System.out.println(inputStream.toString());
			Assert.assertNotNull(inputStream);
			
			messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);
			Assert.assertNotNull(messaging);
			
			sendRequest24.setPayload(new DataHandler[]{new DataHandler(BackendServiceTest.class.getResource(urlPayload))});
			backendServiceStub.sendMessage(sendRequest24, messaging);
		}
		
		listPendingMessagesRequest30 = (org.holodeck.backend.client.types.ListPendingMessagesRequest) getTestObject(org.holodeck.backend.client.types.ListPendingMessagesRequest.class);
		Assert.assertNotNull(listPendingMessagesRequest30);
		
		listPendingMessagesResponse = backendServiceStub.listPendingMessages(listPendingMessagesRequest30);
		
		Assert.assertNotNull(listPendingMessagesResponse);
		Assert.assertNotNull(listPendingMessagesResponse.getMessageID());
		Assert.assertEquals(max, listPendingMessagesResponse.getMessageID().length);
		
		
		stub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
		downloadMessageRequest32 = (org.holodeck.backend.client.types.DownloadMessageRequest) getTestObject(org.holodeck.backend.client.types.DownloadMessageRequest.class);
		
		for (i = 0; i < max; i ++)
		{
			//System.out.println("Download message: " + listPendingMessagesResponse.getMessageID()[i]);
			downloadMessageRequest32.setMessageID(listPendingMessagesResponse.getMessageID()[i]);
			downloadMessageResponse = stub.downloadMessage(downloadMessageRequest32);	
			Assert.assertNotNull(downloadMessageResponse);	
			Assert.assertNotNull(downloadMessageResponse.getPayload());
			Assert.assertNotNull(downloadMessageResponse.getPayload()[0]);
			
			originalData = IOUtils.toByteArray(BackendServiceTest.class.getResourceAsStream(urlPayload)) ;
			receivedData = IOUtils.toByteArray(downloadMessageResponse.getPayload()[0].getInputStream()) ;
			
			Assert.assertTrue(Arrays.areEqual(originalData, receivedData));
		}

		System.out.println(messageEndTest);
	}
	
	/**
	 * Test that sends nineteen messages with a PDF attachment file (3MB) for monitoring the performance of Holodeck.
	 * This is the maximum amount of files that Holodeck can manage.
	 * This test has to be executed on its own. This is because other tests could interfere in the performance of 
	 * Holodeck, modifying the results.
	 * 
	 * @throws Exception the exception
	 */
	@Ignore
	@Test
	public void testSendNineteenNormalPDFMessages() throws java.lang.Exception {
		String messageStartTest = "Init testSendNineteenNormalPDFMessages";
		String messageEndTest = "End testSendNineteenNormalPDFMessages";
		String urlMessage = "/send/messaging_1.xml";
		String urlPayload = "/send/payloadPDFNormal.pdf";
		org.holodeck.backend.client.BackendServiceStub backendServiceStub = null;
		org.holodeck.backend.client.types.SendRequest sendRequest24 = null;
		org.holodeck.backend.client.types.MessagingE messaging = null;
		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = null;
		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = null;
		org.holodeck.backend.client.BackendServiceStub stub = null;
		org.holodeck.backend.client.types.DownloadMessageRequest downloadMessageRequest32 = null;
		org.holodeck.backend.client.types.DownloadMessageResponse downloadMessageResponse = null;
		byte[] originalData = null;
		byte[] receivedData = null;
		int i = 0;
		int max = 19;
		InputStream inputStream = null;

		System.out.println(messageStartTest);
		
		for (i = 0; i < max; i ++)
		{
			backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
			sendRequest24 = (org.holodeck.backend.client.types.SendRequest) getTestObject(org.holodeck.backend.client.types.SendRequest.class);
	
			inputStream = BackendServiceTest.class.getResourceAsStream(urlMessage);
			System.out.println(inputStream.toString());
			Assert.assertNotNull(inputStream);
			
			messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);
			Assert.assertNotNull(messaging);
			
			sendRequest24.setPayload(new DataHandler[]{new DataHandler(BackendServiceTest.class.getResource(urlPayload))});
			backendServiceStub.sendMessage(sendRequest24, messaging);
		}
		
		listPendingMessagesRequest30 = (org.holodeck.backend.client.types.ListPendingMessagesRequest) getTestObject(org.holodeck.backend.client.types.ListPendingMessagesRequest.class);
		Assert.assertNotNull(listPendingMessagesRequest30);
		
		listPendingMessagesResponse = backendServiceStub.listPendingMessages(listPendingMessagesRequest30);
		
		Assert.assertNotNull(listPendingMessagesResponse);
		Assert.assertNotNull(listPendingMessagesResponse.getMessageID());
		Assert.assertEquals(max, listPendingMessagesResponse.getMessageID().length);
		
		
		stub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
		downloadMessageRequest32 = (org.holodeck.backend.client.types.DownloadMessageRequest) getTestObject(org.holodeck.backend.client.types.DownloadMessageRequest.class);
		
		for (i = 0; i < max; i ++)
		{
			//System.out.println("Download message: " + listPendingMessagesResponse.getMessageID()[i]);
			downloadMessageRequest32.setMessageID(listPendingMessagesResponse.getMessageID()[i]);
			downloadMessageResponse = stub.downloadMessage(downloadMessageRequest32);	
			Assert.assertNotNull(downloadMessageResponse);	
			Assert.assertNotNull(downloadMessageResponse.getPayload());
			Assert.assertNotNull(downloadMessageResponse.getPayload()[0]);
			
			originalData = IOUtils.toByteArray(BackendServiceTest.class.getResourceAsStream(urlPayload)) ;
			receivedData = IOUtils.toByteArray(downloadMessageResponse.getPayload()[0].getInputStream()) ;
			
			Assert.assertTrue(Arrays.areEqual(originalData, receivedData));
		}

		System.out.println(messageEndTest);
	}
	
	
	/**
	 * Test that sends nineteen messages with a PDF attachment file (15MB) for monitoring the performance of Holodeck.
	 * This is the maximum amount of files that Holodeck can manage.
	 * This test has to be executed on its own. This is because other tests could interfere in the performance of 
	 * Holodeck, modifying the results.
	 * 
	 * @throws Exception the exception
	 */
	@Ignore
	@Test
	public void testSendNineteenLargePDFMessages() throws java.lang.Exception {
		String messageStartTest = "Init testSendNineteenLargePDFMessages";
		String messageEndTest = "End testSendNineteenLargePDFMessages";
		String urlMessage = "/send/messaging_1.xml";
		String urlPayload = "/send/payloadPDFLarge.pdf";
		org.holodeck.backend.client.BackendServiceStub backendServiceStub = null;
		org.holodeck.backend.client.types.SendRequest sendRequest24 = null;
		org.holodeck.backend.client.types.MessagingE messaging = null;
		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = null;
		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = null;
		org.holodeck.backend.client.BackendServiceStub stub = null;
		org.holodeck.backend.client.types.DownloadMessageRequest downloadMessageRequest32 = null;
		org.holodeck.backend.client.types.DownloadMessageResponse downloadMessageResponse = null;
		byte[] originalData = null;
		byte[] receivedData = null;
		int i = 0;
		int max = 19;
		InputStream inputStream = null;

		System.out.println(messageStartTest);
		
		for (i = 0; i < max; i ++)
		{
			backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
			sendRequest24 = (org.holodeck.backend.client.types.SendRequest) getTestObject(org.holodeck.backend.client.types.SendRequest.class);
	
			inputStream = BackendServiceTest.class.getResourceAsStream(urlMessage);
			System.out.println(inputStream.toString());
			Assert.assertNotNull(inputStream);
			
			messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);
			Assert.assertNotNull(messaging);
			
			sendRequest24.setPayload(new DataHandler[]{new DataHandler(BackendServiceTest.class.getResource(urlPayload))});
			backendServiceStub.sendMessage(sendRequest24, messaging);
		}
		
		listPendingMessagesRequest30 = (org.holodeck.backend.client.types.ListPendingMessagesRequest) getTestObject(org.holodeck.backend.client.types.ListPendingMessagesRequest.class);
		Assert.assertNotNull(listPendingMessagesRequest30);
		
		listPendingMessagesResponse = backendServiceStub.listPendingMessages(listPendingMessagesRequest30);
		
		Assert.assertNotNull(listPendingMessagesResponse);
		Assert.assertNotNull(listPendingMessagesResponse.getMessageID());
		Assert.assertEquals(max, listPendingMessagesResponse.getMessageID().length);
		
		
		stub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
		downloadMessageRequest32 = (org.holodeck.backend.client.types.DownloadMessageRequest) getTestObject(org.holodeck.backend.client.types.DownloadMessageRequest.class);
		
		for (i = 0; i < max; i ++)
		{
			//System.out.println("Download message: " + listPendingMessagesResponse.getMessageID()[i]);
			downloadMessageRequest32.setMessageID(listPendingMessagesResponse.getMessageID()[i]);
			downloadMessageResponse = stub.downloadMessage(downloadMessageRequest32);	
			Assert.assertNotNull(downloadMessageResponse);	
			Assert.assertNotNull(downloadMessageResponse.getPayload());
			Assert.assertNotNull(downloadMessageResponse.getPayload()[0]);
			
			originalData = IOUtils.toByteArray(BackendServiceTest.class.getResourceAsStream(urlPayload)) ;
			receivedData = IOUtils.toByteArray(downloadMessageResponse.getPayload()[0].getInputStream()) ;
			
			Assert.assertTrue(Arrays.areEqual(originalData, receivedData));
		}

		System.out.println(messageEndTest);
	}
	
	/**
	 * Test that sends nineteen messages with a XML attachment file (6KB) for monitoring the performance of Holodeck.
	 * This is the maximum amount of files that Holodeck can manage.
	 * This test has to be executed on its own. This is because other tests could interfere in the performance of 
	 * Holodeck, modifying the results.
	 *
	 * @throws Exception the exception
	 */
	@Ignore
	@Test
	public void testSendNineteenXMLMessages() throws java.lang.Exception {
		String messageStartTest = "Init testSendNineteenXMLMessages";
		String messageEndTest = "End testSendNineteenXMLMessages";
		String urlMessage = "/send/messaging_1.xml";
		String urlPayload = "/send/payloadXML.xml";
		org.holodeck.backend.client.BackendServiceStub backendServiceStub = null;
		org.holodeck.backend.client.types.SendRequest sendRequest24 = null;
		org.holodeck.backend.client.types.MessagingE messaging = null;
		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = null;
		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = null;
		org.holodeck.backend.client.BackendServiceStub stub = null;
		org.holodeck.backend.client.types.DownloadMessageRequest downloadMessageRequest32 = null;
		org.holodeck.backend.client.types.DownloadMessageResponse downloadMessageResponse = null;
		byte[] originalData = null;
		byte[] receivedData = null;
		int i = 0;
		int max = 19;
		InputStream inputStream = null;

		System.out.println(messageStartTest);
		
		for (i = 0; i < max; i ++)
		{
			backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
			sendRequest24 = (org.holodeck.backend.client.types.SendRequest) getTestObject(org.holodeck.backend.client.types.SendRequest.class);
	
			inputStream = BackendServiceTest.class.getResourceAsStream(urlMessage);
			System.out.println(inputStream.toString());
			Assert.assertNotNull(inputStream);
			
			messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);
			Assert.assertNotNull(messaging);
			
			sendRequest24.setPayload(new DataHandler[]{new DataHandler(BackendServiceTest.class.getResource(urlPayload))});
			backendServiceStub.sendMessage(sendRequest24, messaging);
		}
		
		listPendingMessagesRequest30 = (org.holodeck.backend.client.types.ListPendingMessagesRequest) getTestObject(org.holodeck.backend.client.types.ListPendingMessagesRequest.class);
		Assert.assertNotNull(listPendingMessagesRequest30);
		
		listPendingMessagesResponse = backendServiceStub.listPendingMessages(listPendingMessagesRequest30);
		
		Assert.assertNotNull(listPendingMessagesResponse);
		Assert.assertNotNull(listPendingMessagesResponse.getMessageID());
		Assert.assertEquals(max, listPendingMessagesResponse.getMessageID().length);
		
		
		stub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
		downloadMessageRequest32 = (org.holodeck.backend.client.types.DownloadMessageRequest) getTestObject(org.holodeck.backend.client.types.DownloadMessageRequest.class);
		
		for (i = 0; i < max; i ++)
		{
			//System.out.println("Download message: " + listPendingMessagesResponse.getMessageID()[i]);
			downloadMessageRequest32.setMessageID(listPendingMessagesResponse.getMessageID()[i]);
			downloadMessageResponse = stub.downloadMessage(downloadMessageRequest32);	
			Assert.assertNotNull(downloadMessageResponse);	
			Assert.assertNotNull(downloadMessageResponse.getPayload());
			Assert.assertNotNull(downloadMessageResponse.getPayload()[0]);
			
			originalData = IOUtils.toByteArray(BackendServiceTest.class.getResourceAsStream(urlPayload)) ;
			receivedData = IOUtils.toByteArray(downloadMessageResponse.getPayload()[0].getInputStream()) ;
			
			Assert.assertTrue(Arrays.areEqual(originalData, receivedData));
		}

		System.out.println(messageEndTest);
	}
	
	/**
	 * Test that sends nineteen messages with a TXT attachment file (3KB) for monitoring the performance of Holodeck.
	 * This is the maximum amount of files that Holodeck can manage.
	 * This test has to be executed on its own. This is because other tests could interfere in the performance of 
	 * Holodeck, modifying the results.
	 *
	 * @throws Exception the exception
	 */
	@Ignore
	@Test
	public void testSendNineteenTXTMessages() throws java.lang.Exception {
		String messageStartTest = "Init testSendNineteenTXTMessages";
		String messageEndTest = "End testSendNineteenTXTMessages";
		String urlMessage = "/send/messaging_1.xml";
		String urlPayload = "/send/payloadTXT.txt";
		org.holodeck.backend.client.BackendServiceStub backendServiceStub = null;
		org.holodeck.backend.client.types.SendRequest sendRequest24 = null;
		org.holodeck.backend.client.types.MessagingE messaging = null;
		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = null;
		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = null;
		org.holodeck.backend.client.BackendServiceStub stub = null;
		org.holodeck.backend.client.types.DownloadMessageRequest downloadMessageRequest32 = null;
		org.holodeck.backend.client.types.DownloadMessageResponse downloadMessageResponse = null;
		byte[] originalData = null;
		byte[] receivedData = null;
		int i = 0;
		int max = 19;
		InputStream inputStream = null;

		System.out.println(messageStartTest);
		
		for (i = 0; i < max; i ++)
		{
			backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
			sendRequest24 = (org.holodeck.backend.client.types.SendRequest) getTestObject(org.holodeck.backend.client.types.SendRequest.class);
	
			inputStream = BackendServiceTest.class.getResourceAsStream(urlMessage);
			System.out.println(inputStream.toString());
			Assert.assertNotNull(inputStream);
			
			messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);
			Assert.assertNotNull(messaging);
			
			sendRequest24.setPayload(new DataHandler[]{new DataHandler(BackendServiceTest.class.getResource(urlPayload))});
			backendServiceStub.sendMessage(sendRequest24, messaging);
		}
		
		listPendingMessagesRequest30 = (org.holodeck.backend.client.types.ListPendingMessagesRequest) getTestObject(org.holodeck.backend.client.types.ListPendingMessagesRequest.class);
		Assert.assertNotNull(listPendingMessagesRequest30);
		
		listPendingMessagesResponse = backendServiceStub.listPendingMessages(listPendingMessagesRequest30);
		
		Assert.assertNotNull(listPendingMessagesResponse);
		Assert.assertNotNull(listPendingMessagesResponse.getMessageID());
		Assert.assertEquals(max, listPendingMessagesResponse.getMessageID().length);
		
		
		stub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
		downloadMessageRequest32 = (org.holodeck.backend.client.types.DownloadMessageRequest) getTestObject(org.holodeck.backend.client.types.DownloadMessageRequest.class);
		
		for (i = 0; i < max; i ++)
		{
			//System.out.println("Download message: " + listPendingMessagesResponse.getMessageID()[i]);
			downloadMessageRequest32.setMessageID(listPendingMessagesResponse.getMessageID()[i]);
			downloadMessageResponse = stub.downloadMessage(downloadMessageRequest32);	
			Assert.assertNotNull(downloadMessageResponse);	
			Assert.assertNotNull(downloadMessageResponse.getPayload());
			Assert.assertNotNull(downloadMessageResponse.getPayload()[0]);
			
			originalData = IOUtils.toByteArray(BackendServiceTest.class.getResourceAsStream(urlPayload)) ;
			receivedData = IOUtils.toByteArray(downloadMessageResponse.getPayload()[0].getInputStream()) ;
			
			Assert.assertTrue(Arrays.areEqual(originalData, receivedData));
		}

		System.out.println(messageEndTest);
	}
	
	/**
	 * Test that sends nineteen messages with a PNG attachment file (30KB) for monitoring the performance of Holodeck.
	 * This is the maximum amount of files that Holodeck can manage.
	 * This test has to be executed on its own. This is because other tests could interfere in the performance of 
	 * Holodeck, modifying the results.
	 *
	 * @throws Exception the exception
	 */
	@Ignore
	@Test
	public void testSendNineteenPNGMessages() throws java.lang.Exception {
		String messageStartTest = "Init testSendNineteenPNGMessages";
		String messageEndTest = "End testSendNineteenPNGMessages";
		String urlMessage = "/send/messaging_1.xml";
		String urlPayload = "/send/jira_logo_small.png";
		org.holodeck.backend.client.BackendServiceStub backendServiceStub = null;
		org.holodeck.backend.client.types.SendRequest sendRequest24 = null;
		org.holodeck.backend.client.types.MessagingE messaging = null;
		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = null;
		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = null;
		org.holodeck.backend.client.BackendServiceStub stub = null;
		org.holodeck.backend.client.types.DownloadMessageRequest downloadMessageRequest32 = null;
		org.holodeck.backend.client.types.DownloadMessageResponse downloadMessageResponse = null;
		byte[] originalData = null;
		byte[] receivedData = null;
		int i = 0;
		int max = 19;
		InputStream inputStream = null;
		System.out.println(messageStartTest);
		
		for (i = 0; i < max; i ++)
		{
			backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
			sendRequest24 = (org.holodeck.backend.client.types.SendRequest) getTestObject(org.holodeck.backend.client.types.SendRequest.class);
	
			inputStream = BackendServiceTest.class.getResourceAsStream(urlMessage);
			System.out.println(inputStream.toString());
			Assert.assertNotNull(inputStream);
			
			messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);
			Assert.assertNotNull(messaging);
			
			sendRequest24.setPayload(new DataHandler[]{new DataHandler(BackendServiceTest.class.getResource(urlPayload))});
			backendServiceStub.sendMessage(sendRequest24, messaging);
		}
		
		listPendingMessagesRequest30 = (org.holodeck.backend.client.types.ListPendingMessagesRequest) getTestObject(org.holodeck.backend.client.types.ListPendingMessagesRequest.class);
		Assert.assertNotNull(listPendingMessagesRequest30);
		
		listPendingMessagesResponse = backendServiceStub.listPendingMessages(listPendingMessagesRequest30);
		
		Assert.assertNotNull(listPendingMessagesResponse);
		Assert.assertNotNull(listPendingMessagesResponse.getMessageID());
		Assert.assertEquals(max, listPendingMessagesResponse.getMessageID().length);
		
		
		stub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
		downloadMessageRequest32 = (org.holodeck.backend.client.types.DownloadMessageRequest) getTestObject(org.holodeck.backend.client.types.DownloadMessageRequest.class);
		
		for (i = 0; i < max; i ++)
		{
			//System.out.println("Download message: " + listPendingMessagesResponse.getMessageID()[i]);
			downloadMessageRequest32.setMessageID(listPendingMessagesResponse.getMessageID()[i]);
			downloadMessageResponse = stub.downloadMessage(downloadMessageRequest32);	
			Assert.assertNotNull(downloadMessageResponse);	
			Assert.assertNotNull(downloadMessageResponse.getPayload());
			Assert.assertNotNull(downloadMessageResponse.getPayload()[0]);
			
			originalData = IOUtils.toByteArray(BackendServiceTest.class.getResourceAsStream(urlPayload)) ;
			receivedData = IOUtils.toByteArray(downloadMessageResponse.getPayload()[0].getInputStream()) ;
			
			Assert.assertTrue(Arrays.areEqual(originalData, receivedData));
		}

		System.out.println(messageEndTest);
	}
	
	/**
	 * Test that sends twenty messages with a PDF attachment file (45KB) for monitoring the performance of Holodeck.
	 * This quantity of files exceed the maximum amount that Holodeck can manage, and throws an exception.
	 * This test has to be executed on its own. This is because other tests could interfere in the performance of 
	 * Holodeck, modifying the results.
	 *
	 * @throws Exception the exception
	 */
	@Ignore
	@Test (expected = java.lang.Error.class)
	public void testSendTwentySmallPDFMessages() throws java.lang.Throwable{
		String messageStartTest = "Init testSendTwentySmallPDFMessages";
		String messageEndTest = "End testSendTwentySmallPDFMessages";
		String urlMessage = "/send/messaging_1.xml";
		String urlPayload = "/send/payloadPDFSmall.pdf";
		org.holodeck.backend.client.BackendServiceStub backendServiceStub = null;
		org.holodeck.backend.client.types.SendRequest sendRequest24 = null;
		org.holodeck.backend.client.types.MessagingE messaging = null;
		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = null;
		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = null;
		InputStream inputStream = null;
		int i = 0;
		int max = 20;
			
		System.out.println(messageStartTest);
		
//		try 
//		{
			for (i = 0; i < max; i ++)
			{
				backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
				sendRequest24 = (org.holodeck.backend.client.types.SendRequest) getTestObject(org.holodeck.backend.client.types.SendRequest.class);
		
				inputStream = BackendServiceTest.class.getResourceAsStream(urlMessage);
				System.out.println(inputStream.toString());
				Assert.assertNotNull(inputStream);
				
				messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);
				Assert.assertNotNull(messaging);
				
				sendRequest24.setPayload(new DataHandler[]{new DataHandler(BackendServiceTest.class.getResource(urlPayload))});
				backendServiceStub.sendMessage(sendRequest24, messaging);
			}
            fail("There has been an error. The maximum amount of files that can be sent has been exceeded.");
//	    }
//	    catch(java.lang.Error e) { }

		System.out.println(messageEndTest);
	}
	
	/**
	 * Test that sends twenty messages with a PDF attachment file (3MB) for monitoring the performance of Holodeck.
	 * This quantity of files exceed the maximum amount that Holodeck can manage, and throws an exception.
	 * This test has to be executed on its own. This is because other tests could interfere in the performance of 
	 * Holodeck, modifying the results.
	 * 
	 * @throws Exception the exception
	 */
	@Ignore
	@Test (expected = java.lang.Error.class)
	public void testSendTwentyNormalPDFMessages() throws java.lang.Throwable{
		String messageStartTest = "Init testSendTwentyNormalPDFMessages";
		String messageEndTest = "End testSendTwentyNormalPDFMessages";
		String urlMessage = "/send/messaging_1.xml";
		String urlPayload = "/send/payloadPDFNormal.pdf";
		org.holodeck.backend.client.BackendServiceStub backendServiceStub = null;
		org.holodeck.backend.client.types.SendRequest sendRequest24 = null;
		org.holodeck.backend.client.types.MessagingE messaging = null;
		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = null;
		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = null;
		InputStream inputStream = null;
		int i = 0;
		int max = 20;
			
		System.out.println(messageStartTest);
		
//		try 
//		{
			for (i = 0; i < max; i ++)
			{
				backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
				sendRequest24 = (org.holodeck.backend.client.types.SendRequest) getTestObject(org.holodeck.backend.client.types.SendRequest.class);
		
				inputStream = BackendServiceTest.class.getResourceAsStream(urlMessage);
				System.out.println(inputStream.toString());
				Assert.assertNotNull(inputStream);
				
				messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);
				Assert.assertNotNull(messaging);
				
				sendRequest24.setPayload(new DataHandler[]{new DataHandler(BackendServiceTest.class.getResource(urlPayload))});
				backendServiceStub.sendMessage(sendRequest24, messaging);
			}
            fail("There has been an error. The maximum amount of files that can be sent has been exceeded.");
//	    }
//	    catch(java.lang.Error e) { }

		System.out.println(messageEndTest);
	}
	
	/**
	 * Test that sends twenty messages with a PDF attachment file (14MB) for monitoring the performance of Holodeck.
	 * This quantity of files exceed the maximum amount that Holodeck can manage, and throws an exception.
	 * This test has to be executed on its own. This is because other tests could interfere in the performance of 
	 * Holodeck, modifying the results.
	 *
	 * @throws Exception the exception
	 */
	@Ignore
	@Test (expected = java.lang.Error.class)
	public void testSendTwentyLargePDFMessages() throws java.lang.Throwable{
		String messageStartTest = "Init testSendTwentyLargePDFMessages";
		String messageEndTest = "End testSendTwentyLargePDFMessages";
		String urlMessage = "/send/messaging_1.xml";
		String urlPayload = "/send/payloadPDFLarge.pdf";
		org.holodeck.backend.client.BackendServiceStub backendServiceStub = null;
		org.holodeck.backend.client.types.SendRequest sendRequest24 = null;
		org.holodeck.backend.client.types.MessagingE messaging = null;
		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = null;
		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = null;
		InputStream inputStream = null;
		int i = 0;
		int max = 20;
			
		System.out.println(messageStartTest);
		
//		try 
//		{
			for (i = 0; i < max; i ++)
			{
				backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
				sendRequest24 = (org.holodeck.backend.client.types.SendRequest) getTestObject(org.holodeck.backend.client.types.SendRequest.class);
		
				inputStream = BackendServiceTest.class.getResourceAsStream(urlMessage);
				System.out.println(inputStream.toString());
				Assert.assertNotNull(inputStream);
				
				messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);
				Assert.assertNotNull(messaging);
				
				sendRequest24.setPayload(new DataHandler[]{new DataHandler(BackendServiceTest.class.getResource(urlPayload))});
				backendServiceStub.sendMessage(sendRequest24, messaging);
			}
            fail("There has been an error. The maximum amount of files that can be sent has been exceeded.");
//	    }
//	    catch(java.lang.Error e) { }

		System.out.println(messageEndTest);
	}
	
	/**
	 * Test that sends twenty messages with a XML attachment file (6KB) for monitoring the performance of Holodeck.
	 * This quantity of files exceed the maximum amount that Holodeck can manage, and throws an exception.
	 * This test has to be executed on its own. This is because other tests could interfere in the performance of 
	 * Holodeck, modifying the results.
	 *
	 * @throws Exception the exception
	 */
	@Ignore
	@Test (expected = java.lang.Error.class)
	public void testSendTwentyXMLMessages() throws java.lang.Throwable{
		String messageStartTest = "Init testSendTwentyXMLMessages";
		String messageEndTest = "End testSendTwentyXMLMessages";
		String urlMessage = "/send/messaging_1.xml";
		String urlPayload = "/send/payloadXML.xml";
		org.holodeck.backend.client.BackendServiceStub backendServiceStub = null;
		org.holodeck.backend.client.types.SendRequest sendRequest24 = null;
		org.holodeck.backend.client.types.MessagingE messaging = null;
		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = null;
		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = null;
		InputStream inputStream = null;
		int i = 0;
		int max = 20;
			
		System.out.println(messageStartTest);
		
//		try 
//		{
			for (i = 0; i < max; i ++)
			{
				backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
				sendRequest24 = (org.holodeck.backend.client.types.SendRequest) getTestObject(org.holodeck.backend.client.types.SendRequest.class);
		
				inputStream = BackendServiceTest.class.getResourceAsStream(urlMessage);
				System.out.println(inputStream.toString());
				Assert.assertNotNull(inputStream);
				
				messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);
				Assert.assertNotNull(messaging);
				
				sendRequest24.setPayload(new DataHandler[]{new DataHandler(BackendServiceTest.class.getResource(urlPayload))});
				backendServiceStub.sendMessage(sendRequest24, messaging);
			}
            fail("There has been an error. The maximum amount of files that can be sent has been exceeded.");
//	    }
//	    catch(java.lang.Error e) { }

		System.out.println(messageEndTest);
	}
	
	/**
	 * Test that sends twenty messages with a TXT attachment file (3KB) for monitoring the performance of Holodeck.
	 * This quantity of files exceed the maximum amount that Holodeck can manage, and throws an exception.
	 * This test has to be executed on its own. This is because other tests could interfere in the performance of 
	 * Holodeck, modifying the results.
	 * 
	 * @throws Exception the exception
	 */
	@Ignore
	@Test 
	public void testSendTwentyTXTMessages() throws java.lang.Throwable{
		String messageStartTest = "Init testSendTwentyTXTMessages";
		String messageEndTest = "End testSendTwentyTXTMessages";
		String urlMessage = "/send/messaging_1.xml";
		String urlPayload = "/send/payloadTXT.txt";
		org.holodeck.backend.client.BackendServiceStub backendServiceStub = null;
		org.holodeck.backend.client.types.SendRequest sendRequest24 = null;
		org.holodeck.backend.client.types.MessagingE messaging = null;
		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = null;
		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = null;
		InputStream inputStream = null;
		int i = 0;
		int max = 20;
			
		System.out.println(messageStartTest);
		
//		try 
//		{
			for (i = 0; i < max; i ++)
			{
				backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
				sendRequest24 = (org.holodeck.backend.client.types.SendRequest) getTestObject(org.holodeck.backend.client.types.SendRequest.class);
		
				inputStream = BackendServiceTest.class.getResourceAsStream(urlMessage);
				System.out.println(inputStream.toString());
				Assert.assertNotNull(inputStream);
				
				messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);
				Assert.assertNotNull(messaging);
				
				sendRequest24.setPayload(new DataHandler[]{new DataHandler(BackendServiceTest.class.getResource(urlPayload))});
				backendServiceStub.sendMessage(sendRequest24, messaging);
			}
            fail("There has been an error. The maximum amount of files that can be sent has been exceeded.");
//	    }
//	    catch(java.lang.Error e) { }

		System.out.println(messageEndTest);
	}
	
	/**
	 * Test that sends twenty messages with a PNG attachment file (30KB) for monitoring the performance of Holodeck.
	 * This quantity of files exceed the maximum amount that Holodeck can manage, and throws an exception.
	 * This test has to be executed on its own. This is because other tests could interfere in the performance of 
	 * Holodeck, modifying the results.
	 *
	 * @throws Exception the exception
	 */
	@Ignore
	@Test (expected = java.lang.Error.class)
	public void testSendTwentyPNGMessages() throws java.lang.Throwable{
		String messageStartTest = "Init testSendTwentyPNGMessages";
		String messageEndTest = "End testSendTwentyPNGMessages";
		String urlMessage = "/send/messaging_1.xml";
		String urlPayload = "/send/jira_logo_small.png";
		org.holodeck.backend.client.BackendServiceStub backendServiceStub = null;
		org.holodeck.backend.client.types.SendRequest sendRequest24 = null;
		org.holodeck.backend.client.types.MessagingE messaging = null;
		org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest30 = null;
		org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessagesResponse = null;
		InputStream inputStream = null;
		int i = 0;
		int max = 20;
			
		System.out.println(messageStartTest);
		
//		try 
//		{
			for (i = 0; i < max; i ++)
			{
				backendServiceStub = new org.holodeck.backend.client.BackendServiceStub(BACKEND_WS_URL);//the default implementation should point to the right endpoint
				sendRequest24 = (org.holodeck.backend.client.types.SendRequest) getTestObject(org.holodeck.backend.client.types.SendRequest.class);
		
				inputStream = BackendServiceTest.class.getResourceAsStream(urlMessage);
				System.out.println(inputStream.toString());
				Assert.assertNotNull(inputStream);
				
				messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(inputStream);
				Assert.assertNotNull(messaging);
				
				sendRequest24.setPayload(new DataHandler[]{new DataHandler(BackendServiceTest.class.getResource(urlPayload))});
				backendServiceStub.sendMessage(sendRequest24, messaging);
			}
            fail("There has been an error. The maximum amount of files that can be sent has been exceeded.");
//	    }
//	    catch(java.lang.Error e) { }

		System.out.println(messageEndTest);
	}
}
