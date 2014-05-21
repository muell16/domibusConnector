package eu.ecodex.webadmin.jsf;

import java.io.Serializable;
import java.util.Date;

import eu.ecodex.webadmin.blogic.connector.statistics.ICustomService;
import eu.ecodex.webadmin.blogic.connector.statistics.ISummaryService;

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

    private ISummaryService summaryService;
    private ICustomService customService;

    private Integer categoryNumber;

    private Date fromDate;
    private Date toDate;

    private boolean summarySelected = false;
    private boolean customSelected = false;

    public Integer getCategoryNumber() {
        return categoryNumber;
    }

    public void setCategoryNumber(Integer categoryNumber) {
        this.categoryNumber = categoryNumber;
    }

    public String start() {

        if (categoryNumber != null && categoryNumber.equals(1)) {
            summarySelected = true;
            customSelected = false;
            summaryService.generateMessageSummary();
        } else if (categoryNumber != null && categoryNumber.equals(2)) {
            summarySelected = false;
            customSelected = true;
        }

        return "/pages/connector-statistics.xhtml";
    }

    public ISummaryService getSummaryService() {
        return summaryService;
    }

    public void setSummaryService(ISummaryService summaryService) {
        this.summaryService = summaryService;
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

    public ICustomService getCustomService() {
        return customService;
    }

    public void setCustomService(ICustomService customService) {
        this.customService = customService;
    }

    public void setCustomSelected(boolean customSelected) {
        this.customSelected = customSelected;
    }

}
