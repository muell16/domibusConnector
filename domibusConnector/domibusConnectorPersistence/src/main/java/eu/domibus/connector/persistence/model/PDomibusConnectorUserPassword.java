package eu.domibus.connector.persistence.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.apache.commons.lang3.builder.ToStringBuilder;

import eu.domibus.connector.persistence.model.enums.UserRole;

@Entity
@Table(name = "DOMIBUS_CONNECTOR_USER_PWD")
public class PDomibusConnectorUserPassword {

    @Id
    @TableGenerator(name = "seqStoreUserPassword", table = "DOMIBUS_CONNECTOR_SEQ_STORE", pkColumnName = "SEQ_NAME", pkColumnValue = "DOMIBUS_CONNECTOR_USER_PWD.ID", valueColumnName = "SEQ_VALUE", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seqStoreUserPassword")
	@Column(name = "ID")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private PDomibusConnectorUser user;
    
    @Column(nullable = false)
    private String password;
	
    @Column(nullable = false)
    private String salt;
    
    @Column(name="CURRENT_PWD")
	private boolean currentPassword;
    
    @Column(name="INITIAL_PWD")
	private boolean initialPassword;
		
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

	public PDomibusConnectorUser getUser() {
		return user;
	}

	public void setUser(PDomibusConnectorUser user) {
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
