package eu.ecodex.dc5.message.model;

import java.io.Serializable;

import lombok.*;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * @author riederb
 * @version 1.0
 */
@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DomibusConnectorAction implements Serializable {

	@Id
	@GeneratedValue
	private Long id;
	private String action;


	/**
	 *
	 * @param action action
	 */
	public DomibusConnectorAction(final String action){
		this.action = action;
	}

	public String getAction(){
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setDbKey(Long dbKey) {
		this.id = dbKey;
	}

	public Long getDbKey() {
		return id;
	}

	@Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("action", this.action);
        return builder.toString();        
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DomibusConnectorAction other = (DomibusConnectorAction) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		return true;
	}
}