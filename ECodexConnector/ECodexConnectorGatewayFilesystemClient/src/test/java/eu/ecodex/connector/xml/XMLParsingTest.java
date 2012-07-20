package eu.ecodex.connector.xml;

import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class XMLParsingTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public XMLParsingTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( XMLParsingTest.class );
    }

    /**
     * Rigourous Test :-)
     */   
    public void testApp()
    {
    	List<Map<String, String>> values = AxiomUtil.getNodeWithChildrenValues("D:\\e-Codex\\messages\\msg1\\envelope.xml", "MessageID");
    	
    	assertNotNull(values);
    	System.out.println("Size: " + values.size());
    	for(Map<String, String> mapVals : values) {
    		System.out.println("Size: " + mapVals.size());
    		for(String keys : mapVals.keySet()) {
    			System.out.println(keys + " = " + mapVals.get(keys));
    		}
    	}
    	
        
    }
}
