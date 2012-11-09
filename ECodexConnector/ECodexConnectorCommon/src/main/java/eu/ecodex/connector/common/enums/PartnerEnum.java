package eu.ecodex.connector.common.enums;

public enum PartnerEnum {
    DE("DE", "GW"), IT("DE", "GW"), ES("DE", "GW"), EE("DE", "GW");

    private final String name;
    private final String role;

    private PartnerEnum(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public static PartnerEnum findValue(String name, String role) {
        for (PartnerEnum e : values()) {
            if (e.getName().equals(name) && e.getRole().equals(role)) {
                return e;
            }
        }

        throw new IllegalArgumentException("No PartnerEnum found for name \"" + name + "\" and role \"" + role + "\"!");
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

}
