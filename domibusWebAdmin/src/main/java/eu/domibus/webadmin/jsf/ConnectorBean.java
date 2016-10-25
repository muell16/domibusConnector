package eu.domibus.webadmin.jsf;

import java.io.Serializable;
import java.util.Date;

import javax.faces.event.AjaxBehaviorEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eu.domibus.webadmin.blogic.connector.monitoring.IConnectorMonitoringService;
import eu.domibus.webadmin.blogic.connector.statistics.IConnectorCustomService;
import eu.domibus.webadmin.blogic.connector.statistics.IConnectorSummaryService;

/**
 * Main Class for the National Connector, which includes the different statistic
 * and monitoring services. Contains the conditions for rendering the different
 * sections in the jsf pages.
 * 
 * @author michalim
 * 
 */
public class ConnectorBean implements Serializable {

    private static final long serialVersionUID = -3920852382271662993L;
    
    protected final Log logger = LogFactory.getLog(getClass());

    private IConnectorSummaryService connectorSummaryService;
    private IConnectorCustomService connectorCustomService;
    private IConnectorMonitoringService connectorMonitoringService;

    private Integer categoryNumber;

    private Date fromDate;
    private Date toDate;

    private boolean summarySelected = true;
    private boolean customSelected = true;

    public Integer getCategoryNumber() {
        return categoryNumber;
    }

    public void setCategoryNumber(Integer categoryNumber) {
        this.categoryNumber = categoryNumber;
    }

    public String start(AjaxBehaviorEvent abe) {
        if (categoryNumber != null && categoryNumber.equals(1)) {
            summarySelected = true;
            customSelected = false;
            connectorSummaryService.generateMessageSummary();
        } else if (categoryNumber != null && categoryNumber.equals(2)) {
            summarySelected = false;
            customSelected = true;
        }

        return "/pages/connector-statistics.xhtml";
    }
    
    public void init(){
//    	summarySelected = true;
//    	customSelected = true;
    	logger.info("ConnectorBean.init()");
    	if(connectorSummaryService.getPieModelMessageSummary()==null || connectorSummaryService.getPieModelServiceSummary()==null)
    		connectorSummaryService.generateMessageSummary();
    	if(connectorCustomService.getCustomResultList()==null)
    		connectorCustomService.generateCustomReport();
    }
    
    public String generateCustomReport(){
    	summarySelected = false;
    	connectorCustomService.generateCustomReport();
    	
    	return "/pages/connector-statistics.xhtml";
    }

    public boolean isSummarySelected() {
        return summarySelected;
    }

    public void setSummarySelected(boolean summarySelected) {
        this.summarySelected = summarySelected;
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

    public boolean isCustomSelected() {
        return customSelected;
    }

    public void setCustomSelected(boolean customSelected) {
        this.customSelected = customSelected;
    }

    public IConnectorSummaryService getConnectorSummaryService() {
        return connectorSummaryService;
    }

    public void setConnectorSummaryService(IConnectorSummaryService connectorSummaryService) {
        this.connectorSummaryService = connectorSummaryService;
    }

    public IConnectorCustomService getConnectorCustomService() {
        return connectorCustomService;
    }

    public void setConnectorCustomService(IConnectorCustomService connectorCustomService) {
        this.connectorCustomService = connectorCustomService;
    }

    public IConnectorMonitoringService getConnectorMonitoringService() {
        return connectorMonitoringService;
    }

    public void setConnectorMonitoringService(IConnectorMonitoringService connectorMonitoringService) {
        this.connectorMonitoringService = connectorMonitoringService;
    }

}
