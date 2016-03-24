package eu.domibus.webadmin.jsf;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.AjaxBehaviorEvent;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import eu.domibus.webadmin.dao.IDomibusWebAdminReportDao;
import eu.domibus.webadmin.model.connector.PeriodDO;
import eu.domibus.webadmin.model.connector.PeriodEntryDO;

public class ReportsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3507794352791840241L;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

	private Date fromDate;
    private Date toDate;
    private boolean includeEvidences;
    private boolean reportGenerated;
    private List<PeriodEntryDO> result;
    private Map<String,PeriodDO> periodMap;
    private List<PeriodDO> periods;
    
    private IDomibusWebAdminReportDao reportDao;
    
    public String generateReport(){
    	if(fromDate==null){
    		try {
				fromDate=sdf.parse("01.01.2000");
			} catch (ParseException e) {
				e.printStackTrace();
			}
    	}
    	
    	if(toDate==null){
    		toDate = new Date();
    	}
    	
    	if(includeEvidences){
    		setResult(reportDao.loadReportWithEvidences(fromDate, toDate));
    	}else{
    		setResult(reportDao.loadReport(fromDate, toDate));
    	}
    	
    	periodMap = new HashMap<String,PeriodDO>();
    	
    	for(PeriodEntryDO entry:result){
    		String period = entry.getMonth()+"/"+entry.getYear();
    		if(!periodMap.containsKey(period)){
    			PeriodDO p = new PeriodDO();
    			p.setPeriod(period);
    			p.setYear(entry.getYear());
        		p.setMonth(entry.getMonth().length()>1?entry.getMonth():"0"+entry.getMonth());
        		p.setEntries(new ArrayList<PeriodEntryDO>());
    			periodMap.put(period, p);
    		}
    		
    		periodMap.get(period).getEntries().add(entry);
    		periodMap.get(period).setSumReceived(periodMap.get(period).getSumReceived()+entry.getReceived());
    		periodMap.get(period).setSumSent(periodMap.get(period).getSumSent()+entry.getSent());
    	}
    	
    	periods = new ArrayList<PeriodDO>(periodMap.size());
    	periods.addAll(periodMap.values());
    	
    	Collections.sort(periods, new Comparator<PeriodDO>() {
    	        @Override
    	        public int compare(PeriodDO c1, PeriodDO c2)
    	        {

    	        	String p1 = c1.getYear()+c1.getMonth();
    	        	String p2 = c2.getYear()+c2.getMonth();
    	        	
    	            return  p1.compareTo(p2);
    	        }
    	    });
    	
    	this.reportGenerated = true;
    	
    	return "/pages/reports.xhtml";
    }
    
    public void postProcessExcel(Object document){
    	HSSFWorkbook wb = (HSSFWorkbook) document;
    	wb.setSheetName(0, sdf.format(fromDate)+" - "+sdf.format(toDate));
    	HSSFSheet sheet = wb.getSheetAt(0);
    	sheet.getRow(0);
    }
    
    public String start(AjaxBehaviorEvent abe) {
        

        return "/pages/reports.xhtml";
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

	public boolean isIncludeEvidences() {
		return includeEvidences;
	}

	public void setIncludeEvidences(boolean includeEvidences) {
		this.includeEvidences = includeEvidences;
	}

	public boolean isReportGenerated() {
		return reportGenerated;
	}

	public void setReportGenerated(boolean reportGenerated) {
		this.reportGenerated = reportGenerated;
	}

	public List<PeriodEntryDO> getResult() {
		return result;
	}

	public void setResult(List<PeriodEntryDO> result) {
		this.result = result;
	}

	public List<PeriodDO> getPeriods() {
		return periods;
	}

	public void setPeriods(List<PeriodDO> periods) {
		this.periods = periods;
	}

	public IDomibusWebAdminReportDao getReportDao() {
		return reportDao;
	}

	public void setReportDao(IDomibusWebAdminReportDao reportDao) {
		this.reportDao = reportDao;
	}
}
