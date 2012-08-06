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
	@TableGenerator(name="TABLE_GEN_MESSAGE", table="SEQUENCE_TABLE", pkColumnName="SEQ_NAME",
			valueColumnName="SEQ_COUNT", pkColumnValue="MESSAGE_SEQ", allocationSize=1)
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="TABLE_GEN_MESSAGE")
	@Column(name = "idMessage", unique = true, nullable = false)
	private Integer idMessage;
	
	/** The uid. */
	@Column(name = "uid", length = 128)
	private String uid;
	
	/** The pmode. */
	@Column(name = "pmode", length = 128)
	private String pmode;
	
	/** The date. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date", length = 19)
	private Date date;
	
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
	 * @param uid the uid
	 * @param pmode the pmode
	 * @param date the date
	 * @param directory the directory
	 * @param downloaded the downloaded
	 * @param deleted the deleted
	 * @param payloads the payloads
	 */
	public Message(Integer idMessage, String uid, String pmode, Timestamp date, String directory, boolean downloaded,
			boolean deleted, List<Payload> payloads) {
		this.idMessage = idMessage;
		this.uid = uid;
		this.pmode = pmode;
		this.date = date;
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
	 * Gets the uid.
	 *
	 * @return the uid
	 */
	public String getUid() {
		return this.uid;
	}

	/**
	 * Sets the uid.
	 *
	 * @param uid the new uid
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(Date date) {
		this.date = date;
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
		return "Message [idMessage=" + idMessage + ", uid=" + uid + ", pmode=" + pmode + ", date=" + date
				+ ", directory=" + directory + ", downloaded=" + downloaded + ", deleted=" + deleted + ", payloads="
				+ payloads + "]";
	}
}