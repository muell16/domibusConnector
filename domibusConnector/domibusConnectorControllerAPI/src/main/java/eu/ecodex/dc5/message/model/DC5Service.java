package eu.ecodex.dc5.message.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.Embeddable;


/**
 * @author riederb
 * @version 1.0
 */
@Embeddable

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
@NoArgsConstructor
public class DC5Service {

	private String service;
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