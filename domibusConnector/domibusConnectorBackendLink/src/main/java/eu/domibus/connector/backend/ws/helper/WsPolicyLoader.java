
package eu.domibus.connector.backend.ws.helper;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.xml.stream.XMLStreamException;
import org.apache.cxf.staxutils.StaxUtils;
import org.apache.cxf.ws.policy.WSPolicyFeature;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyBuilder;
import org.apache.neethi.PolicyReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component
public class WsPolicyLoader {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(WsPolicyLoader.class);
    
    public WSPolicyFeature loadPolicyFeature() {
        WSPolicyFeature policyFeature = new WSPolicyFeature();
        policyFeature.setEnabled(true);
        
        //
        InputStream is = getClass().getResourceAsStream("/wsdl/backend.policy.xml");
        if (is == null) {
            throw new RuntimeException("error!");
        }
        //        Policy policy = policyBuilder.getPolicy(is);
        List<Element> policyElements = new ArrayList<Element>();
        try {
            Element e = StaxUtils.read(is).getDocumentElement();
            LOGGER.debug("adding policy element [{}]", e);
            policyElements.add(e);
        } catch (XMLStreamException ex) {
            throw new RuntimeException("cannot parse policy: /wsdl/backend.policy.xml");
        }
        policyFeature.getPolicyElements().addAll(policyElements);
        
        is = getClass().getResourceAsStream("/wsdl/backend.policy.xml");
        PolicyBuilder policyBuilder = new PolicyBuilder();
        Policy policy = policyBuilder.getPolicy(is);
        policyFeature.getPolicies().add(policy);
        

        return policyFeature;
    }
    
    
}
