package eu.domibus.webadmin.blogic.connector.statistics.impl;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import eu.domibus.connector.common.db.model.DomibusConnectorEvidence;
import eu.domibus.connector.common.db.model.DomibusConnectorMessageInfo;
import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorService;
import eu.domibus.webadmin.blogic.connector.statistics.IConnectorCustomService;
import eu.domibus.webadmin.blogic.connector.statistics.IConnectorMessageFilter;
import eu.domibus.webadmin.commons.BLConstants;
import eu.domibus.webadmin.dao.IDomibusMessageWebAdminDao;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorActionDao;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorPartyDao;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorServiceDao;
import eu.domibus.webadmin.model.connector.MessageReportDO;

@Service
public class ConnectorCustomServiceImpl implements IConnectorCustomService, Serializable {

    private static final long serialVersionUID = 5288892319790964868L;

    @Autowired
    private IDomibusMessageWebAdminDao domibusMessageWebAdminDao;
    @Autowired
    private IDomibusWebAdminConnectorPartyDao domibusWebAdminConnectorPartyDao;
    @Autowired
    private IDomibusWebAdminConnectorServiceDao domibusWebAdminConnectorServiceDao;
    @Autowired
    private IDomibusWebAdminConnectorActionDao domibusWebAdminConnectorActionDao;
    @Autowired
    private IConnectorMessageFilter connectorMessageFilter;

    private List<String> fromPartyList;
    private List<String> toPartyList;
    private List<String> serviceList;
    private List<String> actionList;
    private String selectedFromParty;
    private String selectedToParty;
    private String selectedService;
    private String direction;
    private String status;
    private String selectedAction;
    private String evidence;
    private Date fromDate;
    private Date toDate;
    private Integer countResult;

    // The List, which is displayed as result table in main.xhtml
    private List<MessageReportDO> customResultList;

    // Selected entry in result table, necessary for evidence history    
    private MessageReportDO selectedMessageReportDO;

    @PostConstruct
    public void init() {
        List<DomibusConnectorParty> resultListParty = domibusWebAdminConnectorPartyDao.getPartyList();
        List<DomibusConnectorService> resultListService = domibusWebAdminConnectorServiceDao.getServiceList();
        List<DomibusConnectorAction> resultListAction = domibusWebAdminConnectorActionDao.getActionList();
        fromPartyList = new ArrayList<String>();
        toPartyList = new ArrayList<String>();
        serviceList = new ArrayList<String>();
        actionList = new ArrayList<String>();
        fromPartyList.add(BLConstants.selectorAll);
        toPartyList.add(BLConstants.selectorAll);
        serviceList.add(BLConstants.selectorAll);
        actionList.add(BLConstants.selectorAll);
        for (DomibusConnectorParty domibusConnectorParty : resultListParty) {
            fromPartyList.add(domibusConnectorParty.getPartyId());
            toPartyList.add(domibusConnectorParty.getPartyId());
        }

        for (DomibusConnectorService domibusConnectorService : resultListService) {
            serviceList.add(domibusConnectorService.getService());
        }

        for (DomibusConnectorAction domibusConnectorAction : resultListAction) {
            actionList.add(domibusConnectorAction.getAction());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.domibus.webadmin.blogic.impl.ICustomServiceImpl#generateCustomReport()
     */
    @Override
    public String generateCustomReport() {

        List<DomibusConnectorMessageInfo> resultList = domibusMessageWebAdminDao.findMessageByDate(fromDate, toDate);

        customResultList = new ArrayList<MessageReportDO>();

        // Convert to MessageReportDO and generate Evidence History
        for (DomibusConnectorMessageInfo domibusConnectorMessageInfo : resultList) {
            MessageReportDO messageReportDO = new MessageReportDO(domibusConnectorMessageInfo);
            generateEvidenceHistory(messageReportDO);
            customResultList.add(messageReportDO);
        }

        // Apply selected Filter from View
        if (!customResultList.isEmpty()) {

            if (!StringUtils.isEmpty(selectedFromParty) && !BLConstants.selectorAll.equals(selectedFromParty)) {
                customResultList = connectorMessageFilter.filterByFromParty(selectedFromParty, customResultList);
            }
            if (!StringUtils.isEmpty(selectedToParty) && !BLConstants.selectorAll.equals(selectedToParty)) {
                customResultList = connectorMessageFilter.filterByToParty(selectedToParty, customResultList);
            }
            if (!StringUtils.isEmpty(direction) && !BLConstants.selectorAll.equals(direction)) {
                customResultList = connectorMessageFilter.filterByDirection(direction, customResultList);
            }
            if (!StringUtils.isEmpty(status) && !BLConstants.selectorAll.equals(status)) {
                customResultList = connectorMessageFilter.filterByStatus(status, customResultList);
            }
            if (!StringUtils.isEmpty(selectedService) && !BLConstants.selectorAll.equals(selectedService)) {
                customResultList = connectorMessageFilter.filterByService(selectedService, customResultList);
            }
            if (!StringUtils.isEmpty(selectedAction) && !BLConstants.selectorAll.equals(selectedAction)) {
                customResultList = connectorMessageFilter.filterByAction(selectedAction, customResultList);
            }
            if (!StringUtils.isEmpty(evidence) && !BLConstants.selectorAll.equals(evidence)) {
                customResultList = connectorMessageFilter.filterByLastEvidence(evidence, customResultList);
            }

            countResult = customResultList.size();

        }

        return "/pages/connector-statistics.xhtml";
    }

    /**
     * The history of Evidences taken from ECODEX_EVIDENCES are conditioned to a
     * date sorted String representation
     *
     * @param messageReportDO
     */
    private void generateEvidenceHistory(MessageReportDO messageReportDO) {

        List<DomibusConnectorEvidence> evidenceList = new ArrayList<DomibusConnectorEvidence>();

        Set<DomibusConnectorEvidence> evidences = messageReportDO.getMessage().getEvidences();

        for (DomibusConnectorEvidence eCodexEvidence : evidences) {
            evidenceList.add(eCodexEvidence);
        }

        if (!evidenceList.isEmpty()) {
            Comparator<DomibusConnectorEvidence> comp = new Comparator<DomibusConnectorEvidence>() {
                @Override
                public int compare(DomibusConnectorEvidence e1, DomibusConnectorEvidence e2) {
                    return e1.getUpdated().compareTo(e2.getUpdated());
                }
            };

            Collections.sort(evidenceList, comp);
            messageReportDO.setEvidenceList(evidenceList);
            // Set Last Evidence
            DomibusConnectorEvidence lastEvidence = evidenceList.get(evidenceList.size() - 1);
            messageReportDO.setLastEvidenceType(lastEvidence.getType().toString());
            String history = "";
            for (DomibusConnectorEvidence eCodexEvidence : evidenceList) {
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                history += df.format(eCodexEvidence.getUpdated()) + " - " + eCodexEvidence.getType() + "<br/>";
            }

            messageReportDO.setEvidenceHistory(history);
        }

    }

    public IDomibusMessageWebAdminDao getDomibusMessageWebAdminDao() {
        return domibusMessageWebAdminDao;
    }

    public void setDomibusMessageWebAdminDao(
            IDomibusMessageWebAdminDao domibusMessageWebAdminDao) {
        this.domibusMessageWebAdminDao = domibusMessageWebAdminDao;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public IDomibusWebAdminConnectorActionDao getDomibusWebAdminConnectorActionDao() {
        return domibusWebAdminConnectorActionDao;
    }

    public void setDomibusWebAdminConnectorActionDao(
            IDomibusWebAdminConnectorActionDao domibusWebAdminConnectorActionDao) {
        this.domibusWebAdminConnectorActionDao = domibusWebAdminConnectorActionDao;
    }

    public List<String> getActionList() {
        return actionList;
    }

    public void setActionList(List<String> actionList) {
        this.actionList = actionList;
    }

    public String getSelectedAction() {
        return selectedAction;
    }

    public void setSelectedAction(String selectedAction) {
        this.selectedAction = selectedAction;
    }

    @Override
    public List<MessageReportDO> getCustomResultList() {
        return customResultList;
    }

    public void setCustomResultList(List<MessageReportDO> customResultList) {
        this.customResultList = customResultList;
    }

    public Integer getCountResult() {
        return countResult;
    }

    public void setCountResult(Integer countResult) {
        this.countResult = countResult;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public MessageReportDO getSelectedMessageReportDO() {
        return selectedMessageReportDO;
    }

    public void setSelectedMessageReportDO(MessageReportDO selectedMessageReportDO) {
        this.selectedMessageReportDO = selectedMessageReportDO;
    }

    public IConnectorMessageFilter getConnectorMessageFilter() {
        return connectorMessageFilter;
    }

    public void setConnectorMessageFilter(IConnectorMessageFilter connectorMessageFilter) {
        this.connectorMessageFilter = connectorMessageFilter;
    }

    public String getSelectedFromParty() {
        return selectedFromParty;
    }

    public void setSelectedFromParty(String selectedFromParty) {
        this.selectedFromParty = selectedFromParty;
    }

    public IDomibusWebAdminConnectorPartyDao getDomibusWebAdminConnectorPartyDao() {
        return domibusWebAdminConnectorPartyDao;
    }

    public void setDomibusWebAdminConnectorPartyDao(
            IDomibusWebAdminConnectorPartyDao domibusWebAdminConnectorPartyDao) {
        this.domibusWebAdminConnectorPartyDao = domibusWebAdminConnectorPartyDao;
    }

    public List<String> getFromPartyList() {
        return fromPartyList;
    }

    public void setFromPartyList(List<String> fromPartyList) {
        this.fromPartyList = fromPartyList;
    }

    public List<String> getToPartyList() {
        return toPartyList;
    }

    public void setToPartyList(List<String> toPartyList) {
        this.toPartyList = toPartyList;
    }

    public String getSelectedToParty() {
        return selectedToParty;
    }

    public void setSelectedToParty(String selectedToParty) {
        this.selectedToParty = selectedToParty;
    }

    public IDomibusWebAdminConnectorServiceDao getDomibusWebAdminConnectorServiceDao() {
        return domibusWebAdminConnectorServiceDao;
    }

    public void setDomibusWebAdminConnectorServiceDao(
            IDomibusWebAdminConnectorServiceDao domibusWebAdminConnectorServiceDao) {
        this.domibusWebAdminConnectorServiceDao = domibusWebAdminConnectorServiceDao;
    }

    public List<String> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<String> serviceList) {
        this.serviceList = serviceList;
    }

    public String getSelectedService() {
        return selectedService;
    }

    public void setSelectedService(String selectedService) {
        this.selectedService = selectedService;
    }

}
