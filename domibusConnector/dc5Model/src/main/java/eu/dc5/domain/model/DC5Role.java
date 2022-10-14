package eu.dc5.domain.model;


import javax.persistence.Embeddable;

@Embeddable
public class DC5Role {
    private String role;
    private DC5RoleType roleType;

    public DC5Role() {

    }

    public DC5Role(String role, DC5RoleType roleType) {
        this.role = role;
        this.roleType = roleType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public DC5RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(DC5RoleType roleType) {
        this.roleType = roleType;
    }
}
