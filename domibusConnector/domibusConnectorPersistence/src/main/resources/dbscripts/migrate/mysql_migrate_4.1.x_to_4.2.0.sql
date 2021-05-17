-- *********************************************************************
-- Update Database Script - from domibusConnector 4.1 to 4.2
-- *********************************************************************
-- adds quartz tables
--

SET FOREIGN_KEY_CHECKS=0;
-- requires MySQL >= 5.6.6, default since MySQL 8.0.2
SET EXPLICIT_DEFAULTS_FOR_TIMESTAMP = ON;
-- fixes UUID bug: https://bugs.mysql.com/bug.php?id=101820es UUID bug
-- also see: https://stackoverflow.com/questions/36296558/mysql-generating-duplicate-uuid
SET names utf8;

-- #################### 1/6 RENAME TABLE tables that need to be recreated ####################

RENAME TABLE DOMIBUS_CONNECTOR_MESSAGE TO BKP_DC_MESSAGE;
RENAME TABLE DOMIBUS_CONNECTOR_MESSAGE_INFO TO BKP_DC_MESSAGE_INFO;
RENAME TABLE DOMIBUS_CONNECTOR_PARTY TO BKP_DC_PARTY;
RENAME TABLE DOMIBUS_CONNECTOR_ACTION TO BKP_DC_ACTION;
RENAME TABLE DOMIBUS_CONNECTOR_SERVICE TO BKP_DC_SERVICE;
RENAME TABLE DOMIBUS_CONNECTOR_PROPERTY to bkp_dc_property;
RENAME TABLE DOMIBUS_CONNECTOR_MSG_ERROR to bkp_dc_msg_err;
RENAME TABLE DOMIBUS_CONNECTOR_MSG_CONT to bkp_dc_msg_cont;
RENAME TABLE DOMIBUS_CONNECTOR_EVIDENCE to bkp_dc_evidence;
RENAME TABLE DOMIBUS_CONNECTOR_BIGDATA to bkp_dc_bigdata;

-- #################### 2/6 CREATE tables, structural changes ####################

-- DROP some constraints from backup table, this is because some rows need to be deleted before the data is transferred
-- to the new table
ALTER TABLE BKP_DC_MESSAGE_INFO DROP FOREIGN KEY FK_DC_MSG_INFO_01;
ALTER TABLE BKP_DC_MESSAGE_INFO DROP FOREIGN KEY FK_DC_MSG_INFO_02;
ALTER TABLE BKP_DC_MESSAGE_INFO DROP FOREIGN KEY FK_DC_MSG_INFO_03;
ALTER TABLE BKP_DC_MESSAGE_INFO DROP FOREIGN KEY FK_DC_MSG_INFO_04;

ALTER TABLE DOMIBUS_CONNECTOR_BACK_2_S DROP FOREIGN KEY FK_DC_BACK2S_02;


DROP TABLE IF EXISTS SequenceHelper;
CREATE TABLE IF NOT EXISTS SequenceHelper
(
    name VARCHAR(70) NOT NULL UNIQUE,
    next BIGINT NOT NULL
);

delimiter $$
DROP function if exists next_val_in_seq;
create function next_val_in_seq(seq_name varchar(10))
    returns BIGINT
    DETERMINISTIC
begin
    UPDATE SequenceHelper SET next = (@next := next) + 1 WHERE name = seq_name;
    return (SELECT @next);
end $$
$$ DELIMITER ;

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
    JOB_DATA          BLOB          NULL,
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
    NEXT_FIRE_TIME BIGINT(13)    NULL,
    PREV_FIRE_TIME BIGINT(13)    NULL,
    PRIORITY       BIGINT(13)    NULL,
    TRIGGER_STATE  VARCHAR(16)  NOT NULL,
    TRIGGER_TYPE   VARCHAR(8)   NOT NULL,
    START_TIME     BIGINT(13)    NOT NULL,
    END_TIME       BIGINT(13)    NULL,
    CALENDAR_NAME  VARCHAR(200) NULL,
    MISFIRE_INSTR  SMALLINT(2)     NULL,
    JOB_DATA       BLOB          NULL,
    CONSTRAINT QRTZ_TRIGGERS_PK PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    CONSTRAINT QRTZ_TRIGGER_TO_JOBS_FK FOREIGN KEY (SCHED_NAME, JOB_NAME, JOB_GROUP)
        REFERENCES QRTZ_JOB_DETAILS (SCHED_NAME, JOB_NAME, JOB_GROUP)
);
CREATE TABLE QRTZ_SIMPLE_TRIGGERS
(
    SCHED_NAME      VARCHAR(120)  NOT NULL,
    TRIGGER_NAME    VARCHAR(200)  NOT NULL,
    TRIGGER_GROUP   VARCHAR(200)  NOT NULL,
    REPEAT_COUNT    BIGINT(7)      NOT NULL,
    REPEAT_INTERVAL BIGINT(12)     NOT NULL,
    TIMES_TRIGGERED BIGINT(10) NOT NULL,
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
    SCHED_NAME    VARCHAR(120)  NOT NULL,
    TRIGGER_NAME  VARCHAR(200)  NOT NULL,
    TRIGGER_GROUP VARCHAR(200)  NOT NULL,
    STR_PROP_1    VARCHAR(512)  NULL,
    STR_PROP_2    VARCHAR(512)  NULL,
    STR_PROP_3    VARCHAR(512)  NULL,
    INT_PROP_1    INT NULL,
    INT_PROP_2    INT NULL,
    LONG_PROP_1   BIGINT     NULL,
    LONG_PROP_2   BIGINT     NULL,
    DEC_PROP_1    NUMERIC(13, 4) NULL,
    DEC_PROP_2    NUMERIC(13, 4) NULL,
    BOOL_PROP_1   VARCHAR(1)    NULL,
    BOOL_PROP_2   VARCHAR(1)    NULL,
    CONSTRAINT QRTZ_SIMPROP_TRIG_PK PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    CONSTRAINT QRTZ_SIMPROP_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);
CREATE TABLE QRTZ_BLOB_TRIGGERS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    TRIGGER_NAME  VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    BLOB_DATA     BLOB          NULL,
    CONSTRAINT QRTZ_BLOB_TRIG_PK PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    CONSTRAINT QRTZ_BLOB_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);
CREATE TABLE QRTZ_CALENDARS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    CALENDAR_NAME VARCHAR(200) NOT NULL,
    CALENDAR      BLOB          NOT NULL,
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
    FIRED_TIME        BIGINT(13)    NOT NULL,
    SCHED_TIME        BIGINT(13)    NOT NULL,
    PRIORITY          INTEGER    NOT NULL,
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
    LAST_CHECKIN_TIME BIGINT(13)    NOT NULL,
    CHECKIN_INTERVAL  BIGINT(13)    NOT NULL,
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

-- DOMIBUS_CONNECTOR_MESSAGE

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

CREATE TABLE DOMIBUS_CONNECTOR_MSG_ERROR
(
    ID            BIGINT      NOT NULL,
    MESSAGE_ID    BIGINT      NOT NULL,
    ERROR_MESSAGE VARCHAR(2048) NOT NULL,
    DETAILED_TEXT LONGTEXT,
    ERROR_SOURCE  LONGTEXT,
    CREATED       TIMESTAMP           NOT NULL
);

CREATE TABLE DOMIBUS_CONNECTOR_MSG_CONT
(
    ID                    BIGINT NOT NULL,
    MESSAGE_ID            BIGINT,
    CONTENT_TYPE          VARCHAR(255),
   CONTENT LONGBLOB,
    CHECKSUM LONGTEXT,
    CREATED               TIMESTAMP NULL,
    STORAGE_PROVIDER_NAME VARCHAR(255),
    STORAGE_REFERENCE_ID  VARCHAR(512),
    DIGEST                VARCHAR(512),
    PAYLOAD_NAME          VARCHAR(512),
    PAYLOAD_IDENTIFIER    VARCHAR(512),
    PAYLOAD_DESCRIPTION LONGTEXT,
    PAYLOAD_MIMETYPE      VARCHAR(255),
    PAYLOAD_SIZE          BIGINT,
    DETACHED_SIGNATURE_ID BIGINT,
    DELETED               TIMESTAMP NULL
);

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

CREATE TABLE DOMIBUS_CONNECTOR_BIGDATA
(
    ID          VARCHAR(255) NOT NULL,
    CHECKSUM    LONGTEXT,
    CREATED     TIMESTAMP NULL,
    MESSAGE_ID  BIGINT,
    LAST_ACCESS TIMESTAMP NULL,
    NAME        LONGTEXT,
   CONTENT LONGBLOB,
    MIMETYPE    VARCHAR(255)
);

-- DOMIBUS_CONNECTOR_MESSAGE_INFO ---

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

CREATE TABLE DOMIBUS_CONNECTOR_PARTY
(
    ID            BIGINT NOT NULL,
    FK_PMODE_SET  BIGINT NOT NULL,
    IDENTIFIER    VARCHAR(255),
    PARTY_ID      VARCHAR(255)  NOT NULL,
    ROLE          VARCHAR(255)  NOT NULL,
    PARTY_ID_TYPE VARCHAR(512)  NOT NULL
);

CREATE TABLE DOMIBUS_CONNECTOR_ACTION
(
    ID           BIGINT NOT NULL,
    FK_PMODE_SET BIGINT NOT NULL,
    ACTION       VARCHAR(255)  NOT NULL,
    PDF_REQUIRED BOOLEAN  NOT NULL
);

CREATE TABLE DOMIBUS_CONNECTOR_SERVICE
(
    ID           BIGINT NOT NULL,
    FK_PMODE_SET BIGINT NOT NULL,
    SERVICE      VARCHAR(255)  NOT NULL,
    SERVICE_TYPE VARCHAR(255)
);

CREATE TABLE DOMIBUS_CONNECTOR_PROPERTY
(
    ID             BIGINT NOT NULL PRIMARY KEY,
    PROPERTY_NAME  VARCHAR(2048) NOT NULL,
    PROPERTY_VALUE VARCHAR(2048) NULL
);

create table DC_LINK_CONFIGURATION
(
    ID          BIGINT not null,
    CONFIG_NAME VARCHAR(255)  not null,
    LINK_IMPL   VARCHAR(255)
);

create table DC_LINK_CONFIG_PROPERTY
(
    DC_LINK_CONFIGURATION_ID BIGINT not null,
    PROPERTY_NAME            VARCHAR(255)  not null,
    PROPERTY_VALUE           LONGTEXT
);

create table DC_MESSAGE_LANE
(
    ID          BIGINT not null,
    NAME        VARCHAR(255)  not null,
    DESCRIPTION LONGTEXT
);

create table DC_MESSAGE_LANE_PROPERTY
(
    DC_MESSAGE_LANE_ID BIGINT not null,
    PROPERTY_NAME      VARCHAR(255)  not null,
    PROPERTY_VALUE     LONGTEXT
);

create table DC_LINK_PARTNER
(
    ID             BIGINT not null,
    NAME           VARCHAR(255)  not null,
    DESCRIPTION    LONGTEXT,
    ENABLED        BOOLEAN,
    LINK_CONFIG_ID BIGINT,
    LINK_TYPE      VARCHAR(20),
    LINK_MODE      VARCHAR(20)
);

create table DC_LINK_PARTNER_PROPERTY
(
    DC_LINK_PARTNER_ID BIGINT not null,
    PROPERTY_NAME      VARCHAR(255)  not null,
    PROPERTY_VALUE     LONGTEXT
);

create table DC_TRANSPORT_STEP
(
    ID                          BIGINT not null,
    CONNECTOR_MESSAGE_ID        BIGINT not null,
    LINK_PARTNER_NAME           VARCHAR(255)  not null,
    ATTEMPT                     INT            not null,
    TRANSPORT_ID                VARCHAR(255),
    TRANSPORT_SYSTEM_MESSAGE_ID VARCHAR(255),
    REMOTE_MESSAGE_ID           VARCHAR(255),
    CREATED                     TIMESTAMP NULL
);

create table DC_TRANSPORT_STEP_STATUS
(
    TRANSPORT_STEP_ID BIGINT not null,
    STATE             VARCHAR(40)   not null,
    CREATED           TIMESTAMP NULL,
    TEXT              LONGTEXT
);

create table DC_MSGCNT_DETSIG
(
    ID             BIGINT not null,
    SIGNATURE LONGTEXT,
    SIGNATURE_NAME VARCHAR(255),
    SIGNATURE_TYPE VARCHAR(255)
);

CREATE TABLE DC_PMODE_SET
(
    ID              BIGINT,
    FK_MESSAGE_LANE BIGINT,
    CREATED         TIMESTAMP NULL,
    DESCRIPTION LONGTEXT,
    ACTIVE          BOOLEAN DEFAULT 0 NOT NULL
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
VALUES (1, 'default_message_lane', 'default message lane');

-- DC_PMODE_SET

INSERT INTO DC_PMODE_SET (ID, FK_MESSAGE_LANE, CREATED, DESCRIPTION, ACTIVE)
VALUES (1,
        1,
        SYSDATE(),
        'initial set created by migration script',
        1);

-- DOMIBUS_CONNECTOR_PARTY ---
-- delete old default value row
insert into SequenceHelper values ('party', 1000);

delete
from BKP_DC_PARTY
where PARTY_ID = 'n.a.' ;

insert into DOMIBUS_CONNECTOR_PARTY
values (1, 1, null, 'n.a.', 'n.a.', 'n.a.');

insert into DOMIBUS_CONNECTOR_PARTY
select next_val_in_seq('party'),
       1,
       null as IDENTIFIER,
       PARTY_ID,
       ROLE,
       PARTY_ID_TYPE
from BKP_DC_PARTY;

INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE
VALUES ('DOMIBUS_CONNECTOR_PARTY.ID', (select max(ID) from DOMIBUS_CONNECTOR_PARTY) + 1 );

-- DOMIBUS_CONNECTOR_ACTION ---
insert into SequenceHelper values ('action', 1000);

-- delete old default value row
delete
from BKP_DC_ACTION
where ACTION = 'n.a.';

insert into DOMIBUS_CONNECTOR_ACTION
values (1, 1, 'n.a.', 0);

insert into DOMIBUS_CONNECTOR_ACTION
select next_val_in_seq('action'),
       1,
    ACTION,
    PDF_REQUIRED
from BKP_DC_ACTION;

INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE
VALUES ('DOMIBUS_CONNECTOR_ACTION.ID',(select max(ID) from DOMIBUS_CONNECTOR_ACTION) + 1);

-- DOMIBUS_CONNECTOR_SERVICE ---
insert into SequenceHelper values ('service', 1000);

-- delete old default value row
delete
from BKP_DC_SERVICE
where SERVICE = 'n.a.';

insert into DOMIBUS_CONNECTOR_SERVICE
values (1, 1, 'n.a.', 'n.a.');

insert into DOMIBUS_CONNECTOR_SERVICE
select next_val_in_seq('service'),
       1,
       SERVICE,
       SERVICE_TYPE
from BKP_DC_SERVICE;

INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE
VALUES ('DOMIBUS_CONNECTOR_SERVICE.ID', (select max(ID) from DOMIBUS_CONNECTOR_SERVICE) + 1);

-- DOMIBUS_CONNECTOR_PROPERTY ---
insert into SequenceHelper values ('property', 1000);

insert into DOMIBUS_CONNECTOR_PROPERTY
select next_val_in_seq('property') as ID, PROPERTY_NAME, PROPERTY_VALUE
from bkp_dc_property;

UPDATE DOMIBUS_CONNECTOR_SEQ_STORE
set SEQ_VALUE=(SELECT IFNULL((select max(ID) + 1 from DOMIBUS_CONNECTOR_PROPERTY) + 1, 1000))
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

DROP function next_val_in_seq;
DROP table SequenceHelper;
drop table bkp_dc_msg_err;
drop table bkp_dc_msg_cont;
drop table bkp_dc_evidence;
drop table bkp_dc_bigdata;
drop table BKP_DC_MESSAGE_INFO;
drop table BKP_DC_PARTY;
drop table BKP_DC_ACTION;
drop table BKP_DC_SERVICE;
drop table bkp_dc_property;
drop table BKP_DC_MESSAGE cascade;

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

SET FOREIGN_KEY_CHECKS = 1;
