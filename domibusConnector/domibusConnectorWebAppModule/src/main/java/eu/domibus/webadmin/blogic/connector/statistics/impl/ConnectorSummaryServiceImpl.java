package eu.domibus.webadmin.blogic.connector.statistics.impl;

import eu.domibus.connector.domain.model.DomibusConnectorService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.domibus.connector.persistence.model.PDomibusConnectorService;
import eu.domibus.connector.persistence.service.DomibusConnectorServicePersistenceService;
import eu.domibus.webadmin.blogic.connector.statistics.IConnectorSummaryService;
import eu.domibus.webadmin.persistence.dao.IDomibusMessageWebAdminDao;


@Component
public class ConnectorSummaryServiceImpl implements IConnectorSummaryService, Serializable {

    private static final long serialVersionUID = 4855196930128932326L;

    private String outgoingMessagesCount = "";
    private String incomingMessagesCount = "";
    private List<String> serviceList;
    private PieChartModel pieModelMessageSummary;
    private PieChartModel pieModelServiceSummary;

    @Autowired
    private IDomibusMessageWebAdminDao domibusMessageWebAdminDao;

    @Autowired
    private DomibusConnectorServicePersistenceService domibusWebAdminConnectorServiceDao;

    @PostConstruct
    public void init() {
        serviceList = new ArrayList<String>();
        List<DomibusConnectorService> resultList = domibusWebAdminConnectorServiceDao.getServiceList();
        for (DomibusConnectorService domibusConnectorService : resultList) {
            serviceList.add(domibusConnectorService.getService());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.domibus.webadmin.blogic.impl.IReportingServiceManager#
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
        pieModelServiceSummary = new PieChartModel();
        for (String service : serviceList) {
            HashMap<String, Long> serviceList = domibusMessageWebAdminDao.countService(service);

            pieModelServiceSummary.set(service, serviceList.get(service));

        }

        HashMap<String, Long> serviceList = domibusMessageWebAdminDao.countUndefinedService();
        if (serviceList.containsKey("Undefined")) {
            pieModelServiceSummary.set("Undefined", serviceList.get("Undefined"));
        }

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

    @Override
    public PieChartModel getPieModelMessageSummary() {
        return pieModelMessageSummary;
    }

    public void setPieModelMessageSummary(PieChartModel pieModelMessageSummary) {
        this.pieModelMessageSummary = pieModelMessageSummary;
    }

    @Override
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
