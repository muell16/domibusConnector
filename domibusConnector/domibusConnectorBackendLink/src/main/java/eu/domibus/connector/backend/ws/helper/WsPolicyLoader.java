
package eu.domibus.connector.backend.ws.helper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import org.apache.cxf.staxutils.StaxUtils;
import org.apache.cxf.ws.policy.WSPolicyFeature;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component
public class WsPolicyLoader {
    
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
            policyElements.add(StaxUtils.read(is).getDocumentElement());
        } catch (XMLStreamException ex) {
            throw new RuntimeException("cannot parse policy: /wsdl/backend.policy.xml");
        }
        policyFeature.getPolicyElements().addAll(policyElements);
        
        return policyFeature;
    }
    
    
}
