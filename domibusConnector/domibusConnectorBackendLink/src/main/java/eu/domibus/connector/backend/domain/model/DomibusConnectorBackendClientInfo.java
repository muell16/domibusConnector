
package eu.domibus.connector.backend.domain.model;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DomibusConnectorBackendClientInfo {

    private String backendName;

    private String backendDescription;

    private String backendPushAddress;

    private String backendKeyAlias;

    private String backendKeyPass;

    public String getBackendName() {
        return backendName;
    }

    public void setBackendName(String backendName) {
        this.backendName = backendName;
    }

    public String getBackendDescription() {
        return backendDescription;
    }

    public void setBackendDescription(String backendDescription) {
        this.backendDescription = backendDescription;
    }

    public String getBackendPushAddress() {
        return backendPushAddress;
    }

    public void setBackendPushAddress(String backendPushAddress) {
        this.backendPushAddress = backendPushAddress;
    }

    public String getBackendKeyAlias() {
        return backendKeyAlias;
    }

    public void setBackendKeyAlias(String backendKeyAlias) {
        this.backendKeyAlias = backendKeyAlias;
    }

    public String getBackendKeyPass() {
        return backendKeyPass;
    }

    public void setBackendKeyPass(String backendKeyPass) {
        this.backendKeyPass = backendKeyPass;
    }
    
    
    
}
