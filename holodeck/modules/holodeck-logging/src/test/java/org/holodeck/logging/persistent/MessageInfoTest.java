package org.holodeck.logging.persistent;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * The class <code>MessageInfoTest</code> contains tests for the class <code>{@link MessageInfo}</code>.
 *
 * @generatedBy CodePro at 10.10.12 13:07
 * @author cheny01
 * @version $Revision: 1.0 $
 */
public class MessageInfoTest {
	/**
	 * Run the MessageInfo() constructor test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testMessageInfo_1()
		throws Exception {

		MessageInfo result = new MessageInfo();

		// add additional test code here
		assertNotNull(result);
		assertEquals(null, result.getService());
		assertEquals(null, result.getStatus());
		assertEquals(null, result.getAction());
		assertEquals(null, result.getPmode());
		assertEquals(null, result.getMessageId());
		assertEquals(null, result.getSender());
		assertEquals(null, result.getFromRole());
		assertEquals(null, result.getRecipient());
		assertEquals(null, result.getToRole());
		assertEquals(null, result.getConversationId());
	}

	/**
	 * Run the MessageInfo(String,String,String,String,String,String,String,String,String,String) constructor test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testMessageInfo_2()
		throws Exception {
		String messageId = "messageId";
		String sender = "sender";
		String fromRole = "fromRole";
		String recipient = "recipient";
		String toRole = "toRole";
		String service = "service";
		String action = "action";
		String conversationId = "conversationId";
		String pmode = "pmode";
		String status = "status";

		MessageInfo result = new MessageInfo(messageId, sender, fromRole, recipient, toRole, service, action, conversationId, pmode, status);

		// add additional test code here
		assertNotNull(result);
		assertEquals("service", result.getService());
		assertEquals("status", result.getStatus());
		assertEquals("action", result.getAction());
		assertEquals("pmode", result.getPmode());
		assertEquals("messageId", result.getMessageId());
		assertEquals("sender", result.getSender());
		assertEquals("fromRole", result.getFromRole());
		assertEquals("recipient", result.getRecipient());
		assertEquals("toRole", result.getToRole());
		assertEquals("conversationId", result.getConversationId());
	}

	/**
	 * Run the String getAction() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testGetAction()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");

		String result = fixture.getAction();

		// add additional test code here
		assertEquals("action", result);
	}

	/**
	 * Run the String getConversationId() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testGetConversationId()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");

		String result = fixture.getConversationId();

		// add additional test code here
		assertEquals("conversationId", result);
	}

	/**
	 * Run the String getFromRole() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testGetFromRole()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");

		String result = fixture.getFromRole();

		// add additional test code here
		assertEquals("fromRole", result);
	}

	/**
	 * Run the String getMessageId() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testGetMessageId()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");

		String result = fixture.getMessageId();

		// add additional test code here
		assertEquals("messageId", result);
	}

	/**
	 * Run the String getPmode() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testGetPmode()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");

		String result = fixture.getPmode();

		// add additional test code here
		assertEquals("pmode", result);
	}

	/**
	 * Run the String getRecipient() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testGetRecipient()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");

		String result = fixture.getRecipient();

		// add additional test code here
		assertEquals("recipient", result);
	}

	/**
	 * Run the String getSender() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testGetSender()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");

		String result = fixture.getSender();

		// add additional test code here
		assertEquals("sender", result);
	}

	/**
	 * Run the String getService() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testGetService()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");

		String result = fixture.getService();

		// add additional test code here
		assertEquals("service", result);
	}

	/**
	 * Run the String getStatus() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testGetStatus()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");

		String result = fixture.getStatus();

		// add additional test code here
		assertEquals("status", result);
	}

	/**
	 * Run the String getToRole() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testGetToRole()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");

		String result = fixture.getToRole();

		// add additional test code here
		assertEquals("toRole", result);
	}

	/**
	 * Run the void setAction(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testSetAction()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");
        String action = "action";

		fixture.setAction(action);

		// add additional test code here
		assertTrue(fixture.getAction().equalsIgnoreCase(action));
	}

	/**
	 * Run the void setConversationId(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testSetConversationId()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");
		String conversationId = "conversationId";

		fixture.setConversationId(conversationId);
		assertTrue(fixture.getConversationId().equalsIgnoreCase(conversationId));
		// add additional test code here
	}

	/**
	 * Run the void setFromRole(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testSetFromRole()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");
		String fromRole = "fromRole";

		fixture.setFromRole(fromRole);

		// add additional test code here
		assertTrue(fixture.getFromRole().equalsIgnoreCase(fromRole));
	}

	/**
	 * Run the void setMessageId(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testSetMessageId()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");
		String messageId = "messageId";

		fixture.setMessageId(messageId);

		// add additional test code here
		assertTrue(fixture.getMessageId().equalsIgnoreCase(messageId));
	}

	/**
	 * Run the void setPmode(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testSetPmode()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");
		String pmode = "pmode";

		fixture.setPmode(pmode);
		assertTrue(fixture.getPmode().equalsIgnoreCase(pmode));
		// add additional test code here
	}

	/**
	 * Run the void setRecipient(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testSetRecipient()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");
		String recipient = "recipient";

		fixture.setRecipient(recipient);
		assertTrue(fixture.getRecipient().equalsIgnoreCase(recipient));
		// add additional test code here
	}

	/**
	 * Run the void setSender(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testSetSender()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");
		String sender = "sender";

		fixture.setSender(sender);
		assertTrue(fixture.getSender().equalsIgnoreCase(sender));
		// add additional test code here
	}

	/**
	 * Run the void setService(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testSetService()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");
		String service = "service";

		fixture.setService(service);
		assertTrue(fixture.getService().equalsIgnoreCase(service));
		// add additional test code here
	}

	/**
	 * Run the void setStatus(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testSetStatus()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");
		String status = "status";

		fixture.setStatus(status);
		assertTrue(fixture.getStatus().equalsIgnoreCase(status));
		// add additional test code here
	}

	/**
	 * Run the void setToRole(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Test
	public void testSetToRole()
		throws Exception {
		MessageInfo fixture = new MessageInfo("messageId", "sender", "fromRole", "recipient", "toRole", "service", "action", "conversationId", "pmode", "status");
		String toRole = "toRole";

		fixture.setToRole(toRole);
		assertTrue(fixture.getToRole().equalsIgnoreCase(toRole));
		// add additional test code here
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@Before
	public void setUp()
		throws Exception {
		// add additional set up code here
	}

	/**
	 * Perform post-test clean-up.
	 *
	 * @throws Exception
	 *         if the clean-up fails for some reason
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	@After
	public void tearDown()
		throws Exception {
		// Add additional tear down code here
	}

	/**
	 * Launch the test.
	 *
	 * @param args the command line arguments
	 *
	 * @generatedBy CodePro at 10.10.12 13:07
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(MessageInfoTest.class);
	}
}