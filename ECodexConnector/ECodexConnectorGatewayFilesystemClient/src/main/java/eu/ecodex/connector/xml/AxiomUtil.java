package eu.ecodex.connector.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;

/**
 * The utility class provides abstraction to users so that 

 * the user can just pass in the xml file name and the node he/she

 * wants to access and get the values without having to bother with

 * boilerplate xml handling info.
 * 
 * @author dinuka
 */
public class AxiomUtil {

    /**
     * This method is used if you have for example a node with multiple children

     * Note that this method assumes the node in query is within the root element
     * 
     * @param xmlFilePath the path of the xml file
     * @param nodeName the node name from which you want to retrieve values
     * @return the list containing key value pairs containing the values of the sub elements within

     *         the nodeName passed in.
     */
    public static List<Map<String, String>> getNodeWithChildrenValues(String xmlFilePath, String nodeName) {
        List<Map<String, String>> valueList = new ArrayList<Map<String, String>>();

        StAXOMBuilder staxBuilder = AxiomStaxBuilderFactory.getAxiomBuilderForFile(xmlFilePath);

        OMElement documentElement = staxBuilder.getDocumentElement();
        Iterator nodeElement = documentElement.getChildrenWithName(new QName(nodeName));

        while (nodeElement.hasNext()) {
            OMElement om = (OMElement) nodeElement.next();

            Iterator it = om.getChildElements();
            Map<String, String> valueMap = new HashMap<String, String>();
            while (it.hasNext()) {
                OMElement el = (OMElement) it.next();

                valueMap.put(el.getLocalName(), el.getText());

            }

            valueList.add(valueMap);
        }
        return valueList;
    }

}

