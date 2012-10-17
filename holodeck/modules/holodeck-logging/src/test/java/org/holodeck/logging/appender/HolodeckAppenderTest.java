package org.holodeck.logging.appender;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.holodeck.logging.module.Constants;
import org.holodeck.logging.module.DbStore;
import org.holodeck.logging.persistent.LoggerEvent;
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
		Logger log = Logger.getLogger("TestLogger for loggerevent");
		String sql = "select lo.id from LoggerEvent lo";
		int numBefore = 0;
		int numafter = 0;
		List resultbefore;

		List resultafter;
		resultbefore = dbs.findAll(sql);
		numBefore = resultbefore.size();
		numBefore = resultbefore.size();

		HolodeckAppender fixture = new HolodeckAppender();
		LoggingEvent event = new LoggingEvent("2", log, Priority.DEBUG,
				new Object(), null);
		assertFalse("leider ist noch kein Messageinfo: ",
				event.getMessage() instanceof MessageInfo);
		fixture.append(event);

		resultafter = dbs.findAll(sql);
		numafter = resultafter.size();
		assertEquals("Loggerevent ist nicht in Log-DB gespeichert",
				numBefore + 1, numafter);

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
		Logger log = Logger.getLogger("TestLogger for LoggerMessages");
		String sql = "select mes.id from LoggerMessage mes";
		int numBefore = 0;
		int numafter = 0;
		List resultbefore;
		List resultafter;
		// Anzahl der Datensätze vor Methodeaufruf.
		resultbefore = dbs.findAll(sql);
		numBefore = resultbefore.size();
		numBefore = resultbefore.size();
		// Methode append wird aufgerufen.
		HolodeckAppender fixture = new HolodeckAppender();
		MessageInfo newMessage = new MessageInfo("78", "It.nrw", "admin",
				"it.Kenneydamm", "Mitarbeiter", "sendService", "send", "78",
				"pmode", "SEND");
		LoggingEvent event = new LoggingEvent("2", log, Priority.DEBUG,
				newMessage, null);
		assertTrue("leider ist noch kein Messageinfo: ",
				event.getMessage() instanceof MessageInfo);
		fixture.append(event);
		// Anzahl der Datensätze nach Methodeaufruf.
		resultafter = dbs.findAll(sql);
		numafter = resultafter.size();
		assertEquals("Loggermessage ist nicht in Log-DB gespeichert",
				numBefore + 1, numafter);
		

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