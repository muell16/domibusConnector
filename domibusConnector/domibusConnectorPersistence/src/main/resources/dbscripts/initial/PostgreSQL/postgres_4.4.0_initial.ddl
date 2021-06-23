CREATE TABLE DC_DB_VERSION
(
	TAG varchar(255) NOT NULL
)
;

CREATE TABLE DC_KEYSTORE
(
	ID numeric(10) NOT NULL,
	KEYSTORE bytea NOT NULL,
	PASSWORD varchar(1024),
	PW_SALT varchar(512),
	UPLOADED timestamp without time zone NOT NULL
)
;

CREATE TABLE DC_LINK_CONFIG_PROPERTY
(
	DC_LINK_CONFIGURATION_ID numeric(10) NOT NULL,
	PROPERTY_NAME varchar(255) NOT NULL,
	PROPERTY_VALUE text
)
;

CREATE TABLE DC_LINK_CONFIGURATION
(
	ID numeric(10) NOT NULL,
	CONFIG_NAME varchar(255) NOT NULL,
	LINK_IMPL varchar(255)
)
;

CREATE TABLE DC_LINK_PARTNER
(
	ID numeric(10) NOT NULL,
	NAME varchar(50) NOT NULL,
	DESCRIPTION text,
	ENABLED numeric(1),
	LINK_CONFIG_ID numeric(10),
	LINK_TYPE varchar(20),
	LINK_MODE varchar(20)
)
;

CREATE TABLE DC_LINK_PARTNER_PROPERTY
(
	DC_LINK_PARTNER_ID numeric(10) NOT NULL,
	PROPERTY_NAME varchar(255) NOT NULL,
	PROPERTY_VALUE text
)
;

CREATE TABLE DC_MESSAGE_LANE
(
	ID numeric(10) NOT NULL,
	NAME varchar(255) NOT NULL,
	DESCRIPTION text
)
;

CREATE TABLE DC_MESSAGE_LANE_PROPERTY
(
	DC_MESSAGE_LANE_ID numeric(10) NOT NULL,
	PROPERTY_NAME varchar(255) NOT NULL,
	PROPERTY_VALUE text
)
;

CREATE TABLE DC_MSGCNT_DETSIG
(
	ID numeric(10) NOT NULL,
	SIGNATURE text,
	SIGNATURE_NAME varchar(255),
	SIGNATURE_TYPE varchar(255)
)
;

CREATE TABLE DC_PMODE_SET
(
	ID numeric(10) NOT NULL,
	FK_MESSAGE_LANE numeric(10),
	CREATED timestamp without time zone NOT NULL,
	DESCRIPTION text,
	ACTIVE numeric(1) NOT NULL,
	PMODES bytea,
	FK_CONNECTORSTORE numeric(10)
)
;

CREATE TABLE DC_TRANSPORT_STEP
(
	ID numeric(10) NOT NULL,
	CONNECTOR_MESSAGE_ID varchar(255) NOT NULL,
	LINK_PARTNER_NAME varchar(255) NOT NULL,
	ATTEMPT numeric(2) NOT NULL,
	TRANSPORT_ID varchar(255),
	TRANSPORT_SYSTEM_MESSAGE_ID varchar(255),
	REMOTE_MESSAGE_ID varchar(255),
	CREATED timestamp without time zone,
	TRANSPORT_MESSAGE text,
	FINAL_STATE_REACHED timestamp without time zone
)
;

CREATE TABLE DC_TRANSPORT_STEP_STATUS
(
	TRANSPORT_STEP_ID numeric(10) NOT NULL,
	STATE varchar(40) NOT NULL,
	CREATED timestamp without time zone,
	TEXT text
)
;

CREATE TABLE DOMIBUS_CONNECTOR_ACTION
(
	ID numeric(10) NOT NULL,
	FK_PMODE_SET numeric(10) NOT NULL,
	ACTION varchar(255) NOT NULL
)
;

CREATE TABLE DOMIBUS_CONNECTOR_BIGDATA
(
	ID varchar(255) NOT NULL,
	CHECKSUM text,
	CREATED timestamp without time zone NOT NULL,
	MESSAGE_ID numeric(10),
	LAST_ACCESS timestamp without time zone,
	NAME text,
	CONTENT bytea,
	MIMETYPE varchar(255)
)
;

CREATE TABLE DOMIBUS_CONNECTOR_EVIDENCE
(
	ID numeric(10) NOT NULL,
	MESSAGE_ID numeric(10) NOT NULL,
	CONNECTOR_MESSAGE_ID varchar(255),
	TYPE varchar(255),
	EVIDENCE text,
	DELIVERED_NAT timestamp without time zone,
	DELIVERED_GW timestamp without time zone,
	UPDATED timestamp without time zone NOT NULL
)
;

CREATE TABLE DOMIBUS_CONNECTOR_MESSAGE
(
	ID numeric(10) NOT NULL,
	EBMS_MESSAGE_ID varchar(255),
	BACKEND_MESSAGE_ID varchar(255),
	BACKEND_NAME varchar(255),
	CONNECTOR_MESSAGE_ID varchar(255),
	CONVERSATION_ID varchar(255),
	HASH_VALUE text,
	CONFIRMED timestamp without time zone,
	REJECTED timestamp without time zone,
	DELIVERED_BACKEND timestamp without time zone,
	DELIVERED_GW timestamp without time zone,
	UPDATED timestamp without time zone NOT NULL,
	CREATED timestamp without time zone NOT NULL,
	DIRECTION_SOURCE varchar(20),
	DIRECTION_TARGET varchar(20),
	GATEWAY_NAME varchar(255)
)
;

CREATE TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO
(
	ID numeric(10) NOT NULL,
	MESSAGE_ID numeric(10) NOT NULL,
	FK_FROM_PARTY_ID numeric(10),
	FK_TO_PARTY_ID numeric(10),
	ORIGINAL_SENDER varchar(2048),
	FINAL_RECIPIENT varchar(2048),
	FK_SERVICE numeric(10),
	FK_ACTION numeric(10),
	CREATED timestamp without time zone NOT NULL,
	UPDATED timestamp without time zone NOT NULL
)
;

CREATE TABLE DOMIBUS_CONNECTOR_MSG_CONT
(
	ID numeric(10) NOT NULL,
	MESSAGE_ID numeric(10) NOT NULL,
	CONTENT_TYPE varchar(255),
	CONTENT bytea,
	CHECKSUM text,
	CREATED timestamp without time zone NOT NULL,
	STORAGE_PROVIDER_NAME varchar(255),
	STORAGE_REFERENCE_ID varchar(512),
	DIGEST varchar(512),
	PAYLOAD_NAME varchar(512),
	PAYLOAD_IDENTIFIER varchar(512),
	PAYLOAD_DESCRIPTION text,
	PAYLOAD_MIMETYPE varchar(255),
	PAYLOAD_SIZE numeric(10),
	DETACHED_SIGNATURE_ID numeric(10),
	DELETED timestamp without time zone,
	CONNECTOR_MESSAGE_ID varchar(512)
)
;

CREATE TABLE DOMIBUS_CONNECTOR_MSG_ERROR
(
	ID numeric(10) NOT NULL,
	MESSAGE_ID numeric(10) NOT NULL,
	ERROR_MESSAGE varchar(2048) NOT NULL,
	DETAILED_TEXT text,
	ERROR_SOURCE text,
	CREATED timestamp without time zone NOT NULL
)
;

CREATE TABLE DOMIBUS_CONNECTOR_PARTY
(
	ID numeric(10) NOT NULL,
	FK_PMODE_SET numeric(10) NOT NULL,
	IDENTIFIER varchar(255),
	PARTY_ID varchar(255) NOT NULL,
	ROLE varchar(255) NOT NULL,
	PARTY_ID_TYPE varchar(512) NOT NULL
)
;

CREATE TABLE DOMIBUS_CONNECTOR_PROPERTY
(
	ID numeric(10) NOT NULL,
	PROPERTY_NAME varchar(2048) NOT NULL,
	PROPERTY_VALUE varchar(2048)
)
;

CREATE TABLE DOMIBUS_CONNECTOR_SEQ_STORE
(
	SEQ_NAME varchar(255) NOT NULL,
	SEQ_VALUE numeric(10) NOT NULL
)
;

CREATE TABLE DOMIBUS_CONNECTOR_SERVICE
(
	ID numeric(10) NOT NULL,
	FK_PMODE_SET numeric(10) NOT NULL,
	SERVICE varchar(255) NOT NULL,
	SERVICE_TYPE varchar(512)
)
;

CREATE TABLE DOMIBUS_CONNECTOR_USER
(
	ID numeric(10) NOT NULL,
	USERNAME varchar(50) NOT NULL,
	ROLE varchar(50) NOT NULL,
	LOCKED numeric(1) NOT NULL   DEFAULT 0,
	NUMBER_OF_GRACE_LOGINS numeric(2) NOT NULL   DEFAULT 5,
	GRACE_LOGINS_USED numeric(2) NOT NULL   DEFAULT 0,
	CREATED timestamp without time zone NOT NULL
)
;

CREATE TABLE DOMIBUS_CONNECTOR_USER_PWD
(
	ID numeric(10) NOT NULL,
	USER_ID numeric(10) NOT NULL,
	PASSWORD varchar(1024) NOT NULL,
	SALT varchar(512) NOT NULL,
	CURRENT_PWD numeric(1) NOT NULL   DEFAULT 0,
	INITIAL_PWD numeric(1) NOT NULL   DEFAULT 0,
	CREATED timestamp without time zone NOT NULL
)
;

ALTER TABLE DC_DB_VERSION ADD CONSTRAINT PK_DC_DB_VERSION
	PRIMARY KEY (TAG)
;

ALTER TABLE DC_KEYSTORE ADD CONSTRAINT PK_DC_CONNECTORSTORE
	PRIMARY KEY (ID)
;

ALTER TABLE DC_LINK_CONFIG_PROPERTY ADD CONSTRAINT PK_DC_LINK_CONF_PROP
	PRIMARY KEY (DC_LINK_CONFIGURATION_ID,PROPERTY_NAME)
;

ALTER TABLE DC_LINK_CONFIGURATION ADD CONSTRAINT PK_DC_LINK_CONFIGURATION
	PRIMARY KEY (ID)
;

ALTER TABLE DC_LINK_CONFIGURATION 
  ADD CONSTRAINT UN_DC_LINK_CONF_NAME_01 UNIQUE (CONFIG_NAME)
;

ALTER TABLE DC_LINK_PARTNER ADD CONSTRAINT PK_DC_LINK_P
	PRIMARY KEY (ID)
;

ALTER TABLE DC_LINK_PARTNER 
  ADD CONSTRAINT UN_DC_LINK_P_01 UNIQUE (NAME)
;

ALTER TABLE DC_LINK_PARTNER_PROPERTY ADD CONSTRAINT PK_DC_LINK_P_PROP
	PRIMARY KEY (DC_LINK_PARTNER_ID,PROPERTY_NAME)
;

ALTER TABLE DC_MESSAGE_LANE ADD CONSTRAINT PK_DC_MSG_LANE
	PRIMARY KEY (ID)
;

ALTER TABLE DC_MESSAGE_LANE 
  ADD CONSTRAINT UN_DC_MSG_LANE_01 UNIQUE (NAME)
;

ALTER TABLE DC_MESSAGE_LANE_PROPERTY ADD CONSTRAINT PK_DC_MSG_LANE_PROP
	PRIMARY KEY (DC_MESSAGE_LANE_ID,PROPERTY_NAME)
;

ALTER TABLE DC_MSGCNT_DETSIG ADD CONSTRAINT PK_DETSIG
	PRIMARY KEY (ID)
;

ALTER TABLE DC_PMODE_SET ADD CONSTRAINT PK_DC_PMODE_SET
	PRIMARY KEY (ID)
;

ALTER TABLE DC_TRANSPORT_STEP ADD CONSTRAINT PK_DC_TRANSPORT_STEP
	PRIMARY KEY (ID)
;

ALTER TABLE DC_TRANSPORT_STEP_STATUS ADD CONSTRAINT PK_DC_TRANS_STEP_STATUS
	PRIMARY KEY (TRANSPORT_STEP_ID,STATE)
;

ALTER TABLE DOMIBUS_CONNECTOR_ACTION ADD CONSTRAINT PK_DC_ACTION
	PRIMARY KEY (ID)
;

ALTER TABLE DOMIBUS_CONNECTOR_BIGDATA ADD CONSTRAINT PK_DC_BIGDATA
	PRIMARY KEY (ID)
;

ALTER TABLE DOMIBUS_CONNECTOR_EVIDENCE ADD CONSTRAINT PK_DC_EVIDENCE
	PRIMARY KEY (ID)
;

ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE ADD CONSTRAINT PK_DC_MESSAGE
	PRIMARY KEY (ID)
;

ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE 
  ADD CONSTRAINT UQ_DC_MSG_BCK_MSG_ID UNIQUE (BACKEND_MESSAGE_ID)
;

ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE 
  ADD CONSTRAINT UQ_DC_MSG_CON_MSG_ID UNIQUE (CONNECTOR_MESSAGE_ID)
;

ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT PK_DC_MESSAGE_INFO
	PRIMARY KEY (ID)
;

ALTER TABLE DOMIBUS_CONNECTOR_MSG_CONT ADD CONSTRAINT PK_DC_MSG_CONT
	PRIMARY KEY (ID)
;

ALTER TABLE DOMIBUS_CONNECTOR_MSG_ERROR ADD CONSTRAINT PK_DC_MSG_ERROR
	PRIMARY KEY (ID)
;

ALTER TABLE DOMIBUS_CONNECTOR_PARTY ADD CONSTRAINT PK_DC_PARTY
	PRIMARY KEY (ID)
;

ALTER TABLE DOMIBUS_CONNECTOR_PROPERTY ADD CONSTRAINT PK_DOMIBUS_CONN_03
	PRIMARY KEY (ID)
;

ALTER TABLE DOMIBUS_CONNECTOR_SEQ_STORE ADD CONSTRAINT PK_DC_SEQ_STORE
	PRIMARY KEY (SEQ_NAME)
;

ALTER TABLE DOMIBUS_CONNECTOR_SERVICE ADD CONSTRAINT PK_DC_SERVICE
	PRIMARY KEY (ID)
;

ALTER TABLE DOMIBUS_CONNECTOR_USER ADD CONSTRAINT PK_DC_USER
	PRIMARY KEY (ID)
;

ALTER TABLE DOMIBUS_CONNECTOR_USER 
  ADD CONSTRAINT USERNAME_UNIQUE UNIQUE (USERNAME)
;

ALTER TABLE DOMIBUS_CONNECTOR_USER_PWD ADD CONSTRAINT PK_DC_USER_PWD
	PRIMARY KEY (ID)
;

ALTER TABLE DC_LINK_CONFIG_PROPERTY ADD CONSTRAINT FK_DC_LINK_CONF_PROP_01
	FOREIGN KEY (DC_LINK_CONFIGURATION_ID) REFERENCES DC_LINK_CONFIGURATION (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DC_LINK_PARTNER ADD CONSTRAINT FK_DC_LINK_P_01
	FOREIGN KEY (LINK_CONFIG_ID) REFERENCES DC_LINK_CONFIGURATION (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DC_LINK_PARTNER_PROPERTY ADD CONSTRAINT FK_DC_LINK_P_PROP_01
	FOREIGN KEY (DC_LINK_PARTNER_ID) REFERENCES DC_LINK_PARTNER (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DC_MESSAGE_LANE_PROPERTY ADD CONSTRAINT FK_DC_MSG_LANE_PROP_01
	FOREIGN KEY (DC_MESSAGE_LANE_ID) REFERENCES DC_MESSAGE_LANE (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DC_PMODE_SET ADD CONSTRAINT FK_DC_PMODE_SET_01
	FOREIGN KEY (FK_MESSAGE_LANE) REFERENCES DC_MESSAGE_LANE (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DC_PMODE_SET ADD CONSTRAINT FK_DC_PMODE_SET_02
	FOREIGN KEY (FK_CONNECTORSTORE) REFERENCES DC_KEYSTORE (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DC_TRANSPORT_STEP_STATUS ADD CONSTRAINT FK_DC_TRANS_STEP_STATUS_01
	FOREIGN KEY (TRANSPORT_STEP_ID) REFERENCES DC_TRANSPORT_STEP (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DOMIBUS_CONNECTOR_ACTION ADD CONSTRAINT FK_DC_ACTION_PMODE_SET_01
	FOREIGN KEY (FK_PMODE_SET) REFERENCES DC_PMODE_SET (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DOMIBUS_CONNECTOR_BIGDATA ADD CONSTRAINT FK_DC_BIGDATA_01
	FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DOMIBUS_CONNECTOR_EVIDENCE ADD CONSTRAINT FK_DC_EVIDENCES_01
	FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_DC_MSG_INFO_ACTION
	FOREIGN KEY (FK_ACTION) REFERENCES DOMIBUS_CONNECTOR_ACTION (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_DC_MSG_INFO_F_PARTY
	FOREIGN KEY (FK_FROM_PARTY_ID) REFERENCES DOMIBUS_CONNECTOR_PARTY (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_DC_MSG_INFO_I
	FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_DC_MSG_INFO_SERVICE
	FOREIGN KEY (FK_SERVICE) REFERENCES DOMIBUS_CONNECTOR_SERVICE (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_DC_MSG_INFO_T_PARTY
	FOREIGN KEY (FK_TO_PARTY_ID) REFERENCES DOMIBUS_CONNECTOR_PARTY (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DOMIBUS_CONNECTOR_MSG_CONT ADD CONSTRAINT FK_DC_CON_01
	FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DOMIBUS_CONNECTOR_MSG_CONT ADD CONSTRAINT FK_DC_MSG_CONT_02
	FOREIGN KEY (DETACHED_SIGNATURE_ID) REFERENCES DC_MSGCNT_DETSIG (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DOMIBUS_CONNECTOR_MSG_ERROR ADD CONSTRAINT FK_DC_MSG_ERROR_01
	FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DOMIBUS_CONNECTOR_PARTY ADD CONSTRAINT FK_DC_SERVICE_PMODE_SET_01
	FOREIGN KEY (FK_PMODE_SET) REFERENCES DC_PMODE_SET (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DOMIBUS_CONNECTOR_SERVICE ADD CONSTRAINT FK_DC_SERVICE_PMODE_SET_01
	FOREIGN KEY (FK_PMODE_SET) REFERENCES DC_PMODE_SET (ID) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE DOMIBUS_CONNECTOR_USER_PWD ADD CONSTRAINT FK_DC_USER_PWD_01
	FOREIGN KEY (USER_ID) REFERENCES DOMIBUS_CONNECTOR_USER (ID) ON DELETE No Action ON UPDATE No Action
;
