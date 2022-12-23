package eu.ecodex.dc5.message.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;



@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString

@Valid

@Entity
public class DC5Role {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @NotNull
    @NotBlank
    private String role;
    @NonNull
    @NotNull
    @Convert(converter = DC5RoleTypeConverter.class)
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

    public static class DC5RoleTypeConverter implements AttributeConverter<DC5RoleType, String> {

        @Override
        public String convertToDatabaseColumn(DC5RoleType attribute) {
            return attribute.name();
        }

        @Override
        public DC5RoleType convertToEntityAttribute(String dbData) {
            return DC5RoleType.valueOf(dbData);
        }
    }
}
