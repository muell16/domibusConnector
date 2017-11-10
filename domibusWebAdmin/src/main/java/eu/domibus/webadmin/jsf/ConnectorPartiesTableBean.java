package eu.domibus.webadmin.jsf;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorPartyPK;
import eu.domibus.webadmin.blogic.connector.pmode.IConnectorPModeSupport;

public class ConnectorPartiesTableBean {

	private final static Logger LOG = LoggerFactory.getLogger(ConnectorPartiesTableBean.class);
	
	/**
	 * backend
	 */
	private IConnectorPModeSupport pModeSupport;
	
	/**
	 * list of parties - retrieved from backend
	 */
	private List<DomibusConnectorParty> partyList;
	
	/**
	 * list of selected parties
	 */
	private List<DomibusConnectorParty> selectedParties = new ArrayList<>();
	
	/**
	 * Holds the party which is being edited by the createEditDialog
	 */
	private DomibusConnectorParty party;

	/**
	 * handles if a new party is created (true) or
	 * if a party is being edited
	 */
	private boolean createNewPartyMode;
	
	/**
	 * holds the editDialogTitle text 
	 */
	private String editDialogTitle = "";
		
	/**
	 * holds the text of the confirmButton at the editDialog
	 */
	private String editDialogConfirmButtonText = "";

	/**
	 * holds the reference of the old party
	 */
	private DomibusConnectorPartyPK oldPartyPK;
	
	
	public void init() {
		setPartyList(this.pModeSupport.getPartyList());
	}
	
	
	/*
	 * SETTER + GETTER	 
	 */
	
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

	
	public DomibusConnectorParty getParty() {
		return party;
	}

	
	public void setParty(DomibusConnectorParty party) {
		this.party = party;
	}

	
	public String getEditDialogTitle() {
		return editDialogTitle;
	}

	
	public void setEditDialogTitle(String editDialogTitle) {
		this.editDialogTitle = editDialogTitle;
	}
	
	
	public void setSelectedParties(List<DomibusConnectorParty> selectedParties) {
		LOG.trace("#setSelectedParties: called with [{}]",  selectedParties);
		this.selectedParties = selectedParties;
	}
	
	
	public String getEditDialogConfirmButtonText() {
		return editDialogConfirmButtonText;
	}

	
	public void setEditDialogConfirmButtonText(String editDialogConfirmButtonText) {
		this.editDialogConfirmButtonText = editDialogConfirmButtonText;
	}

	public boolean isCreateNewPartyMode() {
		return createNewPartyMode;
	}


	public void setCreateNewPartyMode(boolean createNewPartyMode) {
		this.createNewPartyMode = createNewPartyMode;
	}

	
	/*
	 * ACTION LISTENER, BUTTON HANDLER
	 */
	
	public void deleteSelectedParties(ActionEvent actionEvent) {
		LOG.trace("#deleteSelectedParties: delete [{}] following parties: [{}]", 
				selectedParties == null ? 0 : selectedParties.size(), 
						 selectedParties);			
	}
	
	
	public void confirmDeleteSelectedParties(ActionEvent actionEvent) {
		LOG.trace("#confirmDeleteSelectedParties: delete confirmed, calling Service to delete [{}]", selectedParties);
		//TODO: delete DB entries
		
		for (DomibusConnectorParty p : selectedParties) {			
			this.pModeSupport.deleteParty(p);
			//TODO: handle exceptions in delete, decide abbort all deletions? -> should all deletions be in one transaction?
		}			
	}
	
	
	public void createNewParty(ActionEvent actionEvent) {
		LOG.trace("#createNewParty: called");
		this.party = new DomibusConnectorParty();
		this.createNewPartyMode = true;
	}
	
	
	public void editParty() {
		LOG.trace("#editParty: called with party: [{}]", this.party);
		//TODO: handle edit...
		this.oldPartyPK = new DomibusConnectorPartyPK(party.getPartyId(), party.getRole());
		this.createNewPartyMode = false;		
	}

	
	public void saveParty(ActionEvent actionEvent) {
		LOG.trace("#saveParty: called with party [{}] and mode createNewParty is [{}]", this.party, this.createNewPartyMode);
		if (this.createNewPartyMode) {
			//TODO: create new party
			this.pModeSupport.createParty(this.party);
		} else {
			//TODO: save change...
			this.pModeSupport.updateParty(this.oldPartyPK, this.party);
		}
		this.party = null;		
		this.oldPartyPK = null;
	}
	
	
	


	
	
}
