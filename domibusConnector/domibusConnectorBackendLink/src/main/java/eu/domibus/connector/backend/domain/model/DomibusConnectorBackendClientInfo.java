
package eu.domibus.connector.backend.domain.model;

import java.util.Objects;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.core.style.ToStringCreator;

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
    
    @Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("backendName", this.backendName);
        builder.append("backendAlias", this.backendKeyAlias);
        builder.append("backendPushAddress", this.backendPushAddress);
        return builder.toString();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.backendName);
        return builder.toHashCode();        
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DomibusConnectorBackendClientInfo other = (DomibusConnectorBackendClientInfo) obj;
        if (!Objects.equals(this.backendName, other.backendName)) {
            return false;
        }
        return true;
    }


    public boolean isPushBackend() {
        return this.backendPushAddress != null;
    }
}
