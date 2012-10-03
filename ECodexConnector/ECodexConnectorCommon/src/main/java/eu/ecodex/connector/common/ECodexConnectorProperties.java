package eu.ecodex.connector.common;

public class ECodexConnectorProperties {

    String gatewayEndpointAddress;
    boolean useContentMapper;
    boolean useSecurityToolkit;
    boolean useEvidencesToolkit;
    String gatewayAsSenderPartyId;

    public boolean isUseContentMapper() {
        return useContentMapper;
    }

    public void setUseContentMapper(boolean useContentMapper) {
        this.useContentMapper = useContentMapper;
    }

    public boolean isUseSecurityToolkit() {
        return useSecurityToolkit;
    }

    public void setUseSecurityToolkit(boolean useSecurityToolkit) {
        this.useSecurityToolkit = useSecurityToolkit;
    }

    public boolean isUseEvidencesToolkit() {
        return useEvidencesToolkit;
    }

    public void setUseEvidencesToolkit(boolean useEvidencesToolkit) {
        this.useEvidencesToolkit = useEvidencesToolkit;
    }

    public String getGatewayEndpointAddress() {
        return gatewayEndpointAddress;
    }

    public void setGatewayEndpointAddress(String gatewayEndpointAddress) {
        this.gatewayEndpointAddress = gatewayEndpointAddress;
    }

    public String getGatewayAsSenderPartyId() {
        return gatewayAsSenderPartyId;
    }

    public void setGatewayAsSenderPartyId(String gatewayAsSenderPartyId) {
        this.gatewayAsSenderPartyId = gatewayAsSenderPartyId;
    }

}
