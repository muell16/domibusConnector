package eu.domibus.webadmin.jsf;

import java.util.List;

import org.primefaces.model.UploadedFile;

import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorService;
import eu.domibus.webadmin.blogic.connector.pmode.IConnectorPModeSupport;

public class ConnectorDataTablesBean {

	
	private IConnectorPModeSupport pModeSupport;
	
	private List<DomibusConnectorAction> actionList;
	private List<DomibusConnectorService> serviceList;
	private List<DomibusConnectorParty> partyList;
	
	private UploadedFile pmodeFile;
	
	
	public void init(){
		setActionList(pModeSupport.getActionList());
		setServiceList(pModeSupport.getServiceList());
		setPartyList(pModeSupport.getPartyList());
	}
	
	public void importFromPModes(){
		if(pmodeFile!=null){
			pModeSupport.importFromPModeFile(pmodeFile);
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

	public void setPmodeFile(UploadedFile pmodeFile) {
		this.pmodeFile = pmodeFile;
	}

	public IConnectorPModeSupport getpModeSupport() {
		return pModeSupport;
	}

	public void setpModeSupport(IConnectorPModeSupport pModeSupport) {
		this.pModeSupport = pModeSupport;
	}


}
