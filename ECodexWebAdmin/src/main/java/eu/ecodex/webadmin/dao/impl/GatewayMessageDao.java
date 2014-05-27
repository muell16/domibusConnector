package eu.ecodex.webadmin.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import eu.ecodex.webadmin.dao.IGatewayMessageDao;
import eu.ecodex.webadmin.model.gateway.TbReceiptTracking;

public class GatewayMessageDao implements IGatewayMessageDao {

    @PersistenceContext(unitName = "ecodex.webadmin")
    private EntityManager em;

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.ecodex.webadmin.dao.impl.IGatewayMessageDao#findMessagesByDate(java
     * .util.Date, java.util.Date)
     */
    @Override
    public List<TbReceiptTracking> findMessagesByDate(Date fromDate, Date toDate) {
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
            q = em.createQuery("from TbReceiptTracking m where m.LAST_TRANSMISSION >:fromDate and m.LAST_TRANSMISSION <:toDate");
            q.setParameter("fromDate", dFrom);
            q.setParameter("toDate", dTo);
        } else if (fromDate == null && toDate != null) {
            q = em.createQuery("from TbReceiptTracking m where m.LAST_TRANSMISSION <:toDate");
            q.setParameter("toDate", toDate);
        } else if (fromDate != null && toDate == null) {
            q = em.createQuery("from TbReceiptTracking m where m.LAST_TRANSMISSION >:fromDate");
            q.setParameter("fromDate", fromDate);
        } else {
            q = em.createQuery("from TbReceiptTracking m where m.LAST_TRANSMISSION >:fromDate and m.LAST_TRANSMISSION <:toDate");
            q.setParameter("fromDate", fromDate);
            q.setParameter("toDate", toDate);
        }
        List<TbReceiptTracking> resultList = q.getResultList();
        return resultList;
    }

}
