package eu.ecodex.dc5.pmode;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.ErrorCode;
import eu.domibus.connector.controller.spring.ConnectorMessageProcessingProperties;

public class DCPModeException extends DomibusConnectorControllerException {

    public static final ErrorCode P_MODE_FILE_NOT_CONFIGURED = new ErrorCode("P203", String.format("For the given domain is no P-Mode file defined. Check [%s] properties", ConnectorMessageProcessingProperties.PModeConfig.PREFIX));

    public DCPModeException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DCPModeException(ErrorCode errorCode, String arg0) {
        super(errorCode, arg0);
    }

    public DCPModeException(ErrorCode errorCode, Throwable arg0) {
        super(errorCode, arg0);
    }

    public DCPModeException(ErrorCode errorCode, String arg0, Throwable arg1) {
        super(errorCode, arg0, arg1);
    }
}
