/*
 * 
 */
package org.holodeck.backend.db.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The Class Message.
 */
@Entity
@Table(name = "Message")
public class Message implements java.io.Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4797100166607757335L;

	// Fields
	/** The id message. */
	@Id
	@TableGenerator(name="TABLE_GEN_MESSAGE", table="SEQUENCE_TABLE", pkColumnName="SEQ_NAME",
	valueColumnName="SEQ_COUNT", pkColumnValue="MESSAGE_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="TABLE_GEN_MESSAGE")
	@Column(name = "idMessage")
	private Integer idMessage;
	
	/** The messageUID. */
	@Column(name = "messageUID", length = 128)
	private String messageUID;
	
	/** The pmode. */
	@Column(name = "pmode", length = 128)
	private String pmode;
	
	/** The messageDate. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "messageDate", length = 19)
	private Date messageDate;
	
	/** The directory. */
	@Column(name = "directory", length = 1024)
	private String directory;
	
	/** The downloaded. */
	@Column(name = "downloaded")
	private boolean downloaded;
	
	/** The deleted. */
	@Column(name = "deleted")
	private boolean deleted;
	
	/** The payloads. */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "message")
	private List<Payload> payloads = new ArrayList<Payload>(0);

	// Constructors
	/**
	 * Instantiates a new message.
	 */
	public Message() {
	}

	/**
	 * Instantiates a new message.
	 *
	 * @param idMessage the id message
	 */
	public Message(Integer idMessage) {
		this.idMessage = idMessage;
	}

	/**
	 * Instantiates a new message.
	 *
	 * @param idMessage the id message
	 * @param messageUID the messageUID
	 * @param pmode the pmode
	 * @param messageDate the messageDate
	 * @param directory the directory
	 * @param downloaded the downloaded
	 * @param deleted the deleted
	 * @param payloads the payloads
	 */
	public Message(Integer idMessage, String messageUID, String pmode, Timestamp messageDate, String directory, boolean downloaded,
			boolean deleted, List<Payload> payloads) {
		this.idMessage = idMessage;
		this.messageUID = messageUID;
		this.pmode = pmode;
		this.messageDate = messageDate;
		this.directory = directory;
		this.downloaded = downloaded;
		this.deleted = deleted;
		this.payloads = payloads;
	}

	/**
	 * Gets the pmode.
	 *
	 * @return the pmode
	 */
	public String getPmode() {
		return pmode;
	}

	/**
	 * Sets the pmode.
	 *
	 * @param pmode the new pmode
	 */
	public void setPmode(String pmode) {
		this.pmode = pmode;
	}

	// Property accessors
	/**
	 * Gets the id message.
	 *
	 * @return the id message
	 */
	public Integer getIdMessage() {
		return this.idMessage;
	}

	/**
	 * Sets the id message.
	 *
	 * @param idMessage the new id message
	 */
	public void setIdMessage(Integer idMessage) {
		this.idMessage = idMessage;
	}

	/**
	 * Gets the messageUID.
	 *
	 * @return the messageUID
	 */
	public String getMessageUID() {
		return this.messageUID;
	}

	/**
	 * Sets the messageUID.
	 *
	 * @param messageUID the new messageUID
	 */
	public void setMessageUID(String messageUID) {
		this.messageUID = messageUID;
	}

	/**
	 * Gets the messageDate.
	 *
	 * @return the messageDate
	 */
	public Date getMessageDate() {
		return this.messageDate;
	}

	/**
	 * Sets the messageDate.
	 *
	 * @param messageDate the new messageDate
	 */
	public void setMessageDate(Date messageDate) {
		this.messageDate = messageDate;
	}

	/**
	 * Gets the directory.
	 *
	 * @return the directory
	 */
	public String getDirectory() {
		return this.directory;
	}

	/**
	 * Sets the directory.
	 *
	 * @param directory the new directory
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	/**
	 * Gets the downloaded.
	 *
	 * @return the downloaded
	 */
	public boolean getDownloaded() {
		return this.downloaded;
	}

	/**
	 * Sets the downloaded.
	 *
	 * @param downloaded the new downloaded
	 */
	public void setDownloaded(boolean downloaded) {
		this.downloaded = downloaded;
	}

	/**
	 * Gets the deleted.
	 *
	 * @return the deleted
	 */
	public boolean getDeleted() {
		return this.deleted;
	}

	/**
	 * Sets the deleted.
	 *
	 * @param deleted the new deleted
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Gets the payloads.
	 *
	 * @return the payloads
	 */
	public List<Payload> getPayloads() {
		return this.payloads;
	}

	/**
	 * Sets the payloads.
	 *
	 * @param payloads the new payloads
	 */
	public void setPayloads(List<Payload> payloads) {
		this.payloads = payloads;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Message [idMessage=" + idMessage + ", messageUID=" + messageUID + ", pmode=" + pmode + ", messageDate=" + messageDate
				+ ", directory=" + directory + ", downloaded=" + downloaded + ", deleted=" + deleted + ", payloads="
				+ payloads + "]";
	}
}