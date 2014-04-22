package eu.ecodex.webadmin.jsf;

import java.io.Serializable;
import java.util.Date;

import eu.ecodex.webadmin.blogic.ISummaryService;

public class WebAdminServiceBean implements Serializable {

    private static final long serialVersionUID = -3920852382271662993L;

    private ISummaryService summaryService;
    private Integer categoryNumber;
    private Date fromDate;
    private Date toDate;

    private boolean summarySelected = false;

    public Integer getCategoryNumber() {
        return categoryNumber;
    }

    public void setCategoryNumber(Integer categoryNumber) {
        this.categoryNumber = categoryNumber;
    }

    public String start() {
        summarySelected = true;
        summaryService.generateMessageSummary();
        return "/pages/main.xhtml";
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

}
