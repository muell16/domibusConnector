
package eu.domibus.connector.backend.persistence.model;

import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.persistence.model.PDomibusConnectorService;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Entity
@Table(name="DOMIBUS_CONNECTOR_BACKEND_INFO")
public class BackendClientInfo {

    @Id
    private Long id;
        
    @Column(name="BACKEND_NAME", nullable=false, unique=true)
    private String backendName;
    
    @Column(name="BACKEND_DESCRIPTION", nullable=true)
    private String backendDescription;
    
    @Column(name="BACKEND_PUSH_ADDRESS", nullable=true)
    private String backendPushAddress;
    
    @Column(name="BACKEND_KEY_ALIAS")
    private String backendKeyAlias;
    
    @Column(name="BACKEND_KEY_PASS", nullable=true)
    private String backendKeyPass;

    @Column(name="BACKEND_ENABLED")
    private boolean enabled;

    @Column(name="BACKEND_DEFAULT")
    private boolean defaultBackend;

    @Column(name="BACKEND_SERVICE_TYPE")
    private String backendServiceType;
    
    
//    @ManyToMany(fetch=FetchType.EAGER)
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(
            name="DOMIBUS_CONNECTOR_BACK_2_S",
            joinColumns=@JoinColumn(name="DOMIBUS_CONNECTOR_BACKEND_ID", referencedColumnName="ID")
//            inverseJoinColumns=@JoinColumn(name="DOMIBUS_CONNECTOR_SERVICE_ID", referencedColumnName="SERVICE")
    )
    @Column(name = "DOMIBUS_CONNECTOR_SERVICE_ID")
    private Set<String> services = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Set<String> getServices() {
        return services;
    }

    public void setServices(Set<String> services) {
        this.services = services;
    }
  
    public boolean isPushBackend() {
        return backendPushAddress != null;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isDefaultBackend() {
        return defaultBackend;
    }

    public void setDefaultBackend(boolean defaultBackend) {
        this.defaultBackend = defaultBackend;
    }

    public String getBackendServiceType() {
        return backendServiceType;
    }

    public void setBackendServiceType(String backendServiceType) {
        this.backendServiceType = backendServiceType;
    }
}
