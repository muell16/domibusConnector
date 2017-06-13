package connector.domibus.eu.domibusconnectorgatewayservice._1_0;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.1.3
 * 2017-06-13T09:53:22.674+02:00
 * Generated source version: 3.1.3
 * 
 */
@WebService(targetNamespace = "http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", name = "DomibusConnectorGatewayServiceInterface")
@XmlSeeAlso({connector.domibus.eu.domibusconnectorgatewayservice._1.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface DomibusConnectorGatewayServiceInterface {

    @WebMethod
    @WebResult(name = "DomibusConnectorMessages", targetNamespace = "http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", partName = "requestPendingMessagesResponse")
    public connector.domibus.eu.domibusconnectorgatewayservice._1.MessagesType requestPendingMessages(
        @WebParam(partName = "requestPendingMessagesRequest", name = "PendingMessagesRequest", targetNamespace = "http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/")
        java.lang.String requestPendingMessagesRequest
    ) throws RequestPendingMessagesFault;

    @WebMethod
    @WebResult(name = "requestMessageStatusResponse", targetNamespace = "", partName = "requestMessageStatusResponse")
    public java.lang.String requestMessageStatus(
        @WebParam(partName = "requestMessageStatusRequest", name = "MessageStatusRequest", targetNamespace = "http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/")
        java.lang.String requestMessageStatusRequest
    );

    @WebMethod
    @WebResult(name = "MessageErrors", targetNamespace = "http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", partName = "requestMessageErrorsResponse")
    public connector.domibus.eu.domibusconnectorgatewayservice._1.MessageErrorLogEntriesType requestMessageErrors(
        @WebParam(partName = "requestMessageErrorsRequest", name = "MessageErrorsRequest", targetNamespace = "http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/")
        java.lang.String requestMessageErrorsRequest
    );

    @WebMethod
    @WebResult(name = "DomibusConnectorAcknowledgement", targetNamespace = "http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", partName = "sendMessageResponse")
    public connector.domibus.eu.domibusconnectorgatewayservice._1.AcknowledgementType sendMessage(
        @WebParam(partName = "sendMessageRequest", name = "DomibusConnectorMessage", targetNamespace = "http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/")
        connector.domibus.eu.domibusconnectorgatewayservice._1.MessageType sendMessageRequest
    ) throws SendMessageFault;
}
