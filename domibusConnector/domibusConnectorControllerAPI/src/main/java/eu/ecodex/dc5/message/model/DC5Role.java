package eu.ecodex.dc5.message.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class DC5Role {
    private String role;
    private DC5RoleType roleType;

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
