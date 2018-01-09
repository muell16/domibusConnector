package eu.domibus.webadmin.blogic.connector.pmode;

import java.util.List;

import org.primefaces.model.UploadedFile;

import eu.domibus.connector.persistence.model.PDomibusConnectorAction;
import eu.domibus.connector.persistence.model.PDomibusConnectorParty;
import eu.domibus.connector.persistence.model.PDomibusConnectorPartyPK;
import eu.domibus.connector.persistence.model.PDomibusConnectorService;

public interface IConnectorPModeSupport {

	void importFromPModeFile(UploadedFile pmodeFile);

	List<PDomibusConnectorParty> getPartyList();

	List<PDomibusConnectorAction> getActionList();

	List<PDomibusConnectorService> getServiceList();

	void createParty(PDomibusConnectorParty party);
	
	void updateParty(PDomibusConnectorPartyPK oldPartyId, PDomibusConnectorParty updatedParty);
	
	void deleteParty(PDomibusConnectorParty p);

	void createAction(PDomibusConnectorAction action);	

	void updateAction(String oldActionPK, PDomibusConnectorAction action);
	
	void deleteAction(PDomibusConnectorAction action);

	void createService(PDomibusConnectorService service);

	void updateService(String oldServicePK, PDomibusConnectorService service);

	void deleteService(PDomibusConnectorService service);

	
}
