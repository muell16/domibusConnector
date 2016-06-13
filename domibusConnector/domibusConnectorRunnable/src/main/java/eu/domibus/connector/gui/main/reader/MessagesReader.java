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

public static List<Message> readMessages(String msgDirPropertyKey, String msgDirPropertyValue) throws Exception{
	
	if(msgDirPropertyValue == null){
		
		throw new Exception("The configured parameter '"+msgDirPropertyKey+"' is not set properly! Set value is: "+msgDirPropertyValue+"\nPlease start the domibusConnectorConfigurator and configure the property.");
	}

	File messagesDir = new File(msgDirPropertyValue);
	
	if(!messagesDir.exists()){
		throw new Exception("The configured directory to parameter '"+msgDirPropertyKey+"' with value '"+msgDirPropertyValue+"' does not exist!\nPlease start the domibusConnectorConfigurator and configure the property.");
	}
	
	if(!messagesDir.isDirectory()){
		throw new Exception("The configured directory to parameter '"+msgDirPropertyKey+"' with value '"+msgDirPropertyValue+"' is not a directory!\nPlease start the domibusConnectorConfigurator and configure the property.");
	}
		
		if(StringUtils.isEmpty(ConnectorProperties.messagePropertiesFileName)){
			throw new Exception("The configured property '"+ConnectorProperties.OTHER_MSG_PROPERTY_FILE_NAME_KEY+"' is missing or empty!\nPlease start the domibusConnectorConfigurator and configure the property.");
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

						message.setMessageProperties(messageProperties);
						message.setMessageDir(subFile);
						if(!StringUtils.isEmpty(messageProperties.getContentXmlFileName())){
							File contentXMLFile = new File(subFile,messageProperties.getContentXmlFileName());
							if(contentXMLFile.exists()){
								message.setFormXMLFile(contentXMLFile);
							}else{
								messageProperties.setContentXmlFileName("");
								DomibusConnectorRunnableUtil.storeMessagePropertiesToFile(messageProperties, new File(subFile,ConnectorProperties.messagePropertiesFileName));
							}
						}
						if(!StringUtils.isEmpty(messageProperties.getContentPdfFileName())){
							File contentPDFFile = new File(subFile,messageProperties.getContentPdfFileName());
							if(contentPDFFile.exists()){
								message.setFormPDFFile(contentPDFFile);
							}else{
								messageProperties.setContentPdfFileName("");
								DomibusConnectorRunnableUtil.storeMessagePropertiesToFile(messageProperties, new File(subFile,ConnectorProperties.messagePropertiesFileName));
							}
						}
						for(File messageFile:subFile.listFiles()){
							String name = messageFile.getName();
							if(!name.equals(ConnectorProperties.messagePropertiesFileName) && 
									!(messageProperties.getContentXmlFileName()!=null && name.equals(messageProperties.getContentXmlFileName())) &&
									!(messageProperties.getContentPdfFileName()!=null && name.equals(messageProperties.getContentPdfFileName()))){
								message.getAttachments().add(messageFile);
							}
						}
						messages.add(message);
						
					}
				}
			}
		}
		
		
		
		return messages;
	}
}
