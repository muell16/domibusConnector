/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface CheckEvidencesTimoutProcessor {

    void checkEvidencesTimeout() throws DomibusConnectorControllerException;

}
