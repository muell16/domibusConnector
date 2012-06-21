package org.holodeck.common.logging.filter;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.holodeck.common.logging.model.MsgInfo;

public class IllegalCharacterMsgInfoFilter extends Filter {
	@Override
	public int decide(LoggingEvent event) {
		int result = Filter.ACCEPT;
		
		Object o = event.getMessage();
		if(o instanceof MsgInfo) {
			MsgInfo msg = (MsgInfo)o;
			
			//Hier kann überprüft werden ob die Nachricht gültig ist. Falls nicht: result = Filter.DENY
			
			//Ungültige Zeichen für sql abfrage entfernen
			msg.setMessageId( msg.getMessageId().replaceAll("'", "\\\\'") );
			msg.setConversationId( msg.getConversationId().replaceAll("'", "\\\\'") );
			msg.setPmode( msg.getPmode().replaceAll("'", "\\\\'") );
		}
		return result;
	}
}
