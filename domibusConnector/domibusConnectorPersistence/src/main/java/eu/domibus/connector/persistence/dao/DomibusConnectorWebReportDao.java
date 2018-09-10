package eu.domibus.connector.persistence.dao;

import java.util.Date;
import java.util.List;

import eu.domibus.connector.web.dto.WebReportEntry;

public interface DomibusConnectorWebReportDao {

	List<WebReportEntry> loadReportWithEvidences(Date fromDate, Date toDate);

	List<WebReportEntry> loadReport(Date fromDate, Date toDate);

}
