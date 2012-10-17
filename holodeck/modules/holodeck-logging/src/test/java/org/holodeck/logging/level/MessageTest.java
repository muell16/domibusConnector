package org.holodeck.logging.level;

import org.apache.log4j.Level;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * The class <code>MessageTest</code> contains tests for the class <code>{@link Message}</code>.
 *
 * @generatedBy CodePro at 05.10.12 13:49
 * @author cheny01
 * @version $Revision: 1.0 $
 */
public class MessageTest {
	/**
	 * Run the Message(int,String,int) constructor test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.10.12 13:49
	 */
	@Test
	public void testMessage_1()
		throws Exception {
		int level = 1;
		String levelStr = "MESSAGE";
		int syslogEquivalent = 1;

		Message result = new Message(level, levelStr, syslogEquivalent);

		assertNotNull(result);
		assertEquals("MESSAGE", result.toString());
		assertEquals(1, result.toInt());
		assertEquals(1, result.getSyslogEquivalent());
	}

	/**
	 * Run the Level toLevel(int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.10.12 13:49
	 */
	@Test
	public void testToLevel_1()
		throws Exception {
		int value = 10010;

		Level result = Message.toLevel(value);

		assertNotNull(result);
		assertEquals("MESSAGE", result.toString());
		assertEquals(10010, result.toInt());
		assertEquals(7, result.getSyslogEquivalent());
	}

	/**
	 * Run the Level toLevel(int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.10.12 13:49
	 */
	@Test
	public void testToLevel_2()
		throws Exception {
		int value = 1;

		Level result = Message.toLevel(value);

		assertNotNull(result);
		assertEquals("DEBUG", result.toString());
		assertEquals(10000, result.toInt());
		assertEquals(7, result.getSyslogEquivalent());
	}

	/**
	 * Run the Level toLevel(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.10.12 13:49
	 */
	@Test
	public void testToLevel_3()
		throws Exception {
		String level = "x";

		Level result = Message.toLevel(level);
		assertNotNull(result);
		assertEquals("DEBUG", result.toString());
		assertEquals(10000, result.toInt());
		assertEquals(7, result.getSyslogEquivalent());
	}

	/**
	 * Run the Level toLevel(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.10.12 13:49
	 */
	@Test
	public void testToLevel_4()
		throws Exception {
		String level = null;

		Level result = Message.toLevel(level);

		assertNotNull(result);
		assertEquals("DEBUG", result.toString());
		assertEquals(10000, result.toInt());
		assertEquals(7, result.getSyslogEquivalent());
	}

	/**
	 * Run the Level toLevel(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.10.12 13:49
	 */
	@Test
	public void testToLevel_5()
		throws Exception {
		String level = "MESSAGe";
		Level result = Message.toLevel(level);

		assertNotNull(result);
		assertEquals("MESSAGE", result.toString());
		assertEquals(10010, result.toInt());
		assertEquals(7, result.getSyslogEquivalent());
	}

	/**
	 * Run the Level toLevel(int,Level) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.10.12 13:49
	 */
	@Test
	public void testToLevel_6()
		throws Exception {
		int value = 10010;
		Level defaultLevel = Level.toLevel(1);

		Level result = Message.toLevel(value, defaultLevel);

		assertNotNull(result);
		assertEquals("MESSAGE", result.toString());
		assertEquals(10010, result.toInt());
		assertEquals(7, result.getSyslogEquivalent());
	}

	/**
	 * Run the Level toLevel(int,Level) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.10.12 13:49
	 */
	@Test
	public void testToLevel_7()
		throws Exception {
		int value = 1;
		Level defaultLevel = Level.toLevel(1);

		Level result = Message.toLevel(value, defaultLevel);


		assertNotNull(result);
		assertEquals("DEBUG", result.toString());
		assertEquals(10000, result.toInt());
		assertEquals(7, result.getSyslogEquivalent());
	}

	/**
	 * Run the Level toLevel(String,Level) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.10.12 13:49
	 */
	@Test
	public void testToLevel_8()
		throws Exception {
		String level = "MESSAGe";
		Level defaultLevel = Level.toLevel(1);

		Level result = Message.toLevel(level, defaultLevel);

		assertNotNull(result);
		assertEquals("MESSAGE", result.toString());
		assertEquals(10010, result.toInt());
		assertEquals(7, result.getSyslogEquivalent());
	}

	/**
	 * Run the Level toLevel(String,Level) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.10.12 13:49
	 */
	@Test
	public void testToLevel_9()
		throws Exception {
		String level = null;
		Level defaultLevel = Level.toLevel(1);

		Level result = Message.toLevel(level, defaultLevel);

		assertNotNull(result);
		assertEquals("DEBUG", result.toString());
		assertEquals(10000, result.toInt());
		assertEquals(7, result.getSyslogEquivalent());
	}

	/**
	 * Run the Level toLevel(String,Level) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.10.12 13:49
	 */
	@Test
	public void testToLevel_10()
		throws Exception {
		String level = "";
		Level defaultLevel = Level.toLevel(1);

		Level result = Message.toLevel(level, defaultLevel);

		assertNotNull(result);
		assertEquals("DEBUG", result.toString());
		assertEquals(10000, result.toInt());
		assertEquals(7, result.getSyslogEquivalent());
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 05.10.12 13:49
	 */
	@Before
	public void setUp()
		throws Exception {

	}

	/**
	 * Perform post-test clean-up.
	 *
	 * @throws Exception
	 *         if the clean-up fails for some reason
	 *
	 * @generatedBy CodePro at 05.10.12 13:49
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
	 * @generatedBy CodePro at 05.10.12 13:49
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(MessageTest.class);
	}
}