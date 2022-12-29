package eu.ecodex.dc5.domain;

import eu.domibus.connector.domain.model.DC5BusinessDomain;
import org.slf4j.MDC;

import java.io.Closeable;

public class CurrentBusinessDomain {

    public static final String BUSINESS_DOMAIN_MDC_KEY = "domain";

    private static final ThreadLocal<DC5BusinessDomain.BusinessDomainId> currentMessageLaneId = new ThreadLocal<>();

    public static DC5BusinessDomain.BusinessDomainId getCurrentBusinessDomain() {
        return currentMessageLaneId.get();
    }

    public static void setCurrentBusinessDomain(DC5BusinessDomain.BusinessDomainId businessDomainId) {
        if (businessDomainId == null) {
            MDC.remove(BUSINESS_DOMAIN_MDC_KEY);
            currentMessageLaneId.remove();
        } else {
            currentMessageLaneId.set(businessDomainId);
            MDC.put(BUSINESS_DOMAIN_MDC_KEY, businessDomainId.getBusinessDomainId());
        }
    }

    public static void clear() {
        setCurrentBusinessDomain(null);
    }

    public static CloseAbleBusinessDomain setCloseAbleCurrentBusinessDomain(DC5BusinessDomain.BusinessDomainId businessDomainId) {
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
