package eu.domibus.connector.common;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NationalImplementationFactory {

    static final Logger LOGGER = LoggerFactory.getLogger(NationalImplementationFactory.class);

    protected String implementationClassName;
    protected String implClassPropertyName;
    protected String defaultImplementationClassName;
    protected Map<String, Boolean> preConditions = new HashMap<String, Boolean>();

    public void setImplementationClassName(String implementationClassName) {
        this.implementationClassName = implementationClassName;
    }

    public void setImplClassPropertyName(String implClassPropertyName) {
        this.implClassPropertyName = implClassPropertyName;
    }

    public void setDefaultImplementationClassName(String defaultImplementationClassName) {
        this.defaultImplementationClassName = defaultImplementationClassName;
    }

    public Map<String, Boolean> getPreConditions() {
        return preConditions;
    }

    public void setPreConditions(Map<String, Boolean> preConditions) {
        this.preConditions = preConditions;
    }

    public Object createImplementation() throws Exception {
        Object impl = null;
        if (checkPreConditions()) {
            LOGGER.info("Creating implementation " + implementationClassName);
            try {
                impl = Class.forName(implementationClassName).getConstructor().newInstance();
                if (impl != null)
                    LOGGER.info("Implementaion of type {} successfully created.", implementationClassName);
            } catch (Exception e) {
                LOGGER.error(
                        "Could not create an instance of implementation with full qualified class name {}. Please check the property '{}' in your configuration!",
                        implementationClassName, implClassPropertyName);
                LOGGER.error("", e);
            }
        }

        if (impl == null)
            impl = createDefaultImplementation();

        return impl;
    }

    private boolean checkPreConditions() {
        boolean preConditionsFullfilled = true;
        if (!preConditions.isEmpty()) {
            for (String key : preConditions.keySet()) {
                if (!preConditions.get(key)) {
                    LOGGER.warn("Precondition property {} not true!", key);
                    preConditionsFullfilled = false;
                }
            }
        }
        if (preConditionsFullfilled && (implementationClassName == null || implementationClassName.isEmpty())) {
            LOGGER.info(
                    "There is no value for the property '{}' given in your configuration. Default implementation will be instantiated.",
                    implClassPropertyName);
            preConditionsFullfilled = false;
        }

        return preConditionsFullfilled;
    }

    private Object createDefaultImplementation() throws Exception {
        Object impl = null;
        LOGGER.info("Creating default implementation " + defaultImplementationClassName);
        try {
            impl = Class.forName(defaultImplementationClassName).getConstructor().newInstance();
        } catch (Exception e) {
            LOGGER.error("Could not create an instance of the default implementation with full qualified class name "
                    + defaultImplementationClassName, e);
        }
        return impl;
    }

}
