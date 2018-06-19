
package eu.domibus.connector.backend.ws.helper;

import eu.domibus.connector.backend.ws.link.spring.WSBackendLinkConfigurationProperties;
import org.apache.cxf.staxutils.StaxUtils;
import org.apache.cxf.ws.policy.WSPolicyFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component
public class WsPolicyLoader {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WsPolicyLoader.class);

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
            throw new UncheckedIOException(String.format("ws policy [%s] cannot be read!", backendLinkConfigurationProperties.getWsPolicy()), ioe);
        }
        if (is == null) {
            throw new WsPolicyLoaderException(String.format("ws policy [%s] cannot be read! InputStream is nulL!", backendLinkConfigurationProperties.getWsPolicy()));
        }
        List<Element> policyElements = new ArrayList<Element>();
        try {
            Element e = StaxUtils.read(is).getDocumentElement();
            LOGGER.debug("adding policy element [{}]", e);
            policyElements.add(e);
        } catch (XMLStreamException ex) {
            throw new WsPolicyLoaderException("cannot parse policy: /wsdl/backend.policy.xml", ex);
        }
        policyFeature.getPolicyElements().addAll(policyElements);

        return policyFeature;
    }

    
}
