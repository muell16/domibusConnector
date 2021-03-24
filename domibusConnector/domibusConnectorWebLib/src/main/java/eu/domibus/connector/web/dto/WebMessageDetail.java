package eu.domibus.connector.web.dto;

import java.util.LinkedList;

public class WebMessageDetail {
	
	public class Party{
	    private String partyId;
	    private String partyIdType;
		/**
		 * @param partyId
		 * @param partyIdType
		 */
		public Party(String partyId, String partyIdType) {
			super();
			this.partyId = partyId;
			this.partyIdType = partyIdType;
		}
		/**
		 * @param partyId
		 */
		public Party(String partyId) {
			super();
			this.partyId = partyId;
		}
		public String getPartyId() {
			return partyId;
		}
		public void setPartyId(String partyId) {
			this.partyId = partyId;
		}
		public String getPartyIdType() {
			return partyIdType;
		}
		public void setPartyIdType(String partyIdType) {
			this.partyIdType = partyIdType;
		}
	}
	
	public class Service{
		private String service;
	    private String serviceType;
		/**
		 * @param service
		 * @param serviceType
		 */
		public Service(String service, String serviceType) {
			super();
			this.service = service;
			this.serviceType = serviceType;
		}
		/**
		 * @param service
		 */
		public Service(String service) {
			super();
			this.service = service;
		}
		public String getService() {
			return service;
		}
		public void setService(String service) {
			this.service = service;
		}
		public String getServiceType() {
			return serviceType;
		}
		public void setServiceType(String serviceType) {
			this.serviceType = serviceType;
		}
	}
	
	public class Action{
		private String action;

		/**
		 * @param action
		 */
		public Action(String action) {
			super();
			this.action = action;
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}
	}

	private Service service;
	private Action action;
	private Party from;
	private Party to;
	private String originalSender;
	private String finalRecipient;
	
	
	private LinkedList<WebMessageError> errors = new LinkedList<WebMessageError>();
	

	


	public String getOriginalSender() {
		return originalSender;
	}


	public void setOriginalSender(String originalSender) {
		this.originalSender = originalSender;
	}


	public String getFinalRecipient() {
		return finalRecipient;
	}


	public void setFinalRecipient(String finalRecipient) {
		this.finalRecipient = finalRecipient;
	}


	
	



	public Service getService() {
		return service;
	}


	public void setService(Service service) {
		this.service = service;
	}


	public Action getAction() {
		return action;
	}


	public void setAction(Action action) {
		this.action = action;
	}


	public Party getFrom() {
		return from;
	}


	public void setFrom(Party from) {
		this.from = from;
	}


	public Party getTo() {
		return to;
	}


	public void setTo(Party to) {
		this.to = to;
	}





	public LinkedList<WebMessageError> getErrors() {
		return errors;
	}


}
