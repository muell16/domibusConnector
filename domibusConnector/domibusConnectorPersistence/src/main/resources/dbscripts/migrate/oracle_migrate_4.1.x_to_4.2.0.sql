-- *********************************************************************
-- Update Database Script - from domibusConnector 4.1 to 4.2
-- *********************************************************************
-- adds quartz tables
--

-- #################### 1/6 RENAME tables that need to be recreated ####################

RENAME DOMIBUS_CONNECTOR_MESSAGE TO BKP_DC_MESSAGE;
RENAME DOMIBUS_CONNECTOR_MESSAGE_INFO TO BKP_DC_MESSAGE_INFO;
RENAME DOMIBUS_CONNECTOR_PARTY TO BKP_DC_PARTY;
RENAME DOMIBUS_CONNECTOR_ACTION TO BKP_DC_ACTION;
RENAME DOMIBUS_CONNECTOR_SERVICE TO BKP_DC_SERVICE;
rename DOMIBUS_CONNECTOR_PROPERTY to bkp_dc_property;
rename DOMIBUS_CONNECTOR_MSG_ERROR to bkp_dc_msg_err;
rename DOMIBUS_CONNECTOR_MSG_CONT to bkp_dc_msg_cont;
rename DOMIBUS_CONNECTOR_EVIDENCE to bkp_dc_evidence;
rename DOMIBUS_CONNECTOR_BIGDATA to bkp_dc_bigdata;

-- #################### 2/6 CREATE tables, structural changes ####################

-- DROP some constraints from backup table, this is because some rows need to be deleted before the data is transferred
-- to the new table
ALTER TABLE BKP_DC_MESSAGE_INFO DROP CONSTRAINT FK_DC_MSG_INFO_01;
ALTER TABLE BKP_DC_MESSAGE_INFO DROP CONSTRAINT FK_DC_MSG_INFO_02;
ALTER TABLE BKP_DC_MESSAGE_INFO DROP CONSTRAINT FK_DC_MSG_INFO_03;
ALTER TABLE BKP_DC_MESSAGE_INFO DROP CONSTRAINT FK_DC_MSG_INFO_04;

ALTER TABLE DOMIBUS_CONNECTOR_BACK_2_S
    DROP CONSTRAINT FK_DC_BACK2S_02;

CREATE TABLE qrtz_job_details
(
    SCHED_NAME        VARCHAR2(120) NOT NULL,
    JOB_NAME          VARCHAR2(200) NOT NULL,
    JOB_GROUP         VARCHAR2(200) NOT NULL,
    DESCRIPTION       VARCHAR2(250) NULL,
    JOB_CLASS_NAME    VARCHAR2(250) NOT NULL,
    IS_DURABLE        VARCHAR2(1)   NOT NULL,
    IS_NONCONCURRENT  VARCHAR2(1)   NOT NULL,
    IS_UPDATE_DATA    VARCHAR2(1)   NOT NULL,
    REQUESTS_RECOVERY VARCHAR2(1)   NOT NULL,
    JOB_DATA          BLOB          NULL,
    CONSTRAINT QRTZ_JOB_DETAILS_PK PRIMARY KEY (SCHED_NAME, JOB_NAME, JOB_GROUP)
);
CREATE TABLE qrtz_triggers
(
    SCHED_NAME     VARCHAR2(120) NOT NULL,
    TRIGGER_NAME   VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP  VARCHAR2(200) NOT NULL,
    JOB_NAME       VARCHAR2(200) NOT NULL,
    JOB_GROUP      VARCHAR2(200) NOT NULL,
    DESCRIPTION    VARCHAR2(250) NULL,
    NEXT_FIRE_TIME NUMBER(13)    NULL,
    PREV_FIRE_TIME NUMBER(13)    NULL,
    PRIORITY       NUMBER(13)    NULL,
    TRIGGER_STATE  VARCHAR2(16)  NOT NULL,
    TRIGGER_TYPE   VARCHAR2(8)   NOT NULL,
    START_TIME     NUMBER(13)    NOT NULL,
    END_TIME       NUMBER(13)    NULL,
    CALENDAR_NAME  VARCHAR2(200) NULL,
    MISFIRE_INSTR  NUMBER(2)     NULL,
    JOB_DATA       BLOB          NULL,
    CONSTRAINT QRTZ_TRIGGERS_PK PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    CONSTRAINT QRTZ_TRIGGER_TO_JOBS_FK FOREIGN KEY (SCHED_NAME, JOB_NAME, JOB_GROUP)
        REFERENCES QRTZ_JOB_DETAILS (SCHED_NAME, JOB_NAME, JOB_GROUP)
);
CREATE TABLE qrtz_simple_triggers
(
    SCHED_NAME      VARCHAR2(120)  NOT NULL,
    TRIGGER_NAME    VARCHAR2(200)  NOT NULL,
    TRIGGER_GROUP   VARCHAR2(200)  NOT NULL,
    REPEAT_COUNT    NUMBER(7)      NOT NULL,
    REPEAT_INTERVAL NUMBER(12)     NOT NULL,
    TIMES_TRIGGERED DECIMAL(10, 0) NOT NULL,
    CONSTRAINT QRTZ_SIMPLE_TRIG_PK PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    CONSTRAINT QRTZ_SIMPLE_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);
CREATE TABLE qrtz_cron_triggers
(
    SCHED_NAME      VARCHAR2(120) NOT NULL,
    TRIGGER_NAME    VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP   VARCHAR2(200) NOT NULL,
    CRON_EXPRESSION VARCHAR2(120) NOT NULL,
    TIME_ZONE_ID    VARCHAR2(80),
    CONSTRAINT QRTZ_CRON_TRIG_PK PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    CONSTRAINT QRTZ_CRON_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);
CREATE TABLE qrtz_simprop_triggers
(
    SCHED_NAME    VARCHAR2(120)  NOT NULL,
    TRIGGER_NAME  VARCHAR2(200)  NOT NULL,
    TRIGGER_GROUP VARCHAR2(200)  NOT NULL,
    STR_PROP_1    VARCHAR2(512)  NULL,
    STR_PROP_2    VARCHAR2(512)  NULL,
    STR_PROP_3    VARCHAR2(512)  NULL,
    INT_PROP_1    DECIMAL(10, 0) NULL,
    INT_PROP_2    DECIMAL(10, 0) NULL,
    LONG_PROP_1   NUMBER(13)     NULL,
    LONG_PROP_2   NUMBER(13)     NULL,
    DEC_PROP_1    NUMERIC(13, 4) NULL,
    DEC_PROP_2    NUMERIC(13, 4) NULL,
    BOOL_PROP_1   VARCHAR2(1)    NULL,
    BOOL_PROP_2   VARCHAR2(1)    NULL,
    CONSTRAINT QRTZ_SIMPROP_TRIG_PK PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    CONSTRAINT QRTZ_SIMPROP_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);
CREATE TABLE qrtz_blob_triggers
(
    SCHED_NAME    VARCHAR2(120) NOT NULL,
    TRIGGER_NAME  VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    BLOB_DATA     BLOB          NULL,
    CONSTRAINT QRTZ_BLOB_TRIG_PK PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    CONSTRAINT QRTZ_BLOB_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);
CREATE TABLE qrtz_calendars
(
    SCHED_NAME    VARCHAR2(120) NOT NULL,
    CALENDAR_NAME VARCHAR2(200) NOT NULL,
    CALENDAR      BLOB          NOT NULL,
    CONSTRAINT QRTZ_CALENDARS_PK PRIMARY KEY (SCHED_NAME, CALENDAR_NAME)
);
CREATE TABLE qrtz_paused_trigger_grps
(
    SCHED_NAME    VARCHAR2(120) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    CONSTRAINT QRTZ_PAUSED_TRIG_GRPS_PK PRIMARY KEY (SCHED_NAME, TRIGGER_GROUP)
);
CREATE TABLE qrtz_fired_triggers
(
    SCHED_NAME        VARCHAR2(120) NOT NULL,
    ENTRY_ID          VARCHAR2(95)  NOT NULL,
    TRIGGER_NAME      VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP     VARCHAR2(200) NOT NULL,
    INSTANCE_NAME     VARCHAR2(200) NOT NULL,
    FIRED_TIME        NUMBER(13)    NOT NULL,
    SCHED_TIME        NUMBER(13)    NOT NULL,
    PRIORITY          NUMBER(13)    NOT NULL,
    STATE             VARCHAR2(16)  NOT NULL,
    JOB_NAME          VARCHAR2(200) NULL,
    JOB_GROUP         VARCHAR2(200) NULL,
    IS_NONCONCURRENT  VARCHAR2(1)   NULL,
    REQUESTS_RECOVERY VARCHAR2(1)   NULL,
    CONSTRAINT QRTZ_FIRED_TRIGGER_PK PRIMARY KEY (SCHED_NAME, ENTRY_ID)
);
CREATE TABLE qrtz_scheduler_state
(
    SCHED_NAME        VARCHAR2(120) NOT NULL,
    INSTANCE_NAME     VARCHAR2(200) NOT NULL,
    LAST_CHECKIN_TIME NUMBER(13)    NOT NULL,
    CHECKIN_INTERVAL  NUMBER(13)    NOT NULL,
    CONSTRAINT QRTZ_SCHEDULER_STATE_PK PRIMARY KEY (SCHED_NAME, INSTANCE_NAME)
);
CREATE TABLE qrtz_locks
(
    SCHED_NAME VARCHAR2(120) NOT NULL,
    LOCK_NAME  VARCHAR2(40)  NOT NULL,
    CONSTRAINT QRTZ_LOCKS_PK PRIMARY KEY (SCHED_NAME, LOCK_NAME)
);

create index idx_qrtz_j_req_recovery on qrtz_job_details (SCHED_NAME, REQUESTS_RECOVERY);
create index idx_qrtz_j_grp on qrtz_job_details (SCHED_NAME, JOB_GROUP);

create index idx_qrtz_t_j on qrtz_triggers (SCHED_NAME, JOB_NAME, JOB_GROUP);
create index idx_qrtz_t_jg on qrtz_triggers (SCHED_NAME, JOB_GROUP);
create index idx_qrtz_t_c on qrtz_triggers (SCHED_NAME, CALENDAR_NAME);
create index idx_qrtz_t_g on qrtz_triggers (SCHED_NAME, TRIGGER_GROUP);
create index idx_qrtz_t_state on qrtz_triggers (SCHED_NAME, TRIGGER_STATE);
create index idx_qrtz_t_n_state on qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP, TRIGGER_STATE);
create index idx_qrtz_t_n_g_state on qrtz_triggers (SCHED_NAME, TRIGGER_GROUP, TRIGGER_STATE);
create index idx_qrtz_t_next_fire_time on qrtz_triggers (SCHED_NAME, NEXT_FIRE_TIME);
create index idx_qrtz_t_nft_st on qrtz_triggers (SCHED_NAME, TRIGGER_STATE, NEXT_FIRE_TIME);
create index idx_qrtz_t_nft_misfire on qrtz_triggers (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME);
create index idx_qrtz_t_nft_st_misfire on qrtz_triggers (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_STATE);
create index idx_qrtz_t_nft_st_misfire_grp on qrtz_triggers (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_GROUP,
                                                             TRIGGER_STATE);

create index idx_qrtz_ft_trig_inst_name on qrtz_fired_triggers (SCHED_NAME, INSTANCE_NAME);
create index idx_qrtz_ft_inst_job_req_rcvry on qrtz_fired_triggers (SCHED_NAME, INSTANCE_NAME, REQUESTS_RECOVERY);
create index idx_qrtz_ft_j_g on qrtz_fired_triggers (SCHED_NAME, JOB_NAME, JOB_GROUP);
create index idx_qrtz_ft_jg on qrtz_fired_triggers (SCHED_NAME, JOB_GROUP);
create index idx_qrtz_ft_t_g on qrtz_fired_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);
create index idx_qrtz_ft_tg on qrtz_fired_triggers (SCHED_NAME, TRIGGER_GROUP);

----- DOMIBUS_CONNECTOR_MESSAGE ------

CREATE TABLE DOMIBUS_CONNECTOR_MESSAGE
(
    ID                   DECIMAL(10, 0) NOT NULL,
    EBMS_MESSAGE_ID      VARCHAR2(255 CHAR),
    BACKEND_MESSAGE_ID   VARCHAR2(255 CHAR),
    BACKEND_NAME         VARCHAR2(255 CHAR),
    CONNECTOR_MESSAGE_ID VARCHAR2(255 CHAR),
    CONVERSATION_ID      VARCHAR2(255 CHAR),
    HASH_VALUE           CLOB,
    CONFIRMED            TIMESTAMP,
    REJECTED             TIMESTAMP,
    DELIVERED_BACKEND    TIMESTAMP,
    DELIVERED_GW         TIMESTAMP,
    UPDATED              TIMESTAMP,
    CREATED              TIMESTAMP,
    DIRECTION_SOURCE     VARCHAR2(20),
    DIRECTION_TARGET     VARCHAR2(20),
    GATEWAY_NAME         VARCHAR2(255)
);

CREATE TABLE DOMIBUS_CONNECTOR_MSG_ERROR
(
    ID            DECIMAL(10, 0)      NOT NULL,
    MESSAGE_ID    DECIMAL(10, 0)      NOT NULL,
    ERROR_MESSAGE VARCHAR2(2048 CHAR) NOT NULL,
    DETAILED_TEXT CLOB,
    ERROR_SOURCE  CLOB,
    CREATED       TIMESTAMP           NOT NULL
);

CREATE TABLE DOMIBUS_CONNECTOR_MSG_CONT
(
    ID                    DECIMAL(10, 0) NOT NULL,
    MESSAGE_ID            DECIMAL(10, 0),
    CONTENT_TYPE          VARCHAR2(255 CHAR),
    CONTENT               BLOB,
    CHECKSUM              CLOB,
    CREATED               TIMESTAMP,
    STORAGE_PROVIDER_NAME VARCHAR2(255),
    STORAGE_REFERENCE_ID  VARCHAR2(512),
    DIGEST                VARCHAR2(512),
    PAYLOAD_NAME          VARCHAR2(512),
    PAYLOAD_IDENTIFIER    VARCHAR2(512),
    PAYLOAD_DESCRIPTION   CLOB,
    PAYLOAD_MIMETYPE      VARCHAR2(255),
    PAYLOAD_SIZE          DECIMAL(10, 0),
    DETACHED_SIGNATURE_ID DECIMAL(10, 0),
    DELETED               TIMESTAMP
);

CREATE TABLE DOMIBUS_CONNECTOR_EVIDENCE
(
    ID                   DECIMAL(10, 0) NOT NULL,
    MESSAGE_ID           DECIMAL(10, 0) NOT NULL,
    CONNECTOR_MESSAGE_ID VARCHAR2(255),
    TYPE                 VARCHAR2(255 CHAR),
    EVIDENCE             CLOB,
    DELIVERED_NAT        TIMESTAMP,
    DELIVERED_GW         TIMESTAMP,
    UPDATED              TIMESTAMP
);

CREATE TABLE DOMIBUS_CONNECTOR_BIGDATA
(
    ID          VARCHAR2(255 CHAR) NOT NULL,
    CHECKSUM    CLOB,
    CREATED     TIMESTAMP,
    MESSAGE_ID  DECIMAL(10, 0),
    LAST_ACCESS TIMESTAMP,
    NAME        CLOB,
    CONTENT     BLOB,
    MIMETYPE    VARCHAR2(255 CHAR)
);

----- DOMIBUS_CONNECTOR_MESSAGE_INFO ------

CREATE TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO
(
    ID               DECIMAL(10, 0) NOT NULL,
    MESSAGE_ID       DECIMAL(10, 0) NOT NULL,
    FK_FROM_PARTY_ID DECIMAL(10, 0),
    FK_TO_PARTY_ID   DECIMAL(10, 0),
    ORIGINAL_SENDER  VARCHAR2(2048 CHAR),
    FINAL_RECIPIENT  VARCHAR2(2048 CHAR),
    FK_SERVICE       DECIMAL(10, 0),
    FK_ACTION        DECIMAL(10, 0),
    CREATED          TIMESTAMP,
    UPDATED          TIMESTAMP
);

CREATE TABLE DOMIBUS_CONNECTOR_PARTY
(
    ID            DECIMAL(10, 0) NOT NULL,
    FK_PMODE_SET  DECIMAL(10, 0) NOT NULL,
    IDENTIFIER    VARCHAR2(255),
    PARTY_ID      VARCHAR2(255)  NOT NULL,
    ROLE          VARCHAR2(255)  NOT NULL,
    PARTY_ID_TYPE VARCHAR2(512)  NOT NULL
);

CREATE TABLE DOMIBUS_CONNECTOR_ACTION
(
    ID           DECIMAL(10, 0) NOT NULL,
    FK_PMODE_SET DECIMAL(10, 0) NOT NULL,
    ACTION       VARCHAR2(255)  NOT NULL,
    PDF_REQUIRED DECIMAL(1, 0)  NOT NULL
);

CREATE TABLE DOMIBUS_CONNECTOR_SERVICE
(
    ID           DECIMAL(10, 0) NOT NULL,
    FK_PMODE_SET DECIMAL(10, 0) NOT NULL,
    SERVICE      VARCHAR2(255)  NOT NULL,
    SERVICE_TYPE VARCHAR2(255)
);

CREATE TABLE DOMIBUS_CONNECTOR_PROPERTY
(
    ID             DECIMAL(10, 0) NOT NULL PRIMARY KEY,
    PROPERTY_NAME  VARCHAR2(2048) NOT NULL,
    PROPERTY_VALUE VARCHAR2(2048) NULL
);

create table DC_LINK_CONFIGURATION
(
    ID          DECIMAL(10, 0) not null,
    CONFIG_NAME VARCHAR2(255)  not null,
    LINK_IMPL   VARCHAR2(255)
);

create table DC_LINK_CONFIG_PROPERTY
(
    DC_LINK_CONFIGURATION_ID DECIMAL(10, 0) not null,
    PROPERTY_NAME            VARCHAR2(255)  not null,
    PROPERTY_VALUE           CLOB
);

create table DC_MESSAGE_LANE
(
    ID          DECIMAL(10, 0) not null,
    NAME        VARCHAR2(255)  not null,
    DESCRIPTION CLOB
);

create table DC_MESSAGE_LANE_PROPERTY
(
    DC_MESSAGE_LANE_ID DECIMAL(10, 0) not null,
    PROPERTY_NAME      VARCHAR2(255)  not null,
    PROPERTY_VALUE     CLOB
);

create table DC_LINK_PARTNER
(
    ID             DECIMAL(10, 0) not null,
    NAME           VARCHAR2(255)  not null,
    DESCRIPTION    CLOB,
    ENABLED        DECIMAL(1, 0),
    LINK_CONFIG_ID DECIMAL(10, 0),
    LINK_TYPE      VARCHAR2(20),
    LINK_MODE      VARCHAR2(20)
);

create table DC_LINK_PARTNER_PROPERTY
(
    DC_LINK_PARTNER_ID DECIMAL(10, 0) not null,
    PROPERTY_NAME      VARCHAR2(255)  not null,
    PROPERTY_VALUE     CLOB
);

create table DC_TRANSPORT_STEP
(
    ID                          DECIMAL(10, 0) not null,
    CONNECTOR_MESSAGE_ID        DECIMAL(10, 0) not null,
    LINK_PARTNER_NAME           VARCHAR2(255)  not null,
    ATTEMPT                     INT            not null,
    TRANSPORT_ID                VARCHAR2(255),
    TRANSPORT_SYSTEM_MESSAGE_ID VARCHAR2(255),
    REMOTE_MESSAGE_ID           VARCHAR2(255),
    CREATED                     TIMESTAMP
);

create table DC_TRANSPORT_STEP_STATUS
(
    TRANSPORT_STEP_ID DECIMAL(10, 0) not null,
    STATE             VARCHAR2(40)   not null,
    CREATED           TIMESTAMP,
    TEXT              CLOB
);

create table DC_MSGCNT_DETSIG
(
    ID             DECIMAL(10, 0) not null,
    SIGNATURE      CLOB,
    SIGNATURE_NAME VARCHAR2(255),
    SIGNATURE_TYPE VARCHAR2(255)
);

CREATE TABLE DC_PMODE_SET
(
    ID              DECIMAL(10, 0),
    FK_MESSAGE_LANE DECIMAL(10, 0),
    CREATED         TIMESTAMP,
    DESCRIPTION     CLOB,
    ACTIVE          DECIMAL(1, 0) DEFAULT 0 NOT NULL
);

-- #################### 3/6 TRANSFER data ####################

INSERT INTO DOMIBUS_CONNECTOR_MESSAGE
select ID,
       EBMS_MESSAGE_ID,
       BACKEND_MESSAGE_ID,
       BACKEND_NAME,
       CONNECTOR_MESSAGE_ID,
       CONVERSATION_ID,
       HASH_VALUE,
       CONFIRMED,
       REJECTED,
       DELIVERED_BACKEND,
       DELIVERED_GW,
       UPDATED,
       CREATED,
       (CASE DIRECTION
            WHEN 'GW_TO_NAT' THEN 'GATEWAY'
            WHEN 'NAT_TO_GW' THEN 'BACKEND'
            ELSE null END) as DIRECTION_SOURCE,
       (CASE DIRECTION
            WHEN 'GW_TO_NAT' THEN 'BACKEND'
            WHEN 'NAT_TO_GW' THEN 'GATEWAY'
            ELSE null END) as DIRECTION_TARGET,
       null                as GATEWAY_NAME

from BKP_DC_MESSAGE M;

insert into DOMIBUS_CONNECTOR_MSG_ERROR
select id, MESSAGE_ID, ERROR_MESSAGE, DETAILED_TEXT, ERROR_SOURCE, CREATED
from bkp_dc_msg_err;


insert into DOMIBUS_CONNECTOR_MSG_CONT
select id, MESSAGE_ID, CONTENT_TYPE, CONTENT, CHECKSUM, CREATED, null, null, null, null, null, null, null, null, null, null
from bkp_dc_msg_cont;


insert into DOMIBUS_CONNECTOR_EVIDENCE
select ID, MESSAGE_ID, CONNECTOR_MESSAGE_ID, TYPE, EVIDENCE, DELIVERED_NAT, DELIVERED_GW, UPDATED
from bkp_dc_evidence;


insert into DOMIBUS_CONNECTOR_BIGDATA
select *
from bkp_dc_bigdata;

-- DC_MESSAGE_LANE

INSERT INTO DC_MESSAGE_LANE (ID, NAME, DESCRIPTION)
VALUES (1, 'defaultMessageLane', 'default message lane');

-- DC_PMODE_SET

INSERT INTO DC_PMODE_SET (ID, FK_MESSAGE_LANE, CREATED, DESCRIPTION, ACTIVE)
VALUES (1,
        1,
        sysdate,
        'initial set created by migration script',
        1);

----- DOMIBUS_CONNECTOR_PARTY ------

CREATE SEQUENCE DC_PARTY_SEQ INCREMENT BY 1 START WITH 1000;

-- delete old default value row
delete
from BKP_DC_PARTY
where PARTY_ID = 'n.a.' ;

insert into DOMIBUS_CONNECTOR_PARTY
values (1, 1, null, 'n.a.', 'n.a.', 'n.a.');

insert into DOMIBUS_CONNECTOR_PARTY
select DC_PARTY_SEQ.nextval,
       1,
       null as IDENTIFIER,
       PARTY_ID,
       ROLE,
       PARTY_ID_TYPE
from BKP_DC_PARTY;

INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE
VALUES ('DOMIBUS_CONNECTOR_PARTY.ID', DC_PARTY_SEQ.nextval);


----- DOMIBUS_CONNECTOR_ACTION ------

CREATE SEQUENCE DC_ACTION_SEQ INCREMENT BY 1 START WITH 1000;

-- delete old default value row
delete
from BKP_DC_ACTION
where ACTION = 'n.a.';

insert into DOMIBUS_CONNECTOR_ACTION
values (1, 1, 'n.a.', 0);

insert into DOMIBUS_CONNECTOR_ACTION
select DC_ACTION_SEQ.nextval,
       1,
       ACTION,
       PDF_REQUIRED
from BKP_DC_ACTION;

INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE
VALUES ('DOMIBUS_CONNECTOR_ACTION.ID', DC_ACTION_SEQ.nextval);

----- DOMIBUS_CONNECTOR_SERVICE ------

CREATE SEQUENCE DC_SERVICE_SEQ INCREMENT BY 1 START WITH 1000;

-- delete old default value row
delete
from BKP_DC_SERVICE
where SERVICE = 'n.a.';

insert into DOMIBUS_CONNECTOR_SERVICE
values (1, 1, 'n.a.', 'n.a.');

insert into DOMIBUS_CONNECTOR_SERVICE
select DC_SERVICE_SEQ.nextval,
       1,
       SERVICE,
       SERVICE_TYPE
from BKP_DC_SERVICE;

INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE
VALUES ('DOMIBUS_CONNECTOR_SERVICE.ID', DC_SERVICE_SEQ.nextval);

CREATE SEQUENCE DC_PROPERTY_SEQ INCREMENT BY 1 START WITH 1000;

insert into DOMIBUS_CONNECTOR_PROPERTY
select DC_PROPERTY_SEQ.nextval as ID, PROPERTY_NAME, PROPERTY_VALUE
from bkp_dc_property;

UPDATE DOMIBUS_CONNECTOR_SEQ_STORE
set SEQ_VALUE=DC_PROPERTY_SEQ.nextval
where SEQ_NAME = 'DOMIBUS_CONNECTOR_PROPERTY.ID';


insert into DOMIBUS_CONNECTOR_MESSAGE_INFO
select B.ID,
       B.MESSAGE_ID,
       CASE
           when FROM_PARTY_ID is not null and FROM_PARTY_ROLE is not null
               then (select id
                     from DOMIBUS_CONNECTOR_PARTY FP
                     where FP.PARTY_ID = FROM_PARTY_ID
                       and FP.ROLE = FROM_PARTY_ROLE)
           else 1
           end
           as FK_FROM_PARTY_ID,
       CASE
           when TO_PARTY_ID is not null and TO_PARTY_ROLE is not null
               then (select id
                     from DOMIBUS_CONNECTOR_PARTY FP
                     where FP.PARTY_ID = TO_PARTY_ID
                       and FP.ROLE = TO_PARTY_ROLE)
           else 1
           end
           as FK_TO_PARTY_ID,
       ORIGINAL_SENDER,
       FINAL_RECIPIENT,
       CASE
           when B.SERVICE is not null
               then (select id from DOMIBUS_CONNECTOR_SERVICE S where S.SERVICE = B.SERVICE)
           else 1
           end
           as FK_SERVICE,
       CASE
           when B.ACTION is not null
               then (select id from DOMIBUS_CONNECTOR_ACTION A where A.ACTION = B.ACTION)
           else 1
           end
           as FK_ACTION,
       CREATED,
       UPDATED
from BKP_DC_MESSAGE_INFO B;

-- #################### 4/6 DELETE temporary tables, frees fk names ####################

DROP SEQUENCE DC_PARTY_SEQ;
DROP SEQUENCE DC_ACTION_SEQ;
DROP SEQUENCE DC_SERVICE_SEQ;
drop sequence DC_PROPERTY_SEQ;
drop table bkp_dc_msg_err;
drop table bkp_dc_msg_cont;
drop table bkp_dc_evidence;
drop table bkp_dc_bigdata;
drop table BKP_DC_MESSAGE_INFO;
drop table BKP_DC_PARTY;
drop table BKP_DC_ACTION;
drop table BKP_DC_SERVICE;
drop table BKP_DC_PROPERTY;
drop table BKP_DC_MESSAGE cascade constraints;

-- #################### 5/6 ADD the constraints ####################

alter table DC_LINK_CONFIGURATION add constraint PK_DC_LINK_CONFIGURATION primary key (ID);
alter table DC_LINK_CONFIGURATION add constraint UN_DC_LINK_CONF_NAME_01 unique (CONFIG_NAME);

ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE
    ADD CONSTRAINT PK_DC_MESSAGE PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE
    ADD CONSTRAINT UN_DC_MSG_ID UNIQUE (CONNECTOR_MESSAGE_ID);
ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE
    ADD CONSTRAINT UN_DC_MSG_NAT_MSG_01 UNIQUE (BACKEND_MESSAGE_ID);

ALTER TABLE DOMIBUS_CONNECTOR_MSG_ERROR
    ADD CONSTRAINT PK_DC_MSG_ERROR PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_MSG_ERROR
    ADD CONSTRAINT FK_DC_MSG_ERROR_01 FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID);

alter table DC_MSGCNT_DETSIG
    add constraint PK_DETSIG primary key (ID);

ALTER TABLE DOMIBUS_CONNECTOR_MSG_CONT
    ADD CONSTRAINT PK_DC_MSG_CONT PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_MSG_CONT
    ADD CONSTRAINT FK_DC_CON_01 FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID);
ALTER TABLE DOMIBUS_CONNECTOR_MSG_CONT
    ADD CONSTRAINT FK_DETSIG_MSGCONT FOREIGN KEY (DETACHED_SIGNATURE_ID) REFERENCES DC_MSGCNT_DETSIG (ID);

ALTER TABLE DOMIBUS_CONNECTOR_EVIDENCE
    ADD CONSTRAINT PK_DC_EVIDENCE PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_EVIDENCE
    ADD CONSTRAINT FK_DC_EVIDENCES_01 FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID);

ALTER TABLE DOMIBUS_CONNECTOR_BIGDATA
    ADD PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_BIGDATA
    ADD CONSTRAINT FK_DC_BIGDATA_01 FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID);

alter table DC_PMODE_SET
    add CONSTRAINT PK_DC_PMODE_SET PRIMARY KEY (ID);

ALTER TABLE DOMIBUS_CONNECTOR_PARTY
    ADD CONSTRAINT PK_DC_PARTY PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_PARTY
    ADD CONSTRAINT FK_DC_PARTY_PMODE_SET_01 FOREIGN KEY (FK_PMODE_SET) REFERENCES DC_PMODE_SET (ID);

ALTER TABLE DOMIBUS_CONNECTOR_SERVICE
    ADD CONSTRAINT PK_DC_SERVICE PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_SERVICE
    ADD CONSTRAINT FK_DC_SERVICE_PMODE_SET_01 FOREIGN KEY (FK_PMODE_SET) REFERENCES DC_PMODE_SET (ID);

ALTER TABLE DOMIBUS_CONNECTOR_ACTION
    ADD CONSTRAINT PK_DC_ACTION PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_ACTION
    ADD CONSTRAINT FK_ACTION_PMODE_SET_01 FOREIGN KEY (FK_PMODE_SET) REFERENCES DC_PMODE_SET (ID);

ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO
    ADD CONSTRAINT PK_DC_MSG_INFO PRIMARY KEY (ID);
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

alter table DC_LINK_CONFIG_PROPERTY
    add constraint PK_DC_LINK_CONF_PROP
        primary key (DC_LINK_CONFIGURATION_ID, PROPERTY_NAME);
alter table DC_LINK_CONFIG_PROPERTY
    add constraint FK_DC_LINK_CONF_PROP_01
        foreign key (DC_LINK_CONFIGURATION_ID) references DC_LINK_CONFIGURATION (ID);

alter table DC_MESSAGE_LANE
    add constraint PK_DC_MSG_LANE primary key (ID);
alter table DC_MESSAGE_LANE
    add constraint UN_DC_MSG_LANE_01 unique (NAME);

alter table DC_MESSAGE_LANE_PROPERTY
    add constraint PK_DC_MSG_LANE_PROP
        primary key (DC_MESSAGE_LANE_ID, PROPERTY_NAME);
alter table DC_MESSAGE_LANE_PROPERTY
    add constraint FK_DC_MSG_LANE_PROP_01
        foreign key (DC_MESSAGE_LANE_ID) references DC_MESSAGE_LANE (ID);

alter table DC_LINK_PARTNER
    add constraint PK_DC_LINK_P
        primary key (ID);
alter table DC_LINK_PARTNER
    add constraint UN_DC_LINK_P_01 unique (NAME);
alter table DC_LINK_PARTNER
    add constraint FK_DC_LINK_P_01
        foreign key (LINK_CONFIG_ID) references DC_LINK_CONFIGURATION (ID);

alter table DC_LINK_PARTNER_PROPERTY
    add constraint PK_DC_LINK_P_PROP
        primary key (DC_LINK_PARTNER_ID, PROPERTY_NAME);
alter table DC_LINK_PARTNER_PROPERTY
    add constraint FK_DC_LINK_P_PROP_01
        foreign key (DC_LINK_PARTNER_ID) references DC_LINK_PARTNER (ID);

alter table DC_TRANSPORT_STEP
    add constraint PK_DC_TRANSPORT_STEP
        primary key (ID);
alter table DC_TRANSPORT_STEP
    add constraint FK_MESSAGESTEP_MESSAGE
        foreign key (CONNECTOR_MESSAGE_ID) references DOMIBUS_CONNECTOR_MESSAGE (ID);

alter table DC_TRANSPORT_STEP_STATUS
    add constraint PK_DC_TRANS_STEP_STATUS primary key (TRANSPORT_STEP_ID, STATE);
alter table DC_TRANSPORT_STEP_STATUS
    add constraint FK_DC_TRANS_STEP_STATUS_01
        foreign key (TRANSPORT_STEP_ID) references DC_TRANSPORT_STEP (ID);

-- #################### 6/6 UPDATE Version ####################

INSERT INTO DC_DB_VERSION (TAG)
VALUES ('V4.2.8');
