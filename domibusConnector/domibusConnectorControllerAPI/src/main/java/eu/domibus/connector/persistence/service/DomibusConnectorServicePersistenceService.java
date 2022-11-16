
package eu.domibus.connector.persistence.service;

import eu.ecodex.dc5.message.model.DC5Service;
import java.util.List;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 * @deprecated use DomibusConnectorPModeService instead!
 */
@Deprecated
public interface DomibusConnectorServicePersistenceService {

    public DC5Service persistNewService(DC5Service newService);
    
    public List<DC5Service> getServiceList();
    
    public DC5Service updateService(DC5Service oldService, DC5Service newService);
    
    public void deleteService(DC5Service service);

    public DC5Service getService(String service);

	List<String> getServiceListString();

}
