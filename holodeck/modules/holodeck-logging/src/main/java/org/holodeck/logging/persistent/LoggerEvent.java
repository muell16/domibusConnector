package org.holodeck.logging.persistent;

import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.Size;


/**
 * @author Stefan Mueller
 * @author Tim Nowosadtko
 */

@Entity
@Table(name = "LoggerEvent")

public class LoggerEvent {
	
	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy="uuid")

	@Column(name = "LOGDate")
	private String LOGDate;
	
	@Column(name = "Logger")
	private String Logger;
	
	@Column(name = "Priority")
	private String Priority;
	
	@Column(name = "Log_ClassName")
	private String Log_ClassName;
	
	@Column(name = "Log_MethodName")
	private String Log_MethodName;
	
	@Column(name = "Log_LineNumber")
	private String Log_LineNumber;
	
	@Column(name = "Msg",length=2000)
	private String Msg;
	
	public LoggerEvent(){
		
	}
	
	public LoggerEvent(String lOGDate, String logger, String priority,
			String log_ClassName, String log_MethodName, String log_LineNumber,
			String msg) {
		super();
		LOGDate = lOGDate;
		Logger = logger;
		Priority = priority;
		Log_ClassName = log_ClassName;
		Log_MethodName = log_MethodName;
		Log_LineNumber = log_LineNumber;
		Msg = msg;
	}
	
	public LoggerEvent(LoggingEvent event){
		LocationInfo locinfo = event.getLocationInformation();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		this.LOGDate=df.format(new Date(event.getTimeStamp()));
		this.Logger=event.getLoggerName();
		this.Priority=event.getLevel().toString(); 
		Log_ClassName = locinfo.getClassName();
		Log_MethodName = locinfo.getMethodName();
		Log_LineNumber = locinfo.getLineNumber();
		Msg = event.getMessage() == null ? "null" : event.getMessage().toString();

	}
	
	
	public String getLOGDate() {
		return LOGDate;
	}
	public void setLOGDate(String lOGDate) {
		LOGDate = lOGDate;
	}
	public String getLogger() {
		return Logger;
	}
	public void setLogger(String logger) {
		Logger = logger;
	}
	public String getPriority() {
		return Priority;
	}
	public void setPriority(String priority) {
		Priority = priority;
	}
	public String getLog_ClassName() {
		return Log_ClassName;
	}
	public void setLog_ClassName(String log_ClassName) {
		Log_ClassName = log_ClassName;
	}
	public String getLog_MethodName() {
		return Log_MethodName;
	}
	public void setLog_MethodName(String log_MethodName) {
		Log_MethodName = log_MethodName;
	}
	public String getLog_LineNumber() {
		return Log_LineNumber;
	}
	public void setLog_LineNumber(String log_LineNumber) {
		Log_LineNumber = log_LineNumber;
	}
	public String getMsg() {
		return Msg;
	}
	public void setMsg(String msg) {
		Msg = msg;
	}
	
	
}
