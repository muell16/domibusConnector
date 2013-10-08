package eu.ecodex.webadmin.repository.connector.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ECODEX_SERVICE")
public class ECodexService {

    @Id
    @Column(name = "ECDX_SERVICE")
    private String service;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
