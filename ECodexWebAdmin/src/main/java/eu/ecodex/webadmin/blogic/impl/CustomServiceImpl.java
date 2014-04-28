package eu.ecodex.webadmin.blogic.impl;

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
import eu.ecodex.webadmin.blogic.ICustomService;
import eu.ecodex.webadmin.blogic.IMessageFilter;
import eu.ecodex.webadmin.commons.BLConstants;
import eu.ecodex.webadmin.dao.IECodexMessageWebAdminDao;
import eu.ecodex.webadmin.model.MessageReportDO;

public class CustomServiceImpl implements ICustomService, Serializable {

    private static final long serialVersionUID = 5288892319790964868L;

    private IECodexMessageWebAdminDao eCodexMessageWebAdminDao;
    private IMessageFilter messageFilter;

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

    private List<MessageReportDO> customResultList;

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
                customResultList = messageFilter.filterByFromParty(fromParty, customResultList);
            }
            if (!BLConstants.selectorAll.equals(toParty)) {
                customResultList = messageFilter.filterByToParty(toParty, customResultList);
            }
            if (!BLConstants.selectorAll.equals(direction)) {
                customResultList = messageFilter.filterByDirection(direction, customResultList);
            }
            if (!BLConstants.selectorAll.equals(status)) {
                customResultList = messageFilter.filterByStatus(status, customResultList);
            }
            if (!BLConstants.selectorAll.equals(service)) {
                customResultList = messageFilter.filterByService(service, customResultList);
            }
            if (!BLConstants.selectorAll.equals(action)) {
                customResultList = messageFilter.filterByAction(action, customResultList);
            }
            if (!BLConstants.selectorAll.equals(evidence)) {
                customResultList = messageFilter.filterByLastEvidence(evidence, customResultList);
            }

            countResult = customResultList.size();

        }

        return "/pages/main.xhtml";
    }

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

    public IMessageFilter getMessageFilter() {
        return messageFilter;
    }

    public void setMessageFilter(IMessageFilter messageFilter) {
        this.messageFilter = messageFilter;
    }

}
