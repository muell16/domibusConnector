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

import eu.ecodex.connector.common.db.model.ECodexMessageInfo;
import eu.ecodex.connector.common.db.model.ECodexService;
import eu.ecodex.connector.common.enums.ECodexMessageDirection;
import eu.ecodex.webadmin.dao.IECodexMessageWebAdminDao;

@Repository
public class ECodexMessageWebAdminDao implements IECodexMessageWebAdminDao, Serializable {

    private static final long serialVersionUID = 6927282911714964185L;

    @PersistenceContext(unitName = "ecodex.connector")
    private EntityManager em;

    @Override
    public Long countOutgoingMessages() {

        Query query = em.createQuery("select count(*) from ECodexMessage m where m.direction=:direction");
        query.setParameter("direction", ECodexMessageDirection.NAT_TO_GW);

        return (Long) query.getSingleResult();
    }

    @Override
    public Long countIncomingMessages() {

        Query query = em.createQuery("select count(*) from ECodexMessage m where m.direction=:direction");
        query.setParameter("direction", ECodexMessageDirection.GW_TO_NAT);

        return (Long) query.getSingleResult();
    }

    @Override
    public HashMap<String, Long> countService() {
        HashMap<String, Long> serviceMap = new HashMap<String, Long>();
        Query query = em.createQuery("select count(*) from ECodexMessageInfo m where m.service=:service");
        ECodexService eCodexService = new ECodexService();
        eCodexService.setService("EPO");
        query.setParameter("service", eCodexService);
        serviceMap.put("EPO", (Long) query.getSingleResult());
        query = em.createQuery("select count(*) from ECodexMessageInfo m where m.service is null");
        serviceMap.put("Undefined", (Long) query.getSingleResult());
        return serviceMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ECodexMessageInfo> findMessageByDate(Date fromDate, Date toDate) {
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
            q = em.createQuery("from ECodexMessageInfo m where m.created >:fromDate and m.created <:toDate");
            q.setParameter("fromDate", dFrom);
            q.setParameter("toDate", dTo);
        } else if (fromDate == null && toDate != null) {
            q = em.createQuery("from ECodexMessageInfo m where m.created <:toDate");
            q.setParameter("toDate", toDate);
        } else if (fromDate != null && toDate == null) {
            q = em.createQuery("from ECodexMessageInfo m where m.created >:fromDate");
            q.setParameter("fromDate", fromDate);
        } else {
            q = em.createQuery("from ECodexMessageInfo m where m.created >:fromDate and m.created <:toDate");
            q.setParameter("fromDate", fromDate);
            q.setParameter("toDate", toDate);
        }

        return q.getResultList();
    }

}
