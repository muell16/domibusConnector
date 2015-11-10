
CREATE TABLE DOMIBUS_CONNECTOR_MESSAGES (
	ID NUMBER(10) NOT NULL,
	EBMS_MESSAGE_ID VARCHAR2(255) UNIQUE,
	NAT_MESSAGE_ID VARCHAR2(255) UNIQUE,
	CONVERSATION_ID VARCHAR2(255),
	DIRECTION VARCHAR2(10),
	HASH_VALUE VARCHAR2(1000),
	CONFIRMED TIMESTAMP,
	REJECTED TIMESTAMP,
	DELIVERED_NAT TIMESTAMP,
	DELIVERED_GW TIMESTAMP,
	UPDATED TIMESTAMP,
	PRIMARY KEY (ID)
);

CREATE TABLE DOMIBUS_CONNECTOR_EVIDENCES (
	ID  NUMBER(10) NOT NULL,
	MESSAGE_ID  NUMBER(10) NOT NULL,
	TYPE VARCHAR2(255),
	EVIDENCE CLOB,
	DELIVERED_NAT TIMESTAMP,
	DELIVERED_GW TIMESTAMP,
	UPDATED TIMESTAMP,
	PRIMARY KEY (ID),
	FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGES (ID)
);

CREATE TABLE DOMIBUS_CONNECTOR_SEQ_STORE (
	SEQ_NAME VARCHAR2(255) NOT NULL,
	SEQ_VALUE  NUMBER(10) NOT NULL,
	PRIMARY KEY(SEQ_NAME)
);

INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE VALUES ('DOMIBUS_CONNECTOR_MESSAGES.ID', 0);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE VALUES ('DOMIBUS_CONNECTOR_EVIDENCES.ID', 0);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE VALUES ('DOMIBUS_CONNECTOR_MESSAGE_INFO.ID', 0);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE VALUES ('DOMIBUS_CONNECTOR_MSG_ERROR.ID', 0);

CREATE TABLE DOMIBUS_CONNECTOR_PARTY (
	PARTY_ID VARCHAR2(50) NOT NULL,
	ROLE VARCHAR2(50) NOT NULL,
	PARTY_ID_TYPE VARCHAR2(50),
	PRIMARY KEY (PARTY_ID, ROLE)
);

INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('AT', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('DE', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('EE', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('ES', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('EC', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('IT', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('GR', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('NL', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('PL', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('CZ', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('ITIC', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('ATOS', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('IE', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');

CREATE TABLE DOMIBUS_CONNECTOR_ACTION (
	ACTION VARCHAR2(50) NOT NULL,
	PDF_REQUIRED NUMBER(1,0) DEFAULT 1 NOT NULL,
	PRIMARY KEY (ACTION)
);

INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_A', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_B', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_C', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_D', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_E', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_F', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_G', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('FreeFormLetter', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('br_merger_notification', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('br_result_notification', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('SubmissionAcceptanceRejection', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('RelayREMMDAcceptanceRejection', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('RelayREMMDFailure', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('DeliveryNonDeliveryToRecipient', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('RetrievalNonRetrievalToRecipient', 0);

CREATE TABLE DOMIBUS_CONNECTOR_SERVICE (
	SERVICE VARCHAR2(50) NOT NULL,
	SERVICE_TYPE VARCHAR2(255) NOT NULL,
	PRIMARY KEY (SERVICE)
);

INSERT INTO DOMIBUS_CONNECTOR_SERVICE VALUES ('EPO', 'urn:e-codex:services:');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE VALUES ('BR', 'urn:e-codex:services:');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE VALUES ('SmallClaims', 'urn:e-codex:services:');

CREATE TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO (
	ID  NUMBER(10) NOT NULL,
	MESSAGE_ID  NUMBER(10) UNIQUE NOT NULL,
	FROM_PARTY_ID VARCHAR2(50),
	FROM_PARTY_ROLE VARCHAR2(50),
	TO_PARTY_ID VARCHAR2(50),
	TO_PARTY_ROLE VARCHAR2(50),
	ORIGINAL_SENDER VARCHAR2(50),
	FINAL_RECIPIENT VARCHAR2(50),
	SERVICE VARCHAR2(50),
	ACTION VARCHAR2(50),
	CREATED TIMESTAMP NOT NULL,
	UPDATED TIMESTAMP NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGES (ID),
	FOREIGN KEY (FROM_PARTY_ID, FROM_PARTY_ROLE) REFERENCES DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE),
	FOREIGN KEY (TO_PARTY_ID, TO_PARTY_ROLE) REFERENCES DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE),
	FOREIGN KEY (SERVICE) REFERENCES DOMIBUS_CONNECTOR_SERVICE (SERVICE),
	FOREIGN KEY (ACTION) REFERENCES DOMIBUS_CONNECTOR_ACTION (ACTION)
);

CREATE TABLE DOMIBUS_CONNECTOR_MSG_ERROR (
	ID  NUMBER(10) NOT NULL,
	MESSAGE_ID  NUMBER(10) NOT NULL,
	ERROR_MESSAGE VARCHAR2(255) NOT NULL,
	DETAILED_TEXT CLOB,
	ERROR_SOURCE VARCHAR2(255),
	CREATED TIMESTAMP NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGES (ID)
);

commit;


----------------------- Quartz Scheduler Tables ------------------------------

CREATE TABLE DCON_QRTZ_job_details
  (
    JOB_NAME  VARCHAR2(200) NOT NULL,
    JOB_GROUP VARCHAR2(200) NOT NULL,
    DESCRIPTION VARCHAR2(250) NULL,
    JOB_CLASS_NAME   VARCHAR2(250) NOT NULL, 
    IS_DURABLE VARCHAR2(1) NOT NULL,
    IS_VOLATILE VARCHAR2(1) NOT NULL,
    IS_STATEFUL VARCHAR2(1) NOT NULL,
    REQUESTS_RECOVERY VARCHAR2(1) NOT NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (JOB_NAME,JOB_GROUP)
);
CREATE TABLE DCON_QRTZ_job_listeners
  (
    JOB_NAME  VARCHAR2(200) NOT NULL, 
    JOB_GROUP VARCHAR2(200) NOT NULL,
    JOB_LISTENER VARCHAR2(200) NOT NULL,
    PRIMARY KEY (JOB_NAME,JOB_GROUP,JOB_LISTENER),
    FOREIGN KEY (JOB_NAME,JOB_GROUP) 
	REFERENCES DCON_QRTZ_JOB_DETAILS(JOB_NAME,JOB_GROUP)
);
CREATE TABLE DCON_QRTZ_triggers
  (
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    JOB_NAME  VARCHAR2(200) NOT NULL, 
    JOB_GROUP VARCHAR2(200) NOT NULL,
    IS_VOLATILE VARCHAR2(1) NOT NULL,
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
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (JOB_NAME,JOB_GROUP) 
	REFERENCES DCON_QRTZ_JOB_DETAILS(JOB_NAME,JOB_GROUP) 
);
CREATE TABLE DCON_QRTZ_simple_triggers
  (
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    REPEAT_COUNT NUMBER(7) NOT NULL,
    REPEAT_INTERVAL NUMBER(12) NOT NULL,
    TIMES_TRIGGERED NUMBER(10) NOT NULL,
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP) 
	REFERENCES DCON_QRTZ_TRIGGERS(TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE DCON_QRTZ_cron_triggers
  (
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    CRON_EXPRESSION VARCHAR2(120) NOT NULL,
    TIME_ZONE_ID VARCHAR2(80),
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP) 
	REFERENCES DCON_QRTZ_TRIGGERS(TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE DCON_QRTZ_blob_triggers
  (
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    BLOB_DATA BLOB NULL,
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP) 
        REFERENCES DCON_QRTZ_TRIGGERS(TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE DCON_QRTZ_trigger_listeners
  (
    TRIGGER_NAME  VARCHAR2(200) NOT NULL, 
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    TRIGGER_LISTENER VARCHAR2(200) NOT NULL,
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_LISTENER),
    FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP) 
	REFERENCES DCON_QRTZ_TRIGGERS(TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE DCON_QRTZ_calendars
  (
    CALENDAR_NAME  VARCHAR2(200) NOT NULL, 
    CALENDAR BLOB NOT NULL,
    PRIMARY KEY (CALENDAR_NAME)
);
CREATE TABLE DCON_QRTZ_paused_trigger_grps
  (
    TRIGGER_GROUP  VARCHAR2(200) NOT NULL, 
    PRIMARY KEY (TRIGGER_GROUP)
);
CREATE TABLE DCON_QRTZ_fired_triggers 
  (
    ENTRY_ID VARCHAR2(95) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    IS_VOLATILE VARCHAR2(1) NOT NULL,
    INSTANCE_NAME VARCHAR2(200) NOT NULL,
    FIRED_TIME NUMBER(13) NOT NULL,
    PRIORITY NUMBER(13) NOT NULL,
    STATE VARCHAR2(16) NOT NULL,
    JOB_NAME VARCHAR2(200) NULL,
    JOB_GROUP VARCHAR2(200) NULL,
    IS_STATEFUL VARCHAR2(1) NULL,
    REQUESTS_RECOVERY VARCHAR2(1) NULL,
    PRIMARY KEY (ENTRY_ID)
);
CREATE TABLE DCON_QRTZ_scheduler_state 
  (
    INSTANCE_NAME VARCHAR2(200) NOT NULL,
    LAST_CHECKIN_TIME NUMBER(13) NOT NULL,
    CHECKIN_INTERVAL NUMBER(13) NOT NULL,
    PRIMARY KEY (INSTANCE_NAME)
);
CREATE TABLE DCON_QRTZ_locks
  (
    LOCK_NAME  VARCHAR2(40) NOT NULL, 
    PRIMARY KEY (LOCK_NAME)
);
INSERT INTO DCON_QRTZ_locks values('TRIGGER_ACCESS');
INSERT INTO DCON_QRTZ_locks values('JOB_ACCESS');
INSERT INTO DCON_QRTZ_locks values('CALENDAR_ACCESS');
INSERT INTO DCON_QRTZ_locks values('STATE_ACCESS');
INSERT INTO DCON_QRTZ_locks values('MISFIRE_ACCESS');
create index didx_qrtz_j_req_recovery on DCON_QRTZ_job_details(REQUESTS_RECOVERY);
create index didx_qrtz_t_next_fire_time on DCON_QRTZ_triggers(NEXT_FIRE_TIME);
create index didx_qrtz_t_state on DCON_QRTZ_triggers(TRIGGER_STATE);
create index didx_qrtz_t_nft_st on DCON_QRTZ_triggers(NEXT_FIRE_TIME,TRIGGER_STATE);
create index didx_qrtz_t_volatile on DCON_QRTZ_triggers(IS_VOLATILE);
create index didx_qrtz_ft_trig_name on DCON_QRTZ_fired_triggers(TRIGGER_NAME);
create index didx_qrtz_ft_trig_group on DCON_QRTZ_fired_triggers(TRIGGER_GROUP);
create index didx_qrtz_ft_trig_nm_gp on DCON_QRTZ_fired_triggers(TRIGGER_NAME,TRIGGER_GROUP);
create index didx_qrtz_ft_trig_volatile on DCON_QRTZ_fired_triggers(IS_VOLATILE);
create index didx_qrtz_ft_trig_inst_name on DCON_QRTZ_fired_triggers(INSTANCE_NAME);
create index didx_qrtz_ft_job_name on DCON_QRTZ_fired_triggers(JOB_NAME);
create index didx_qrtz_ft_job_group on DCON_QRTZ_fired_triggers(JOB_GROUP);
create index didx_qrtz_ft_job_stateful on DCON_QRTZ_fired_triggers(IS_STATEFUL);
create index didx_qrtz_ft_job_req_recovery on DCON_QRTZ_fired_triggers(REQUESTS_RECOVERY);



commit;