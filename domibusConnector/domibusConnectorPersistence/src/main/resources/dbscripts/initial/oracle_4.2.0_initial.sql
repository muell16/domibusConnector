-- quartz table init
--
-- A hint submitted by a user: Oracle DB MUST be created as "shared" and the 
-- job_queue_processes parameter  must be greater than 2
-- However, these settings are pretty much standard after any
-- Oracle install, so most users need not worry about this.
--
-- Many other users (including the primary author of Quartz) have had success
-- runing in dedicated mode, so only consider the above as a hint ;-)
--



CREATE TABLE qrtz_job_details
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    JOB_NAME  VARCHAR2(200) NOT NULL,
    JOB_GROUP VARCHAR2(200) NOT NULL,
    DESCRIPTION VARCHAR2(250) NULL,
    JOB_CLASS_NAME   VARCHAR2(250) NOT NULL, 
    IS_DURABLE VARCHAR2(1) NOT NULL,
    IS_NONCONCURRENT VARCHAR2(1) NOT NULL,
    IS_UPDATE_DATA VARCHAR2(1) NOT NULL,
    REQUESTS_RECOVERY VARCHAR2(1) NOT NULL,
    JOB_DATA BLOB NULL,
    CONSTRAINT QRTZ_JOB_DETAILS_PK PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
);
CREATE TABLE qrtz_triggers
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    JOB_NAME  VARCHAR2(200) NOT NULL, 
    JOB_GROUP VARCHAR2(200) NOT NULL,
    DESCRIPTION VARCHAR2(250) NULL,
    NEXT_FIRE_TIME NUMBER(13) NULL,
    PREV_FIRE_TIME NUMBER(13) NULL,
    PRIORITY NUMBER(13) NULL,
    TRIGGER_STATE VARCHAR2(16) NOT NULL,
    TRIGGER_TYPE VARCHAR2(8) NOT NULL,
    START_TIME NUMBER(13) NOT NULL,
    END_TIME NUMBER(13) NULL,
    CALENDAR_NAME VARCHAR2(200) NULL,
    MISFIRE_INSTR NUMBER(2) NULL,
    JOB_DATA BLOB NULL,
    CONSTRAINT QRTZ_TRIGGERS_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    CONSTRAINT QRTZ_TRIGGER_TO_JOBS_FK FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP) 
      REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP) 
);
CREATE TABLE qrtz_simple_triggers
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    REPEAT_COUNT NUMBER(7) NOT NULL,
    REPEAT_INTERVAL NUMBER(12) NOT NULL,
    TIMES_TRIGGERED NUMBER(10) NOT NULL,
    CONSTRAINT QRTZ_SIMPLE_TRIG_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    CONSTRAINT QRTZ_SIMPLE_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
	REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE qrtz_cron_triggers
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    CRON_EXPRESSION VARCHAR2(120) NOT NULL,
    TIME_ZONE_ID VARCHAR2(80),
    CONSTRAINT QRTZ_CRON_TRIG_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    CONSTRAINT QRTZ_CRON_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
      REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE qrtz_simprop_triggers
  (          
    SCHED_NAME VARCHAR2(120) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    STR_PROP_1 VARCHAR2(512) NULL,
    STR_PROP_2 VARCHAR2(512) NULL,
    STR_PROP_3 VARCHAR2(512) NULL,
    INT_PROP_1 NUMBER(10) NULL,
    INT_PROP_2 NUMBER(10) NULL,
    LONG_PROP_1 NUMBER(13) NULL,
    LONG_PROP_2 NUMBER(13) NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR2(1) NULL,
    BOOL_PROP_2 VARCHAR2(1) NULL,
    CONSTRAINT QRTZ_SIMPROP_TRIG_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    CONSTRAINT QRTZ_SIMPROP_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
      REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE qrtz_blob_triggers
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    BLOB_DATA BLOB NULL,
    CONSTRAINT QRTZ_BLOB_TRIG_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    CONSTRAINT QRTZ_BLOB_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE qrtz_calendars
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    CALENDAR_NAME  VARCHAR2(200) NOT NULL, 
    CALENDAR BLOB NOT NULL,
    CONSTRAINT QRTZ_CALENDARS_PK PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
);
CREATE TABLE qrtz_paused_trigger_grps
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    TRIGGER_GROUP  VARCHAR2(200) NOT NULL, 
    CONSTRAINT QRTZ_PAUSED_TRIG_GRPS_PK PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
);
CREATE TABLE qrtz_fired_triggers 
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    ENTRY_ID VARCHAR2(95) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    INSTANCE_NAME VARCHAR2(200) NOT NULL,
    FIRED_TIME NUMBER(13) NOT NULL,
    SCHED_TIME NUMBER(13) NOT NULL,
    PRIORITY NUMBER(13) NOT NULL,
    STATE VARCHAR2(16) NOT NULL,
    JOB_NAME VARCHAR2(200) NULL,
    JOB_GROUP VARCHAR2(200) NULL,
    IS_NONCONCURRENT VARCHAR2(1) NULL,
    REQUESTS_RECOVERY VARCHAR2(1) NULL,
    CONSTRAINT QRTZ_FIRED_TRIGGER_PK PRIMARY KEY (SCHED_NAME,ENTRY_ID)
);
CREATE TABLE qrtz_scheduler_state 
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    INSTANCE_NAME VARCHAR2(200) NOT NULL,
    LAST_CHECKIN_TIME NUMBER(13) NOT NULL,
    CHECKIN_INTERVAL NUMBER(13) NOT NULL,
    CONSTRAINT QRTZ_SCHEDULER_STATE_PK PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
);
CREATE TABLE qrtz_locks
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    LOCK_NAME  VARCHAR2(40) NOT NULL, 
    CONSTRAINT QRTZ_LOCKS_PK PRIMARY KEY (SCHED_NAME,LOCK_NAME)
);

create index idx_qrtz_j_req_recovery on qrtz_job_details(SCHED_NAME,REQUESTS_RECOVERY);
create index idx_qrtz_j_grp on qrtz_job_details(SCHED_NAME,JOB_GROUP);

create index idx_qrtz_t_j on qrtz_triggers(SCHED_NAME,JOB_NAME,JOB_GROUP);
create index idx_qrtz_t_jg on qrtz_triggers(SCHED_NAME,JOB_GROUP);
create index idx_qrtz_t_c on qrtz_triggers(SCHED_NAME,CALENDAR_NAME);
create index idx_qrtz_t_g on qrtz_triggers(SCHED_NAME,TRIGGER_GROUP);
create index idx_qrtz_t_state on qrtz_triggers(SCHED_NAME,TRIGGER_STATE);
create index idx_qrtz_t_n_state on qrtz_triggers(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
create index idx_qrtz_t_n_g_state on qrtz_triggers(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
create index idx_qrtz_t_next_fire_time on qrtz_triggers(SCHED_NAME,NEXT_FIRE_TIME);
create index idx_qrtz_t_nft_st on qrtz_triggers(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
create index idx_qrtz_t_nft_misfire on qrtz_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
create index idx_qrtz_t_nft_st_misfire on qrtz_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
create index idx_qrtz_t_nft_st_misfire_grp on qrtz_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);

create index idx_qrtz_ft_trig_inst_name on qrtz_fired_triggers(SCHED_NAME,INSTANCE_NAME);
create index idx_qrtz_ft_inst_job_req_rcvry on qrtz_fired_triggers(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
create index idx_qrtz_ft_j_g on qrtz_fired_triggers(SCHED_NAME,JOB_NAME,JOB_GROUP);
create index idx_qrtz_ft_jg on qrtz_fired_triggers(SCHED_NAME,JOB_GROUP);
create index idx_qrtz_ft_t_g on qrtz_fired_triggers(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
create index idx_qrtz_ft_tg on qrtz_fired_triggers(SCHED_NAME,TRIGGER_GROUP);

-- INITIAL DATABASE SCRIPT FOR domibusConnector 4.2.x
--

CREATE TABLE DOMIBUS_CONNECTOR_SEQ_STORE (SEQ_NAME VARCHAR2(255 CHAR) NOT NULL, SEQ_VALUE NUMBER(10) NOT NULL);
ALTER TABLE DOMIBUS_CONNECTOR_SEQ_STORE ADD CONSTRAINT PK_DOMIBUS_CONNECTOR_SEQ_STORE PRIMARY KEY (SEQ_NAME);

INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE VALUES ('DOMIBUS_CONNECTOR_MESSAGES.ID', 1000);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE VALUES ('DOMIBUS_CONNECTOR_EVIDENCES.ID', 1000);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE VALUES ('DOMIBUS_CONNECTOR_MESSAGE_INFO.ID', 1000);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE VALUES ('DOMIBUS_CONNECTOR_MSG_ERROR.ID', 1000);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE VALUES ('DOMIBUS_CONNECTOR_USER.ID', 1000);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE VALUES ('DOMIBUS_CONNECTOR_USER_PWD.ID', 1000);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE VALUES ('DOMIBUS_CONNECTOR_PROPERTY.ID', 1000);

CREATE TABLE DC_PMODE_SET (
    ID DECIMAL(10,0),
    FK_MESSAGE_LANE DECIMAL (10,0),
    CREATED TIMESTAMP,
    DESCRIPTION TEXT,
    ACTIVE DECIMAL(1, 0),
    CONSTRAINT PK_DC_PMODE_SET PRIMARY KEY (ID)
);

CREATE TABLE DOMIBUS_CONNECTOR_ACTION (
    ID DECIMAL(10,0) NOT NULL,
    FK_PMODE_SET DECIMAL(10,0) NOT NULL,
    ACTION VARCHAR(255) NOT NULL,
    PDF_REQUIRED DECIMAL(1, 0) NOT NULL
);
ALTER TABLE DOMIBUS_CONNECTOR_ACTION ADD CONSTRAINT PK_DOMIBUS_CONNECTOR_ACTION PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_ACTION ADD CONSTRAINT FK_ACTION_PMODE_SET_ID FOREIGN KEY (FK_PMODE_SET) REFERENCES DC_PMODE_SET(ID);


CREATE TABLE DOMIBUS_CONNECTOR_SERVICE (
    ID DECIMAL(10,0) NOT NULL,
    FK_PMODE_SET DECIMAL(10,0) NOT NULL,
    SERVICE VARCHAR(255) NOT NULL,
    SERVICE_TYPE VARCHAR(255) NOT NULL
    );
ALTER TABLE DOMIBUS_CONNECTOR_SERVICE ADD CONSTRAINT PK_DOMIBUS_CONNECTOR_SERVICE PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_SERVICE ADD CONSTRAINT FK_SERVICE_PMODE_SET_ID FOREIGN KEY (FK_PMODE_SET) REFERENCES DC_PMODE_SET(ID);

CREATE TABLE DOMIBUS_CONNECTOR_PARTY (
    ID DECIMAL(10,0) NOT NULL,
    FK_PMODE_SET DECIMAL(10,0) NOT NULL,
    IDENTIFIER VARCHAR(255),
    PARTY_ID VARCHAR(255) NOT NULL,
    ROLE VARCHAR(255) NOT NULL,
    PARTY_ID_TYPE VARCHAR(512) NOT NULL
);
ALTER TABLE DOMIBUS_CONNECTOR_PARTY ADD CONSTRAINT PK_DOMIBUS_CONNECTOR_PARTY PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_PARTY ADD CONSTRAINT FK_PARTY_PMODE_SET_ID FOREIGN KEY (FK_PMODE_SET) REFERENCES DC_PMODE_SET(ID);

CREATE TABLE DOMIBUS_CONNECTOR_MSG_ERROR (
    ID DECIMAL(10, 0) NOT NULL,
    MESSAGE_ID DECIMAL(10, 0) NOT NULL,
    ERROR_MESSAGE VARCHAR2(2048 CHAR) NOT NULL,
    DETAILED_TEXT CLOB,
    ERROR_SOURCE CLOB,
    CREATED TIMESTAMP NOT NULL);
ALTER TABLE DOMIBUS_CONNECTOR_MSG_ERROR ADD CONSTRAINT PK_DOMIBUS_CONNECTOR_MSG_ERROR PRIMARY KEY (ID);

CREATE TABLE DOMIBUS_CONNECTOR_MSG_CONT (
    ID DECIMAL(10, 0) NOT NULL,
    MESSAGE_ID DECIMAL(10, 0),
    CONTENT_TYPE VARCHAR2(255 CHAR),
    CONTENT BLOB,
    CHECKSUM CLOB,
    CREATED TIMESTAMP,
    STORAGE_PROVIDER_NAME VARCHAR2(255),
    STORAGE_REFERENCE_ID VARCHAR2(512),
    DIGEST VARCHAR2(512),
    PAYLOAD_NAME VARCHAR2(512),
    PAYLOAD_IDENTIFIER VARCHAR2(512),
    PAYLOAD_DESCRIPTION CLOB,
    PAYLOAD_MIMETYPE VARCHAR2(255),
    DETACHED_SIGNATURE_ID DECIMAL(10,0)
);

ALTER TABLE DOMIBUS_CONNECTOR_MSG_CONT ADD CONSTRAINT PK_DOMIBUS_CONNECTOR_MSG__01 PRIMARY KEY (ID);

CREATE TABLE DOMIBUS_CONNECTOR_BIGDATA (
ID VARCHAR2(255 CHAR) NOT NULL,
    CHECKSUM CLOB,
    CREATED TIMESTAMP,
    MESSAGE_ID DECIMAL(10, 0),
    LAST_ACCESS TIMESTAMP,
    NAME CLOB,
    CONTENT BLOB,
    MIMETYPE VARCHAR2(255 CHAR));
ALTER TABLE DOMIBUS_CONNECTOR_BIGDATA ADD PRIMARY KEY (ID);

CREATE TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO (
    ID DECIMAL(10, 0) NOT NULL,
    MESSAGE_ID DECIMAL(10, 0) NOT NULL,
--     FROM_PARTY_ID VARCHAR2(255 CHAR),
--     FROM_PARTY_ROLE VARCHAR2(255 CHAR),
--     TO_PARTY_ID VARCHAR2(255 CHAR),
--     TO_PARTY_ROLE VARCHAR2(255 CHAR),
    FK_FROM_PARTY_ID DECIMAL(10, 0),
    FK_TO_PARTY_ID DECIMAL(10, 0),
    ORIGINAL_SENDER VARCHAR2(255 CHAR),
    FINAL_RECIPIENT VARCHAR2(255 CHAR),
    FK_SERVICE DECIMAL(10, 0),
    FK_ACTION DECIMAL(10, 0),
    CREATED TIMESTAMP,
    UPDATED TIMESTAMP);
ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT PK_CONNECTOR_MESSAGE_INFO PRIMARY KEY (ID);

CREATE TABLE DOMIBUS_CONNECTOR_MESSAGE (
    ID DECIMAL(10, 0) NOT NULL,
    EBMS_MESSAGE_ID VARCHAR2(255 CHAR),
    BACKEND_MESSAGE_ID VARCHAR2(255 CHAR),
    BACKEND_NAME VARCHAR2(255 CHAR),
    CONNECTOR_MESSAGE_ID VARCHAR2(255 CHAR),
    CONVERSATION_ID VARCHAR2(255 CHAR),
    HASH_VALUE CLOB,
    CONFIRMED TIMESTAMP,
    REJECTED TIMESTAMP,
    DELIVERED_BACKEND TIMESTAMP,
    DELIVERED_GW TIMESTAMP,
    UPDATED TIMESTAMP,
    CREATED TIMESTAMP,
    DIRECTION_SOURCE VARCHAR2(20),
    DIRECTION_TARGET VARCHAR2(20),
    GATEWAY_NAME VARCHAR(255)
);

ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE ADD CONSTRAINT PK_DOMIBUS_CONNECTOR_MESSAGE PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE ADD CONSTRAINT UNIQUE_CONNECTOR_MESSAGE_ID UNIQUE (CONNECTOR_MESSAGE_ID);
ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE ADD CONSTRAINT UQ_DOMIBUS_CONNE_EBMS_MESSAGE UNIQUE (EBMS_MESSAGE_ID);
ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE ADD CONSTRAINT UQ_DOMIBUS_CONNE_NAT_MESSAGE_ UNIQUE (BACKEND_MESSAGE_ID);

CREATE TABLE DOMIBUS_CONNECTOR_EVIDENCE (ID DECIMAL(10, 0) NOT NULL, MESSAGE_ID DECIMAL(10, 0) NOT NULL, CONNECTOR_MESSAGE_ID VARCHAR(255), TYPE VARCHAR2(255 CHAR), EVIDENCE CLOB, DELIVERED_NAT TIMESTAMP, DELIVERED_GW TIMESTAMP, UPDATED TIMESTAMP);
ALTER TABLE DOMIBUS_CONNECTOR_EVIDENCE ADD CONSTRAINT PK_DOMIBUS_CONNECTOR_EVIDENCE PRIMARY KEY (ID);

CREATE TABLE DOMIBUS_CONNECTOR_CONT_TYPE (MESSAGE_CONTENT_TYPE VARCHAR2(255 CHAR) NOT NULL);
ALTER TABLE DOMIBUS_CONNECTOR_CONT_TYPE ADD CONSTRAINT PK_DOMIBUS_CONN_02 PRIMARY KEY (MESSAGE_CONTENT_TYPE);

CREATE TABLE DOMIBUS_CONNECTOR_BACKEND_INFO (ID DECIMAL(10, 0) NOT NULL, BACKEND_NAME VARCHAR2(255 CHAR) NOT NULL, BACKEND_KEY_ALIAS VARCHAR2(255 CHAR) NOT NULL, BACKEND_KEY_PASS VARCHAR2(255 CHAR), BACKEND_SERVICE_TYPE VARCHAR2(255 CHAR), BACKEND_ENABLED NUMBER(1) DEFAULT 1, BACKEND_DEFAULT NUMBER(1) DEFAULT 0, BACKEND_DESCRIPTION CLOB, BACKEND_PUSH_ADDRESS CLOB);
ALTER TABLE DOMIBUS_CONNECTOR_BACKEND_INFO ADD CONSTRAINT PK_DOMIBUS_CONNECTOR_BACK_01 PRIMARY KEY (ID);

ALTER TABLE DOMIBUS_CONNECTOR_BACKEND_INFO ADD CONSTRAINT UN_DOMIBUS_CONNECTOR_BACK_01 UNIQUE (BACKEND_NAME);
ALTER TABLE DOMIBUS_CONNECTOR_BACKEND_INFO ADD CONSTRAINT UN_DOMIBUS_CONNECTOR_BACK_02 UNIQUE (BACKEND_KEY_ALIAS);

CREATE TABLE DOMIBUS_CONNECTOR_BACK_2_S (DOMIBUS_CONNECTOR_SERVICE_ID VARCHAR2(255 CHAR) NOT NULL, DOMIBUS_CONNECTOR_BACKEND_ID DECIMAL(10, 0) NOT NULL);
ALTER TABLE DOMIBUS_CONNECTOR_BACK_2_S ADD CONSTRAINT PK_DOMIBUS_CONN_01 PRIMARY KEY (DOMIBUS_CONNECTOR_SERVICE_ID, DOMIBUS_CONNECTOR_BACKEND_ID);

ALTER TABLE DOMIBUS_CONNECTOR_MSG_ERROR ADD CONSTRAINT FK_DOMIBUS_CONNECTOR_MSG_ERROR FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID);
ALTER TABLE DOMIBUS_CONNECTOR_MSG_CONT ADD CONSTRAINT FK_DOMIBUS_CONN_DOMIBUS_CON_04 FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID);

ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_MSGINFO_FROM_PARTY FOREIGN KEY (FK_FROM_PARTY_ID) REFERENCES DOMIBUS_CONNECTOR_PARTY(ID);
ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_MSGINFO_TO_PARTY FOREIGN KEY (FK_TO_PARTY_ID) REFERENCES DOMIBUS_CONNECTOR_PARTY(ID);
ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_MSGINFO_SERVICE FOREIGN KEY (FK_SERVICE) REFERENCES DOMIBUS_CONNECTOR_SERVICE(ID);
ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_MSGINFO_ACTION FOREIGN KEY (FK_ACTION) REFERENCES DOMIBUS_CONNECTOR_ACTION(ID);
ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_DOMIBUS_CONNECTOR_MESSAGE_I FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID);

ALTER TABLE DOMIBUS_CONNECTOR_EVIDENCE ADD CONSTRAINT FK_DOMIBUS_CONNECTOR_EVIDENCES FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID);

ALTER TABLE DOMIBUS_CONNECTOR_BACK_2_S ADD CONSTRAINT FK_DOMIBUS_CONN_DOMIBUS_CON_01 FOREIGN KEY (DOMIBUS_CONNECTOR_BACKEND_ID) REFERENCES DOMIBUS_CONNECTOR_BACKEND_INFO (ID);


ALTER TABLE DOMIBUS_CONNECTOR_BIGDATA ADD CONSTRAINT FK_DOMIBUS_CONNECTOR_BIGDATA FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID);

CREATE TABLE DOMIBUS_CONNECTOR_PROPERTY (
  ID DECIMAL(10, 0) NOT NULL PRIMARY KEY,
  PROPERTY_NAME VARCHAR2(2048) NOT NULL,
  PROPERTY_VALUE VARCHAR2(2048) NULL
);

-- USER:

CREATE TABLE DOMIBUS_CONNECTOR_USER (
	ID NUMBER(10) NOT NULL,
	USERNAME VARCHAR2(50) NOT NULL,
	ROLE VARCHAR2(50) NOT NULL,
	LOCKED NUMBER(1) DEFAULT 0 NOT NULL,
	NUMBER_OF_GRACE_LOGINS NUMBER(2) DEFAULT 5 NOT NULL,
	GRACE_LOGINS_USED NUMBER(2) DEFAULT 0 NOT NULL,
	CREATED TIMESTAMP NOT NULL
);

CREATE TABLE DOMIBUS_CONNECTOR_USER_PWD (
	ID NUMBER(10) NOT NULL,
	USER_ID NUMBER(10) NOT NULL,
	PASSWORD VARCHAR2(1024) NOT NULL,
	SALT VARCHAR2(512) NOT NULL,
	CURRENT_PWD NUMBER(1) DEFAULT 0 NOT NULL,
	INITIAL_PWD NUMBER(1) DEFAULT 0 NOT NULL,
	CREATED TIMESTAMP NOT NULL
);

/* Create Primary Keys, Indexes, Uniques, Checks, Triggers */

ALTER TABLE  DOMIBUS_CONNECTOR_USER
 ADD CONSTRAINT PK_CONNECTOR_USER
	PRIMARY KEY (ID)
;

ALTER TABLE DOMIBUS_CONNECTOR_USER_PWD
 ADD CONSTRAINT PK_CONNECTOR_PASSWORD
	PRIMARY KEY (ID);


INSERT INTO DOMIBUS_CONNECTOR_USER (ID, USERNAME, ROLE, LOCKED, NUMBER_OF_GRACE_LOGINS, GRACE_LOGINS_USED, CREATED) VALUES (1, 'admin', 'ADMIN', 0, 5, 0, current_timestamp);
INSERT INTO DOMIBUS_CONNECTOR_USER_PWD (ID, USER_ID, PASSWORD, SALT, CURRENT_PWD, INITIAL_PWD, CREATED) VALUES (1, 1, '2bf5e637d0d82a75ca43e3be85df2c23febffc0cc221f5e010937005df478a19b5eaab59fe7e4e97f6b43ba648c169effd432e19817f386987d058c239236306', '5b424031616564356639', 1, 1, current_timestamp);

INSERT INTO DOMIBUS_CONNECTOR_USER (ID, USERNAME, ROLE, LOCKED, NUMBER_OF_GRACE_LOGINS, GRACE_LOGINS_USED, CREATED) VALUES (2, 'user', 'USER', 0, 5, 0, current_timestamp);
INSERT INTO DOMIBUS_CONNECTOR_USER_PWD (ID, USER_ID, PASSWORD, SALT, CURRENT_PWD, INITIAL_PWD, CREATED) VALUES (2, 2, '2bf5e637d0d82a75ca43e3be85df2c23febffc0cc221f5e010937005df478a19b5eaab59fe7e4e97f6b43ba648c169effd432e19817f386987d058c239236306', '5b424031616564356639', 1, 1, current_timestamp);

create table DC_LINK_CONFIGURATION
(
	ID NUMBER(10) not null,
	CONFIG_NAME VARCHAR(255) not null
		constraint UNQ_DC_LINK_CONFIG_NMAE
			unique,
	LINK_IMPL VARCHAR(255),
	constraint PK_DC_LINK_CONFIGURATION
		primary key (ID)
);

create table DC_LINK_CONFIG_PROPERTY
(
	DC_LINK_CONFIGURATION_ID NUMBER(10) not null,
	PROPERTY_NAME VARCHAR(255) not null,
	PROPERTY_VALUE CLOB,
	constraint PK_DC_LINK_CONFIG_PROPERTY
		primary key (DC_LINK_CONFIGURATION_ID, PROPERTY_NAME),
	constraint FK_LINKPROPERTY_LINKCONFIG
		foreign key (DC_LINK_CONFIGURATION_ID) references DC_LINK_CONFIGURATION (ID)
);

 create table DC_MESSAGE_LANE
(
	ID NUMBER(10) not null,
	NAME VARCHAR(255) not null
		constraint UNQ_DC_MESSAGE_LANE
			unique,
	DESCRIPTION CLOB,
	constraint PK_DC_MESSAGE_LANE_ID
		primary key (ID)
);

create table DC_MESSAGE_LANE_PROPERTY
(
	DC_MESSAGE_LANE_ID NUMBER(10) not null,
	PROPERTY_NAME VARCHAR(255) not null,
	PROPERTY_VALUE CLOB,
	constraint PK_DC_MESSAGE_LANE_PROPERTY
		primary key (DC_MESSAGE_LANE_ID, PROPERTY_NAME),
	constraint FK_MSGLANEPROPERTY_MSGLANE
		foreign key (DC_MESSAGE_LANE_ID) references DC_MESSAGE_LANE (ID)
);



create table DC_LINK_PARTNER
(
	ID NUMBER(10) not null,
	NAME VARCHAR(255) not null
		constraint UNQ_LINK_INFO_NAME
			unique,
	DESCRIPTION CLOB,
	ENABLED BOOLEAN,
	LINK_CONFIG_ID NUMBER(10),
	LINK_TYPE VARCHAR(20),
	LINK_MODE VARCHAR(20),
	constraint PK_DC_LINK_PARTNER
		primary key (ID),
	constraint FK_LINKINFO_LINKCONFIG
		foreign key (LINK_CONFIG_ID) references DC_LINK_CONFIGURATION (ID)
);

create table DC_LINK_PARTNER_PROPERTY
(
	DC_LINK_PARTNER_ID NUMBER(10) not null,
	PROPERTY_NAME VARCHAR(255) not null,
	PROPERTY_VALUE CLOB,
	constraint PK_DC_LINK_PARTNER_PROPERTY
		primary key (DC_LINK_PARTNER_ID, PROPERTY_NAME),
	constraint FK_LINKPROPERTY_LINKPARTNER
		foreign key (DC_LINK_PARTNER_ID) references DC_LINK_PARTNER (ID)
);

create table DC_TRANSPORT_STEP
(
	ID NUMBER(10) not null,
	MESSAGE_ID NUMBER(10) not null,
	LINK_PARTNER_NAME VARCHAR(255) not null,
	ATTEMPT INT not null,
	TRANSPORT_ID VARCHAR(255),
	TRANSPORT_SYSTEM_MESSAGE_ID VARCHAR(255),
	REMOTE_MESSAGE_ID VARCHAR(255),
	CREATED TIMESTAMP,
	constraint PK_DC_TRANSPORT_STEP
		primary key (ID),
	constraint FK_MESSAGESTEP_MESSAGE
		foreign key (MESSAGE_ID) references DOMIBUS_CONNECTOR_MESSAGE (ID)
);

create table DC_TRANSPORT_STEP_STATUS
(
	TRANSPORT_STEP_ID NUMBER(10) not null,
	STATE VARCHAR(40) not null,
	CREATED TIMESTAMP,
	TEXT CLOB,
	constraint PK_DC_TRANSPORT_STEP_STATUS
		primary key (TRANSPORT_STEP_ID, STATE),
	constraint FK_TRANSPORTSTEPSTATUS_TRANSPORTSTEP
		foreign key (TRANSPORT_STEP_ID) references DC_TRANSPORT_STEP (ID)
);

create table DC_MSG_CONTENT_DETACHED_SIGNATURE
(
    ID NUMBER(10) not null,
    SIGNATURE CLOB,
    SIGNATURE_NAME VARCHAR(255),
    SIGNATURE_TYPE VARCHAR(255),
        constraint PK_DETACHED_SIGNATURE
        primary key (ID)
);