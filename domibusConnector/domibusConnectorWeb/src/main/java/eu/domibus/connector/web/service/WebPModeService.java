package eu.domibus.connector.web.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import eu.domibus.configuration.Configuration;
import eu.domibus.configuration.Configuration.BusinessProcesses.Actions.Action;
import eu.domibus.configuration.Configuration.BusinessProcesses.Parties.Party;
import eu.domibus.configuration.Configuration.BusinessProcesses.Parties.Party.Identifier;
import eu.domibus.configuration.Configuration.BusinessProcesses.Parties.PartyIdTypes.PartyIdType;
import eu.domibus.configuration.Configuration.BusinessProcesses.Process.InitiatorParties.InitiatorParty;
import eu.domibus.configuration.Configuration.BusinessProcesses.Process.ResponderParties.ResponderParty;
import eu.domibus.configuration.Configuration.BusinessProcesses.Roles.Role;
import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorParty;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.domain.model.builder.DomibusConnectorActionBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorPartyBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorServiceBuilder;
import eu.domibus.connector.persistence.service.DomibusConnectorActionPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorPartyPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorServicePersistenceService;

@Service("webPModeService")
public class WebPModeService {

	protected final static Logger LOGGER = LoggerFactory.getLogger(WebPModeService.class);

	@Autowired
	private DomibusConnectorActionPersistenceService actionPersistenceService;

	@Autowired
	private DomibusConnectorServicePersistenceService servicePersistenceService;

	@Autowired
	private DomibusConnectorPartyPersistenceService partyPersistenceService;

	// SETTER //
	public void setActionPersistenceService(DomibusConnectorActionPersistenceService actionPersistenceService) {
		this.actionPersistenceService = actionPersistenceService;
	}

	public void setServicePersistenceService(DomibusConnectorServicePersistenceService servicePersistenceService) {
		this.servicePersistenceService = servicePersistenceService;
	}

	public void setPartyPersistenceService(DomibusConnectorPartyPersistenceService partyPersistenceService) {
		this.partyPersistenceService = partyPersistenceService;
	}

	public DomibusConnectorService getService(String serviceName) {
		return this.servicePersistenceService.getService(serviceName);
	}
	
	public DomibusConnectorAction getAction(String actionName) {
		return this.actionPersistenceService.getAction(actionName);
	}

	private void importServices(Configuration pmodes) {
		Map<String, DomibusConnectorService> services = new HashMap<String, DomibusConnectorService>();
		for (DomibusConnectorService srv : getServiceList()) {
			services.put(srv.getService(), srv);
		}

		for (eu.domibus.configuration.Configuration.BusinessProcesses.Services.Service configServiceEntry : pmodes.getBusinessProcesses().getServices().getService()) {
			if (!services.containsKey(configServiceEntry.getValue())) {
				DomibusConnectorService newService = DomibusConnectorServiceBuilder.createBuilder()
						.setService(configServiceEntry.getValue())
						.withServiceType(configServiceEntry.getType())
						.build();

				newService = this.servicePersistenceService.persistNewService(newService);
				services.put(configServiceEntry.getValue(), newService);

			}
		}
	}

	private void importActions(Configuration pmodes) {
		HashSet<String> actions = new HashSet<String>();
		for (DomibusConnectorAction dbAction : getActionList()) {
			actions.add(dbAction.getAction());
		}

		for (Action configurationActionEntry : pmodes.getBusinessProcesses().getActions().getAction()) {
			if (!actions.contains(configurationActionEntry.getValue())) {
				DomibusConnectorAction newAction = DomibusConnectorActionBuilder.createBuilder()
						.setAction(configurationActionEntry.getValue())
						.withDocumentRequired(true) //TODO: is true really default here?
						.build();

				newAction = this.actionPersistenceService.persistNewAction(newAction);
				actions.add(newAction.getAction());
			}
		}
	}

	private void importParties(Configuration pmodes) {
		Map<String, String> roles = new HashMap<String, String>();
		for (Role role : pmodes.getBusinessProcesses().getRoles().getRole()) {
			roles.put(role.getName(), role.getValue());
		}

		Map<String, String> partyIdTypes = new HashMap<String, String>();
		for (PartyIdType type : pmodes.getBusinessProcesses().getParties().getPartyIdTypes().getPartyIdType()) {
			partyIdTypes.put(type.getName(), type.getValue());
		}

		Map<String, Identifier> partyIdentifiers = new HashMap<String, Identifier>();
		for (Party party : pmodes.getBusinessProcesses().getParties().getParty()) {
			if (!CollectionUtils.isEmpty(party.getIdentifier()) && party.getIdentifier().get(0) != null) {
				partyIdentifiers.put(party.getName(), party.getIdentifier().get(0));
			}
		}

		Map<String, Map<String, DomibusConnectorParty>> dbParties = new HashMap<String, Map<String, DomibusConnectorParty>>();
		for (final DomibusConnectorParty dbParty : getPartyList()) {
			if (!dbParties.containsKey(dbParty.getPartyId())) {
				dbParties.put(dbParty.getPartyId(), new HashMap<String, DomibusConnectorParty>());
			}
			dbParties.get(dbParty.getPartyId()).put(dbParty.getRole(), dbParty);
		}

		for (eu.domibus.configuration.Configuration.BusinessProcesses.Process process : pmodes.getBusinessProcesses().getProcess()) {
			String initiatorRole = roles.get(process.getInitiatorRole());
			String responderRole = roles.get(process.getResponderRole());
			for (InitiatorParty iParty : process.getInitiatorParties().getInitiatorParty()) {
				checkAndCreateParty(partyIdTypes, partyIdentifiers, dbParties, initiatorRole, iParty.getName());
			}
			for (ResponderParty rParty : process.getResponderParties().getResponderParty()) {
				checkAndCreateParty(partyIdTypes, partyIdentifiers, dbParties, responderRole, rParty.getName());
			}
		}

	}

	private void checkAndCreateParty(Map<String, String> partyIdTypes, Map<String, Identifier> partyIdentifiers,
			Map<String, Map<String, DomibusConnectorParty>> dbParties, String role, String partyName) {
		Identifier pId = partyIdentifiers.get(partyName);
		if (!(dbParties.containsKey(pId.getPartyId()) && dbParties.get(pId.getPartyId()).containsKey(role))) {
			DomibusConnectorParty newParty = DomibusConnectorPartyBuilder.createBuilder()
					.setPartyId(pId.getPartyId())
					.setRole(role)
					.withPartyIdType(partyIdTypes.get(pId.getPartyIdType()))
					.build();

			newParty = this.partyPersistenceService.persistNewParty(newParty);

			Map<String, DomibusConnectorParty> partyRoleMape = dbParties.getOrDefault(pId.getPartyId(), new HashMap<>());
			partyRoleMape.put(role, newParty);
			dbParties.put(pId.getPartyId(), partyRoleMape);

		}
	}


	public List<DomibusConnectorParty> getPartyList() {
		return this.partyPersistenceService.getPartyList();
	}

	public List<DomibusConnectorAction> getActionList() {
		return this.actionPersistenceService.getActionList();
	}

	public List<DomibusConnectorService> getServiceList() {
		return this.servicePersistenceService.getServiceList();
	}

	@Transactional(readOnly = false, value = "transactionManager")
	public void deleteParty(DomibusConnectorParty p) {
		LOGGER.trace("#deleteParty: called, use partyDao to delete");
		this.partyPersistenceService.deleteParty(p);
	}

	@Transactional(readOnly = false, value = "transactionManager")
	public DomibusConnectorParty updateParty(DomibusConnectorParty oldParty, DomibusConnectorParty updatedParty) {
		LOGGER.trace("#updateParty: called, update party [{}] to party [{}]", oldParty, updatedParty);
		return this.partyPersistenceService.updateParty(oldParty, updatedParty);
	}

	@Transactional(readOnly = false, value = "transactionManager")
	public DomibusConnectorParty createParty(DomibusConnectorParty party) {
		LOGGER.trace("#createParty: called with party [{}]", party);
		return this.partyPersistenceService.persistNewParty(party);
	}

	@Transactional(readOnly = false, value = "transactionManager")
	public void deleteAction(DomibusConnectorAction action) {
		LOGGER.trace("deleteAction: delete Action [{}]", action);
		this.actionPersistenceService.deleteAction(action);
	}

	@Transactional(readOnly = false, value = "transactionManager")
	public DomibusConnectorAction createAction(DomibusConnectorAction action) {
		LOGGER.trace("#createAction: called with action [{}]", action);
		return this.actionPersistenceService.persistNewAction(action);
	}

	@Transactional(readOnly = false, value = "transactionManager")
	public DomibusConnectorAction updateAction(DomibusConnectorAction oldAction, DomibusConnectorAction action) {
		LOGGER.trace("updateAction: updateAction with oldAction [{}] and new action [{}]", oldAction, action);
		return this.actionPersistenceService.updateAction(oldAction, action);
	}

	@Transactional(readOnly = false, value = "transactionManager")
	public DomibusConnectorService createService(DomibusConnectorService service) {
		LOGGER.trace("createService: with service [{}]", service);
		return this.servicePersistenceService.persistNewService(service);
	}

	@Transactional(readOnly = false, value = "transactionManager")
	public DomibusConnectorService updateService(DomibusConnectorService oldService, DomibusConnectorService service) {
		LOGGER.trace("updateService: with new service [{}]", service);
		return this.servicePersistenceService.updateService(oldService, service);
	}

	@Transactional(readOnly = false, value = "transactionManager")
	public void deleteService(DomibusConnectorService service) {
		LOGGER.trace("deleteService: with service [{}]", service);
		this.servicePersistenceService.deleteService(service);
	}

	 public static Object byteArrayToXmlObject(final byte[] xmlAsBytes, final Class<?> instantiationClazz,
	            final Class<?>... initializationClasses) throws Exception {

	        try {
	            ByteArrayInputStream bis = new ByteArrayInputStream(xmlAsBytes);

	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            factory.setNamespaceAware(true);
	            Document document = factory.newDocumentBuilder().parse(bis);

	            JAXBContext ctx = JAXBContext.newInstance(initializationClasses);

	            Unmarshaller unmarshaller = ctx.createUnmarshaller();

	            JAXBElement<?> jaxbElement = unmarshaller.unmarshal(document, instantiationClazz);
	            return jaxbElement.getValue();
	        } catch (JAXBException | SAXException | IOException | ParserConfigurationException e) {
	            throw new Exception("Exception parsing byte[] to " + instantiationClazz.getName(), e);
	        }

	    }

	 @Transactional(readOnly = false)
	public boolean importPModes(byte[] contents) {
		if (contents == null || contents.length<1) {
            throw new IllegalArgumentException("pModes are not allowed to be null or empty!");
        }
        LOGGER.debug("Starting import of PModes");
        Configuration pmodes = null;
        try {
            pmodes = (Configuration) byteArrayToXmlObject(contents, Configuration.class, Configuration.class);
        } catch (Exception e) {
            LOGGER.error("Cannot load provided pmode file!", e);
            throw new RuntimeException(e);
        }

        importServices(pmodes);

        importActions(pmodes);

        importParties(pmodes);
        
        return true;
		
	}
}
