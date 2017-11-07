package eu.domibus.webadmin.jsf;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.webadmin.blogic.connector.pmode.IConnectorPModeSupport;

public class ConnectorPartiesTableBean {

	private final static Logger LOG = LoggerFactory.getLogger(ConnectorPartiesTableBean.class);
	
	private IConnectorPModeSupport pModeSupport;
	
	private List<DomibusConnectorParty> partyList;
	private List<DomibusConnectorParty> selectedParties = new ArrayList<>();
	
	public void init() {
		setPartyList(this.pModeSupport.getPartyList());
	}
	
	public IConnectorPModeSupport getpModeSupport() {
		return pModeSupport;
	}

	public void setpModeSupport(IConnectorPModeSupport pModeSupport) {
		this.pModeSupport = pModeSupport;
	}
	
	
	public List<DomibusConnectorParty> getPartyList() {
		return partyList;
	}


	public void setPartyList(List<DomibusConnectorParty> partyList) {	
		this.partyList = partyList;
	}

	public List<DomibusConnectorParty> getSelectedParties() {
		return selectedParties;
	}

	public void setSelectedParties(List<DomibusConnectorParty> selectedParties) {
		LOG.trace("#setSelectedParties: called with [{}]",  selectedParties);
		this.selectedParties = selectedParties;
	}
	
	public void deleteSelectedParties(ActionEvent actionEvent) {
		LOG.trace("#deleteSelectedParties: delete [{}] following parties: [{}]", 
				selectedParties == null ? 0 : selectedParties.size(), 
						 selectedParties);			
	}
	
	public void confirmDeleteSelectedParties(ActionEvent actionEvent) {
		LOG.trace("#confirmDeleteSelectedParties: delete confirmed, calling Service to delete [{}]", selectedParties);
		//TODO: delete DB entries
		//this.pModeSupport
		
		
	}
		
	

}
