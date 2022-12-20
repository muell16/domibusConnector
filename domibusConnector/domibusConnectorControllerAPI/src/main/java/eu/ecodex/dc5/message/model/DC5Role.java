package eu.ecodex.dc5.message.model;


import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

//@Embeddable

@NoArgsConstructor
@AllArgsConstructor
@Getter

@ToString

@Entity
public class DC5Role {

    @Id
    @GeneratedValue
    private Long id;

    private String role;
    private DC5RoleType roleType;

    @Builder(toBuilder = true)
    public DC5Role(String role, DC5RoleType roleType) {
        this.role = role;
        this.roleType = roleType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DC5Role)) return false;

        DC5Role dc5Role = (DC5Role) o;

        if (!Objects.equals(role, dc5Role.role)) return false;
        return roleType == dc5Role.roleType;
    }

    @Override
    public int hashCode() {
        int result = role != null ? role.hashCode() : 0;
        result = 31 * result + (roleType != null ? roleType.hashCode() : 0);
        return result;
    }
}
