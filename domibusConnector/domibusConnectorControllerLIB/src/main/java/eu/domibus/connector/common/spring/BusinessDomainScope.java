package eu.domibus.connector.common.spring;

import eu.domibus.connector.common.service.CurrentBusinessDomain;
import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import javax.enterprise.inject.spi.Bean;
import java.util.HashMap;
import java.util.Map;


public class BusinessDomainScope implements Scope {

    private Map<DomibusConnectorMessageLane.MessageLaneId, BeanStore> businessDomainToBeanStore = new HashMap<>();

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        BeanStore beanStore = getBeanStore();
        return beanStore.get(name, objectFactory);
    }

    @Override
    public Object remove(String name) {
        return getBeanStore().remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        getBeanStore().registerDestructionCallback(name, callback);
    }

    @Override
    public Object resolveContextualObject(String key) {
        return CurrentBusinessDomain.getCurrentBusinessDomain();
    }

    @Override
    public String getConversationId() {
        return null;
    }

    private synchronized BeanStore getBeanStore() {
        DomibusConnectorMessageLane.MessageLaneId currentBusinessDomain = CurrentBusinessDomain.getCurrentBusinessDomain();
        if (currentBusinessDomain == null) {
            throw new IllegalStateException("There is currently no business domain scope active!");
        }
        BeanStore beanStore = businessDomainToBeanStore.get(currentBusinessDomain);
        if (beanStore == null) {
            beanStore = new BeanStore();
            businessDomainToBeanStore.put(currentBusinessDomain, beanStore);
        }
        return beanStore;
    }
}
