package eu.domibus.connector.web.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.persistence.service.*;
import eu.domibus.connector.web.view.areas.configuration.evidences.EvidenceBuilderConfigurationLabels;
import eu.domibus.connector.web.view.areas.configuration.util.ConfigurationUtil;
import org.apache.cxf.common.jaxb.JAXBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import eu.domibus.configuration.Configuration;
import eu.domibus.configuration.Configuration.BusinessProcesses.Parties.PartyIdTypes.PartyIdType;
import eu.domibus.configuration.Configuration.BusinessProcesses.Roles.Role;


@Service("webPModeService")
public class WebPModeService {

	protected final static Logger LOGGER = LoggerFactory.getLogger(WebPModeService.class);


	@Autowired
	private DomibusConnectorPropertiesPersistenceService propertiesPersistenceService;

	@Autowired
	private DomibusConnectorPModeService pModeService;


	// SETTER //
	public void setPModeService(DomibusConnectorPModeService pModeService) {
		this.pModeService = pModeService;
	}

	public void setPropertiesPersistenceService(DomibusConnectorPropertiesPersistenceService propertiesPersistenceService) {
		this.propertiesPersistenceService = propertiesPersistenceService;
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
	public boolean importPModes(byte[] contents, ConfigurationUtil util) {
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

		try {
			DomibusConnectorPModeSet pModeSet = mapPModeConfigurationToPModeSet(pmodes);
			this.updatePModeSet(pModeSet);
		} catch (Exception e) {
			LOGGER.error("Cannot import provided pmode file into database!", e);
			throw new RuntimeException(e);
		}

		try {
			updateHomePartyConfigurationProperties(pmodes, util);
		} catch (Exception e) {
			LOGGER.error("Error while updating home party properties");
		}



		return true;
	}

	private DomibusConnectorPModeSet mapPModeConfigurationToPModeSet(Configuration pmodes) {
		DomibusConnectorPModeSet pModeSet = new DomibusConnectorPModeSet();
		pModeSet.setDescription("Created by p-Mode Upload");

		pModeSet.setServices(importServices(pmodes));
		pModeSet.setActions(importActions(pmodes));
		pModeSet.setParties(importParties(pmodes));
		pModeSet.setCreateDate(new Date());
		return pModeSet;
	}


	private List<DomibusConnectorService> importServices(Configuration pmodes) {
		return pmodes.getBusinessProcesses()
				.getServices()
				.getService()
				.stream()
				.map(s -> {
					DomibusConnectorService service = new DomibusConnectorService();
					service.setService(s.getValue());
					service.setServiceType(s.getType());
					return service;
				})
				.collect(Collectors.toList());
	}

	private List<DomibusConnectorAction> importActions(Configuration pmodes) {

		return pmodes.getBusinessProcesses()
				.getActions()
				.getAction()
				.stream()
				.map(a -> {
					DomibusConnectorAction action = new DomibusConnectorAction();
					action.setAction(a.getValue());
//					action.setDocumentRequired(false);
					return action;
				})
				.collect(Collectors.toList());
	}

	private void updateHomePartyConfigurationProperties(Configuration pmodes, ConfigurationUtil configurationUtil) {
		String homePartyName = pmodes.getParty();

		Configuration.BusinessProcesses.Parties.Party homeParty = pmodes
				.getBusinessProcesses()
				.getParties()
				.getParty()
				.stream()
				.filter(p -> p.getName().equals(homePartyName))
				.findFirst()
				.get();

		Properties homePartyProperties = new Properties();
		homePartyProperties.put(EvidenceBuilderConfigurationLabels.gatewayNameLabels.PROPERTY_NAME_LABEL, homeParty.getName());
		homePartyProperties.put(EvidenceBuilderConfigurationLabels.endpointAddressLabels.PROPERTY_NAME_LABEL, homeParty.getEndpoint());
		propertiesPersistenceService.saveProperties(homePartyProperties);
		configurationUtil.updateConfigurationComponentsOnProperties(homePartyProperties);

	}

	private List<DomibusConnectorParty> importParties(Configuration pmodes) {
		String homeParty = pmodes.getParty();


		Map<String, Role> roles = pmodes.getBusinessProcesses()
				.getRoles()
				.getRole()
				.stream()
				.collect(Collectors.toMap(r -> r.getName(), Function.identity()));

		Map<String, Configuration.BusinessProcesses.Parties.Party> parties = pmodes
				.getBusinessProcesses()
				.getParties()
				.getParty()
				.stream()
				.collect(Collectors.toMap(p -> p.getName(), Function.identity()));

		Map<String, PartyIdType> partyIdTypes = pmodes
				.getBusinessProcesses()
				.getParties()
				.getPartyIdTypes()
				.getPartyIdType()
				.stream()
				.collect(Collectors.toMap(p -> p.getName(), Function.identity()));

		List<DomibusConnectorParty> importedParties = pmodes.getBusinessProcesses()
				.getProcess()
				.stream()
				.map(process ->
						Stream.of(process
										.getInitiatorParties()
										.getInitiatorParty()
										.stream()
										.map(initiatorParty -> this.createParty(partyIdTypes, parties, roles.get(process.getInitiatorRole()), initiatorParty.getName())),
								process.
										getResponderParties().
										getResponderParty().
										stream().
										map(responderParty -> this.createParty(partyIdTypes, parties, roles.get(process.getResponderRole()), responderParty.getName()))
						).flatMap(Function.identity())
				).flatMap(Function.identity())
				.flatMap(Function.identity())
				.distinct() //remove duplicate parties
				.collect(Collectors.toList());
		return importedParties;

	}


	private Stream<DomibusConnectorParty> createParty(Map<String, PartyIdType> partyIdTypes, Map<String, Configuration.BusinessProcesses.Parties.Party> parties,
													  Role role, String partyName) {

		return parties.get(partyName)
				.getIdentifier()
				.stream()
				.map(identifier -> {
					DomibusConnectorParty p = new DomibusConnectorParty();
					p.setPartyName(partyName);
					p.setRole(role.getValue());
					p.setPartyId(identifier.getPartyId());
					String partyIdTypeValue = partyIdTypes.get(identifier.getPartyIdType()).getValue();
					p.setPartyIdType(partyIdTypeValue);
					return p;
				});
	}


	public List<DomibusConnectorParty> getPartyList() {
		return getCurrentPModeSetOrNewSet().getParties();
	}

	public List<DomibusConnectorAction> getActionList() {
		return getCurrentPModeSetOrNewSet().getActions();
	}

	public List<String> getActionListString(){
		return this.getActionList()
				.stream()
				.map(DomibusConnectorAction::getAction)
				.collect(Collectors.toList());
	}

	public List<DomibusConnectorService> getServiceList() {
		return getCurrentPModeSetOrNewSet().getServices();
	}

	public List<String> getServiceListString() {
		return this.getServiceList()
				.stream()
				.map(DomibusConnectorService::getService)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = false, value = "transactionManager")
	public void deleteParty(DomibusConnectorParty p) {
		LOGGER.trace("#deleteParty: called, use partyDao to delete");
		DomibusConnectorPModeSet pModes = this.getCurrentPModeSetOrNewSet();
		pModes.getParties().remove(p);
		pModes.setDescription(String.format("delete party %s clicked in UI", p));
		updatePModeSet(pModes);
	}

	@Transactional(readOnly = false, value = "transactionManager")
	public DomibusConnectorParty updateParty(DomibusConnectorParty oldParty, DomibusConnectorParty updatedParty) {
		LOGGER.trace("#updateParty: called, update party [{}] to party [{}]", oldParty, updatedParty);
		DomibusConnectorPModeSet pModes = this.getCurrentPModeSetOrNewSet();
		pModes.getParties().remove(oldParty);
		pModes.getParties().add(updatedParty);
		pModes.setDescription(String.format("updated party %s in UI", updatedParty));
		//find party in new p-modes by equals
		return updatePModeSet(pModes)
				.getParties()
				.stream()
				.filter(p -> p.equals(updatedParty))
				.findAny()
				.get();
	}

	@Transactional(readOnly = false, value = "transactionManager")
	public DomibusConnectorParty createParty(DomibusConnectorParty party) {
		LOGGER.trace("#createParty: called with party [{}]", party);
		DomibusConnectorPModeSet pModes = this.getCurrentPModeSetOrNewSet();
		pModes.getParties().add(party);
		pModes.setDescription(String.format("added party %s in UI", party));
		//find party in new p-modes by equals
		return updatePModeSet(pModes)
				.getParties()
				.stream()
				.filter(p -> p.equals(party))
				.findAny()
				.get();
	}

	@Transactional(readOnly = false, value = "transactionManager")
	public void deleteAction(DomibusConnectorAction action) {
		LOGGER.trace("deleteAction: delete Action [{}]", action);
		DomibusConnectorPModeSet pModes = this.getCurrentPModeSetOrNewSet();
		pModes.getActions().remove(action);
		pModes.setDescription(String.format("delete action %s clicked in UI", action));
		updatePModeSet(pModes);
	}

	@Transactional(readOnly = false, value = "transactionManager")
	public DomibusConnectorAction createAction(DomibusConnectorAction action) {
		LOGGER.trace("#createAction: called with action [{}]", action);
		DomibusConnectorPModeSet pModes = this.getCurrentPModeSetOrNewSet();
		pModes.getActions().add(action);
		pModes.setDescription(String.format("added action %s in UI", action));
		//find party in new p-modes by equals
		return updatePModeSet(pModes)
				.getActions()
				.stream()
				.filter(p -> p.equals(action))
				.findAny()
				.get();
	}

	@Transactional(readOnly = false, value = "transactionManager")
	public DomibusConnectorAction updateAction(DomibusConnectorAction oldAction, DomibusConnectorAction updatedAction) {
		LOGGER.trace("updateAction: updateAction with oldAction [{}] and new action [{}]", oldAction, updatedAction);
		DomibusConnectorPModeSet pModes = this.getCurrentPModeSetOrNewSet();
		pModes.getActions().remove(oldAction);
		pModes.getActions().add(updatedAction);
		pModes.setDescription(String.format("updated action %s in UI", updatedAction));
		//find party in new p-modes by equals
		return updatePModeSet(pModes)
				.getActions()
				.stream()
				.filter(p -> p.equals(updatedAction))
				.findAny()
				.get();
	}

	@Transactional(readOnly = false, value = "transactionManager")
	public DomibusConnectorService createService(DomibusConnectorService service) {
		LOGGER.trace("createService: with service [{}]", service);
		DomibusConnectorPModeSet pModes = this.getCurrentPModeSetOrNewSet();
		pModes.getServices().add(service);
		pModes.setDescription(String.format("added service %s in UI", service));
		//find party in new p-modes by equals
		return updatePModeSet(pModes)
				.getServices()
				.stream()
				.filter(p -> p.equals(service))
				.findAny()
				.get();
	}

	@Transactional(readOnly = false, value = "transactionManager")
	public DomibusConnectorService updateService(DomibusConnectorService oldService, DomibusConnectorService updatedService) {
		LOGGER.trace("updateService: with new service [{}]", updatedService);
		DomibusConnectorPModeSet pModes = this.getCurrentPModeSetOrNewSet();
		pModes.getServices().remove(oldService);
		pModes.getServices().add(updatedService);
		pModes.setDescription(String.format("updated service %s in UI", updatedService));
		//find party in new p-modes by equals
		return updatePModeSet(pModes)
				.getServices()
				.stream()
				.filter(p -> p.equals(updatedService))
				.findAny()
				.get();
	}

	@Transactional(readOnly = false, value = "transactionManager")
	public void deleteService(DomibusConnectorService service) {
		LOGGER.trace("deleteService: with service [{}]", service);
		DomibusConnectorPModeSet pModes = this.getCurrentPModeSetOrNewSet();
		pModes.getServices().remove(service);
		pModes.setDescription(String.format("delete service %s clicked in UI", service));
		updatePModeSet(pModes);
	}

	private DomibusConnectorPModeSet updatePModeSet(DomibusConnectorPModeSet pModes) {
		DomibusConnectorMessageLane.MessageLaneId laneId = DomibusConnectorMessageLane.getDefaultMessageLaneId();
		this.pModeService.updatePModeConfigurationSet(laneId, pModes);
		return this.getCurrentPModeSet(laneId);
	}

	private DomibusConnectorPModeSet getCurrentPModeSet(DomibusConnectorMessageLane.MessageLaneId laneId) {
		return this.pModeService.getCurrentPModeSet(laneId).get();
	}

	private DomibusConnectorPModeSet getCurrentPModeSetOrNewSet() {
		final DomibusConnectorMessageLane.MessageLaneId laneId = DomibusConnectorMessageLane.getDefaultMessageLaneId();
		Optional<DomibusConnectorPModeSet> currentPModeSetOptional = this.pModeService.getCurrentPModeSet(laneId);
		return currentPModeSetOptional.orElseGet(() -> {
			DomibusConnectorPModeSet set = new DomibusConnectorPModeSet();
			set.setMessageLaneId(laneId);
			return set;
		});
	}

	public boolean importPModes(byte[] pmodeFile, String value, byte[] connectorstore, String value2) {
		// TODO Auto-generated method stub
		return false;
	}


}
