package eu.ecodex.dc5.util.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import eu.domibus.connector.persistence.model.PDomibusConnectorPersistenceModel;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = DC5UserPassword.TABLE_NAME)
public class DC5UserPassword {

	public static final String TABLE_NAME = "DC5_USER_PWD";

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
    
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private DC5User user;
    
    @Column(nullable = false, length = 1024)
    private String password;
	
    @Column(nullable = false, length = 512)
    private String salt;
    
    @Column(name="CURRENT_PWD", nullable = false)
	private boolean currentPassword = true;
    
    @Column(name="INITIAL_PWD", nullable = false)
	private boolean initialPassword = true;
		
    @Column(name = "CREATED", nullable = false)
    private Date created;

    @PrePersist
    public void prePersist() {
    	 if(this.created==null)
         	this.created = new Date();
    }
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DC5User getUser() {
		return user;
	}

	public void setUser(DC5User user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public boolean isCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(boolean currentPassword) {
		this.currentPassword = currentPassword;
	}

	public boolean isInitialPassword() {
		return initialPassword;
	}

	public void setInitialPassword(boolean initialPassword) {
		this.initialPassword = initialPassword;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
    public String toString() {
        ToStringBuilder toString = new ToStringBuilder(this);
        toString.append("id", id);
        toString.append("username", this.user.getUsername());
        toString.append("password", this.password);
        toString.append("currentPassword", this.currentPassword);
        toString.append("initialPassword", this.initialPassword);
        toString.append("created", this.created);
        return toString.build();
    }

}
