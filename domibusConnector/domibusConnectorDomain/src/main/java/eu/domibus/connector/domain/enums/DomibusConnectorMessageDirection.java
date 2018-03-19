package eu.domibus.connector.domain.enums;

public enum DomibusConnectorMessageDirection {
    NAT_TO_GW, //from national to Gateway
    GW_TO_NAT, //from gatway to national
    CONN_TO_GW, //from connector to gw (eg. created evidence)
    CONN_TO_NAT; //from connector to national (eg. created evidence)
}
