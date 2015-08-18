package eu.domibus.connector.common.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DOMIBUS_CONNECTOR_SERVICE")
public class DomibusConnectorService {

    @Id
    @Column(name = "SERVICE")
    private String service;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
