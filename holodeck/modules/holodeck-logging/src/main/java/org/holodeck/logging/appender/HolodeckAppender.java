package org.holodeck.logging.appender;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.holodeck.logging.module.Constants;
import org.holodeck.logging.module.DbStore;
import org.holodeck.logging.persistent.LoggerEvent;
import org.holodeck.logging.persistent.LoggerMessage;
import org.holodeck.logging.persistent.MessageInfo;

/**
 * A custom appender for logging into a database via hibernate
 * 
 * @author Stefan Mueller
 * @author Tim Nowosadtko
 * @date 07-13-2012
 */
public class HolodeckAppender extends AppenderSkeleton implements Appender{

	private static final Logger log = Logger.getLogger(HolodeckAppender.class);
	@Override
	public void close() {
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}
	
    /**
     * @param LoggingEvent
     * This method stores custom and standard logevents into the database. In case of the cuastom logevent MESSAGE it expects a message of type MessageInfo
     */
	protected void append(LoggingEvent event) {
		
		if(Constants.store == null)Constants.store=new DbStore();
		DbStore dbs=Constants.store;
		if(event.getMessage() instanceof MessageInfo){
			log.info("Logelement: Message");
			LoggerMessage lm = new LoggerMessage((MessageInfo)event.getMessage());
			if(dbs!=null)dbs.save(lm);
			
		}
		else{
			LoggerEvent le = new LoggerEvent(event);
			if(dbs!=null)dbs.save(le);
		}
		
	}
}