package eu.domibus.webadmin.blogic.connector.pmode.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import eu.domibus.configuration.Configuration.BusinessProcesses.Services.Service;
import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorPartyPK;
import eu.domibus.connector.common.db.model.DomibusConnectorService;
import eu.domibus.webadmin.blogic.connector.pmode.IConnectorPModeSupport;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorActionDao;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorPartyDao;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorServiceDao;

@org.springframework.stereotype.Service
public class ConnectorPModeSupportImpl implements IConnectorPModeSupport {
	
	protected final static Logger LOG = LoggerFactory.getLogger(ConnectorPModeSupportImpl.class); 
	
	private IDomibusWebAdminConnectorActionDao actionDao;
	
	private IDomibusWebAdminConnectorServiceDao serviceDao;
	
	private IDomibusWebAdminConnectorPartyDao partyDao;

	@Autowired
	public ConnectorPModeSupportImpl(IDomibusWebAdminConnectorActionDao actionDao,
			IDomibusWebAdminConnectorServiceDao serviceDao,
			IDomibusWebAdminConnectorPartyDao partyDao) {
		this.actionDao = actionDao;
		this.serviceDao = serviceDao;
		this.partyDao = partyDao;
	}
	
    /**
     * {@inheritDoc}
     * 
     */
	@Override
    @Transactional(readOnly=false)
	public void importFromPModeFile(UploadedFile pmodeFile) {
        if (pmodeFile == null) {
            throw new IllegalArgumentException("pModeFile is not allowed to be null!");
        }
		LOG.debug("Starting import of PModes from File "+ pmodeFile.getFileName());
		Configuration pmodes = null;
		try {
			pmodes = (Configuration) byteArrayToXmlObject(pmodeFile.getContents(), Configuration.class, Configuration.class);
		} catch (Exception e) {
			LOG.error("Cannot load provided pmode file!", e);
            throw new RuntimeException(e);
		}
		
		importServices(pmodes);
		
		importActions(pmodes);
		
		importParties(pmodes);
		
	}

	private void importServices(Configuration pmodes) {
		Map<String, DomibusConnectorService> services = new HashMap<String, DomibusConnectorService>();
		for(DomibusConnectorService dbService:getServiceList()){
			services.put(dbService.getService(), dbService);
		}
		
		for(Service service:pmodes.getBusinessProcesses().getServices().getService()){
			if(!services.containsKey(service.getValue())){
				DomibusConnectorService newService = new DomibusConnectorService();
				newService.setService(service.getValue());
				newService.setServiceType(service.getType());
				this.serviceDao.persistNewService(newService);
			}
		}
	}

	private void importActions(Configuration pmodes) {
		Set<String> actions = new HashSet<String>();
		for(DomibusConnectorAction dbAction:getActionList()){
			actions.add(dbAction.getAction());
		}
		
		for(Action action:pmodes.getBusinessProcesses().getActions().getAction()){
			if(!actions.contains(action.getValue())){
				DomibusConnectorAction newAction = new DomibusConnectorAction();
				newAction.setAction(action.getValue());
				newAction.setPdfRequired(true);
				this.actionDao.persistNewAction(newAction);
			}
		}
	}
        
	private void importParties(Configuration pmodes) {
		Map<String, String> roles = new HashMap<String,String>();
		for(Role role:pmodes.getBusinessProcesses().getRoles().getRole()){
			roles.put(role.getName(), role.getValue());
		}
		
		Map<String,String> partyIdTypes = new HashMap<String, String>();
		for(PartyIdType type: pmodes.getBusinessProcesses().getParties().getPartyIdTypes().getPartyIdType()){
			partyIdTypes.put(type.getName(), type.getValue());
		}
		
		Map<String,Identifier> partyIdentifiers = new HashMap<String,Identifier>();
		for(Party party:pmodes.getBusinessProcesses().getParties().getParty()){
			if(!CollectionUtils.isEmpty(party.getIdentifier())&& party.getIdentifier().get(0)!=null){
				partyIdentifiers.put(party.getName(), party.getIdentifier().get(0));
			}
		}

		Map<String,Map<String,DomibusConnectorParty>> dbParties = new HashMap<String,Map<String,DomibusConnectorParty>>();
		for(final DomibusConnectorParty dbParty:getPartyList()){
			if(!dbParties.containsKey(dbParty.getPartyId())){
				dbParties.put(dbParty.getPartyId(), new HashMap<String,DomibusConnectorParty>());
			}
			dbParties.get(dbParty.getPartyId()).put(dbParty.getRole(), dbParty);
		}
		
		for(eu.domibus.configuration.Configuration.BusinessProcesses.Process process:pmodes.getBusinessProcesses().getProcess()){
			String initiatorRole = roles.get(process.getInitiatorRole());
			String responderRole = roles.get(process.getResponderRole());
			for(InitiatorParty iParty:process.getInitiatorParties().getInitiatorParty()){
				checkAndCreateParty(partyIdTypes, partyIdentifiers, dbParties, initiatorRole, iParty.getName());
			}
			for(ResponderParty rParty:process.getResponderParties().getResponderParty()){
				checkAndCreateParty(partyIdTypes, partyIdentifiers, dbParties, responderRole, rParty.getName());
			}
		}
		
	}

	private void checkAndCreateParty(Map<String, String> partyIdTypes, Map<String, Identifier> partyIdentifiers,
            Map<String, Map<String, DomibusConnectorParty>> dbParties, String role, String partyName) {
            Identifier pId = partyIdentifiers.get(partyName);
            if(!(dbParties.containsKey(pId.getPartyId()) && dbParties.get(pId.getPartyId()).containsKey(role))){
		DomibusConnectorParty newParty = new DomibusConnectorParty();
		newParty.setPartyId(pId.getPartyId());
		newParty.setPartyIdType(partyIdTypes.get(pId.getPartyIdType()));
		newParty.setRole(role);
                this.partyDao.persistNewParty(newParty);
                
                //TODO: put persisted newParty into dbParties
                Map<String, DomibusConnectorParty> partyRoleMape = dbParties.getOrDefault(pId.getPartyId(), new HashMap<>());
                partyRoleMape.put(role, newParty);
                dbParties.put(pId.getPartyId(), partyRoleMape);
                
                //dbParties.put(newParty.getPartyId(), rolePartyMap);
            }
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
	
	 @Override
	 public List<DomibusConnectorParty> getPartyList(){
		 return partyDao.getPartyList();
	 }
	 
	 @Override
	 public List<DomibusConnectorAction> getActionList(){
		 return actionDao.getActionList();
	 }
	 
	 @Override
	 public List<DomibusConnectorService> getServiceList(){
		 return serviceDao.getServiceList();
	 }
	 
		public void setActionDao(IDomibusWebAdminConnectorActionDao actionDao) {
			this.actionDao = actionDao;
		}

	
		public void setServiceDao(IDomibusWebAdminConnectorServiceDao serviceDao) {
			this.serviceDao = serviceDao;
		}

	
		public void setPartyDao(IDomibusWebAdminConnectorPartyDao partyDao) {
			this.partyDao = partyDao;
		}

		@Override
		@Transactional(readOnly=false, value="transactionManager")
		public void deleteParty(DomibusConnectorParty p) {
			LOG.trace("#deleteParty: called, use partyDao to delete");
			//domibusConnectorParty is detached so find first, and then delete...
			this.partyDao.delete(
					this.partyDao.findById(
							new DomibusConnectorPartyPK(p.getPartyId(), p.getRole())));			
		}

		@Override
		@Transactional(readOnly=false, value="transactionManager")
		public void updateParty(DomibusConnectorPartyPK oldPartyId, DomibusConnectorParty updatedParty) {
			DomibusConnectorParty dbParty = this.partyDao.findById(oldPartyId);
//			altering PK components is not allowed!			
//			dbParty.setPartyId(updatedParty.getPartyId());
//			dbParty.setRole(updatedParty.getRole());
			dbParty.setPartyIdType(updatedParty.getPartyIdType());
			this.partyDao.update(dbParty);			
		}

		@Override
		@Transactional(readOnly=false, value="transactionManager")
		public void createParty(DomibusConnectorParty party) {
			this.partyDao.persistNewParty(party);
		}

		@Override
		@Transactional(readOnly=false, value="transactionManager")
		public void deleteAction(DomibusConnectorAction action) {
			LOG.trace("deleteAction: delete Action [{}]", action);
			this.actionDao.delete(
					this.actionDao.findById(action.getAction()));
			
		}

		@Override
		@Transactional(readOnly=false, value="transactionManager")
		public void createAction(DomibusConnectorAction action) {
			this.actionDao.persistNewAction(action);			
		}

		@Override
		@Transactional(readOnly=false, value="transactionManager")
		public void updateAction(String oldActionPK, DomibusConnectorAction action) {
			LOG.trace("updateAction: updateAction with");			
			this.actionDao.update(action);			
		}

		@Override
		@Transactional(readOnly=false, value="transactionManager")
		public void createService(DomibusConnectorService service) {
			LOG.trace("createService: with service [{}]", service);
			this.serviceDao.persistNewService(service);			
		}

		@Override
		@Transactional(readOnly=false, value="transactionManager")
		public void updateService(String oldServicePK, DomibusConnectorService service) {
			LOG.trace("updateService: with new service [{}]", service);
			this.serviceDao.update(service);			
		}

		@Override
		@Transactional(readOnly=false, value="transactionManager")
		public void deleteService(DomibusConnectorService service) {
			LOG.trace("deleteService: with service [{}]", service);
			DomibusConnectorService dbService = this.serviceDao.findById(service.getService());
			this.serviceDao.delete(dbService);
		}
		
}
