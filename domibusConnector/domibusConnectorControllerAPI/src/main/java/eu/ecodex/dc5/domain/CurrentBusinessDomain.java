package eu.ecodex.dc5.domain;

import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import org.slf4j.MDC;

import java.io.Closeable;

public class CurrentBusinessDomain {

    public static final String BUSINESS_DOMAIN_MDC_KEY = "domain";

    private static final ThreadLocal<DomibusConnectorBusinessDomain.BusinessDomainId> currentMessageLaneId = new ThreadLocal<>();

    public static DomibusConnectorBusinessDomain.BusinessDomainId getCurrentBusinessDomain() {
        return currentMessageLaneId.get();
    }

    public static void setCurrentBusinessDomain(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {
        if (businessDomainId == null) {
            MDC.remove(BUSINESS_DOMAIN_MDC_KEY);
            currentMessageLaneId.remove();
        } else {
            currentMessageLaneId.set(businessDomainId);
            MDC.put(BUSINESS_DOMAIN_MDC_KEY, businessDomainId.getBusinessDomainId());
        }
    }

    public static CloseAbleBusinessDomain setCloseAbleCurrentBusinessDomain(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {
        currentMessageLaneId.set(businessDomainId);
        return () -> {
            currentMessageLaneId.remove();
            MDC.remove(BUSINESS_DOMAIN_MDC_KEY);
        };
    }

    public interface CloseAbleBusinessDomain extends Closeable {
        public void close();
    }

}
