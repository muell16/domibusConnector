package eu.ecodex.dc5.util.model;

import eu.domibus.connector.persistence.model.PDomibusConnectorPersistenceModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = DC5ConfigItem.TABLE_NAME)
@Getter
@Setter
public class DC5ConfigItem {

	public static final String TABLE_NAME = "DC5_CFGITEM";

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

	@Lob
	@Column(name="KEYSTORE", nullable=false)
    private byte[] keystore;

//	@Column(name = "PASSWORD", length = 1024)
//	private String password;
	
	@Column(name="UPLOADED", nullable = false)
    private Date uploaded;
	
	@Column(name="DESCRIPTION", length = 512)
	private String description;

	@PrePersist
    public void prePersist() {
        if(uploaded == null) 
        	uploaded = new Date();
    }

}
