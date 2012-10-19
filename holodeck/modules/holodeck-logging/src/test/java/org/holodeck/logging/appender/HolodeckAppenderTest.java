package org.holodeck.logging.appender;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.holodeck.logging.module.Constants;
import org.holodeck.logging.module.DbStore;
import org.holodeck.logging.persistent.MessageInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The class <code>HolodeckAppenderTest</code> contains tests for the class
 * <code>{@link HolodeckAppender}</code>.
 * 
 * @generatedBy CodePro at 05.10.12 12:37
 * @author cheny01
 * @version $Revision: 1.0 $
 */
public class HolodeckAppenderTest {

	private DbStore dbs;

	/**
	 * Run the HolodeckAppender() constructor test.
	 * 
	 * @generatedBy CodePro at 05.10.12 12:37
	 */
	@Test
	public void testHolodeckAppender_1() throws Exception {
		HolodeckAppender result = new HolodeckAppender();
		assertNotNull(result);
	}

	/**
	 * Run the void append(LoggingEvent) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 05.10.12 12:37
	 */
	@Test
	public void testAppend_1() throws Exception {
		Logger log = Logger.getLogger(HolodeckAppenderTest.class.getName());
		String testme = "This is a Test for the Logger";
		String sql = "select lo.id from LoggerEvent lo where lo.Msg ='"+testme+"'";
		
		int numBefore = 0;
		int numafter = 0;
		List<?> resultbefore;
		List<?> resultafter;
		
		// Count of data at method call
		resultbefore = dbs.findAll(sql);
		numBefore = resultbefore.size();
		
		// Call method append.
		HolodeckAppender fixture = new HolodeckAppender();
		LoggingEvent event = new LoggingEvent("2", log, Level.DEBUG,
				testme, null);
		assertFalse("leider ist noch kein Messageinfo: ",
				event.getMessage() instanceof MessageInfo);
		fixture.append(event);

		// Count of data after method call
		resultafter = dbs.findAll(sql);
		numafter = resultafter.size();
		assertEquals("Loggerevent does not exists in the Log-DB",
				numBefore + 1, numafter);

		dbs.delete("DELETE FROM LoggerEvent lo where lo.Msg ='"+testme+"'");
	}

	/**
	 * Run the void append(LoggingEvent) method test.: for Loggermessages
	 * 
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 05.10.12 12:37
	 */
	@Test
	public void testAppend_2() throws Exception {
		Logger log = Logger.getLogger(HolodeckAppenderTest.class.getName());
		String mID = "42";
		String sql = "SELECT mes.id FROM LoggerMessage mes WHERE messageId='"+mID+"'";
		int numBefore = 0;
		int numafter = 0;
		List<?> resultbefore;
		List<?> resultafter;
		// Count of data at method call
		resultbefore = dbs.findAll(sql);
		numBefore = resultbefore.size();
		numBefore = resultbefore.size();
		// Call method append.
		HolodeckAppender fixture = new HolodeckAppender();
		MessageInfo newMessage = new MessageInfo(mID, "testsender", "testfromRole", "testrecipient", "testtoRole", "testservice", "testaction", "testconversationId", "testpmode", "teststatus");
		LoggingEvent event = new LoggingEvent("2", log, Level.DEBUG,
				newMessage, null);

		fixture.append(event);
		// Count of data after method call
		resultafter = dbs.findAll(sql);
		numafter = resultafter.size();
		assertEquals("Loggermessage does not exists in the Log-DB",
				numBefore + 1, numafter);
		
		dbs.delete("DELETE FROM LoggerMessage mes WHERE mes.messageId='"+mID+"'");


	}

	/**
	 * Run the void close() method test.
	 * 
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 05.10.12 12:37
	 */
	@Test
	public void testClose() throws Exception {
		HolodeckAppender fixture = new HolodeckAppender();
		fixture.close();
	}

	/**
	 * Run the boolean requiresLayout() method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 05.10.12 12:37
	 */
	@Test
	public void testRequiresLayout() throws Exception {
		HolodeckAppender fixture = new HolodeckAppender();

		boolean result = fixture.requiresLayout();

		assertEquals("ist diese Methode komplete?",false, result);
	}

	/**
	 * Perform pre-test initialization.
	 * Initialized a constructor with parameter.
	 * @throws Exception
	 *             if the initialization fails for some reason
	 * 
	 * @generatedBy CodePro at 05.10.12 12:37
	 */
	@Before
	public void setUp() throws Exception {
		Constants.store = new DbStore("logging");
		dbs = Constants.store;
	}

	/**
	 * Perform post-test clean-up. connection is going to closed.
	 * 
	 * @throws Exception
	 *             if the clean-up fails for some reason
	 * 
	 * @generatedBy CodePro at 05.10.12 12:37
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Launch the test.
	 * 
	 * @param args
	 *            the command line arguments
	 * 
	 * @generatedBy CodePro at 05.10.12 12:37
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(HolodeckAppenderTest.class);
	}
}