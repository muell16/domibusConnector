package eu.domibus.connector.gui.main.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import eu.domibus.connector.runnable.util.DomibusConnectorMessageProperties;

public class Message {
	
	File messageDir;
	DomibusConnectorMessageProperties messageProperties;
	File formXMLFile;
	File formPDFFile;
	List<File> attachments = new ArrayList<File>();
	
	public File getMessageDir() {
		return messageDir;
	}
	public DomibusConnectorMessageProperties getMessageProperties() {
		return messageProperties;
	}
	public void setMessageProperties(DomibusConnectorMessageProperties messageProperties) {
		this.messageProperties = messageProperties;
	}
	public void setMessageDir(File messageDir) {
		this.messageDir = messageDir;
	}
	public File getFormXMLFile() {
		return formXMLFile;
	}
	public void setFormXMLFile(File formXMLFile) {
		this.formXMLFile = formXMLFile;
	}
	public File getFormPDFFile() {
		return formPDFFile;
	}
	public void setFormPDFFile(File formPDFFile) {
		this.formPDFFile = formPDFFile;
	}
	public List<File> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<File> attachments) {
		this.attachments = attachments;
	}
	
	
	
}
