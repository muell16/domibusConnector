package eu.domibus.webadmin.jsf;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
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

import eu.domibus.connector.persistence.model.DomibusConnectorAction;
import eu.domibus.webadmin.blogic.connector.pmode.IConnectorPModeSupport;


@Controller("connectorActionsTableBean")
@Scope("view")
public class ConnectorActionsTableBean {

	private final static Logger LOG = LoggerFactory.getLogger(ConnectorActionsTableBean.class);
	
	/**
	 * backend
	 */
	@Autowired
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
	 * Holds the action which is being edited by the createEditDialog
	 */
	private DomibusConnectorAction action;

	/**
	 * handles if a new action is created (true) or
	 * if a action is being edited
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
	 * holds the db key of the old action
	 */
	private String oldActionPK;
	
	
	@PostConstruct
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
	
	
	

	public void confirmDeleteSelectedActions(ActionEvent actionEvent) {
		LOG.trace("#confirmDeleteSelectedActions: delete confirmed, calling Service to delete [{}]", selectedActions);
		//TODO: delete DB entries
		
		//TODO: handle confirm action delete
		
		for (DomibusConnectorAction action : selectedActions) {	
			//TODO: delete Action!
			try {
				this.pModeSupport.deleteAction(action);
			} catch (DataIntegrityViolationException e) {
				LOG.error("#confirmDeleteSelectedActions: DataIntegrityViolationException occured", e);
				
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", 
						String.format("Delete will corrupt data, there are references pointing to this action! [Action Action: %s - Pdf required: %s] ", 
								action.getAction(), action.isPdfRequired()));
				
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				
			} catch (Exception e) {
				LOG.error("#confirmDeleteSelectedActions: Exception occured", e);
				FacesContext.getCurrentInstance()
					.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error!", "Exception occured - delete not possibly! Check log!"));
			}
		}	
		
	}
		
 
	public void createNewAction(ActionEvent actionEvent) {
		LOG.trace("#createNewAction: called");
		this.action = new DomibusConnectorAction();
		this.createNewActionMode = true;
	}
	
	
 
	public void editAction() {
		LOG.trace("#editAction: called with action: [{}]", this.action);
		//TODO: handle edit...
		this.oldActionPK = this.action.getAction();
		this.createNewActionMode = false;		
	}


	
	
	/*
	 * handles the call from showCreateActionDialog  
	 * @param actionEvent
	 */
	public void saveAction(ActionEvent actionEvent) {
		LOG.trace("#saveAction: called with action [{}] and mode createNewAction is [{}]", this.action, this.createNewActionMode);
		if (this.createNewActionMode) {
			//create new action			
			try {
				this.pModeSupport.createAction(this.action);
			} catch (DataIntegrityViolationException e) {
				LOG.warn(":saveAction: create action failed with DataIntegrityException", e);
				
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"Error!", "Creating action failed" );
				
                                RequestContext.getCurrentInstance().showMessageInDialog(message);
                                
                                /*
				FacesContext.getCurrentInstance()												
					.addMessage(null, message);				
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("PF('showCreateActionDialog').show();");
				*/					
				//return to keep old data (this.action, this.oldActionPK), so user input is not lost
				return;
			} catch (Exception e) {
				LOG.error(":saveAction: create action failed with exception", e);
				
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, 
						"Error!", "Creating action failed -> check logs!" );
				
				FacesContext.getCurrentInstance()												
					.addMessage(null, message);
			}
			
			
		} else {
			//TODO: save change...
			try {
				this.pModeSupport.updateAction(oldActionPK, action);
			} catch (Exception e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"Error!", "Updating action failed" );
				
				FacesContext.getCurrentInstance()												
					.addMessage(null, message);
				
				LOG.error(":saveAction: update action failed with exception", e);
			}
			
			
		}
		this.action = null;		
		this.oldActionPK = null;
	}
		
}
