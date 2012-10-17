package org.holodeck.logging.module;

import java.net.URL;

import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.Version;
import org.apache.axis2.modules.Module;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * The class <code>ConstantsTest</code> contains tests for the class <code>{@link Constants}</code>.
 *
 * @generatedBy CodePro at 05.10.12 13:49
 * @author cheny01
 * @version $Revision: 1.0 $
 */
public class ConstantsTest {
	private static final String AxisName = null;
	private static final String nameaxis = null;

	/**
	 * Run the Constants() constructor test.
	 *
	 * @generatedBy CodePro at 05.10.12 13:49
	 */
	@Test
	public void testConstants_1()
		throws Exception {
		Constants result = new Constants();
		assertNotNull(result);
	}

	/**
	 * Run the AxisModule getAxisModule() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.10.12 13:49
	 */
	@Test
	public void testGetAxisModule()
		throws Exception {
		AxisModule m = new AxisModule();
		Constants.setAxisModule(m );
		AxisModule result = Constants.getAxisModule();

		assertNotNull(result);
		assertEquals(null, result.getName());
		assertEquals(null, result.getParent());
		assertEquals(null, result.getFileName());
		assertEquals(null, result.getVersion());
		assertEquals(null, result.getModule());
		assertEquals(null, result.getArchiveName());
		assertEquals(null, result.getFaultInFlow());
		assertEquals(null, result.getFaultOutFlow());
		assertEquals(null, result.getInFlow());
		assertEquals(null, result.getModuleClassLoader());
		assertEquals(null, result.getOutFlow());
		assertEquals(null, result.getModuleDescription());
		assertEquals(null, result.getSupportedPolicyNamespaces());
		assertEquals(null, result.getLocalPolicyAssertions());
	}

	/**
	 * Run the void setAxisModule(AxisModule) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.10.12 13:49
	 */
	@Test
	public void testSetAxisModule()
		throws Exception {
		
		AxisModule m = new AxisModule();
		String archiveName="archivname-1.0";
		m.setArchiveName(archiveName);
		String name="archivname";
		m.setName(name);
		String moduleDescription="moduleDescription";
		m.setModuleDescription(moduleDescription);
		Version version = new Version("1.0");
		m.setVersion(version );
		

		Constants.setAxisModule(m);
		
		assertTrue("Falsche AxisModule-Name gespeichert. ",Constants.getAxisModule().getName().equalsIgnoreCase(name));
		assertTrue("Falsche AxisModule-ArchiveName gespeichert.",Constants.getAxisModule().getArchiveName().equalsIgnoreCase(archiveName));
		assertTrue("Falsche AxisModule-ModuleDescription gespeichert.",Constants.getAxisModule().getModuleDescription().equalsIgnoreCase(moduleDescription));
		assertTrue("Falsche AxisModule-Version gespeichert.",Constants.getAxisModule().getVersion().equals(version));

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
		new org.junit.runner.JUnitCore().run(ConstantsTest.class);
	}
}