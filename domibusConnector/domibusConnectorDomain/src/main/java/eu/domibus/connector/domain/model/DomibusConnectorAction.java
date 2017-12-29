package eu.domibus.connector.domain.model;


/**
 * @author riederb
 * @version 1.0
 * @created 29-Dez-2017 10:05:58
 */
public class DomibusConnectorAction {

	private final String action;
	private final boolean documentRequired;

	/**
	 * 
	 * @param action
	 * @param documentRequired    documentRequired
	 */
	public DomibusConnectorAction(final String action, final boolean documentRequired){
	   this.action = action;
	   this.documentRequired = documentRequired;
	}

	public String getAction(){
		return this.action;
	}

	public boolean isDocumentRequired(){
		return this.documentRequired;
	}

}