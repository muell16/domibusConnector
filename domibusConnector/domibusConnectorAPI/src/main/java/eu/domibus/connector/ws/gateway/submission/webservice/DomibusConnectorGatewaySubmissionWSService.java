package eu.domibus.connector.ws.gateway.submission.webservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.2.1
 * 2018-02-28T06:51:49.878+01:00
 * Generated source version: 3.2.1
 * 
 */
@WebServiceClient(name = "DomibusConnectorGatewaySubmissionWSService", 
                  wsdlLocation = "file:/C:/Entwicklung/git/connector/domibusConnector/domibusConnectorAPI/src/main/resources/wsdl/DomibusConnectorGatewaySubmissionWebService.wsdl",
                  targetNamespace = "http://connector.domibus.eu/ws/gateway/submission/webservice") 
public class DomibusConnectorGatewaySubmissionWSService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://connector.domibus.eu/ws/gateway/submission/webservice", "DomibusConnectorGatewaySubmissionWSService");
    public final static QName DomibusConnectorGatewaySubmissionWebService = new QName("http://connector.domibus.eu/ws/gateway/submission/webservice", "DomibusConnectorGatewaySubmissionWebService");
    static {
        URL url = null;
        try {
            url = new URL("file:/C:/Entwicklung/git/connector/domibusConnector/domibusConnectorAPI/src/main/resources/wsdl/DomibusConnectorGatewaySubmissionWebService.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(DomibusConnectorGatewaySubmissionWSService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:/C:/Entwicklung/git/connector/domibusConnector/domibusConnectorAPI/src/main/resources/wsdl/DomibusConnectorGatewaySubmissionWebService.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public DomibusConnectorGatewaySubmissionWSService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public DomibusConnectorGatewaySubmissionWSService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public DomibusConnectorGatewaySubmissionWSService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public DomibusConnectorGatewaySubmissionWSService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public DomibusConnectorGatewaySubmissionWSService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public DomibusConnectorGatewaySubmissionWSService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns DomibusConnectorGatewaySubmissionWebService
     */
    @WebEndpoint(name = "DomibusConnectorGatewaySubmissionWebService")
    public DomibusConnectorGatewaySubmissionWebService getDomibusConnectorGatewaySubmissionWebService() {
        return super.getPort(DomibusConnectorGatewaySubmissionWebService, DomibusConnectorGatewaySubmissionWebService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns DomibusConnectorGatewaySubmissionWebService
     */
    @WebEndpoint(name = "DomibusConnectorGatewaySubmissionWebService")
    public DomibusConnectorGatewaySubmissionWebService getDomibusConnectorGatewaySubmissionWebService(WebServiceFeature... features) {
        return super.getPort(DomibusConnectorGatewaySubmissionWebService, DomibusConnectorGatewaySubmissionWebService.class, features);
    }

}
