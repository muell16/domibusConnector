package eu.domibus.webadmin.model.connector;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DOMIBUS_WEBADMIN_USER")
public class DomibusWebAdminUser {


	@Id
    @Column(name="USERNAME")
    private String username;
	@Column(name="PASSWORD")
    private String password;
	@Column(name="ROLE")
    private String role;
    @Column(name="SALT")
    private String salt;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

}
