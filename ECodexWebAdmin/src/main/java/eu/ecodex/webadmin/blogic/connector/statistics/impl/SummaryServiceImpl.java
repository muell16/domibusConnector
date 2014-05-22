package eu.ecodex.webadmin.blogic.connector.statistics.impl;

import java.io.Serializable;
import java.util.HashMap;

import org.primefaces.model.chart.PieChartModel;

import eu.ecodex.webadmin.blogic.connector.statistics.ISummaryService;
import eu.ecodex.webadmin.dao.IECodexMessageWebAdminDao;

public class SummaryServiceImpl implements ISummaryService, Serializable {

    private static final long serialVersionUID = 4855196930128932326L;

    private String outgoingMessagesCount = "";
    private String incomingMessagesCount = "";
    private PieChartModel pieModelMessageSummary;
    private PieChartModel pieModelServiceSummary;
    private IECodexMessageWebAdminDao eCodexMessageWebAdminDao;

    /*
     * (non-Javadoc)
     * 
     * @see eu.ecodex.webadmin.blogic.impl.IReportingServiceManager#
     * getNationalMessageCount()
     */

    @Override
    public void generateMessageSummary() {
        Long resultOutgoing = eCodexMessageWebAdminDao.countOutgoingMessages();
        outgoingMessagesCount = resultOutgoing.toString();

        Long resultIncoming = eCodexMessageWebAdminDao.countIncomingMessages();
        incomingMessagesCount = resultIncoming.toString();
        pieModelMessageSummary = new PieChartModel();

        pieModelMessageSummary.set("Incoming Messages", resultIncoming);
        pieModelMessageSummary.set("Outgoing Messages", resultOutgoing);

        HashMap<String, Long> serviceList = eCodexMessageWebAdminDao.countService();

        pieModelServiceSummary = new PieChartModel();

        pieModelServiceSummary.set("European Payment Order", serviceList.get("EPO"));
        pieModelServiceSummary.set("Undefined", serviceList.get("Undefined"));

    }

    public IECodexMessageWebAdminDao geteCodexMessageWebAdminDao() {
        return eCodexMessageWebAdminDao;
    }

    public void seteCodexMessageWebAdminDao(IECodexMessageWebAdminDao eCodexMessageWebAdminDao) {
        this.eCodexMessageWebAdminDao = eCodexMessageWebAdminDao;
    }

    public String getOutgoingMessagesCount() {
        return outgoingMessagesCount;
    }

    public void setOutgoingMessagesCount(String outgoingMessagesCount) {
        this.outgoingMessagesCount = outgoingMessagesCount;
    }

    public String getIncomingMessagesCount() {
        return incomingMessagesCount;
    }

    public void setIncomingMessagesCount(String incomingMessagesCount) {
        this.incomingMessagesCount = incomingMessagesCount;
    }

    public PieChartModel getPieModelMessageSummary() {
        return pieModelMessageSummary;
    }

    public void setPieModelMessageSummary(PieChartModel pieModelMessageSummary) {
        this.pieModelMessageSummary = pieModelMessageSummary;
    }

    public PieChartModel getPieModelServiceSummary() {
        return pieModelServiceSummary;
    }

    public void setPieModelServiceSummary(PieChartModel pieModelServiceSummary) {
        this.pieModelServiceSummary = pieModelServiceSummary;
    }

}
