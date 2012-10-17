package org.holodeck.logging.module;

import java.util.List;

import org.apache.axiom.om.impl.dom.ElementImpl;
import org.apache.axiom.om.impl.dom.factory.OMDOMFactory;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisBinding;
import org.apache.axis2.description.AxisDescription;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.neethi.Assertion;
import org.apache.neethi.Policy;
import org.apache.neethi.builders.xml.XmlPrimtiveAssertion;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * The class <code>LoggingModuleTest</code> contains tests for the class <code>{@link LoggingModule}</code>.
 *
 * @generatedBy CodePro at 05.10.12 13:54
 * @author cheny01
 * @version $Revision: 1.0 $
 */
public class LoggingModuleTest {
	/**
	 * Run the LoggingModule() constructor test.
	 *
	 * @generatedBy CodePro at 05.10.12 13:54
	 */
	@Test
	public void testLoggingModule()
		throws Exception {
		LoggingModule result = new LoggingModule();
		assertNotNull(result);
	}

	/**
	 * Run the void applyPolicy(Policy,AxisDescription) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.10.12 13:54
	 */
	@Test
	public void testApplyPolicy()
		throws Exception {
		LoggingModule fixture = new LoggingModule();
		Policy policy = new Policy();
		AxisDescription axisDescription = new AxisBinding();

		fixture.applyPolicy(policy, axisDescription);
		// add additional test code here
	}

	/**
	 * Run the boolean canSupportAssertion(Assertion) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.10.12 13:54
	 */
	@Test
	public void testCanSupportAssertion()
		throws Exception {
		LoggingModule fixture = new LoggingModule();
		Assertion assertion = new XmlPrimtiveAssertion(new ElementImpl(new OMDOMFactory()));

		boolean result = fixture.canSupportAssertion(assertion);

		assertTrue("Methode CanSupportAssertion liefert Boolean 'false' zurück. ", result);
	}

	/**
	 * Run the void engageNotify(AxisDescription) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.10.12 13:54
	 */
	@Test
	public void testEngageNotify()
		throws Exception {
		LoggingModule fixture = new LoggingModule();
		AxisDescription axisDescription = new AxisBinding();

		fixture.engageNotify(axisDescription);
		// add additional test code here
	}

	/**
	 * Run the String[] getPolicyNamespaces() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.10.12 13:54
	 */
	@Test
	public void testGetPolicyNamespaces()
		throws Exception {
		LoggingModule fixture = new LoggingModule();

		String[] result = fixture.getPolicyNamespaces();

		assertNull("GetPolicyNamespaces liefert kein Null zurück.",result);
	}

	/**
	 * Run the void init(ConfigurationContext,AxisModule) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.10.12 13:54
	 */
	@Test
	public void testInit()
		throws Exception {
		LoggingModule fixture = new LoggingModule();
		
		ConfigurationContext configContext = new ConfigurationContext(new AxisConfiguration());
		AxisModule module = new AxisModule();
		Parameter param= new Parameter();
		param.setName("PersistenceUnit");
		Object value= new String("logging");
		param.setValue(value);
		module.addParameter(param);
		try{
		fixture.init(configContext, module);
		
		assertTrue("store ist nicht mit Name 'logging-mysql' gestartet.",Constants.store.getEntityManagerFactory().isOpen());
		
		DbStore dbs= Constants.store;
		
		String query= "select lo.id from LoggerEvent lo";
		List result = dbs.findAll(query);
		assertNotNull(result.isEmpty());
		}catch(Exception e){
			fail("Die Intialisierung ist noch nicht erfolgreich. Bitte Hibernate Configuration prüfen!");
		}
		}

	
	/**
	 * Run the void shutdown(ConfigurationContext) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.10.12 13:54
	 */
	@Test
	public void testShutdown()
		throws Exception {
		LoggingModule fixture = new LoggingModule();
		ConfigurationContext configCtx = new ConfigurationContext(new AxisConfiguration());

		fixture.shutdown(configCtx);

		// add additional test code here
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 05.10.12 13:54
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
	 * @generatedBy CodePro at 05.10.12 13:54
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
	 * @generatedBy CodePro at 05.10.12 13:54
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(LoggingModuleTest.class);
	}
}