package eu.ecodex.connector.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.impl.builder.StAXOMBuilder;

import eu.ecodex.connector.xml.exception.AxiomBuilderException;

public class AxiomStaxBuilderFactory {

    private static Map<String, StAXOMBuilder> staxBuilderMap = new ConcurrentHashMap<String, StAXOMBuilder>();

    /**
     * The factory method stores the {@link StAXOMBuilder} instance created for each XML file

     * passed in so that we do not need to create unnecessary objects every time.

     * An instance of {@linkplain ConcurrentHashMap} is used so as to make the

     * instances thread safe.
     * 
     * @param xmlFilePath the path of the XML file
     * @return an instance of the {@link StAXOMBuilder} from the cache or newly created
     */
    public static StAXOMBuilder getAxiomBuilderForFile(String xmlFilePath) {
        StAXOMBuilder staxBuilder = null;
        if (staxBuilderMap.containsKey(xmlFilePath)) {
            staxBuilder = staxBuilderMap.get(xmlFilePath);
        } else {
            try {
                staxBuilder = new StAXOMBuilder(new FileInputStream(xmlFilePath));
                staxBuilderMap.put(xmlFilePath, staxBuilder);
            } catch (FileNotFoundException e) {
                throw new AxiomBuilderException(e);
            } catch (XMLStreamException e) {
                throw new AxiomBuilderException(e);
            }
        }

        return staxBuilder;

    }
}

