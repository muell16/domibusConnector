package eu.domibus.webadmin.persistence.model;

import java.util.List;

public class PeriodDO {

	private String period;
	private String year;
	private String month;
	private long sumSent;
	private long sumReceived;
	private List<PeriodEntryDO> entries;
	
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public long getSumSent() {
		return sumSent;
	}
	public void setSumSent(long sumSent) {
		this.sumSent = sumSent;
	}
	public long getSumReceived() {
		return sumReceived;
	}
	public void setSumReceived(long sumReceived) {
		this.sumReceived = sumReceived;
	}
	public List<PeriodEntryDO> getEntries() {
		return entries;
	}
	public void setEntries(List<PeriodEntryDO> entries) {
		this.entries = entries;
	}
	
	
}
