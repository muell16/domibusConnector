package eu.domibus.webadmin.blogic.connector.pmode;

import java.util.List;

import org.primefaces.model.UploadedFile;

import eu.domibus.connector.domain.model.DomibusConnectorParty;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.domain.model.DomibusConnectorAction;

public interface IConnectorPModeSupport {

	void importFromPModeFile(UploadedFile pmodeFile);


	DomibusConnectorParty createParty(DomibusConnectorParty party);
	
    List<DomibusConnectorParty> getPartyList();
    
	DomibusConnectorParty updateParty(DomibusConnectorParty oldParty, DomibusConnectorParty updatedParty);
	
	void deleteParty(DomibusConnectorParty p);
    

	DomibusConnectorAction createAction(DomibusConnectorAction action);	

	DomibusConnectorAction updateAction(DomibusConnectorAction oldAction, DomibusConnectorAction action);
	
    List<DomibusConnectorAction> getActionList();
       
	void deleteAction(DomibusConnectorAction action);
    

	DomibusConnectorService createService(DomibusConnectorService service);

    List<DomibusConnectorService> getServiceList();
    
	DomibusConnectorService updateService(DomibusConnectorService oldService, DomibusConnectorService service);

	void deleteService(DomibusConnectorService service);

	
}
