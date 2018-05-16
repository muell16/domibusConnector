package eu.domibus.connector.domain.model;

import java.io.Serializable;
import org.springframework.core.style.ToStringCreator;


/**
 * @author riederb
 * @version 1.0
 */
public class DomibusConnectorAction implements Serializable {

	private String action;
	private boolean documentRequired;

	/**
	 * 
	 * @param action action
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

	public void setDocumentRequired(boolean documentRequired) {
		this.documentRequired = documentRequired;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("action", this.action);        
        builder.append("requiresDocument", this.documentRequired);
        return builder.toString();        
    }
    
}