package eu.ecodex.webadmin.jsf;

import eu.ecodex.webadmin.blogic.gateway.statistics.IGatewayCustomService;

public class GatewayBean {

    private IGatewayCustomService gatewayCustomService;

    public IGatewayCustomService getGatewayCustomService() {
        return gatewayCustomService;
    }

    public void setGatewayCustomService(IGatewayCustomService gatewayCustomService) {
        this.gatewayCustomService = gatewayCustomService;
    }

}
