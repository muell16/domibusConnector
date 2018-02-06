
package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface CheckEvidencesTimeoutProcessor {

    void checkEvidencesTimeout() throws DomibusConnectorControllerException;

}
