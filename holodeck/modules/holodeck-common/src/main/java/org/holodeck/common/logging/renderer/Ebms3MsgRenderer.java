package org.holodeck.common.logging.renderer;

import org.apache.log4j.or.ObjectRenderer;
import org.holodeck.common.logging.model.MsgInfo;

public class Ebms3MsgRenderer implements ObjectRenderer {
	
	private static final String separator = "-";
	
	public String doRender(Object o) {
		StringBuffer buffer = new StringBuffer(50);
		MsgInfo msg = null;
		
		
		if(o instanceof MsgInfo) {
			msg = (MsgInfo)o;
			buffer.append(msg.getConversationId());
			buffer.append(separator);
			buffer.append(msg.getMessageId());
			buffer.append(separator);
			buffer.append(msg.getPmode());
		}
		
		return buffer.toString();
	}
	
}
