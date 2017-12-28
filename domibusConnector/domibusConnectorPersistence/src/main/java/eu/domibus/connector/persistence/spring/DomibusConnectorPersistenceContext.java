/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.spring;

import eu.domibus.connector.persistence.dao.PDomibusConnectorRepositories;
import eu.domibus.connector.persistence.model.PDomibusConnectorPersistenceModel;
import eu.domibus.connector.persistence.service.impl.DomibusConnectorPersistenceServiceImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
@EntityScan(basePackageClasses={PDomibusConnectorPersistenceModel.class})
@EnableJpaRepositories(basePackageClasses = {PDomibusConnectorRepositories.class} )
@EnableTransactionManagement
public class DomibusConnectorPersistenceContext {

}
