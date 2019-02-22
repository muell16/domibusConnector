package eu.domibus.webadmin.blogic.connector.pmode;

import java.util.List;

import org.primefaces.model.UploadedFile;

import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorPartyPK;
import eu.domibus.connector.common.db.model.DomibusConnectorService;

public interface IConnectorPModeSupport {

	void importFromPModeFile(UploadedFile pmodeFile);

	List<DomibusConnectorParty> getPartyList();

	List<DomibusConnectorAction> getActionList();

	List<DomibusConnectorService> getServiceList();

	void createParty(DomibusConnectorParty party);
	
	void updateParty(DomibusConnectorPartyPK oldPartyId, DomibusConnectorParty updatedParty);
	
	void deleteParty(DomibusConnectorParty p);

	void createAction(DomibusConnectorAction action);	

	void updateAction(String oldActionPK, DomibusConnectorAction action);
	
	void deleteAction(DomibusConnectorAction action);

	void createService(DomibusConnectorService service);

	void updateService(String oldServicePK, DomibusConnectorService service);

	void deleteService(DomibusConnectorService service);

	
}
