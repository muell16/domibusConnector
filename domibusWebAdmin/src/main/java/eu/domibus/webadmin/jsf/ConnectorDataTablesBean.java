package eu.domibus.webadmin.jsf;

import java.util.List;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorService;
import eu.domibus.webadmin.blogic.connector.pmode.IConnectorPModeSupport;


@Controller
@Scope("view")
public class ConnectorDataTablesBean {

	private final static Logger LOG = LoggerFactory.getLogger(ConnectorDataTablesBean.class);
	
	@Autowired
	private IConnectorPModeSupport pModeSupport;
	
	private List<DomibusConnectorAction> actionList;
	private List<DomibusConnectorService> serviceList;
	private List<DomibusConnectorParty> partyList;	
	private List<String> selectedParties; // = new ArrayList<>();
	
	private UploadedFile pmodeFile;
	
			
	public void init() {
		LOG.trace("#init: init method called");
		setActionList(pModeSupport.getActionList());
		setServiceList(pModeSupport.getServiceList());
		setPartyList(pModeSupport.getPartyList());
	}
	
	public void importFromPModes() {
		LOG.trace("#importFromPModes: method called");
		if(pmodeFile!=null){
            try {
                pModeSupport.importFromPModeFile(pmodeFile);
            } catch (Exception e) {
                LOG.warn("#importFromPModes: a exception during import occured!", e);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", 
                    String.format("Import failed! Please check your pmode file! For Details check the logs!"));				                                
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
		} else {
			LOG.warn("#importFromPModes: pmodeFile is null!");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", 
                String.format("Please provide a file!"));				                                
                RequestContext.getCurrentInstance().showMessageInDialog(message);
		}
	}

	public List<DomibusConnectorAction> getActionList() {
		return actionList;
	}


	public void setActionList(List<DomibusConnectorAction> actionList) {
		this.actionList = actionList;
	}


	public List<DomibusConnectorService> getServiceList() {
		return serviceList;
	}


	public void setServiceList(List<DomibusConnectorService> serviceList) {
		this.serviceList = serviceList;
	}


	public List<DomibusConnectorParty> getPartyList() {
		return partyList;
	}


	public void setPartyList(List<DomibusConnectorParty> partyList) {	
		this.partyList = partyList;
	}

	public UploadedFile getPmodeFile() {
		return pmodeFile;
	}

	public void setPmodeFile(UploadedFile pmodeFile) { //kein Aufruf
		LOG.trace("#setPmodeFile: pmodeFile = [{}]", pmodeFile);
		this.pmodeFile = pmodeFile;
	}

	public IConnectorPModeSupport getpModeSupport() {
		return pModeSupport;
	}

	public void setpModeSupport(IConnectorPModeSupport pModeSupport) {
		this.pModeSupport = pModeSupport;
	}

	public List<String> getSelectedParties() {
		return selectedParties;
	}

	public void setSelectedParties(List<String> selectedParties) {
		this.selectedParties = selectedParties;
	}
	
	public void deleteSelectedPartiesAction() {
		LOG.trace("do nothing....");
		//LOG.trace("#deleteSelectedParties: starting deletion for [{}]", this.selectedParties);
		//TODO: open dialog after confirmation start deletion or abort
	}


}
