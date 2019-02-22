package eu.domibus.webadmin.dao;

import java.util.Date;
import java.util.List;

import eu.domibus.webadmin.model.connector.PeriodEntryDO;

public interface IDomibusWebAdminReportDao {

	List<PeriodEntryDO> loadReportWithEvidences(Date fromDate, Date toDate);

	List<PeriodEntryDO> loadReport(Date fromDate, Date toDate);

}
