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

	void deleteParty(DomibusConnectorParty p);

	void updateParty(DomibusConnectorPartyPK oldPartyId, DomibusConnectorParty updatedParty);
	
	void createParty(DomibusConnectorParty party);
	
}
