package eu.ecodex.webadmin.blogic.gateway.statistics;

import java.util.List;

import eu.ecodex.webadmin.model.gateway.TbReceiptTracking;

public interface IGatewayMessageFilter {

    public List<TbReceiptTracking> filterByFromParty(String fromParty, List<TbReceiptTracking> customResultList);

    public List<TbReceiptTracking> filterByToParty(String toParty, List<TbReceiptTracking> customResultList);

    public List<TbReceiptTracking> filterByStatus(String status, List<TbReceiptTracking> customResultList);

    public List<TbReceiptTracking> filterByService(String service, List<TbReceiptTracking> customResultList);

    public List<TbReceiptTracking> filterByAction(String action, List<TbReceiptTracking> customResultList);

}