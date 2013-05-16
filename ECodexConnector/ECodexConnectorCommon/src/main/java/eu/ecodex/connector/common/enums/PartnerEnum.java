package eu.ecodex.connector.common.enums;

public enum PartnerEnum {
    AT("AT", "GW"), DE("DE", "GW"), IT("IT", "GW"), ES("ES", "GW"), EE("EE", "GW"), EU("EU", "GW"), CZ("CZ", "GW"), GR("GR", "GW"), BE("BE", "GW"), TR("TR", "GW"), PL("PL", "GW"), FR("FR", "GW"), NL("NL", "GW"), HU("HU", "GW"), PT("PT", "GW"), MT("MT", "GW"), LT("LT", "GW"), SE("SE", "GW"), GB("GB", "GW");

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
