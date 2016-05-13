package eu.domibus.connector.gui.main.reader;

import java.io.File;
import java.util.List;

import eu.domibus.connector.gui.config.properties.ConnectorProperties;
import eu.domibus.connector.gui.main.data.Message;

public class ReceivedMessagesReader {

	private static final File incomingMessagesDir = new File(ConnectorProperties.incomingMessagesDirectory);
	
	
	public static List<Message> readMessages() throws Exception{
		
		if(incomingMessagesDir== null){
		
			throw new Exception("The configured parameter '"+ConnectorProperties.OTHER_INCOMING_MSG_DIR_KEY+"' is not set properly! Set value is: "+ConnectorProperties.incomingMessagesDirectory);
		}
		
		if(!incomingMessagesDir.exists()){
			throw new Exception("The configured directory to parameter '"+ConnectorProperties.OTHER_INCOMING_MSG_DIR_KEY+"' with value '"+ConnectorProperties.incomingMessagesDirectory+"' does not exist!");
		}
		
		if(!incomingMessagesDir.isDirectory()){
			throw new Exception("The configured directory to parameter '"+ConnectorProperties.OTHER_INCOMING_MSG_DIR_KEY+"' with value '"+ConnectorProperties.incomingMessagesDirectory+"' is not a directory!");
		}
		
		List<Message> receivedMessages = MessagesReader.readMessages(incomingMessagesDir);
		
		return receivedMessages;
	}
	
}
