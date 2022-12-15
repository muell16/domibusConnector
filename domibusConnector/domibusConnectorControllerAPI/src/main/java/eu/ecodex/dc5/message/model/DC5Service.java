package eu.ecodex.dc5.message.model;

import eu.ecodex.dc5.message.validation.IncomingMessageRules;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @author riederb
 * @version 1.0
 */
@Entity
@Getter


@NoArgsConstructor
public class DC5Service {

	@Builder(toBuilder = true)
	private DC5Service(String service, String serviceType) {
		this.service = service;
		this.serviceType = serviceType;
	}

	@Id
	@GeneratedValue
	private long id;

	@NotBlank(groups = IncomingMessageRules.class, message = "A incoming message must have a EBMS ServiceName")
	@NotNull(groups = IncomingMessageRules.class, message = "A incoming message must have a EBMS ServiceName")
	private String service;
	@NotBlank(groups = IncomingMessageRules.class, message = "A incoming message must have a EBMS ServiceType")
	@NotNull(groups = IncomingMessageRules.class, message = "A incoming message must have a EBMS ServiceType")
	private String serviceType;

	public String getService(){
		return this.service;
	}

	public String getServiceType(){
		return this.serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public void setService(String service) {
		this.service = service;
	}

	@Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("service", this.service);
        builder.append("serviceType", this.serviceType);
        return builder.toString();        
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DC5Service that = (DC5Service) o;

		return service != null ? service.equals(that.service) : that.service == null;
	}

	@Override
	public int hashCode() {
		return service != null ? service.hashCode() : 0;
	}
}