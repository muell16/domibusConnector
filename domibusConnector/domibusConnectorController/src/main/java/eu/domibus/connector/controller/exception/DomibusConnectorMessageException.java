package eu.domibus.connector.controller.exception;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

public class DomibusConnectorMessageException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 2899706995862182574L;

    public DomibusConnectorMessageException() {
    }

    public DomibusConnectorMessageException(DomibusConnectorMessage message, Class<?> source) {
        super();
        storeException(message, this, source);
    }

    public DomibusConnectorMessageException(DomibusConnectorMessage message, Throwable cause, Class<?> source) {
        super(cause);
        storeException(message, this, source);
    }

    public DomibusConnectorMessageException(DomibusConnectorMessage message, String text, Class<?> source) {
        super(text);
        storeException(message, this, source);
    }

    public DomibusConnectorMessageException(DomibusConnectorMessage message, String text, Throwable cause, Class<?> source) {
        super(text, cause);
        storeException(message, this, source);
    }

    private void storeException(DomibusConnectorMessage message, Throwable cause, Class<?> source) {
//        DomibusConnectorPersistenceService persistenceService = (DomibusConnectorPersistenceService) DomibusApplicationContextManager
//                .getApplicationContext().getBean("persistenceService");
//            persistenceService.persistMessageErrorFromException(message, cause, source);
        
    }

}
