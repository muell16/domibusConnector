package eu.domibus.webadmin.dao.impl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import eu.domibus.webadmin.dao.IDomibusWebAdminReportDao;
import eu.domibus.webadmin.model.connector.PeriodEntryDO;

public class DomibusWebAdminReportDao extends JdbcDaoSupport implements IDomibusWebAdminReportDao, InitializingBean {

	protected final Log logger = LogFactory.getLog(getClass());
	
	private String reportIncludingEvidencesSQL;
	private String reportExcludingEvidencesSQL;
	
	@Override
	public List<PeriodEntryDO> loadReportWithEvidences(Date fromDate, Date toDate){
		if(this.reportIncludingEvidencesSQL==null){
			loadQueries();
		}
		
		Date[] parameter = new Date[4];
		parameter[0] = fromDate;
		parameter[1] = toDate;
		parameter[2] = fromDate;
		parameter[3] = toDate;
		
		List<PeriodEntryDO> result = getJdbcTemplate().query(this.reportIncludingEvidencesSQL, parameter, new BeanPropertyRowMapper(PeriodEntryDO.class));
		
		return result;
	}
	
	@Override
	public List<PeriodEntryDO> loadReport(Date fromDate, Date toDate){
		if(this.reportExcludingEvidencesSQL==null){
			loadQueries();
		}
		
		Date[] parameter = new Date[4];
		parameter[0] = fromDate;
		parameter[1] = toDate;
		parameter[2] = fromDate;
		parameter[3] = toDate;
		
		List<PeriodEntryDO> result = getJdbcTemplate().query(this.reportExcludingEvidencesSQL, parameter, new BeanPropertyRowMapper(PeriodEntryDO.class));
		
		return result;
	}
	
	private void loadQueries(){
		String query1 = loadQueryFile("/report_queries/report_excl_evidences.sql");
		
		String query2 = loadQueryFile("/report_queries/report_incl_evidences.sql");
		
		this.reportExcludingEvidencesSQL = query1;
		this.reportIncludingEvidencesSQL = query2;
	}
	
	private String loadQueryFile(String pathToResource){
		StringBuffer sb = new StringBuffer();
	    try {
	        Resource resource = new ClassPathResource(pathToResource);
	        DataInputStream in = new DataInputStream(resource.getInputStream());
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));
	        String strLine;
	        while ((strLine = br.readLine()) != null) {
	            sb.append(" " + strLine);
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return sb.toString();
	}
}
