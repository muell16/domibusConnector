--  *********************************************************************
--  Create MariaDB Database Script for DomibusConnector 4.3.x
--  *********************************************************************


CREATE TABLE QRTZ_JOB_DETAILS
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    JOB_NAME          VARCHAR(200) NOT NULL,
    JOB_GROUP         VARCHAR(200) NOT NULL,
    DESCRIPTION       VARCHAR(250) NULL,
    JOB_CLASS_NAME    VARCHAR(250) NOT NULL,
    IS_DURABLE        VARCHAR(1)   NOT NULL,
    IS_NONCONCURRENT  VARCHAR(1)   NOT NULL,
    IS_UPDATE_DATA    VARCHAR(1)   NOT NULL,
    REQUESTS_RECOVERY VARCHAR(1)   NOT NULL,
    JOB_DATA          BLOB         NULL,
    CONSTRAINT QRTZ_JOB_DETAILS_PK PRIMARY KEY (SCHED_NAME, JOB_NAME, JOB_GROUP)
);
CREATE TABLE QRTZ_TRIGGERS
(
    SCHED_NAME     VARCHAR(120) NOT NULL,
    TRIGGER_NAME   VARCHAR(200) NOT NULL,
    TRIGGER_GROUP  VARCHAR(200) NOT NULL,
    JOB_NAME       VARCHAR(200) NOT NULL,
    JOB_GROUP      VARCHAR(200) NOT NULL,
    DESCRIPTION    VARCHAR(250) NULL,
    NEXT_FIRE_TIME BIGINT(13)   NULL,
    PREV_FIRE_TIME BIGINT(13)   NULL,
    PRIORITY       BIGINT(13)   NULL,
    TRIGGER_STATE  VARCHAR(16)  NOT NULL,
    TRIGGER_TYPE   VARCHAR(8)   NOT NULL,
    START_TIME     BIGINT(13)   NOT NULL,
    END_TIME       BIGINT(13)   NULL,
    CALENDAR_NAME  VARCHAR(200) NULL,
    MISFIRE_INSTR  SMALLINT(2)  NULL,
    JOB_DATA       BLOB         NULL,
    CONSTRAINT QRTZ_TRIGGERS_PK PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    CONSTRAINT QRTZ_TRIGGER_TO_JOBS_FK FOREIGN KEY (SCHED_NAME, JOB_NAME, JOB_GROUP)
        REFERENCES QRTZ_JOB_DETAILS (SCHED_NAME, JOB_NAME, JOB_GROUP)
);
CREATE TABLE QRTZ_SIMPLE_TRIGGERS
(
    SCHED_NAME      VARCHAR(120) NOT NULL,
    TRIGGER_NAME    VARCHAR(200) NOT NULL,
    TRIGGER_GROUP   VARCHAR(200) NOT NULL,
    REPEAT_COUNT    BIGINT(7)    NOT NULL,
    REPEAT_INTERVAL BIGINT(12)   NOT NULL,
    TIMES_TRIGGERED BIGINT(10)   NOT NULL,
    CONSTRAINT QRTZ_SIMPLE_TRIG_PK PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    CONSTRAINT QRTZ_SIMPLE_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);
CREATE TABLE QRTZ_CRON_TRIGGERS
(
    SCHED_NAME      VARCHAR(120) NOT NULL,
    TRIGGER_NAME    VARCHAR(200) NOT NULL,
    TRIGGER_GROUP   VARCHAR(200) NOT NULL,
    CRON_EXPRESSION VARCHAR(120) NOT NULL,
    TIME_ZONE_ID    VARCHAR(80),
    CONSTRAINT QRTZ_CRON_TRIG_PK PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    CONSTRAINT QRTZ_CRON_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);
CREATE TABLE QRTZ_SIMPROP_TRIGGERS
(
    SCHED_NAME    VARCHAR(120)   NOT NULL,
    TRIGGER_NAME  VARCHAR(200)   NOT NULL,
    TRIGGER_GROUP VARCHAR(200)   NOT NULL,
    STR_PROP_1    VARCHAR(512)   NULL,
    STR_PROP_2    VARCHAR(512)   NULL,
    STR_PROP_3    VARCHAR(512)   NULL,
    INT_PROP_1    INT            NULL,
    INT_PROP_2    INT            NULL,
    LONG_PROP_1   BIGINT         NULL,
    LONG_PROP_2   BIGINT         NULL,
    DEC_PROP_1    NUMERIC(13, 4) NULL,
    DEC_PROP_2    NUMERIC(13, 4) NULL,
    BOOL_PROP_1   VARCHAR(1)     NULL,
    BOOL_PROP_2   VARCHAR(1)     NULL,
    CONSTRAINT QRTZ_SIMPROP_TRIG_PK PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    CONSTRAINT QRTZ_SIMPROP_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);
CREATE TABLE QRTZ_BLOB_TRIGGERS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    TRIGGER_NAME  VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    BLOB_DATA     BLOB         NULL,
    CONSTRAINT QRTZ_BLOB_TRIG_PK PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    CONSTRAINT QRTZ_BLOB_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);
CREATE TABLE QRTZ_CALENDARS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    CALENDAR_NAME VARCHAR(200) NOT NULL,
    CALENDAR      BLOB         NOT NULL,
    CONSTRAINT QRTZ_CALENDARS_PK PRIMARY KEY (SCHED_NAME, CALENDAR_NAME)
);
CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    CONSTRAINT QRTZ_PAUSED_TRIG_GRPS_PK PRIMARY KEY (SCHED_NAME, TRIGGER_GROUP)
);
CREATE TABLE QRTZ_FIRED_TRIGGERS
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    ENTRY_ID          VARCHAR(95)  NOT NULL,
    TRIGGER_NAME      VARCHAR(200) NOT NULL,
    TRIGGER_GROUP     VARCHAR(200) NOT NULL,
    INSTANCE_NAME     VARCHAR(200) NOT NULL,
    FIRED_TIME        BIGINT(13)   NOT NULL,
    SCHED_TIME        BIGINT(13)   NOT NULL,
    PRIORITY          INTEGER      NOT NULL,
    STATE             VARCHAR(16)  NOT NULL,
    JOB_NAME          VARCHAR(200) NULL,
    JOB_GROUP         VARCHAR(200) NULL,
    IS_NONCONCURRENT  VARCHAR(1)   NULL,
    REQUESTS_RECOVERY VARCHAR(1)   NULL,
    CONSTRAINT QRTZ_FIRED_TRIGGER_PK PRIMARY KEY (SCHED_NAME, ENTRY_ID)
);
CREATE TABLE QRTZ_SCHEDULER_STATE
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    INSTANCE_NAME     VARCHAR(200) NOT NULL,
    LAST_CHECKIN_TIME BIGINT(13)   NOT NULL,
    CHECKIN_INTERVAL  BIGINT(13)   NOT NULL,
    CONSTRAINT QRTZ_SCHEDULER_STATE_PK PRIMARY KEY (SCHED_NAME, INSTANCE_NAME)
);
CREATE TABLE QRTZ_LOCKS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    LOCK_NAME  VARCHAR(40)  NOT NULL,
    CONSTRAINT QRTZ_LOCKS_PK PRIMARY KEY (SCHED_NAME, LOCK_NAME)
);

create index idx_qrtz_j_req_recovery on QRTZ_JOB_DETAILS (SCHED_NAME, REQUESTS_RECOVERY);
create index idx_qrtz_j_grp on QRTZ_JOB_DETAILS (SCHED_NAME, JOB_GROUP);

create index idx_qrtz_t_j on QRTZ_TRIGGERS (SCHED_NAME, JOB_NAME, JOB_GROUP);
create index idx_qrtz_t_jg on QRTZ_TRIGGERS (SCHED_NAME, JOB_GROUP);
create index idx_qrtz_t_c on QRTZ_TRIGGERS (SCHED_NAME, CALENDAR_NAME);
create index idx_qrtz_t_g on QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_GROUP);
create index idx_qrtz_t_state on QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_STATE);
create index idx_qrtz_t_n_state on QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP, TRIGGER_STATE);
create index idx_qrtz_t_n_g_state on QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_GROUP, TRIGGER_STATE);
create index idx_qrtz_t_next_fire_time on QRTZ_TRIGGERS (SCHED_NAME, NEXT_FIRE_TIME);
create index idx_qrtz_t_nft_st on QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_STATE, NEXT_FIRE_TIME);
create index idx_qrtz_t_nft_misfire on QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME);
create index idx_qrtz_t_nft_st_misfire on QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_STATE);
create index idx_qrtz_t_nft_st_misfire_grp on QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_GROUP,
                                                             TRIGGER_STATE);

create index idx_qrtz_ft_trig_inst_name on QRTZ_FIRED_TRIGGERS (SCHED_NAME, INSTANCE_NAME);
create index idx_qrtz_ft_inst_job_req_rcvry on QRTZ_FIRED_TRIGGERS (SCHED_NAME, INSTANCE_NAME, REQUESTS_RECOVERY);
create index idx_qrtz_ft_j_g on QRTZ_FIRED_TRIGGERS (SCHED_NAME, JOB_NAME, JOB_GROUP);
create index idx_qrtz_ft_jg on QRTZ_FIRED_TRIGGERS (SCHED_NAME, JOB_GROUP);
create index idx_qrtz_ft_t_g on QRTZ_FIRED_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);
create index idx_qrtz_ft_tg on QRTZ_FIRED_TRIGGERS (SCHED_NAME, TRIGGER_GROUP);

CREATE TABLE DOMIBUS_CONNECTOR_SEQ_STORE
(
    SEQ_NAME  VARCHAR(255) NOT NULL,
    SEQ_VALUE BIGINT       NOT NULL
);
ALTER TABLE DOMIBUS_CONNECTOR_SEQ_STORE
    ADD CONSTRAINT PK_DOMIBUS_CONNECTOR_SEQ_STORE PRIMARY KEY (SEQ_NAME);

INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE
VALUES ('DOMIBUS_CONNECTOR_MESSAGE.ID', 1000);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE
VALUES ('DOMIBUS_CONNECTOR_EVIDENCE.ID', 1000);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE
VALUES ('DOMIBUS_CONNECTOR_MESSAGE_INFO.ID', 1000);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE
VALUES ('DOMIBUS_CONNECTOR_MSG_ERROR.ID', 1000);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE
VALUES ('DOMIBUS_CONNECTOR_USER.ID', 1000);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE
VALUES ('DOMIBUS_CONNECTOR_USER_PWD.ID', 1000);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE
VALUES ('DOMIBUS_CONNECTOR_PROPERTY.ID', 1000);

CREATE TABLE DC_PMODE_SET
(
    ID              BIGINT,
    FK_MESSAGE_LANE BIGINT,
    CREATED         TIMESTAMP,
    DESCRIPTION     LONGTEXT,
    ACTIVE          BOOLEAN,
    CONSTRAINT PK_DC_PMODE_SET PRIMARY KEY (ID)
);

CREATE TABLE DOMIBUS_CONNECTOR_ACTION
(
    ID           BIGINT       NOT NULL,
    FK_PMODE_SET BIGINT       NOT NULL,
    ACTION       VARCHAR(255) NOT NULL,
    PDF_REQUIRED BOOLEAN      NOT NULL
);
ALTER TABLE DOMIBUS_CONNECTOR_ACTION
    ADD CONSTRAINT PK_DC_ACTION PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_ACTION
    ADD CONSTRAINT FK_ACTION_PMODE_SET_01 FOREIGN KEY (FK_PMODE_SET) REFERENCES DC_PMODE_SET (ID);


CREATE TABLE DOMIBUS_CONNECTOR_SERVICE
(
    ID           BIGINT       NOT NULL,
    FK_PMODE_SET BIGINT       NOT NULL,
    SERVICE      VARCHAR(255) NOT NULL,
    SERVICE_TYPE VARCHAR(255)
);
ALTER TABLE DOMIBUS_CONNECTOR_SERVICE
    ADD CONSTRAINT PK_DC_SERVICE PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_SERVICE
    ADD CONSTRAINT FK_DC_SERVICE_PMODE_SET_01 FOREIGN KEY (FK_PMODE_SET) REFERENCES DC_PMODE_SET (ID);

CREATE TABLE DOMIBUS_CONNECTOR_PARTY
(
    ID            BIGINT       NOT NULL,
    FK_PMODE_SET  BIGINT       NOT NULL,
    IDENTIFIER    VARCHAR(255),
    PARTY_ID      VARCHAR(255) NOT NULL,
    ROLE          VARCHAR(255) NOT NULL,
    PARTY_ID_TYPE VARCHAR(512) NOT NULL
);
ALTER TABLE DOMIBUS_CONNECTOR_PARTY
    ADD CONSTRAINT PK_DC_PARTY PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_PARTY
    ADD CONSTRAINT FK_DC_PARTY_PMODE_SET_01 FOREIGN KEY (FK_PMODE_SET) REFERENCES DC_PMODE_SET (ID);

CREATE TABLE DOMIBUS_CONNECTOR_MSG_ERROR
(
    ID            BIGINT        NOT NULL,
    MESSAGE_ID    BIGINT        NOT NULL,
    ERROR_MESSAGE VARCHAR(2048) NOT NULL,
    DETAILED_TEXT LONGTEXT,
    ERROR_SOURCE  LONGTEXT,
    CREATED       TIMESTAMP     NOT NULL
);
ALTER TABLE DOMIBUS_CONNECTOR_MSG_ERROR
    ADD CONSTRAINT PK_DC_MSG_ERROR PRIMARY KEY (ID);

CREATE TABLE DOMIBUS_CONNECTOR_MSG_CONT
(
    ID                    BIGINT NOT NULL,
    MESSAGE_ID            BIGINT,
    CONTENT_TYPE          VARCHAR(255),
    CONTENT               LONGBLOB,
    CHECKSUM              LONGTEXT,
    CREATED               TIMESTAMP,
    STORAGE_PROVIDER_NAME VARCHAR(255),
    STORAGE_REFERENCE_ID  VARCHAR(512),
    DIGEST                VARCHAR(512),
    PAYLOAD_NAME          VARCHAR(512),
    PAYLOAD_IDENTIFIER    VARCHAR(512),
    PAYLOAD_DESCRIPTION   LONGTEXT,
    PAYLOAD_MIMETYPE      VARCHAR(255),
    PAYLOAD_SIZE          BIGINT,
    DETACHED_SIGNATURE_ID BIGINT,
    DELETED               TIMESTAMP NULL,
    CONNECTOR_MESSAGE_ID  VARCHAR(512)
);

ALTER TABLE DOMIBUS_CONNECTOR_MSG_CONT
    ADD CONSTRAINT PK_DC_MSG_CONT PRIMARY KEY (ID);

CREATE TABLE DOMIBUS_CONNECTOR_BIGDATA
(
    ID          VARCHAR(255) NOT NULL,
    CHECKSUM    LONGTEXT,
    CREATED     TIMESTAMP,
    MESSAGE_ID  BIGINT,
    LAST_ACCESS TIMESTAMP NULL,
    NAME        LONGTEXT,
    CONTENT     LONGBLOB,
    MIMETYPE    VARCHAR(255)
);
ALTER TABLE DOMIBUS_CONNECTOR_BIGDATA
    ADD PRIMARY KEY (ID);

CREATE TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO
(
    ID               BIGINT NOT NULL,
    MESSAGE_ID       BIGINT NOT NULL,
    FK_FROM_PARTY_ID BIGINT,
    FK_TO_PARTY_ID   BIGINT,
    ORIGINAL_SENDER  VARCHAR(2048),
    FINAL_RECIPIENT  VARCHAR(2048),
    FK_SERVICE       BIGINT,
    FK_ACTION        BIGINT,
    CREATED          TIMESTAMP NULL,
    UPDATED          TIMESTAMP NULL
);
ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO
    ADD CONSTRAINT PK_DC_MSG_INFO PRIMARY KEY (ID);

CREATE TABLE DOMIBUS_CONNECTOR_MESSAGE
(
    ID                   BIGINT NOT NULL,
    EBMS_MESSAGE_ID      VARCHAR(255),
    BACKEND_MESSAGE_ID   VARCHAR(255),
    BACKEND_NAME         VARCHAR(255),
    CONNECTOR_MESSAGE_ID VARCHAR(255),
    CONVERSATION_ID      VARCHAR(255),
    HASH_VALUE           LONGTEXT,
    CONFIRMED            TIMESTAMP NULL,
    REJECTED             TIMESTAMP NULL,
    DELIVERED_BACKEND    TIMESTAMP NULL,
    DELIVERED_GW         TIMESTAMP NULL,
    UPDATED              TIMESTAMP NULL,
    CREATED              TIMESTAMP NULL,
    DIRECTION_SOURCE     VARCHAR(20),
    DIRECTION_TARGET     VARCHAR(20),
    GATEWAY_NAME         VARCHAR(255)
);

ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE
    ADD CONSTRAINT PK_DC_MESSAGE PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE
    ADD CONSTRAINT UN_DC_MSG_ID UNIQUE (CONNECTOR_MESSAGE_ID);
ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE
    ADD CONSTRAINT UN_DC_MSG_NAT_MSG_01 UNIQUE (BACKEND_MESSAGE_ID);

CREATE TABLE DOMIBUS_CONNECTOR_EVIDENCE
(
    ID                   BIGINT NOT NULL,
    MESSAGE_ID           BIGINT NOT NULL,
    CONNECTOR_MESSAGE_ID VARCHAR(255),
    TYPE                 VARCHAR(255),
    EVIDENCE             LONGTEXT,
    DELIVERED_NAT        TIMESTAMP NULL,
    DELIVERED_GW         TIMESTAMP NULL,
    UPDATED              TIMESTAMP NULL
);
ALTER TABLE DOMIBUS_CONNECTOR_EVIDENCE
    ADD CONSTRAINT PK_DC_EVIDENCE PRIMARY KEY (ID);

CREATE TABLE DOMIBUS_CONNECTOR_CONT_TYPE
(
    MESSAGE_CONTENT_TYPE VARCHAR(255) NOT NULL
);
ALTER TABLE DOMIBUS_CONNECTOR_CONT_TYPE
    ADD CONSTRAINT PK_DC_CONT_TYPE PRIMARY KEY (MESSAGE_CONTENT_TYPE);

CREATE TABLE DOMIBUS_CONNECTOR_BACKEND_INFO
(
    ID                   BIGINT       NOT NULL,
    BACKEND_NAME         VARCHAR(255) NOT NULL,
    BACKEND_KEY_ALIAS    VARCHAR(255) NOT NULL,
    BACKEND_KEY_PASS     VARCHAR(255),
    BACKEND_SERVICE_TYPE VARCHAR(255),
    BACKEND_ENABLED      BOOLEAN DEFAULT TRUE,
    BACKEND_DEFAULT      BOOLEAN DEFAULT FALSE,
    BACKEND_DESCRIPTION  LONGTEXT,
    BACKEND_PUSH_ADDRESS LONGTEXT
);
ALTER TABLE DOMIBUS_CONNECTOR_BACKEND_INFO
    ADD CONSTRAINT PK_DC_BACK_INFO PRIMARY KEY (ID);

ALTER TABLE DOMIBUS_CONNECTOR_BACKEND_INFO
    ADD CONSTRAINT UN_DC_BACK_INFO_01 UNIQUE (BACKEND_NAME);
ALTER TABLE DOMIBUS_CONNECTOR_BACKEND_INFO
    ADD CONSTRAINT UN_DC_BACK_INFO_02 UNIQUE (BACKEND_KEY_ALIAS);

CREATE TABLE DOMIBUS_CONNECTOR_BACK_2_S
(
    DOMIBUS_CONNECTOR_SERVICE_ID VARCHAR(255) NOT NULL,
    DOMIBUS_CONNECTOR_BACKEND_ID BIGINT       NOT NULL
);
ALTER TABLE DOMIBUS_CONNECTOR_BACK_2_S
    ADD CONSTRAINT PK_DC_BACK2S PRIMARY KEY (DOMIBUS_CONNECTOR_SERVICE_ID, DOMIBUS_CONNECTOR_BACKEND_ID);

ALTER TABLE DOMIBUS_CONNECTOR_MSG_ERROR
    ADD CONSTRAINT FK_DC_MSG_ERROR_01 FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID);
ALTER TABLE DOMIBUS_CONNECTOR_MSG_CONT
    ADD CONSTRAINT FK_DC_CON_01 FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID);

ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO
    ADD CONSTRAINT FK_DC_MSG_INFO_F_PARTY FOREIGN KEY (FK_FROM_PARTY_ID) REFERENCES DOMIBUS_CONNECTOR_PARTY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO
    ADD CONSTRAINT FK_DC_MSG_INFO_T_PARTY FOREIGN KEY (FK_TO_PARTY_ID) REFERENCES DOMIBUS_CONNECTOR_PARTY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO
    ADD CONSTRAINT FK_DC_MSG_INFO_SERVICE FOREIGN KEY (FK_SERVICE) REFERENCES DOMIBUS_CONNECTOR_SERVICE (ID);
ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO
    ADD CONSTRAINT FK_DC_MSG_INFO_ACTION FOREIGN KEY (FK_ACTION) REFERENCES DOMIBUS_CONNECTOR_ACTION (ID);
ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO
    ADD CONSTRAINT FK_DC_MSG_INFO_I FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID);

ALTER TABLE DOMIBUS_CONNECTOR_EVIDENCE
    ADD CONSTRAINT FK_DC_EVIDENCES_01 FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID);

ALTER TABLE DOMIBUS_CONNECTOR_BACK_2_S
    ADD CONSTRAINT FK_DC_BACK2S_01 FOREIGN KEY (DOMIBUS_CONNECTOR_BACKEND_ID) REFERENCES DOMIBUS_CONNECTOR_BACKEND_INFO (ID);

ALTER TABLE DOMIBUS_CONNECTOR_BIGDATA
    ADD CONSTRAINT FK_DC_BIGDATA_01 FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID);

CREATE TABLE DOMIBUS_CONNECTOR_PROPERTY
(
    ID             BIGINT        NOT NULL PRIMARY KEY,
    PROPERTY_NAME  VARCHAR(2048) NOT NULL,
    PROPERTY_VALUE VARCHAR(2048) NULL
);

-- USER:

CREATE TABLE DOMIBUS_CONNECTOR_USER
(
    ID                     BIGINT                 NOT NULL,
    USERNAME               VARCHAR(50)            NOT NULL,
    ROLE                   VARCHAR(50)            NOT NULL,
    LOCKED                 BOOLEAN  DEFAULT FALSE NOT NULL,
    NUMBER_OF_GRACE_LOGINS SMALLINT DEFAULT 5     NOT NULL,
    GRACE_LOGINS_USED      SMALLINT DEFAULT 0     NOT NULL,
    CREATED                TIMESTAMP              NOT NULL
);

CREATE TABLE DOMIBUS_CONNECTOR_USER_PWD
(
    ID          BIGINT        NOT NULL,
    USER_ID     BIGINT        NOT NULL,
    PASSWORD    VARCHAR(1024) NOT NULL,
    SALT        VARCHAR(512)  NOT NULL,
    CURRENT_PWD SMALLINT      NOT NULL,
    INITIAL_PWD SMALLINT      NOT NULL,
    CREATED     TIMESTAMP     NOT NULL
);

/* Create Primary Keys, Indexes, Uniques, Checks, Triggers */

ALTER TABLE DOMIBUS_CONNECTOR_USER
    ADD CONSTRAINT PK_DC_USER
        PRIMARY KEY (ID)
;

ALTER TABLE DOMIBUS_CONNECTOR_USER_PWD
    ADD CONSTRAINT PK_DC_USER_PWD
        PRIMARY KEY (ID);


INSERT INTO DOMIBUS_CONNECTOR_USER (ID, USERNAME, ROLE, LOCKED, NUMBER_OF_GRACE_LOGINS, GRACE_LOGINS_USED, CREATED)
VALUES (1, 'admin', 'ADMIN', 0, 5, 0, current_timestamp);
INSERT INTO DOMIBUS_CONNECTOR_USER_PWD (ID, USER_ID, PASSWORD, SALT, CURRENT_PWD, INITIAL_PWD, CREATED)
VALUES (1, 1,
        '2bf5e637d0d82a75ca43e3be85df2c23febffc0cc221f5e010937005df478a19b5eaab59fe7e4e97f6b43ba648c169effd432e19817f386987d058c239236306',
        '5b424031616564356639', 1, 1, current_timestamp);

INSERT INTO DOMIBUS_CONNECTOR_USER (ID, USERNAME, ROLE, LOCKED, NUMBER_OF_GRACE_LOGINS, GRACE_LOGINS_USED, CREATED)
VALUES (2, 'user', 'USER', 0, 5, 0, current_timestamp);
INSERT INTO DOMIBUS_CONNECTOR_USER_PWD (ID, USER_ID, PASSWORD, SALT, CURRENT_PWD, INITIAL_PWD, CREATED)
VALUES (2, 2,
        '2bf5e637d0d82a75ca43e3be85df2c23febffc0cc221f5e010937005df478a19b5eaab59fe7e4e97f6b43ba648c169effd432e19817f386987d058c239236306',
        '5b424031616564356639', 1, 1, current_timestamp);

create table DC_LINK_CONFIGURATION
(
    ID          BIGINT       not null,
    CONFIG_NAME VARCHAR(255) not null,
    LINK_IMPL   VARCHAR(255),
    constraint PK_DC_LINK_CONFIGURATION
        primary key (ID),
    constraint UN_DC_LINK_CONF_NAME_01
        unique (CONFIG_NAME)
);

create table DC_LINK_CONFIG_PROPERTY
(
    DC_LINK_CONFIGURATION_ID BIGINT       not null,
    PROPERTY_NAME            VARCHAR(255) not null,
    PROPERTY_VALUE           LONGTEXT,
    constraint PK_DC_LINK_CONF_PROP
        primary key (DC_LINK_CONFIGURATION_ID, PROPERTY_NAME),
    constraint FK_DC_LINK_CONF_PROP_01
        foreign key (DC_LINK_CONFIGURATION_ID) references DC_LINK_CONFIGURATION (ID)
);

create table DC_MESSAGE_LANE
(
    ID          BIGINT       not null,
    NAME        VARCHAR(255) not null,
    DESCRIPTION LONGTEXT,
    constraint PK_DC_MSG_LANE
        primary key (ID),
    constraint UN_DC_MSG_LANE_01
        unique (NAME)
);

create table DC_MESSAGE_LANE_PROPERTY
(
    DC_MESSAGE_LANE_ID BIGINT       not null,
    PROPERTY_NAME      VARCHAR(255) not null,
    PROPERTY_VALUE     LONGTEXT,
    constraint PK_DC_MSG_LANE_PROP
        primary key (DC_MESSAGE_LANE_ID, PROPERTY_NAME),
    constraint FK_DC_MSG_LANE_PROP_01
        foreign key (DC_MESSAGE_LANE_ID) references DC_MESSAGE_LANE (ID)
);

INSERT INTO DC_MESSAGE_LANE (ID, NAME, DESCRIPTION)
VALUES (1, 'default_message_lane', 'default message lane');

create table DC_LINK_PARTNER
(
    ID             BIGINT       not null,
    NAME           VARCHAR(255) not null,
    DESCRIPTION    LONGTEXT,
    ENABLED        BOOLEAN,
    LINK_CONFIG_ID BIGINT,
    LINK_TYPE      VARCHAR(20),
    LINK_MODE      VARCHAR(20),
    constraint PK_DC_LINK_P
        primary key (ID),
    constraint FK_DC_LINK_P_01
        foreign key (LINK_CONFIG_ID) references DC_LINK_CONFIGURATION (ID),
    constraint UN_DC_LINK_P_01
        unique (NAME)
);

create table DC_LINK_PARTNER_PROPERTY
(
    DC_LINK_PARTNER_ID BIGINT       not null,
    PROPERTY_NAME      VARCHAR(255) not null,
    PROPERTY_VALUE     LONGTEXT,
    constraint PK_DC_LINK_P_PROP
        primary key (DC_LINK_PARTNER_ID, PROPERTY_NAME),
    constraint FK_DC_LINK_P_PROP_01
        foreign key (DC_LINK_PARTNER_ID) references DC_LINK_PARTNER (ID)
);

create table DC_TRANSPORT_STEP
(
    ID                          BIGINT       not null,
    CONNECTOR_MESSAGE_ID        VARCHAR(255) not null,
    LINK_PARTNER_NAME           VARCHAR(255) not null,
    ATTEMPT                     INT          not null,
    TRANSPORT_ID                VARCHAR(255),
    TRANSPORT_SYSTEM_MESSAGE_ID VARCHAR(255),
    REMOTE_MESSAGE_ID           VARCHAR(255),
    CREATED                     TIMESTAMP,
    TRANSPORTED_MESSAGE         LONGTEXT,
    FINAL_STATE_REACHED         TIMESTAMP,
    constraint PK_DC_TRANSPORT_STEP
        primary key (ID)
);

create table DC_TRANSPORT_STEP_STATUS
(
    TRANSPORT_STEP_ID BIGINT      not null,
    STATE             VARCHAR(40) not null,
    CREATED           TIMESTAMP,
    TEXT              LONGTEXT,
    constraint PK_DC_TRANS_STEP_STATUS
        primary key (TRANSPORT_STEP_ID, STATE),
    constraint FK_DC_TRANS_STEP_STATUS_01
        foreign key (TRANSPORT_STEP_ID) references DC_TRANSPORT_STEP (ID)
);

create table DC_MSGCNT_DETSIG
(
    ID             BIGINT not null,
    SIGNATURE      LONGTEXT,
    SIGNATURE_NAME VARCHAR(255),
    SIGNATURE_TYPE VARCHAR(255),
    constraint PK_DETSIG
        primary key (ID)
);

CREATE TABLE DC_DB_VERSION
(
    TAG VARCHAR(255) PRIMARY KEY
);
INSERT INTO DC_DB_VERSION (TAG)
VALUES ('V4.3');
