package eu.domibus.connector.domain.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DomibusConnectorPModeSet {

    private DomibusConnectorMessageLane.MessageLaneId messageLaneId;
    private String description;
    private Date createDate;
    private byte[] pModes;

    private List<DomibusConnectorParty> parties = new ArrayList<>();
    private List<DomibusConnectorAction> actions = new ArrayList<>();
    private List<DomibusConnectorService> services = new ArrayList<>();
    
    private DomibusConnectorKeystore connectorstore;
    private DomibusConnectorParty homeParty;


    public List<DomibusConnectorParty> getParties() {
        return parties;
    }

    public void setParties(List<DomibusConnectorParty> parties) {
        this.parties = parties;
    }

    public List<DomibusConnectorAction> getActions() {
        return actions;
    }

    public void setActions(List<DomibusConnectorAction> actions) {
        this.actions = actions;
    }

    public List<DomibusConnectorService> getServices() {
        return services;
    }

    public void setServices(List<DomibusConnectorService> services) {
        this.services = services;
    }

    public DomibusConnectorMessageLane.MessageLaneId getMessageLaneId() {
        return messageLaneId;
    }

    public void setMessageLaneId(DomibusConnectorMessageLane.MessageLaneId messageLaneId) {
        this.messageLaneId = messageLaneId;
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
