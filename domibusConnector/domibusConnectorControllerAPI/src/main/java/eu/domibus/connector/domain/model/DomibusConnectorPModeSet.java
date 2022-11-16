package eu.domibus.connector.domain.model;

import eu.ecodex.dc5.message.model.DC5Action;
import eu.ecodex.dc5.message.model.DomibusConnectorParty;
import eu.ecodex.dc5.message.model.DC5Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DomibusConnectorPModeSet {

    private DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId;
    private String description;
    private Date createDate;
    private byte[] pModes;

    private List<DomibusConnectorParty> parties = new ArrayList<>();
    private List<DC5Action> actions = new ArrayList<>();
    private List<DC5Service> services = new ArrayList<>();
    
    private DomibusConnectorKeystore connectorstore;
    private DomibusConnectorParty homeParty;


    public List<DomibusConnectorParty> getParties() {
        return parties;
    }

    public void setParties(List<DomibusConnectorParty> parties) {
        this.parties = parties;
    }

    public List<DC5Action> getActions() {
        return actions;
    }

    public void setActions(List<DC5Action> actions) {
        this.actions = actions;
    }

    public List<DC5Service> getServices() {
        return services;
    }

    public void setServices(List<DC5Service> services) {
        this.services = services;
    }

    public DomibusConnectorBusinessDomain.BusinessDomainId getMessageLaneId() {
        return businessDomainId;
    }

    public void setMessageLaneId(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {
        this.businessDomainId = businessDomainId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public DomibusConnectorKeystore getConnectorstore() {
        return connectorstore;
    }

    public void setConnectorstore(DomibusConnectorKeystore connectorstore) {
        this.connectorstore = connectorstore;
    }

    public byte[] getpModes() {
		return pModes;
	}

	public void setpModes(byte[] pModes) {
		this.pModes = pModes;
	}

    public DomibusConnectorParty getHomeParty() {
        return homeParty;
    }

    public void setHomeParty(DomibusConnectorParty homeParty) {
        this.homeParty = homeParty;
    }
}
