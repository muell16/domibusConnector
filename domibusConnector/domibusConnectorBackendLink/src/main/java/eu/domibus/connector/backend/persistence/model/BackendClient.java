
package eu.domibus.connector.backend.persistence.model;

import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.persistence.model.PDomibusConnectorService;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Entity
@Table(name="DOMIBUS_CONNECTOR_BACKEND_INFO")
public class BackendClient {

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
    
    @Column(name="BACKEND_KEY_PASS")
    private String backendKeyPass;
    
    
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="DOMIBUS_CONNECTOR_BACK_2_S",
            joinColumns=@JoinColumn(name="DOMIBUS_CONNECTOR_BACKEND_ID", referencedColumnName="ID"),
            inverseJoinColumns=@JoinColumn(name="DOMIBUS_CONNECTOR_SERVICE_ID", referencedColumnName="SERVICE")
    )
    private Set<PDomibusConnectorService> services = new HashSet<>();

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

    public Set<PDomibusConnectorService> getServices() {
        return services;
    }

    public void setServices(Set<PDomibusConnectorService> services) {
        this.services = services;
    }
  
}
