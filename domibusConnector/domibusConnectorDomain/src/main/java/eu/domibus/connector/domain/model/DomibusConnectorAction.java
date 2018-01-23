package eu.domibus.connector.domain.model;

import java.io.Serializable;
import org.springframework.core.style.ToStringCreator;


/**
 * @author riederb
 * @version 1.0
 * @created 29-Dez-2017 10:05:58
 */
public class DomibusConnectorAction implements Serializable {

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

    @Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("action", this.action);        
        builder.append("requiresDocument", this.documentRequired);
        return builder.toString();        
    }
    
}