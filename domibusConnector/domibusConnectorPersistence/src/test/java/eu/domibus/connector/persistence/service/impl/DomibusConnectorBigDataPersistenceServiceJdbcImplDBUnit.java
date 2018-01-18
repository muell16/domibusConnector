/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.persistence.service.CommonPersistenceDBUnitITCase;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
public class DomibusConnectorBigDataPersistenceServiceJdbcImplDBUnit {

    private final static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorBigDataPersistenceServiceJdbcImplDBUnit.class);

    //static ConfigurableApplicationContext APPLICATION_CONTEXT;
  
    @BeforeClass
    public static void InitClass() {
        
        
        
    }
        
    @Before
    public void setUp() {
        //TODO: setUp Database for testing!
    }

    @Test
    public void testSetDataSource() {
    }

    @Test
    public void testPostConstruct() {
    }

    @Test
    public void testGetReadableDataSource() {
    }

    @Test
    public void testCreateDomibusConnectorBigDataReference() {
    }

    @Test
    public void testDeleteDomibusConnectorBigDataReference() {
    }
    
}
