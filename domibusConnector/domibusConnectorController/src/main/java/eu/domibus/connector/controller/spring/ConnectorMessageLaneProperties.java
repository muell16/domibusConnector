package eu.domibus.connector.controller.spring;

public class ConnectorMessageLaneProperties {

    /**
     * is the loopback processing enabled
     * if enabled the suffix _1 will be removed from any
     * incoming EBMS message id
     */
    private boolean loopbackEnabled = true;

    public boolean isLoopbackEnabled() {
        return loopbackEnabled;
    }

    public void setLoopbackEnabled(boolean loopbackEnabled) {
        this.loopbackEnabled = loopbackEnabled;
    }
}
