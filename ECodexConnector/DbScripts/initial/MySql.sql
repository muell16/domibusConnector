
CREATE TABLE ECODEX_MESSAGES (
	ID BIGINT NOT NULL,
	EBMS_MESSAGE_ID VARCHAR(255) UNIQUE,
	NAT_MESSAGE_ID VARCHAR(255) UNIQUE,
	CONVERSATION_ID VARCHAR(255),
	DIRECTION VARCHAR(10),
	HASH_VALUE VARCHAR(1000),
	CONFIRMED DATETIME,
	REJECTED DATETIME,
	DELIVERED_NAT DATETIME,
	DELIVERED_GW DATETIME,
	UPDATED TIMESTAMP,
	PRIMARY KEY (ID)
);

CREATE TABLE ECODEX_EVIDENCES (
	ID BIGINT NOT NULL,
	MESSAGE_ID BIGINT NOT NULL,
	TYPE VARCHAR(255),
	EVIDENCE TEXT,
	DELIVERED_NAT DATETIME,
	DELIVERED_GW DATETIME,
	UPDATED TIMESTAMP,
	PRIMARY KEY (ID),
	FOREIGN KEY (MESSAGE_ID) REFERENCES ECODEX_MESSAGES (ID)
);

CREATE TABLE ECODEX_SEQ_STORE (
	SEQ_NAME VARCHAR(255) NOT NULL,
	SEQ_VALUE BIGINT NOT NULL,
	PRIMARY KEY(SEQ_NAME)
);

INSERT INTO ECODEX_SEQ_STORE VALUES ('ECODEX_MESSAGES.ID', 0);
INSERT INTO ECODEX_SEQ_STORE VALUES ('ECODEX_EVIDENCES.ID', 0);
INSERT INTO ECODEX_SEQ_STORE VALUES ('ECODEX_MESSAGE_INFO.ID', 0);

CREATE TABLE ECODEX_PARTY (
	PARTY_ID VARCHAR(50) NOT NULL,
	ROLE VARCHAR(50) NOT NULL,
	PARTY_ID_TYPE VARCHAR(50),
	PRIMARY KEY (PARTY_ID, ROLE)
);

INSERT INTO ECODEX_PARTY VALUES ('AT', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO ECODEX_PARTY VALUES ('DE', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO ECODEX_PARTY VALUES ('EE', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO ECODEX_PARTY VALUES ('ES', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO ECODEX_PARTY VALUES ('EU', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO ECODEX_PARTY VALUES ('IT', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO ECODEX_PARTY VALUES ('GR', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO ECODEX_PARTY VALUES ('NL', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO ECODEX_PARTY VALUES ('PL', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO ECODEX_PARTY VALUES ('CZ', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');

CREATE TABLE ECODEX_ACTION (
	ECDX_ACTION VARCHAR(50) NOT NULL,
	PDF_REQUIRED SMALLINT NOT NULL DEFAULT 1,
	PRIMARY KEY (ECDX_ACTION)
);

INSERT INTO ECODEX_ACTION VALUES ('Form_A', 1);
INSERT INTO ECODEX_ACTION VALUES ('Form_B', 1);
INSERT INTO ECODEX_ACTION VALUES ('Form_C', 1);
INSERT INTO ECODEX_ACTION VALUES ('Form_D', 1);
INSERT INTO ECODEX_ACTION VALUES ('Form_E', 1);
INSERT INTO ECODEX_ACTION VALUES ('Form_F', 1);
INSERT INTO ECODEX_ACTION VALUES ('Form_G', 1);
INSERT INTO ECODEX_ACTION VALUES ('FreeFormLetter', 1);
INSERT INTO ECODEX_ACTION VALUES ('SubmissionAcceptanceRejection', 0);
INSERT INTO ECODEX_ACTION VALUES ('RelayREMMDAcceptanceRejection', 0);
INSERT INTO ECODEX_ACTION VALUES ('RelayREMMDFailure', 0);
INSERT INTO ECODEX_ACTION VALUES ('DeliveryNonDeliveryToRecipient', 0);
INSERT INTO ECODEX_ACTION VALUES ('RetrievalNonRetrievalToRecipient', 0);

CREATE TABLE ECODEX_SERVICE (
	ECDX_SERVICE VARCHAR(50) NOT NULL,
	PRIMARY KEY (ECDX_SERVICE)
);

INSERT INTO ECODEX_SERVICE VALUES ('EPO');

CREATE TABLE ECODEX_MESSAGE_INFO (
	ID  BIGINT NOT NULL,
	MESSAGE_ID  BIGINT UNIQUE NOT NULL,
	FROM_PARTY_ID VARCHAR(50),
	FROM_PARTY_ROLE VARCHAR(50),
	TO_PARTY_ID VARCHAR(50),
	TO_PARTY_ROLE VARCHAR(50),
	ORIGINAL_SENDER VARCHAR(50),
	FINAL_RECIPIENT VARCHAR(50),
	ECDX_SERVICE VARCHAR(50),
	ECDX_ACTION VARCHAR(50),
	CREATED DATETIME NOT NULL,
	UPDATED DATETIME NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (MESSAGE_ID) REFERENCES ECODEX_MESSAGES (ID),
	FOREIGN KEY (FROM_PARTY_ID, FROM_PARTY_ROLE) REFERENCES ECODEX_PARTY (PARTY_ID, ROLE),
	FOREIGN KEY (TO_PARTY_ID, TO_PARTY_ROLE) REFERENCES ECODEX_PARTY (PARTY_ID, ROLE),
	FOREIGN KEY (ECDX_SERVICE) REFERENCES ECODEX_SERVICE (ECDX_SERVICE),
	FOREIGN KEY (ECDX_ACTION) REFERENCES ECODEX_ACTION (ECDX_ACTION)
);



commit;


----------------------- Quartz Scheduler Tables ------------------------------

CREATE TABLE ECON_QRTZ_JOB_DETAILS
  (
    JOB_NAME  VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    JOB_CLASS_NAME   VARCHAR(250) NOT NULL,
    IS_DURABLE VARCHAR(1) NOT NULL,
    IS_VOLATILE VARCHAR(1) NOT NULL,
    IS_STATEFUL VARCHAR(1) NOT NULL,
    REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (JOB_NAME,JOB_GROUP)
);

CREATE TABLE ECON_QRTZ_JOB_LISTENERS
  (
    JOB_NAME  VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    JOB_LISTENER VARCHAR(200) NOT NULL,
    PRIMARY KEY (JOB_NAME,JOB_GROUP,JOB_LISTENER),
    FOREIGN KEY (JOB_NAME,JOB_GROUP)
        REFERENCES ECON_QRTZ_JOB_DETAILS(JOB_NAME,JOB_GROUP)
);

CREATE TABLE ECON_QRTZ_TRIGGERS
  (
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    JOB_NAME  VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    IS_VOLATILE VARCHAR(1) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    NEXT_FIRE_TIME BIGINT(13) NULL,
    PREV_FIRE_TIME BIGINT(13) NULL,
    PRIORITY INTEGER NULL,
    TRIGGER_STATE VARCHAR(16) NOT NULL,
    TRIGGER_TYPE VARCHAR(8) NOT NULL,
    START_TIME BIGINT(13) NOT NULL,
    END_TIME BIGINT(13) NULL,
    CALENDAR_NAME VARCHAR(200) NULL,
    MISFIRE_INSTR SMALLINT(2) NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (JOB_NAME,JOB_GROUP)
        REFERENCES ECON_QRTZ_JOB_DETAILS(JOB_NAME,JOB_GROUP)
);

CREATE TABLE ECON_QRTZ_SIMPLE_TRIGGERS
  (
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    REPEAT_COUNT BIGINT(7) NOT NULL,
    REPEAT_INTERVAL BIGINT(12) NOT NULL,
    TIMES_TRIGGERED BIGINT(10) NOT NULL,
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES ECON_QRTZ_TRIGGERS(TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE ECON_QRTZ_CRON_TRIGGERS
  (
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    CRON_EXPRESSION VARCHAR(200) NOT NULL,
    TIME_ZONE_ID VARCHAR(80),
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES ECON_QRTZ_TRIGGERS(TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE ECON_QRTZ_BLOB_TRIGGERS
  (
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    BLOB_DATA BLOB NULL,
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES ECON_QRTZ_TRIGGERS(TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE ECON_QRTZ_TRIGGER_LISTENERS
  (
    TRIGGER_NAME  VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    TRIGGER_LISTENER VARCHAR(200) NOT NULL,
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_LISTENER),
    FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES ECON_QRTZ_TRIGGERS(TRIGGER_NAME,TRIGGER_GROUP)
);


CREATE TABLE ECON_QRTZ_CALENDARS
  (
    CALENDAR_NAME  VARCHAR(200) NOT NULL,
    CALENDAR BLOB NOT NULL,
    PRIMARY KEY (CALENDAR_NAME)
);



CREATE TABLE ECON_QRTZ_PAUSED_TRIGGER_GRPS
  (
    TRIGGER_GROUP  VARCHAR(200) NOT NULL, 
    PRIMARY KEY (TRIGGER_GROUP)
);

CREATE TABLE ECON_QRTZ_FIRED_TRIGGERS
  (
    ENTRY_ID VARCHAR(95) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    IS_VOLATILE VARCHAR(1) NOT NULL,
    INSTANCE_NAME VARCHAR(200) NOT NULL,
    FIRED_TIME BIGINT(13) NOT NULL,
    PRIORITY INTEGER NOT NULL,
    STATE VARCHAR(16) NOT NULL,
    JOB_NAME VARCHAR(200) NULL,
    JOB_GROUP VARCHAR(200) NULL,
    IS_STATEFUL VARCHAR(1) NULL,
    REQUESTS_RECOVERY VARCHAR(1) NULL,
    PRIMARY KEY (ENTRY_ID)
);

CREATE TABLE ECON_QRTZ_SCHEDULER_STATE
  (
    INSTANCE_NAME VARCHAR(200) NOT NULL,
    LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
    CHECKIN_INTERVAL BIGINT(13) NOT NULL,
    PRIMARY KEY (INSTANCE_NAME)
);

CREATE TABLE ECON_QRTZ_LOCKS
  (
    LOCK_NAME  VARCHAR(40) NOT NULL, 
    PRIMARY KEY (LOCK_NAME)
);


INSERT INTO ECON_QRTZ_LOCKS values('TRIGGER_ACCESS');
INSERT INTO ECON_QRTZ_LOCKS values('JOB_ACCESS');
INSERT INTO ECON_QRTZ_LOCKS values('CALENDAR_ACCESS');
INSERT INTO ECON_QRTZ_LOCKS values('STATE_ACCESS');
INSERT INTO ECON_QRTZ_LOCKS values('MISFIRE_ACCESS');


commit;