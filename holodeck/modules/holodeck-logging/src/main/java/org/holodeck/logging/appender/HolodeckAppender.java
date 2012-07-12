package org.holodeck.logging.appender;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.holodeck.logging.level.Message;
import org.holodeck.logging.module.Constants;
import org.holodeck.logging.module.DbStore;
import org.holodeck.logging.persistent.LoggerEvent;
import org.holodeck.logging.persistent.LoggerMessage;
import org.holodeck.logging.persistent.MessageInfo;


public class HolodeckAppender extends AppenderSkeleton implements Appender{

	private static final Logger log = Logger.getLogger(HolodeckAppender.class);
	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean requiresLayout() {
		// TODO Auto-generated method stub
		return false;
	}

	protected void append(LoggingEvent event) {
		
		// TODO Auto-generated method stub
//		log.info("Create DBStore");
		System.out.println("test1");
//		Constants.loadDataStore();
		if(Constants.store == null)Constants.store=new DbStore();
		DbStore dbs=Constants.store;
//		System.out.println("dbs"+dbs.toString());
		System.out.println(event.getMessage().getClass().getName());
		if(event.getMessage() instanceof MessageInfo){
			log.info("Logelement: Message");
			log.info("Messageclass: "+event.getMessage().toString());
			LoggerMessage lm = new LoggerMessage((MessageInfo)event.getMessage());
			if(dbs!=null)dbs.save(lm);
			System.out.println("Message gespeichert.");
			
		}
		else{
			LoggerEvent le = new LoggerEvent(event);
			if(dbs!=null)dbs.save(le);
			System.out.println("Event gespeichert.");
		}
		
	}
}