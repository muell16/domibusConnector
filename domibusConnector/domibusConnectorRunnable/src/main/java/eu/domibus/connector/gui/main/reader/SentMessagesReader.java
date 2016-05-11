package eu.domibus.connector.gui.main.reader;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.common.util.StringUtils;

import eu.domibus.connector.gui.config.properties.ConnectorProperties;
import eu.domibus.connector.gui.main.data.Message;
import eu.domibus.connector.runnable.util.DomibusConnectorMessageProperties;
import eu.domibus.connector.runnable.util.DomibusConnectorRunnableUtil;

public class SentMessagesReader {

	private static final File outgoingMessagesDir = new File(ConnectorProperties.outgoingMessagesDirectory);
	
	
	public static List<Message> readMessages() throws Exception{
		
		if(outgoingMessagesDir== null){
		
			throw new Exception("The configured parameter '"+ConnectorProperties.OTHER_OUTGOING_MSG_DIR_KEY+"' is not set properly! Set value is: "+ConnectorProperties.outgoingMessagesDirectory);
		}
		
		if(!outgoingMessagesDir.exists()){
			throw new Exception("The configured directory to parameter '"+ConnectorProperties.OTHER_OUTGOING_MSG_DIR_KEY+"' with value '"+ConnectorProperties.outgoingMessagesDirectory+"' does not exist!");
		}
		
		if(!outgoingMessagesDir.isDirectory()){
			throw new Exception("The configured directory to parameter '"+ConnectorProperties.OTHER_OUTGOING_MSG_DIR_KEY+"' with value '"+ConnectorProperties.outgoingMessagesDirectory+"' is not a directory!");
		}
		
		List<Message> sentMessages = MessagesReader.readMessages(outgoingMessagesDir);
		
		return sentMessages;
	}
	
}
