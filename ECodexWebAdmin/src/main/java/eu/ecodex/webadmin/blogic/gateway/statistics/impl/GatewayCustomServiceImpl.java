package eu.ecodex.webadmin.blogic.gateway.statistics.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import eu.ecodex.webadmin.blogic.gateway.statistics.IGatewayCustomService;
import eu.ecodex.webadmin.blogic.gateway.statistics.IGatewayMessageFilter;
import eu.ecodex.webadmin.commons.BLConstants;
import eu.ecodex.webadmin.dao.IGatewayMessageDao;
import eu.ecodex.webadmin.model.gateway.TbReceiptTracking;

public class GatewayCustomServiceImpl implements Serializable, IGatewayCustomService {

    private static final long serialVersionUID = 7602105517343576780L;

    private IGatewayMessageDao gatewayMessageDao;

    private String fromParty;
    private String toParty;
    private String status;
    private String service;
    private String action;
    private Date fromDate;
    private Date toDate;
    private Integer countResult;

    private List<TbReceiptTracking> customResultList;
    private IGatewayMessageFilter gatewayMessageFilter;

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.ecodex.webadmin.blogic.gateway.statistics.impl.IGatewayCustomService
     * #generateCustomReport()
     */
    @Override
    public String generateCustomReport() {
        customResultList = gatewayMessageDao.findMessagesByDate(fromDate, toDate);

        // Apply selected Filter from View
        if (!customResultList.isEmpty()) {

            if (!BLConstants.selectorAll.equals(fromParty)) {
                customResultList = gatewayMessageFilter.filterByFromParty(fromParty, customResultList);
            }
            if (!BLConstants.selectorAll.equals(toParty)) {
                customResultList = gatewayMessageFilter.filterByToParty(toParty, customResultList);
            }
            if (!BLConstants.selectorAll.equals(status)) {
                customResultList = gatewayMessageFilter.filterByStatus(status, customResultList);
            }
            if (!BLConstants.selectorAll.equals(service)) {
                customResultList = gatewayMessageFilter.filterByService(service, customResultList);
            }
            if (!BLConstants.selectorAll.equals(action)) {
                customResultList = gatewayMessageFilter.filterByAction(action, customResultList);
            }
        }
        countResult = customResultList.size();
        return "/pages/gateway-statistics.xhtml";
    }

    public IGatewayMessageDao getGatewayMessageDao() {
        return gatewayMessageDao;
    }

    public void setGatewayMessageDao(IGatewayMessageDao gatewayMessageDao) {
        this.gatewayMessageDao = gatewayMessageDao;
    }

    public String getFromParty() {
        return fromParty;
    }

    public void setFromParty(String fromParty) {
        this.fromParty = fromParty;
    }

    public String getToParty() {
        return toParty;
    }

    public void setToParty(String toParty) {
        this.toParty = toParty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Integer getCountResult() {
        return countResult;
    }

    public void setCountResult(Integer countResult) {
        this.countResult = countResult;
    }

    public List<TbReceiptTracking> getCustomResultList() {
        return customResultList;
    }

    public void setCustomResultList(List<TbReceiptTracking> customResultList) {
        this.customResultList = customResultList;
    }

    public IGatewayMessageFilter getGatewayMessageFilter() {
        return gatewayMessageFilter;
    }

    public void setGatewayMessageFilter(IGatewayMessageFilter gatewayMessageFilter) {
        this.gatewayMessageFilter = gatewayMessageFilter;
    }

}
