package eu.domibus.webadmin.blogic.connector.pmode.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import eu.domibus.connector.persistence.model.PDomibusConnectorAction;
import eu.domibus.connector.persistence.model.PDomibusConnectorParty;
import eu.domibus.connector.persistence.model.PDomibusConnectorPartyPK;
import eu.domibus.connector.persistence.model.PDomibusConnectorService;
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
		Map<String, PDomibusConnectorService> services = new HashMap<String, PDomibusConnectorService>();
		for(PDomibusConnectorService dbService:getServiceList()){
			services.put(dbService.getService(), dbService);
		}
		
		for(Service service:pmodes.getBusinessProcesses().getServices().getService()){
			if(!services.containsKey(service.getValue())){
				PDomibusConnectorService newService = new PDomibusConnectorService();
				newService.setService(service.getValue());
				newService.setServiceType(service.getType());
				this.serviceDao.persistNewService(newService);
                services.put(service.getValue(), newService);
			}
		}
	}

	private void importActions(Configuration pmodes) {
		HashSet<String> actions = new HashSet<String>();
		for(PDomibusConnectorAction dbAction:getActionList()){
			actions.add(dbAction.getAction());
		}
		
		for(Action action:pmodes.getBusinessProcesses().getActions().getAction()){
			if(!actions.contains(action.getValue())){
				PDomibusConnectorAction newAction = new PDomibusConnectorAction();
				newAction.setAction(action.getValue());
				newAction.setDocumentRequired(true);
				this.actionDao.persistNewAction(newAction);
                actions.add(action.getValue());
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

		Map<String,Map<String,PDomibusConnectorParty>> dbParties = new HashMap<String,Map<String,PDomibusConnectorParty>>();
		for(final PDomibusConnectorParty dbParty:getPartyList()){
			if(!dbParties.containsKey(dbParty.getPartyId())){
				dbParties.put(dbParty.getPartyId(), new HashMap<String,PDomibusConnectorParty>());
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
            Map<String, Map<String, PDomibusConnectorParty>> dbParties, String role, String partyName) {
            Identifier pId = partyIdentifiers.get(partyName);
            if(!(dbParties.containsKey(pId.getPartyId()) && dbParties.get(pId.getPartyId()).containsKey(role))){
		PDomibusConnectorParty newParty = new PDomibusConnectorParty();
		newParty.setPartyId(pId.getPartyId());
		newParty.setPartyIdType(partyIdTypes.get(pId.getPartyIdType()));
		newParty.setRole(role);
                this.partyDao.persistNewParty(newParty);
                
                //TODO: put persisted newParty into dbParties
                Map<String, PDomibusConnectorParty> partyRoleMape = dbParties.getOrDefault(pId.getPartyId(), new HashMap<>());
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
	 public List<PDomibusConnectorParty> getPartyList(){
		 return partyDao.getPartyList();
	 }
	 
	 @Override
	 public List<PDomibusConnectorAction> getActionList(){
		 return actionDao.getActionList();
	 }
	 
	 @Override
	 public List<PDomibusConnectorService> getServiceList(){
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
		public void deleteParty(PDomibusConnectorParty p) {
			LOG.trace("#deleteParty: called, use partyDao to delete");
			//domibusConnectorParty is detached so find first, and then delete...
			this.partyDao.delete(this.partyDao.findById(new PDomibusConnectorPartyPK(p.getPartyId(), p.getRole())));			
		}

		@Override
		@Transactional(readOnly=false, value="transactionManager")
		public void updateParty(PDomibusConnectorPartyPK oldPartyId, PDomibusConnectorParty updatedParty) {
			PDomibusConnectorParty dbParty = this.partyDao.findById(oldPartyId);
//			altering PK components is not allowed!			
//			dbParty.setPartyId(updatedParty.getPartyId());
//			dbParty.setRole(updatedParty.getRole());
			dbParty.setPartyIdType(updatedParty.getPartyIdType());
			this.partyDao.update(dbParty);			
		}

		@Override
		@Transactional(readOnly=false, value="transactionManager")
		public void createParty(PDomibusConnectorParty party) {
			this.partyDao.persistNewParty(party);
		}

		@Override
		@Transactional(readOnly=false, value="transactionManager")
		public void deleteAction(PDomibusConnectorAction action) {
			LOG.trace("deleteAction: delete Action [{}]", action);
			this.actionDao.delete(
					this.actionDao.findById(action.getAction()));
			
		}

		@Override
		@Transactional(readOnly=false, value="transactionManager")
		public void createAction(PDomibusConnectorAction action) {
			this.actionDao.persistNewAction(action);			
		}

		@Override
		@Transactional(readOnly=false, value="transactionManager")
		public void updateAction(String oldActionPK, PDomibusConnectorAction action) {
			LOG.trace("updateAction: updateAction with");			
			this.actionDao.update(action);			
		}

		@Override
		@Transactional(readOnly=false, value="transactionManager")
		public void createService(PDomibusConnectorService service) {
			LOG.trace("createService: with service [{}]", service);
			this.serviceDao.persistNewService(service);			
		}

		@Override
		@Transactional(readOnly=false, value="transactionManager")
		public void updateService(String oldServicePK, PDomibusConnectorService service) {
			LOG.trace("updateService: with new service [{}]", service);
			this.serviceDao.update(service);			
		}

		@Override
		@Transactional(readOnly=false, value="transactionManager")
		public void deleteService(PDomibusConnectorService service) {
			LOG.trace("deleteService: with service [{}]", service);
			PDomibusConnectorService dbService = this.serviceDao.findById(service.getService());
			this.serviceDao.delete(dbService);
		}
		
}
