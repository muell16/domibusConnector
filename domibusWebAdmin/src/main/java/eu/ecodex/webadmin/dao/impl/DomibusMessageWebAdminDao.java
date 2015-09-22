package eu.ecodex.webadmin.dao.impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.domibus.connector.common.db.model.DomibusConnectorMessageInfo;
import eu.domibus.connector.common.db.model.DomibusConnectorService;
import eu.domibus.connector.common.enums.MessageDirection;
import eu.ecodex.webadmin.dao.IDomibusMessageWebAdminDao;

@Transactional(readOnly=true, value="transactionManager")
public class DomibusMessageWebAdminDao implements IDomibusMessageWebAdminDao, Serializable {

    private static final long serialVersionUID = 6927282911714964185L;

    @PersistenceContext(unitName = "domibus.connector")
    private EntityManager em;

    @Override
    public Long countOutgoingMessages() {

        Query query = em.createQuery("select count(*) from DomibusConnectorMessage m where m.direction=:direction");
        query.setParameter("direction", MessageDirection.NAT_TO_GW);

        return (Long) query.getSingleResult();
    }

    @Override
    public Long countIncomingMessages() {

        Query query = em.createQuery("select count(*) from DomibusConnectorMessage m where m.direction=:direction");
        query.setParameter("direction", MessageDirection.GW_TO_NAT);

        return (Long) query.getSingleResult();
    }

    @Override
    public HashMap<String, Long> countService(String service) {
        HashMap<String, Long> serviceMap = new HashMap<String, Long>();
        Query query = em.createQuery("select count(*) from DomibusConnectorMessageInfo m where m.service=:service");
        DomibusConnectorService eCodexService = new DomibusConnectorService();
        eCodexService.setService(service);
        query.setParameter("service", eCodexService);
        serviceMap.put(service, (Long) query.getSingleResult());
        return serviceMap;
    }
    
    @Override
    public HashMap<String, Long> countUndefinedService() {
        HashMap<String, Long> serviceMap = new HashMap<String, Long>();
        Query query = em.createQuery("select count(*) from DomibusConnectorMessageInfo m where m.service is null");
        serviceMap.put("Undefined", (Long) query.getSingleResult());
        return serviceMap;
    }
    


    @SuppressWarnings("unchecked")
    @Override
    public List<DomibusConnectorMessageInfo> findMessageByDate(Date fromDate, Date toDate) {
        Query q;
        if (fromDate == null && toDate == null) {
            // Search without parameter is limited to a year!
            Calendar cFrom = Calendar.getInstance();
            cFrom = Calendar.getInstance();
            cFrom.add(Calendar.YEAR, -100);
            Date dFrom = cFrom.getTime();
            Calendar cTo = Calendar.getInstance();
            cTo = Calendar.getInstance();
            Date dTo = cTo.getTime();
            q = em.createQuery("from DomibusConnectorMessageInfo m where m.created >=:fromDate and m.created <=:toDate");
            q.setParameter("fromDate", dFrom);
            q.setParameter("toDate", dTo);
        } else if (fromDate == null && toDate != null) {
            Calendar cTo = Calendar.getInstance();
            cTo.setTime(toDate);
            cTo.add(Calendar.DAY_OF_MONTH, 1);
            toDate = cTo.getTime();
            q = em.createQuery("from DomibusConnectorMessageInfo m where m.created <=:toDate");
            q.setParameter("toDate", toDate);
        } else if (fromDate != null && toDate == null) {
            q = em.createQuery("from DomibusConnectorMessageInfo m where m.created >=:fromDate");
            q.setParameter("fromDate", fromDate);
        } else {
            Calendar cTo = Calendar.getInstance();
            cTo.setTime(toDate);
            cTo.add(Calendar.DAY_OF_MONTH, 1);
            toDate = cTo.getTime();
            q = em.createQuery("from DomibusConnectorMessageInfo m where m.created >=:fromDate and m.created <=:toDate");
            q.setParameter("fromDate", fromDate);
            q.setParameter("toDate", toDate);
        }

        return q.getResultList();
    }

}
