package eu.domibus.connector.web.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.domibus.connector.persistence.service.web.DomibusConnectorWebReportPersistenceService;
import eu.domibus.connector.web.dto.WebReportEntry;

@Service("webReportsService")
public class WebReportsService {

	private DomibusConnectorWebReportPersistenceService reportPersistenceService;

	@Autowired
	public void setReportPersistenceService(DomibusConnectorWebReportPersistenceService reportPersistenceService) {
		this.reportPersistenceService = reportPersistenceService;
	}
	
	public WebReportsService() {
		// TODO Auto-generated constructor stub
	}
	
	public List<WebReportEntry> generateReport(Date fromDate, Date toDate, boolean includeEvidences){
		List<WebReportEntry> report = null;
		if(includeEvidences) {
			report = reportPersistenceService.loadReportWithEvidences(fromDate, toDate);
		}else {
			report = reportPersistenceService.loadReport(fromDate, toDate);
		}
		return report;
	}
}
