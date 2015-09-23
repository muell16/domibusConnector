package eu.domibus.connector.common.exception;

import eu.domibus.connector.common.DomibusApplicationContextManager;
import eu.domibus.connector.common.db.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.common.message.Message;

public class DomibusConnectorMessageException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 2899706995862182574L;

    public DomibusConnectorMessageException() {
    }

    public DomibusConnectorMessageException(Message message, Class<?> source) {
        super();
        storeException(message, this, source);
    }

    public DomibusConnectorMessageException(Message message, Throwable cause, Class<?> source) {
        super(cause);
        storeException(message, this, source);
    }

    public DomibusConnectorMessageException(Message message, String text, Class<?> source) {
        super(text);
        storeException(message, this, source);
    }

    public DomibusConnectorMessageException(Message message, String text, Throwable cause, Class<?> source) {
        super(text, cause);
        storeException(message, this, source);
    }

    private void storeException(Message message, Throwable cause, Class<?> source) {
        DomibusConnectorPersistenceService persistenceService = (DomibusConnectorPersistenceService) DomibusApplicationContextManager
                .getApplicationContext().getBean("persistenceService");
        try {
            persistenceService.persistMessageErrorFromException(message, cause, source);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

}
