package eu.domibus.connector.web.persistence.service.impl;

import java.util.Date;
import java.util.List;

import eu.domibus.connector.web.persistence.service.DomibusConnectorWebReportPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;

import eu.domibus.connector.web.persistence.dao.DomibusConnectorWebReportDao;
import eu.domibus.connector.web.dto.WebReportEntry;

@org.springframework.stereotype.Service("webReportPersistenceService")
public class DomibusConnectorWebReportPersistenceServiceImpl implements DomibusConnectorWebReportPersistenceService {

//	private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorMessagePersistenceServiceImpl.class);

    private DomibusConnectorWebReportDao reportDao;
    
	
	@Override
	public List<WebReportEntry> loadReportWithEvidences(Date fromDate, Date toDate) {
		return reportDao.loadReportWithEvidences(fromDate, toDate);
	}

	@Override
	public List<WebReportEntry> loadReport(Date fromDate, Date toDate) {
		return reportDao.loadReport(fromDate, toDate);
	}

	@Autowired
	public void setReportDao(DomibusConnectorWebReportDao reportDao) {
		this.reportDao = reportDao;
	}

}