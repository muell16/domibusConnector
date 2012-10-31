package eu.ecodex.connector.common;

public class ECodexConnectorProperties {

    String gatewayEndpointAddress;
    String gatewayName;
    String gatewayAsSenderPartyId;
    boolean useContentMapper;
    boolean useSecurityToolkit;
    boolean useEvidencesToolkit;
    String postalAddressStreet;
    String postalAddressLocality;
    String postalAddressPostalCode;
    String postalAddressCountry;

    public String getGatewayEndpointAddress() {
        return gatewayEndpointAddress;
    }

    public void setGatewayEndpointAddress(String gatewayEndpointAddress) {
        this.gatewayEndpointAddress = gatewayEndpointAddress;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public String getGatewayAsSenderPartyId() {
        return gatewayAsSenderPartyId;
    }

    public void setGatewayAsSenderPartyId(String gatewayAsSenderPartyId) {
        this.gatewayAsSenderPartyId = gatewayAsSenderPartyId;
    }

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

    public String getPostalAddressStreet() {
        return postalAddressStreet;
    }

    public void setPostalAddressStreet(String postalAddressStreet) {
        this.postalAddressStreet = postalAddressStreet;
    }

    public String getPostalAddressLocality() {
        return postalAddressLocality;
    }

    public void setPostalAddressLocality(String postalAddressLocality) {
        this.postalAddressLocality = postalAddressLocality;
    }

    public String getPostalAddressPostalCode() {
        return postalAddressPostalCode;
    }

    public void setPostalAddressPostalCode(String postalAddressPostalCode) {
        this.postalAddressPostalCode = postalAddressPostalCode;
    }

    public String getPostalAddressCountry() {
        return postalAddressCountry;
    }

    public void setPostalAddressCountry(String postalAddressCountry) {
        this.postalAddressCountry = postalAddressCountry;
    }

}
