package eu.ecodex.webadmin.blogic.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.ecodex.connector.common.db.model.ECodexMessageInfo;
import eu.ecodex.webadmin.blogic.ICustomService;
import eu.ecodex.webadmin.dao.IECodexMessageWebAdminDao;
import eu.ecodex.webadmin.model.MessageReportDO;

public class CustomServiceImpl implements ICustomService, Serializable {

    private static final long serialVersionUID = 5288892319790964868L;

    private IECodexMessageWebAdminDao eCodexMessageWebAdminDao;

    private String country;
    private String direction;
    private String status;
    private Date fromDate;
    private Date toDate;

    List<MessageReportDO> customResultList;

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

        for (ECodexMessageInfo eCodexMessageInfo : resultList) {
            MessageReportDO messageReportDO = new MessageReportDO();

            messageReportDO.setEbmsMessageId(eCodexMessageInfo.getMessage().getEbmsMessageId());
            if (eCodexMessageInfo.getFrom() != null) {
                messageReportDO.setFromParty(eCodexMessageInfo.getFrom().getPartyId());
            }
            if (eCodexMessageInfo.getTo() != null) {
                messageReportDO.setToParty(eCodexMessageInfo.getTo().getPartyId());
            }
            if (eCodexMessageInfo.getMessage().getDirection() != null) {
                messageReportDO.setDirection(eCodexMessageInfo.getMessage().getDirection().name());
            }

            messageReportDO.setConfirmed(eCodexMessageInfo.getMessage().getConfirmed());
            messageReportDO.setRejected(eCodexMessageInfo.getMessage().getRejected());

            customResultList.add(messageReportDO);
        }

        return "/pages/main.xhtml";
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public List<MessageReportDO> getCustomResultList() {
        return customResultList;
    }

    public void setCustomResultList(List<MessageReportDO> customResultList) {
        this.customResultList = customResultList;
    }

}
