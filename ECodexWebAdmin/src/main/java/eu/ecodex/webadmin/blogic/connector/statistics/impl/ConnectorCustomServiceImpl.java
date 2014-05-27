package eu.ecodex.webadmin.blogic.connector.statistics.impl;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import eu.ecodex.connector.common.db.model.ECodexEvidence;
import eu.ecodex.connector.common.db.model.ECodexMessageInfo;
import eu.ecodex.webadmin.blogic.connector.statistics.IConnectorCustomService;
import eu.ecodex.webadmin.blogic.connector.statistics.IConnectorMessageFilter;
import eu.ecodex.webadmin.commons.BLConstants;
import eu.ecodex.webadmin.dao.IECodexMessageWebAdminDao;
import eu.ecodex.webadmin.model.connector.MessageReportDO;

public class ConnectorCustomServiceImpl implements IConnectorCustomService, Serializable {

    private static final long serialVersionUID = 5288892319790964868L;

    private IECodexMessageWebAdminDao eCodexMessageWebAdminDao;
    private IConnectorMessageFilter connectorMessageFilter;

    private String fromParty;
    private String toParty;
    private String direction;
    private String status;
    private String service;
    private String action;
    private String evidence;
    private Date fromDate;
    private Date toDate;
    private Integer countResult;

    // The List, which is displayed as result table in main.xhtml
    private List<MessageReportDO> customResultList;

    // Selected entry in result table, necessary for evidence history
    private MessageReportDO selectedMessageReportDO;

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.ecodex.webadmin.blogic.impl.ICustomServiceImpl#generateCustomReport()
     */
    @Override
    public String generateCustomReport() {

        List<ECodexMessageInfo> resultList = eCodexMessageWebAdminDao.findMessageByDate(fromDate, toDate);

        customResultList = new ArrayList<MessageReportDO>();

        // Convert to MessageReportDO and generate Evidence History
        for (ECodexMessageInfo eCodexMessageInfo : resultList) {
            MessageReportDO messageReportDO = new MessageReportDO(eCodexMessageInfo);
            generateEvidenceHistory(messageReportDO);
            customResultList.add(messageReportDO);
        }

        // Apply selected Filter from View
        if (!customResultList.isEmpty()) {

            if (!BLConstants.selectorAll.equals(fromParty)) {
                customResultList = connectorMessageFilter.filterByFromParty(fromParty, customResultList);
            }
            if (!BLConstants.selectorAll.equals(toParty)) {
                customResultList = connectorMessageFilter.filterByToParty(toParty, customResultList);
            }
            if (!BLConstants.selectorAll.equals(direction)) {
                customResultList = connectorMessageFilter.filterByDirection(direction, customResultList);
            }
            if (!BLConstants.selectorAll.equals(status)) {
                customResultList = connectorMessageFilter.filterByStatus(status, customResultList);
            }
            if (!BLConstants.selectorAll.equals(service)) {
                customResultList = connectorMessageFilter.filterByService(service, customResultList);
            }
            if (!BLConstants.selectorAll.equals(action)) {
                customResultList = connectorMessageFilter.filterByAction(action, customResultList);
            }
            if (!BLConstants.selectorAll.equals(evidence)) {
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

        List<ECodexEvidence> evidenceList = new ArrayList<ECodexEvidence>();

        Set<ECodexEvidence> evidences = messageReportDO.getMessage().getEvidences();

        for (ECodexEvidence eCodexEvidence : evidences) {
            evidenceList.add(eCodexEvidence);
        }

        if (!evidenceList.isEmpty()) {
            Comparator<ECodexEvidence> comp = new Comparator<ECodexEvidence>() {
                @Override
                public int compare(ECodexEvidence e1, ECodexEvidence e2) {
                    return e1.getUpdated().compareTo(e2.getUpdated());
                }
            };

            Collections.sort(evidenceList, comp);
            messageReportDO.setEvidenceList(evidenceList);
            // Set Last Evidence
            ECodexEvidence lastEvidence = evidenceList.get(evidenceList.size() - 1);
            messageReportDO.setLastEvidenceType(lastEvidence.getType().toString());
            String history = "";
            for (ECodexEvidence eCodexEvidence : evidenceList) {
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                history += df.format(eCodexEvidence.getUpdated()) + " - " + eCodexEvidence.getType() + "<br/>";
            }

            messageReportDO.setEvidenceHistory(history);
        }

    }

    public IECodexMessageWebAdminDao geteCodexMessageWebAdminDao() {
        return eCodexMessageWebAdminDao;
    }

    public void seteCodexMessageWebAdminDao(IECodexMessageWebAdminDao eCodexMessageWebAdminDao) {
        this.eCodexMessageWebAdminDao = eCodexMessageWebAdminDao;
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

}
