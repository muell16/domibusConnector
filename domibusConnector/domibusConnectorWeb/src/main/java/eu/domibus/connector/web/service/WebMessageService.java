package eu.domibus.connector.web.service;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.domibus.connector.persistence.service.web.DomibusConnectorWebMessagePersistenceService;
import eu.domibus.connector.web.dto.WebMessage;

@Service("webMessageService")
public class WebMessageService {

	private DomibusConnectorWebMessagePersistenceService messagePersistenceService;
	
	@Autowired
	public void setMessagePersistenceService(DomibusConnectorWebMessagePersistenceService messagePersistenceService) {
		this.messagePersistenceService = messagePersistenceService;
	}
	
	public LinkedList<WebMessage> getInitialList(){
		return messagePersistenceService.getAllMessages();
	}

}
