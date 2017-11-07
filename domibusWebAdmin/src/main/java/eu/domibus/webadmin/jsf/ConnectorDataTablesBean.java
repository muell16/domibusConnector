package eu.domibus.webadmin.jsf;

import java.util.List;

import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorService;
import eu.domibus.webadmin.blogic.connector.pmode.IConnectorPModeSupport;

public class ConnectorDataTablesBean {

	private final static Logger LOG = LoggerFactory.getLogger(ConnectorDataTablesBean.class);
	
	private IConnectorPModeSupport pModeSupport;
	
	private List<DomibusConnectorAction> actionList;
	private List<DomibusConnectorService> serviceList;
	private List<DomibusConnectorParty> partyList;
	
	private UploadedFile pmodeFile;
	
	
	public void init() {
		LOG.trace("#init: init method called");
		setActionList(pModeSupport.getActionList());
		setServiceList(pModeSupport.getServiceList());
		setPartyList(pModeSupport.getPartyList());
	}
	
	public void importFromPModes(){ //kein aufruf!
		LOG.trace("#importFromPModes: method called");
		if(pmodeFile!=null){
			pModeSupport.importFromPModeFile(pmodeFile);
		} else {
			LOG.warn("#importFromPModes: pmodeFile is null!");
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


}
