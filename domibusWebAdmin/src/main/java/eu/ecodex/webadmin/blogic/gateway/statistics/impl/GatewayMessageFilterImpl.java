package eu.ecodex.webadmin.blogic.gateway.statistics.impl;

import java.util.ArrayList;
import java.util.List;

import eu.ecodex.webadmin.blogic.gateway.statistics.IGatewayMessageFilter;
import eu.ecodex.webadmin.model.gateway.TbReceiptTracking;

public class GatewayMessageFilterImpl implements IGatewayMessageFilter {

    // AT_IT_EPO_RelayREMMDAcceptanceRejection

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.ecodex.webadmin.blogic.gateway.statistics.impl.IGatewayMessageFilter
     * #filterByFromParty(java.lang.String, java.util.List)
     */
    @Override
    public List<TbReceiptTracking> filterByFromParty(String fromParty, List<TbReceiptTracking> customResultList) {

        List<TbReceiptTracking> resultList = new ArrayList<TbReceiptTracking>();

        for (TbReceiptTracking tbReceiptTracking : customResultList) {
            if (tbReceiptTracking.getPMODE() != null && tbReceiptTracking.getPMODE().length() > 2) {
                String fromPartyDB = tbReceiptTracking.getPMODE().substring(0, 2);
                if (fromParty.equals(fromPartyDB)) {
                    resultList.add(tbReceiptTracking);
                }
            }
        }
        return resultList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.ecodex.webadmin.blogic.gateway.statistics.impl.IGatewayMessageFilter
     * #filterByToParty(java.lang.String, java.util.List)
     */
    @Override
    public List<TbReceiptTracking> filterByToParty(String toParty, List<TbReceiptTracking> customResultList) {

        List<TbReceiptTracking> resultList = new ArrayList<TbReceiptTracking>();

        for (TbReceiptTracking tbReceiptTracking : customResultList) {
            if (tbReceiptTracking.getPMODE() != null && tbReceiptTracking.getPMODE().length() > 5) {
                String toPartyDB = tbReceiptTracking.getPMODE().substring(3, 5);
                if (toParty.equals(toPartyDB)) {
                    resultList.add(tbReceiptTracking);
                }
            }
        }
        return resultList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.ecodex.webadmin.blogic.gateway.statistics.impl.IGatewayMessageFilter
     * #filterByStatus(java.lang.String, java.util.List)
     */
    @Override
    public List<TbReceiptTracking> filterByStatus(String status, List<TbReceiptTracking> customResultList) {

        List<TbReceiptTracking> resultList = new ArrayList<TbReceiptTracking>();

        for (TbReceiptTracking tbReceiptTracking : customResultList) {
            if (tbReceiptTracking.getSTATUS() != null) {
                if (tbReceiptTracking.getSTATUS().equals(status)) {
                    resultList.add(tbReceiptTracking);
                }
            }
        }
        return resultList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.ecodex.webadmin.blogic.gateway.statistics.impl.IGatewayMessageFilter
     * #filterByService(java.lang.String, java.util.List)
     */
    @Override
    public List<TbReceiptTracking> filterByService(String service, List<TbReceiptTracking> customResultList) {

        List<TbReceiptTracking> resultList = new ArrayList<TbReceiptTracking>();

        for (TbReceiptTracking tbReceiptTracking : customResultList) {
            if (tbReceiptTracking.getPMODE() != null && tbReceiptTracking.getPMODE().length() > 1) {
                if (tbReceiptTracking.getPMODE().indexOf(service) != -1) {
                    resultList.add(tbReceiptTracking);
                }
            }
        }
        return resultList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.ecodex.webadmin.blogic.gateway.statistics.impl.IGatewayMessageFilter
     * #filterByAction(java.lang.String, java.util.List)
     */
    @Override
    public List<TbReceiptTracking> filterByAction(String action, List<TbReceiptTracking> customResultList) {

        List<TbReceiptTracking> resultList = new ArrayList<TbReceiptTracking>();

        for (TbReceiptTracking tbReceiptTracking : customResultList) {
            if (tbReceiptTracking.getPMODE() != null && tbReceiptTracking.getPMODE().length() > 1) {
                if (tbReceiptTracking.getPMODE().indexOf(action) != -1) {
                    resultList.add(tbReceiptTracking);
                }
            }
        }
        return resultList;
    }

}
