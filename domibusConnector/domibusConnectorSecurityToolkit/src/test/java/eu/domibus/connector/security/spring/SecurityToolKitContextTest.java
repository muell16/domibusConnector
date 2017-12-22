/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.security.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *  Tests if context is loading..
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    locations = {
        "/spring/context/DomibusConnectorSecurityToolkitContext.xml", 
        "/test/context/testContext.xml"
    },
    initializers = ConfigFileApplicationContextInitializer.class)
//@ActiveProfiles({"test", "db_h2"})
//@TestPropertySource("classpath:test.properties")
public class SecurityToolKitContextTest {


    @Test
    public void testContextLoads() {
    }
    
}
