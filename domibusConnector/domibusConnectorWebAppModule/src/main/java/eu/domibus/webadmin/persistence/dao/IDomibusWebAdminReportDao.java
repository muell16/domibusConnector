package eu.domibus.webadmin.persistence.dao;

import java.util.Date;
import java.util.List;

import eu.domibus.webadmin.persistence.model.PeriodEntryDO;

@Deprecated //will be moved to persistence
public interface IDomibusWebAdminReportDao {

	List<PeriodEntryDO> loadReportWithEvidences(Date fromDate, Date toDate);

	List<PeriodEntryDO> loadReport(Date fromDate, Date toDate);

}
