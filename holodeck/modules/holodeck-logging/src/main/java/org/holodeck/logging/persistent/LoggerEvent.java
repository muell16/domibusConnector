package org.holodeck.logging.persistent;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.hibernate.annotations.GenericGenerator;


/**
 * This Class represents a database table for standard logging events
 * 
 * @author Stefan Mueller
 * @author Tim Nowosadtko
 * @date 07-13-2012
 */
@Entity
@Table(name = "LoggerEvent")

public class LoggerEvent {
	
	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy="uuid")
	@Column(name = "Id")
	protected String id;
	
	@Column(name = "LOGDate")
	protected Date LOGDate;
	
	@Column(name = "Logger")
	protected String Logger;
	
	@Column(name = "Priority")
	protected String Priority;
	
	@Column(name = "Log_ClassName")
	protected String Log_ClassName;
	
	@Column(name = "Log_MethodName")
	protected String Log_MethodName;
	
	@Column(name = "Log_LineNumber")
	protected String Log_LineNumber;
	
	@Column(name = "Msg",length=2000)
	protected String Msg;
	
	public LoggerEvent(){
		LOGDate = new Date();
	}
	
	public LoggerEvent(String logger, String priority,
			String log_ClassName, String log_MethodName, String log_LineNumber,
			String msg) {
		super();
		LOGDate = new Date();
		Logger = logger;
		Priority = priority;
		Log_ClassName = log_ClassName;
		Log_MethodName = log_MethodName;
		Log_LineNumber = log_LineNumber;
		Msg = msg;
	}
	
    /**
     * @param LoggingEvent
     * This constructor expects a LoggingEvent instance and creates an new instance of LoggerEvent
     */
	public LoggerEvent(LoggingEvent event){
		LocationInfo locinfo = event.getLocationInformation();
		
		this.LOGDate = new Date(event.getTimeStamp());
		this.Logger=event.getLoggerName();
		this.Priority=event.getLevel().toString(); 
		Log_ClassName = locinfo.getClassName();
		Log_MethodName = locinfo.getMethodName();
		Log_LineNumber = locinfo.getLineNumber();
		Msg = event.getMessage() == null ? "null" : event.getMessage().toString();
	}
	
	
	/*
	 * Setter and Getter
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getLOGDate() {
		return LOGDate;
	}
	public void setLOGDate(Date lOGDate) {
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
