package eu.ecodex.connector.gwc;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import backend.ecodex.org.ListPendingMessagesRequest;
import backend.ecodex.org.ListPendingMessagesResponse;
import eu.ecodex.connector.common.ECodexConnectorProperties;
import eu.ecodex.connector.generated.BackendService;
import eu.ecodex.connector.generated.BackendServiceStub;
import eu.ecodex.connector.generated.ListPendingMessagesFault;

public class ECodexConnectorGatewayWebserviceClientImpl implements ECodexConnectorGatewayWebserviceClient {

    org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ECodexConnectorGatewayWebserviceClientImpl.class);

    private final BackendService gatewayBackendWebservice;

    public ECodexConnectorGatewayWebserviceClientImpl(ECodexConnectorProperties connectorProperties) throws AxisFault {
        super();
        gatewayBackendWebservice = new BackendServiceStub(connectorProperties.getGatewayEndpointAddress());
    }

    @Override
    public void sendMessageWithReference(byte[] content) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendMessage(byte[] content) {
        // TODO Auto-generated method stub

    }

    @Override
    public String[] listPendingMessages() {

        ListPendingMessagesRequest request = new ListPendingMessagesRequest();

        try {
            ListPendingMessagesResponse response = gatewayBackendWebservice.listPendingMessages(request);

            return response.getMessageID();
        } catch (RemoteException e) {
            LOGGER.error("Could not execute! ", e);
        } catch (ListPendingMessagesFault e) {
            LOGGER.error("Could not execute! ", e);
        }
        return null;
    }

    @Override
    public byte[] downloadMessage(String messageId) {
        // TODO Auto-generated method stub
        return null;
    }

}
