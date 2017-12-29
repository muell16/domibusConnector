package eu.domibus.connector.domain.model;


/**
 * Internal part of the {@link DomibusConnectorMessage}. All message related
 * errors raised while processing a message and all message related errors
 * reported by the gateway are stored and added to the message.
 * @author riederb
 * @version 1.0
 * @updated 29-Dez-2017 10:12:49
 */
public class DomibusConnectorMessageError {

	private final String text;
	private final String details;
	private final String source;

	/**
	 * 
	 * @param text
	 * @param details
	 * @param source    source
	 */
	public DomibusConnectorMessageError(final String text, final String details, final String source){
	   this.text = text;
	   this.details = details;
	   this.source = source;
	}

	public String getText(){
		return this.text;
	}

	public String getDetails(){
		return this.details;
	}

	public String getSource(){
		return this.source;
	}

}