package eu.domibus.connector.persistence.model;

import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import eu.domibus.connector.persistence.model.enums.KeystoreType;

@Entity
@Table(name = PDomibusConnectorKeystore.TABLE_NAME)
public class PDomibusConnectorKeystore {

	public static final String TABLE_NAME = "DC_KEYSTORE";

	@Id
	@Column(name="ID")
	@TableGenerator(name = "seq" + TABLE_NAME,
		table = PDomibusConnectorPersistenceModel.SEQ_STORE_TABLE_NAME,
		pkColumnName = PDomibusConnectorPersistenceModel.SEQ_NAME_COLUMN_NAME,
		pkColumnValue = TABLE_NAME + ".ID",
		valueColumnName = PDomibusConnectorPersistenceModel.SEQ_VALUE_COLUMN_NAME,
		initialValue = PDomibusConnectorPersistenceModel.INITIAL_VALUE,
		allocationSize = PDomibusConnectorPersistenceModel.ALLOCATION_SIZE)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seq" + TABLE_NAME)
	private Long id;
	
	@Column(name="UUID", nullable=false, unique=true)
	private String uuid;

	@Column(name="KEYSTORE", nullable=false)
    private Blob keystore;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "PW_SALT")
	private String pwSalt;
	
	@Column(name="UPLOADED")
    private Date uploaded;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="TYPE")
	@Enumerated(EnumType.STRING)
	private KeystoreType type;
	
	@PrePersist
    public void prePersist() {
        if(uploaded == null) 
        	uploaded = new Date();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Blob getKeystore() {
		return keystore;
	}

	public void setKeystore(Blob keystore) {
		this.keystore = keystore;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPwSalt() {
		return pwSalt;
	}

	public void setPwSalt(String pwSalt) {
		this.pwSalt = pwSalt;
	}

	public Date getUploaded() {
		return uploaded;
	}

	public void setUploaded(Date uploaded) {
		this.uploaded = uploaded;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public KeystoreType getType() {
		return type;
	}

	public void setType(KeystoreType type) {
		this.type = type;
	}

	
}
