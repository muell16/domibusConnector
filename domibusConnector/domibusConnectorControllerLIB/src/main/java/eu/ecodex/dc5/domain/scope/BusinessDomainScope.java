package eu.ecodex.dc5.domain.scope;

import eu.ecodex.dc5.domain.BusinessDomainConfigurationChange;
import eu.ecodex.dc5.domain.CurrentBusinessDomain;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.context.ApplicationListener;

import java.util.HashMap;
import java.util.Map;


public class BusinessDomainScope implements Scope, ApplicationListener<BusinessDomainConfigurationChange> {

    private Map<DC5BusinessDomain.BusinessDomainId, BeanStore> businessDomainToBeanStore = new HashMap<>();

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
        DC5BusinessDomain.BusinessDomainId currentBusinessDomain = CurrentBusinessDomain.getCurrentBusinessDomain();
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

    @Override
    public void onApplicationEvent(BusinessDomainConfigurationChange event) {
        DC5BusinessDomain.BusinessDomainId businessDomainId = event.getBusinessDomainId();
        destroyBeanStoreForBusinessScope(businessDomainId);
    }

    private synchronized void destroyBeanStoreForBusinessScope(DC5BusinessDomain.BusinessDomainId businessDomainId) {
        BeanStore beanStore = businessDomainToBeanStore.get(businessDomainId);
        if (beanStore != null) {
            beanStore.destroy();
            businessDomainToBeanStore.remove(businessDomainId);
        }
    }
}
