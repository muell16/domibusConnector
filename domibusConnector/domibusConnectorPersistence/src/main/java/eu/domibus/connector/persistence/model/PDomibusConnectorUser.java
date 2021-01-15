package eu.domibus.connector.persistence.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.apache.commons.lang3.builder.ToStringBuilder;

import eu.domibus.connector.persistence.model.enums.UserRole;

@Entity
@Table(name = "DOMIBUS_CONNECTOR_USER")
public class PDomibusConnectorUser {

    @Id
	@Column(name = "ID")
    @TableGenerator(name = "seqStoreUser", table = "DOMIBUS_CONNECTOR_SEQ_STORE", pkColumnName = "SEQ_NAME", pkColumnValue = "DOMIBUS_CONNECTOR_USER.ID", valueColumnName = "SEQ_VALUE", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seqStoreUser")
    private Long id;
    
    @Column(name = "USERNAME", nullable=false)
    private String username;
    
    @Column(name = "ROLE", nullable=false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name="LOCKED")
	private boolean locked;

	@Column(name="NUMBER_OF_GRACE_LOGINS", nullable=false)
	private Long numberOfGraceLogins;

	@Column(name="GRACE_LOGINS_USED", nullable=false)
	private Long graceLoginsUsed;
	
    @Column(name = "CREATED", nullable = false)
    private Date created;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<PDomibusConnectorUserPassword> passwords = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if(this.created==null)
        	this.created = new Date();
        if(this.numberOfGraceLogins==null)
        	this.numberOfGraceLogins = Long.valueOf(5);
        if(this.graceLoginsUsed==null)
        	this.graceLoginsUsed=Long.valueOf(0);
    }
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public UserRole getRole() {
		return role;
	}



	public void setRole(UserRole role) {
		this.role = role;
	}



	public boolean isLocked() {
		return locked;
	}



	public void setLocked(boolean locked) {
		this.locked = locked;
	}



	public Long getNumberOfGraceLogins() {
		return numberOfGraceLogins;
	}



	public void setNumberOfGraceLogins(Long numberOfGraceLogins) {
		this.numberOfGraceLogins = numberOfGraceLogins;
	}



	public Long getGraceLoginsUsed() {
		return graceLoginsUsed;
	}



	public void setGraceLoginsUsed(Long graceLoginsUsed) {
		this.graceLoginsUsed = graceLoginsUsed;
	}



	public Date getCreated() {
		return created;
	}



	public void setCreated(Date created) {
		this.created = created;
	}



	public Set<PDomibusConnectorUserPassword> getPasswords() {
		return passwords;
	}

	public void setPasswords(Set<PDomibusConnectorUserPassword> passwords) {
		this.passwords = passwords;
	}

	@Override
    public String toString() {
        ToStringBuilder toString = new ToStringBuilder(this);
        toString.append("id", id);
        toString.append("username", this.username);
        toString.append("role", this.role);
        toString.append("locked", this.locked);
        toString.append("created", this.created);
        return toString.build();
    }

}
