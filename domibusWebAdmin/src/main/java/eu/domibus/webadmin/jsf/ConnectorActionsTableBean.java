package eu.domibus.webadmin.jsf;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.component.log.Log;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorPartyPK;
import eu.domibus.webadmin.blogic.connector.pmode.IConnectorPModeSupport;

public class ConnectorActionsTableBean {

	private final static Logger LOG = LoggerFactory.getLogger(ConnectorActionsTableBean.class);
	
	/**
	 * backend
	 */
	private IConnectorPModeSupport pModeSupport;
	
	/**
	 * list of actions - retrieved from backend
	 */
	private List<DomibusConnectorAction> actionList;
	
	/**
	 * list of selected actions
	 */
	private List<DomibusConnectorAction> selectedActions = new ArrayList<>();
	
	/**
	 * Holds the party which is being edited by the createEditDialog
	 */
	private DomibusConnectorAction action;

	/**
	 * handles if a new party is created (true) or
	 * if a party is being edited
	 */
	private boolean createNewActionMode;
	
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
	private String oldActionPK;
	
	
	public void init() {
		setActionList(pModeSupport.getActionList());
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


	public List<DomibusConnectorAction> getActionList() {
		return actionList;
	}


	public void setActionList(List<DomibusConnectorAction> actionList) {
		this.actionList = actionList;
	}


	public List<DomibusConnectorAction> getSelectedActions() {
		return selectedActions;
	}


	public void setSelectedActions(List<DomibusConnectorAction> selectedActions) {
		this.selectedActions = selectedActions;
	}


	public DomibusConnectorAction getAction() {
		return action;
	}


	public void setAction(DomibusConnectorAction action) {
		this.action = action;
	}


	public boolean isCreateNewActionMode() {
		return createNewActionMode;
	}


	public void setCreateNewActionMode(boolean createNewActionMode) {
		this.createNewActionMode = createNewActionMode;
	}


	public String getEditDialogTitle() {
		return editDialogTitle;
	}


	public void setEditDialogTitle(String editDialogTitle) {
		this.editDialogTitle = editDialogTitle;
	}


	public String getEditDialogConfirmButtonText() {
		return editDialogConfirmButtonText;
	}


	public void setEditDialogConfirmButtonText(String editDialogConfirmButtonText) {
		this.editDialogConfirmButtonText = editDialogConfirmButtonText;
	}


	public String getOldActionPK() {
		return oldActionPK;
	}


	public void setOldActionPK(String oldActionPK) {
		this.oldActionPK = oldActionPK;
	}

	

	
	/*
	 * ACTION LISTENER, BUTTON HANDLER
	 */
	
	public void deleteSelectedActions(ActionEvent actionEvent) {
		LOG.trace("#deleteSelectedActions: delete [{}] following actions: [{}]", 
				selectedActions == null ? 0 : selectedActions.size(), 
						selectedActions);			
	}
	
	
	

	public void confirmDeleteSelectedParties(ActionEvent actionEvent) {
		LOG.trace("#confirmDeleteSelectedParties: delete confirmed, calling Service to delete [{}]", selectedActions);
		//TODO: delete DB entries
		
		//TODO: handle confirm action delete
		
//		for (DomibusConnectorAction action : selectedActions) {	
//			try {
//				this.pModeSupport.deleteParty(p);
//			} catch (DataIntegrityViolationException e) {
//				LOG.error("#confirmDeleteSelectedParties: DataIntegrityViolationException occured", e);
//				
//				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", 
//						String.format("Delete will corrupt data, there are references pointing to this party! [PartyID: %s - Party ID type: %s - Role: %s] ", 
//								p.getPartyId(), p.getPartyIdType(), p.getRole()));
//				
//				FacesContext.getCurrentInstance()												
//					.addMessage(null, message);
//				
//			} catch (Exception e) {
//				LOG.error("#confirmDeleteSelectedParties: Exception occured", e);
//				FacesContext.getCurrentInstance()
//					.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error!", "Exception occured - delete not possibly! Check log!"));
//			}
//		}	
		
	}
	
	
// TODO: createNewAction	
//	public void createNewParty(ActionEvent actionEvent) {
//		LOG.trace("#createNewParty: called");
//		this.party = new DomibusConnectorParty();
//		this.createNewPartyMode = true;
//	}
	
	
// TODO: edit Action	
//	public void editParty() {
//		LOG.trace("#editParty: called with party: [{}]", this.party);
//		//TODO: handle edit...
//		this.oldPartyPK = new DomibusConnectorPartyPK(party.getPartyId(), party.getRole());
//		this.createNewPartyMode = false;		
//	}


	
//TODO: save Action	
//	/*
//	 * handles the call from showCreatePartyDialog and  
//	 * @param actionEvent
//	 */
//	public void saveParty(ActionEvent actionEvent) {
//		LOG.trace("#saveParty: called with party [{}] and mode createNewParty is [{}]", this.party, this.createNewPartyMode);
//		if (this.createNewPartyMode) {
//			//TODO: create new party
//			try {
//				this.pModeSupport.createParty(this.party);
//			} catch (DataIntegrityViolationException e) {
//				LOG.warn(":saveParty: create party failed with DataIntegrityException", e);
//				
//				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
//						"Error!", "Creating party failed" );
//				
//				FacesContext.getCurrentInstance()												
//					.addMessage(null, message);
//				
//				RequestContext context = RequestContext.getCurrentInstance();
//				context.execute("PF('showCreatePartyDialog').show();");
//									
//				//return to keep old data (this.party, this.oldPartyPK)
//				return;
//				
//			} catch (Exception e) {
//				LOG.error(":saveparty: create party failed with exception", e);
//			}
//		} else {
//			//TODO: save change...
//			this.pModeSupport.updateParty(this.oldPartyPK, this.party);
//		}
//		this.party = null;		
//		this.oldPartyPK = null;
//	}
		
}
