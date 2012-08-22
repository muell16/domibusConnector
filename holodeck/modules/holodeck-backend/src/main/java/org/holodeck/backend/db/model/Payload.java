/*
 * 
 */
package org.holodeck.backend.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * The Class Payload.
 */
@Entity
@Table(name = "Payload")
public class Payload implements java.io.Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6525607108255072953L;

	// Fields
	/** The id payload. */
	@Id
	@TableGenerator(name="TABLE_GEN_PAYLOAD", table="SEQUENCE_TABLE", pkColumnName="SEQ_NAME",
	    	valueColumnName="SEQ_COUNT", pkColumnValue="PAYLOAD_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="TABLE_GEN_PAYLOAD")
	@Column(name = "idPayload")
	private Integer idPayload;
	
	/** The message. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idMessage")
	private Message message;
	
	/** The file name. */
	@Column(name = "fileName", length = 256)
	private String fileName;

	// Constructors
	/**
	 * Instantiates a new payload.
	 */
	public Payload() {
	}

	/**
	 * Instantiates a new payload.
	 *
	 * @param idPayload the id payload
	 */
	public Payload(Integer idPayload) {
		this.idPayload = idPayload;
	}

	/**
	 * Instantiates a new payload.
	 *
	 * @param idPayload the id payload
	 * @param message the message
	 * @param fileName the file name
	 */
	public Payload(Integer idPayload, Message message, String fileName) {
		this.idPayload = idPayload;
		this.message = message;
		this.fileName = fileName;
	}

	// Property accessors
	/**
	 * Gets the id payload.
	 *
	 * @return the id payload
	 */
	public Integer getIdPayload() {
		return this.idPayload;
	}

	/**
	 * Sets the id payload.
	 *
	 * @param idPayload the new id payload
	 */
	public void setIdPayload(Integer idPayload) {
		this.idPayload = idPayload;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public Message getMessage() {
		return this.message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(Message message) {
		this.message = message;
	}

	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * Sets the file name.
	 *
	 * @param fileName the new file name
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Payload [idPayload=" + idPayload + ", message=" + message + ", fileName=" + fileName + "]";
	}
}