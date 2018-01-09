package eu.domibus.connector.domain.model;

import java.io.Serializable;


/**
 * @author riederb
 * @version 1.0
 * @created 29-Dez-2017 10:05:58
 */
public class DomibusConnectorService implements Serializable {

	private final String service;
	private final String serviceType;

	/**
	 * 
	 * @param service
	 * @param serviceType
	 */
	public DomibusConnectorService(final String service, final String serviceType){
	   this.service = service;
	   this.serviceType = serviceType;
	}

	public String getService(){
		return this.service;
	}

	public String getServiceType(){
		return this.serviceType;
	}

}