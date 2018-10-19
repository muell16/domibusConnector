
package eu.domibus.connector.controller.processor.evidencetimeout;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface CheckEvidencesTimeoutProcessor {

    void checkEvidencesTimeout() throws DomibusConnectorControllerException;

}
