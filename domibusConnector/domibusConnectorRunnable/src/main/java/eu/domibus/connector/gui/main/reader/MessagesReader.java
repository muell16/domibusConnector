package eu.domibus.connector.gui.main.reader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.common.util.StringUtils;

import eu.domibus.connector.gui.config.properties.ConnectorProperties;
import eu.domibus.connector.gui.main.data.Message;
import eu.domibus.connector.runnable.util.DomibusConnectorMessageProperties;
import eu.domibus.connector.runnable.util.DomibusConnectorRunnableUtil;

public class MessagesReader {

public static List<Message> readMessages(File messagesDir) throws Exception{
		
		
		if(StringUtils.isEmpty(ConnectorProperties.messagePropertiesFileName)){
			throw new Exception("The configured property '"+ConnectorProperties.OTHER_MSG_PROPERTY_FILE_NAME_KEY+"' is missing or empty!");
		}
		
		List<Message> messages = new ArrayList<Message>();

		File[] contents = messagesDir.listFiles();
		
		if(contents!=null && contents.length>0){
			for(File subFile:contents){
				if(subFile.exists() && subFile.isDirectory()){
					DomibusConnectorMessageProperties messageProperties = DomibusConnectorRunnableUtil.loadMessageProperties(
							subFile, ConnectorProperties.messagePropertiesFileName);
					if(messageProperties!=null){
						Message message = new Message();
						message.setAction(convertText(messageProperties.getAction()));
						message.setFinalRecipient(convertText(messageProperties.getFinalRecipient()));
						message.setFromPartyId(convertText(messageProperties.getFromPartyId()));
						message.setOriginalSender(convertText(messageProperties.getOriginalSender()));
						message.setReceivedTimestamp(convertText(messageProperties.getMessageReceivedDatetime()));
						message.setService(convertText(messageProperties.getService()));
						message.setToPartyId(convertText(messageProperties.getToPartyId()));
						message.setEbmsMessageId(convertText(messageProperties.getEbmsMessageId()));
						message.setFromPartyRole(convertText(messageProperties.getFromPartyRole()));
						message.setMessageDir(subFile);
						message.setNationalMessageId(convertText(messageProperties.getNationalMessageId()));
						message.setToPartyRole(convertText(messageProperties.getToPartyRole()));
						
						messages.add(message);
						
					}
				}
			}
		}
		
		
		
		return messages;
	}
	
	private static String convertText(String text){
		return text!=null?text:"unknown";
	}
}
