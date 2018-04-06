
package eu.domibus.connector.backend.persistence.model.testutil;

import eu.domibus.connector.backend.persistence.model.BackendClientInfo;
import eu.domibus.connector.persistence.model.PDomibusConnectorService;
import eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class BackendPersistenceEntityCreator {
    
    private static Set<PDomibusConnectorService> createServiceSet() {
        Set<PDomibusConnectorService> services = new HashSet<>();
        
        services.add(PersistenceEntityCreator.createServiceEPO());
        services.add(PersistenceEntityCreator.createServicePing());
        
        return services;
        
    }
            
    public static BackendClientInfo createBackendClientInfoBob() {
        BackendClientInfo bob = new BackendClientInfo();
        bob.setBackendName("bob");
        bob.setBackendKeyAlias("bob");
                
        bob.setServices(createServiceSet());
        
        return bob;
    }

}
