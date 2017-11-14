package eu.domibus.webadmin.jsf;

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

import eu.domibus.connector.common.db.model.DomibusConnectorService;
import eu.domibus.webadmin.blogic.connector.pmode.IConnectorPModeSupport;

@Controller
@Scope("view")
public class ConnectorServicesTableBean {

	private final static Logger LOG = LoggerFactory.getLogger(ConnectorServicesTableBean.class);
	
	/**
	 * backend
	 */
	@Autowired
	private IConnectorPModeSupport pModeSupport;
	
	/**
	 * list of services - retrieved from backend
	 */
	private List<DomibusConnectorService> serviceList;
	
	/**
	 * list of selected services
	 */
	private List<DomibusConnectorService> selectedServices = new ArrayList<>();
	
	/**
	 * Holds the service which is being edited by the createEditDialog
	 */
	private DomibusConnectorService service;

	/**
	 * handles if a new service is created (true) or
	 * if a service is being edited
	 */
	private boolean createNewServiceMode;
	
	/**
	 * holds the editDialogTitle text 
	 */
	private String editDialogTitle = "";
		
	/**
	 * holds the text of the confirmButton at the editDialog
	 */
	private String editDialogConfirmButtonText = "";

	/**
	 * holds the db key of the old service
	 */
	private String oldServicePK;
	
	
	public void init() {
		setServiceList(pModeSupport.getServiceList());
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


	public List<DomibusConnectorService> getServiceList() {
		return serviceList;
	}


	public void setServiceList(List<DomibusConnectorService> serviceList) {
		this.serviceList = serviceList;
	}


	public List<DomibusConnectorService> getSelectedServices() {
		return selectedServices;
	}


	public void setSelectedServices(List<DomibusConnectorService> selectedServices) {
		this.selectedServices = selectedServices;
	}


	public DomibusConnectorService getService() {
		return service;
	}


	public void setService(DomibusConnectorService service) {
		this.service = service;
	}


	public boolean isCreateNewServiceMode() {
		return createNewServiceMode;
	}


	public void setCreateNewServiceMode(boolean createNewServiceMode) {
		this.createNewServiceMode = createNewServiceMode;
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


	public String getOldServicePK() {
		return oldServicePK;
	}


	public void setOldServicePK(String oldServicePK) {
		this.oldServicePK = oldServicePK;
	}

	

	
	/*
	 * ACTION LISTENER, BUTTON HANDLER
	 */
	
	public void deleteSelectedServices(ActionEvent serviceEvent) {
		LOG.trace("#deleteSelectedServices: delete [{}] following services: [{}]", 
				selectedServices == null ? 0 : selectedServices.size(), 
						selectedServices);			
	}
	
	
	

	public void confirmDeleteSelectedServices(ActionEvent serviceEvent) {
		LOG.trace("#confirmDeleteSelectedServices: delete confirmed, calling Service to delete [{}]", selectedServices);
		
		for (DomibusConnectorService service : selectedServices) {	
			//TODO: delete Service!
			
			try {
				this.pModeSupport.deleteService(service);
			} catch (DataIntegrityViolationException e) {
				LOG.error("#confirmDeleteSelectedServices: DataIntegrityViolationException occured", e);
				
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", 
						String.format("Delete will corrupt data, there are references pointing to this service! [Service Service: %s - service type: %s] ", 
								service.getService(), service.getServiceType()));
				
				FacesContext.getCurrentInstance()												
					.addMessage(null, message);
				
			} catch (Exception e) {
				LOG.error("#confirmDeleteSelectedServices: Exception occured", e);
				FacesContext.getCurrentInstance()
					.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error!", "Exception occured - delete not possibly! Check log!"));
			}
			
			
		}	
		
	}
		
 
	public void createNewService(ActionEvent serviceEvent) {
		LOG.trace("#createNewService: called");
		this.service = new DomibusConnectorService();
		this.createNewServiceMode = true;
	}
	
	
 
	public void editService() {
		LOG.trace("#editService: called with service: [{}]", this.service);
		this.oldServicePK = this.service.getService();
		this.createNewServiceMode = false;		
	}


	
	
	/*
	 * handles the call from showCreateServiceDialog  
	 * @param serviceEvent
	 */
	public void saveService(ActionEvent serviceEvent) {
		LOG.trace("#saveService: called with service [{}] and mode createNewService is [{}]", this.service, this.createNewServiceMode);
		if (this.createNewServiceMode) {
			
			//create new service			
			try {
				this.pModeSupport.createService(this.service);
			} catch (DataIntegrityViolationException e) {
				LOG.warn(":saveService: create service failed with DataIntegrityException", e);
				
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"Error!", "Creating service failed" );
				
				FacesContext.getCurrentInstance()												
					.addMessage(null, message);
				
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("PF('showCreateServiceDialog').show();");
									
				//return to keep old data (this.service, this.oldServicePK), so user input is not lost
				
			} catch (Exception e) {
				LOG.error(":saveService: create service failed with exception", e);
				
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, 
						"Error!", "Creating service failed -> check logs!" );
				
				FacesContext.getCurrentInstance()												
					.addMessage(null, message);
			}
			
			
		} else {
			
		
			//TODO: save change...
			try {
				this.pModeSupport.updateService(oldServicePK, service);
			} catch (Exception e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"Error!", "Updating service failed" );
				
				FacesContext.getCurrentInstance()												
					.addMessage(null, message);
				
				LOG.error(":saveService: update service failed with exception", e);
			}
		}
		
		
		this.service = null;		
		this.oldServicePK = null;
	}
		
}
