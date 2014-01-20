package eu.ecodex.connector.common;

public class ECodexConnectorProperties {

    String gatewayEndpointAddress;
    String gatewayName;
    String gatewayRole;
    boolean useContentMapper;
    boolean useSecurityToolkit;
    boolean useEvidencesToolkit;
    String postalAddressStreet;
    String postalAddressLocality;
    String postalAddressPostalCode;
    String postalAddressCountry;
    long timeoutRelayREMMD;
    long timeoutDelivery;
    long timeoutRetrieval;
    boolean checkEvidences;
    boolean useDynamicDiscovery;
    String dynamicDiscoveryCommunity;
    String dynamicDiscoveryEnvironment;
    String dynamicDiscoveryNormalisationAlgorithm;

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

    public String getGatewayRole() {
        return gatewayRole;
    }

    public void setGatewayRole(String gatewayRole) {
        this.gatewayRole = gatewayRole;
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

    public long getTimeoutRelayREMMD() {
        return timeoutRelayREMMD;
    }

    public void setTimeoutRelayREMMD(long timeoutRelayREMMD) {
        this.timeoutRelayREMMD = timeoutRelayREMMD;
    }

    public long getTimeoutDelivery() {
        return timeoutDelivery;
    }

    public void setTimeoutDelivery(long timeoutDelivery) {
        this.timeoutDelivery = timeoutDelivery;
    }

    public long getTimeoutRetrieval() {
        return timeoutRetrieval;
    }

    public void setTimeoutRetrieval(long timeoutRetrieval) {
        this.timeoutRetrieval = timeoutRetrieval;
    }

    public boolean isCheckEvidences() {
        return checkEvidences;
    }

    public void setCheckEvidences(boolean checkEvidences) {
        this.checkEvidences = checkEvidences;
    }

    public boolean isUseDynamicDiscovery() {
        return useDynamicDiscovery;
    }

    public void setUseDynamicDiscovery(boolean useDynamicDiscovery) {
        this.useDynamicDiscovery = useDynamicDiscovery;
    }

    public String getDynamicDiscoveryCommunity() {
        return dynamicDiscoveryCommunity;
    }

    public void setDynamicDiscoveryCommunity(String dynamicDiscoveryCommunity) {
        this.dynamicDiscoveryCommunity = dynamicDiscoveryCommunity;
    }

    public String getDynamicDiscoveryEnvironment() {
        return dynamicDiscoveryEnvironment;
    }

    public void setDynamicDiscoveryEnvironment(String dynamicDiscoveryEnvironment) {
        this.dynamicDiscoveryEnvironment = dynamicDiscoveryEnvironment;
    }

    public String getDynamicDiscoveryNormalisationAlgorithm() {
        return dynamicDiscoveryNormalisationAlgorithm;
    }

    public void setDynamicDiscoveryNormalisationAlgorithm(String dynamicDiscoveryNormalisationAlgorithm) {
        this.dynamicDiscoveryNormalisationAlgorithm = dynamicDiscoveryNormalisationAlgorithm;
    }

}
