
package eu.domibus.connector.backend.ws.helper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.xml.stream.XMLStreamException;

import eu.domibus.connector.backend.ws.link.spring.WSBackendLinkConfigurationProperties;
import org.apache.cxf.staxutils.StaxUtils;
import org.apache.cxf.ws.policy.WSPolicyFeature;
//import org.apache.neethi.Policy;
//import org.apache.neethi.PolicyBuilder;
import org.apache.neethi.PolicyReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component
public class WsPolicyLoader {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(WsPolicyLoader.class);

    private WSBackendLinkConfigurationProperties backendLinkConfigurationProperties;

    //setter
    @Autowired
    public void setBackendLinkConfigurationProperties(WSBackendLinkConfigurationProperties backendLinkConfigurationProperties) {
        this.backendLinkConfigurationProperties = backendLinkConfigurationProperties;
    }

    public WSPolicyFeature loadPolicyFeature() {
        WSPolicyFeature policyFeature = new WSPolicyFeature();
        policyFeature.setEnabled(true);

        InputStream is = null;
        try {
            is = backendLinkConfigurationProperties.getWsPolicy().getInputStream();
        } catch (IOException ioe) {
            throw new RuntimeException(String.format("ws policy [%s] cannot be read!", backendLinkConfigurationProperties.getWsPolicy()), ioe);
        }
        if (is == null) {
            throw new RuntimeException(String.format("ws policy [%s] cannot be read! InputStream is nulL!", backendLinkConfigurationProperties.getWsPolicy()));
        }
        List<Element> policyElements = new ArrayList<Element>();
        try {
            Element e = StaxUtils.read(is).getDocumentElement();
            LOGGER.debug("adding policy element [{}]", e);
            policyElements.add(e);
        } catch (XMLStreamException ex) {
            throw new RuntimeException("cannot parse policy: /wsdl/backend.policy.xml");
        }
        policyFeature.getPolicyElements().addAll(policyElements);

        return policyFeature;
    }
    
    
}
