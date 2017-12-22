/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.dao;

import eu.domibus.connector.persistence.model.DomibusConnectorService;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface DomibusConnectorServiceDao extends CrudRepository<DomibusConnectorService, String> {

}
