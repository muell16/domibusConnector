package eu.domibus.webadmin.ui.jsf;

import eu.domibus.connector.domain.model.DomibusConnectorParty;
import eu.domibus.connector.domain.model.builder.DomibusConnectorPartyBuilder;
import eu.domibus.connector.domain.model.helper.CopyHelper;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;

import eu.domibus.webadmin.blogic.connector.pmode.IConnectorPModeSupport;

@Controller
@Scope("view")
public class ConnectorPartiesTableBean {

	private final static Logger LOG = LoggerFactory.getLogger(ConnectorPartiesTableBean.class);
	
	/**
	 * backend
	 */
	@Autowired
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
	private DomibusConnectorParty oldParty;
	
	
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
			try {
				this.pModeSupport.deleteParty(p);
			} catch (DataIntegrityViolationException e) {
				LOG.error("#confirmDeleteSelectedParties: DataIntegrityViolationException occured", e);
				
                                
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", 
						String.format("Delete will corrupt data, there are references pointing to this party! [PartyID: %s - Party ID type: %s - Role: %s] ", 
								p.getPartyId(), p.getPartyIdType(), p.getRole()));
				
                                
                                RequestContext.getCurrentInstance().showMessageInDialog(message);
                                
                                /*
				FacesContext.getCurrentInstance()												
					.addMessage(null, message);
				*/
                                
                                
			} catch (Exception e) {
				LOG.error("#confirmDeleteSelectedParties: Exception occured", e);
				FacesContext.getCurrentInstance()
					.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error!", "Exception occured - delete not possibly! Check log!"));
			}
			//TODO: handle exceptions in delete, decide abbort all deletions? -> should all deletions be in one transaction?
		}			
	}
	
	
	public void createNewParty(ActionEvent actionEvent) {
		LOG.trace("#createNewParty: called");
		this.party = DomibusConnectorPartyBuilder.createBuilder()
                .setPartyId(" ")
                .setRole(" ")
                .build();
                
		this.createNewPartyMode = true;
	}
	
	
	public void editParty() {
		LOG.trace("#editParty: called with party: [{}]", this.party);
		//TODO: handle edit...
		this.oldParty = CopyHelper.copyParty(this.party);
		this.createNewPartyMode = false;		
	}

	
	/*
	 * handles the call from showCreatePartyDialog and  
	 * @param actionEvent
	 */
	public void saveParty(ActionEvent actionEvent) {
		LOG.trace("#saveParty: called with party [{}] and mode createNewParty is [{}]", this.party, this.createNewPartyMode);
		if (this.createNewPartyMode) {
			//TODO: create new party
			try {
				this.pModeSupport.createParty(this.party);
			} catch (DataIntegrityViolationException e) {
				LOG.warn(":saveParty: create party failed with DataIntegrityException", e);
				
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"Error!", "Creating party failed" );
				
                                /*
				FacesContext.getCurrentInstance()												
					.addMessage(null, message);
                                        */

                                RequestContext.getCurrentInstance().showMessageInDialog(message);
				
				//RequestContext context = RequestContext.getCurrentInstance();
				//context.execute("PF('showCreatePartyDialog').show();");
									
				//return to keep old data (this.party, this.oldPartyPK)
				return;
				
			} catch (Exception e) {
				LOG.error(":saveparty: create party failed with exception", e);
			}
		} else {
			//TODO: save change...
			this.pModeSupport.updateParty(this.oldParty, this.party);
		}
		this.party = null;		
		this.oldParty = null;
	}
		
}
