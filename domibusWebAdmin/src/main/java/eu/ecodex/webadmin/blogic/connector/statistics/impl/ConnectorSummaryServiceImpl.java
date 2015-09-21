package eu.ecodex.webadmin.blogic.connector.statistics.impl;

import java.io.Serializable;
import java.util.HashMap;

import org.primefaces.model.chart.PieChartModel;

import eu.ecodex.webadmin.blogic.connector.statistics.IConnectorSummaryService;
import eu.ecodex.webadmin.dao.IDomibusMessageWebAdminDao;
import eu.ecodex.webadmin.dao.IDomibusWebAdminUserDao;

public class ConnectorSummaryServiceImpl implements IConnectorSummaryService, Serializable {

    private static final long serialVersionUID = 4855196930128932326L;

    private String outgoingMessagesCount = "";
    private String incomingMessagesCount = "";
    private PieChartModel pieModelMessageSummary;
    private PieChartModel pieModelServiceSummary;
    private IDomibusMessageWebAdminDao domibusMessageWebAdminDao;

    /*
     * (non-Javadoc)
     * 
     * @see eu.ecodex.webadmin.blogic.impl.IReportingServiceManager#
     * getNationalMessageCount()
     */

    @Override
    public void generateMessageSummary() {
        Long resultOutgoing = domibusMessageWebAdminDao.countOutgoingMessages();
        outgoingMessagesCount = resultOutgoing.toString();

        Long resultIncoming = domibusMessageWebAdminDao.countIncomingMessages();
        incomingMessagesCount = resultIncoming.toString();
        pieModelMessageSummary = new PieChartModel();

        pieModelMessageSummary.set("Incoming Messages", resultIncoming);
        pieModelMessageSummary.set("Outgoing Messages", resultOutgoing);

        HashMap<String, Long> serviceList = domibusMessageWebAdminDao.countService();

        pieModelServiceSummary = new PieChartModel();

        pieModelServiceSummary.set("European Payment Order", serviceList.get("EPO"));
        pieModelServiceSummary.set("Undefined", serviceList.get("Undefined"));

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

	public IDomibusMessageWebAdminDao getDomibusMessageWebAdminDao() {
		return domibusMessageWebAdminDao;
	}

	public void setDomibusMessageWebAdminDao(
			IDomibusMessageWebAdminDao domibusMessageWebAdminDao) {
		this.domibusMessageWebAdminDao = domibusMessageWebAdminDao;
	}

}
