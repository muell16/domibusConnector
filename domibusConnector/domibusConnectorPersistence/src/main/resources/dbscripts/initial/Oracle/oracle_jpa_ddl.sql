-- DC_ ...

create table DC_KEYSTORE
(
    ID          NUMBER(19)         not null
        primary key,
    DESCRIPTION VARCHAR2(255 char),
    KEYSTORE    RAW(255)           not null,
    PASSWORD    VARCHAR2(255 char),
    TYPE        VARCHAR2(255 char),
    UPLOADED    TIMESTAMP(6),
    UUID        VARCHAR2(255 char) not null
        constraint UK_90RY06HW9OPTJGEAY7S8MVYYE
            unique
)
/

create table DC_MESSAGE_LANE
(
    ID          NUMBER(19) not null
        primary key,
    DESCRIPTION CLOB,
    NAME        VARCHAR2(255 char)
        constraint UK_LJUYRLY9IS6SIOEIN0RO1YFH3
            unique
)
/

create table DC_MSGCNT_DETSIG
(
    ID             NUMBER(19) not null
        primary key,
    SIGNATURE      BLOB,
    SIGNATURE_NAME VARCHAR2(255 char),
    SIGNATURE_TYPE VARCHAR2(255 char)
)
/

create unique index PK_DC_USER_PWD
    on DC_MSGCNT_DETSIG (ID)
/

create table DC_TRANSPORT_STEP
(
    ID                          NUMBER(19) not null
        primary key,
    ATTEMPT                     NUMBER(10),
    CONNECTOR_MESSAGE_ID        VARCHAR2(255 char),
    CREATED                     TIMESTAMP(6),
    FINAL_STATE_REACHED         TIMESTAMP(6),
    LINK_PARTNER_NAME           VARCHAR2(255 char),
    REMOTE_MESSAGE_ID           VARCHAR2(255 char),
    TRANSPORT_ID                VARCHAR2(255 char),
    TRANSPORT_SYSTEM_MESSAGE_ID VARCHAR2(255 char),
    TRANSPORTED_MESSAGE         CLOB
)
/

-- Cyclic dependencies found

create table DC_LINK_CONFIGURATION
(
    ID          NUMBER(10)    not null
        constraint PK_DC_LINK_CONFIGURATION
            primary key
        primary key,
    CONFIG_NAME VARCHAR2(255) not null
        constraint UN_DC_LINK_CONF_NAME_01
            unique,
    LINK_IMPL   VARCHAR2(255)
)
/

create table DC_LINK_CONFIGURATION
(
    ID          NUMBER(19) not null,
    CONFIG_NAME VARCHAR2(255 char),
    LINK_IMPL   VARCHAR2(255 char)
)
/

create table DC_LINK_CONFIG_PROPERTY
(
    DC_LINK_CONFIGURATION_ID NUMBER(19)         not null
        constraint FK62L6HJP3V8Y2MGS1RFWAQSLQM
            references DC_LINK_CONFIGURATION,
    PROPERTY_VALUE           VARCHAR2(255 char),
    PROPERTY_NAME            VARCHAR2(255 char) not null,
    primary key (DC_LINK_CONFIGURATION_ID, PROPERTY_NAME)
)
/

create index SYS_C007063
    on DC_LINK_CONFIGURATION ()
/

create table DC_LINK_PARTNER
(
    ID             NUMBER(19) not null
        primary key,
    DESCRIPTION    VARCHAR2(255 char),
    ENABLED        NUMBER(1)  not null,
    NAME           VARCHAR2(255 char)
        constraint UK_50Y2L6V1VLCOAIMOAE2RPK5R6
            unique,
    LINK_TYPE      VARCHAR2(255 char),
    LINK_CONFIG_ID NUMBER(19)
        constraint FKDHL3VSSLWV2BO9TTJC5LNM4H6
            references DC_LINK_CONFIGURATION
)
/

create unique index PK_DC_MSG_ERROR
    on DC_LINK_PARTNER (ID)
/

-- Cyclic dependencies found

create table DC_LINK_PARTNER_PROPERTY
(
    DC_LINK_PARTNER_ID NUMBER(10)    not null
        constraint FK_DC_LINK_P_PROP_01
            references DC_LINK_PARTNER (),
    PROPERTY_NAME      VARCHAR2(255) not null,
    PROPERTY_VALUE     CLOB,
    constraint PK_DC_LINK_P_PROP
        primary key (DC_LINK_PARTNER_ID, PROPERTY_NAME),
    primary key (DC_LINK_PARTNER_ID, PROPERTY_NAME)
)
/

create table DC_LINK_PARTNER_PROPERTY
(
    DC_LINK_PARTNER_ID NUMBER(19)         not null,
    PROPERTY_VALUE     VARCHAR2(255 char),
    PROPERTY_NAME      VARCHAR2(255 char) not null,
    constraint FKQ1JP8N1V9EOVKN9MMSLNHLHHK
        foreign key () references DC_LINK_PARTNER
)
/

create index SYS_C007069
    on DC_LINK_PARTNER_PROPERTY ()
/

-- Cyclic dependencies found

create table DC_MESSAGE_LANE_PROPERTY
(
    DC_MESSAGE_LANE_ID NUMBER(10)    not null
        constraint FK_DC_MSG_LANE_PROP_01
            references DC_MESSAGE_LANE (),
    PROPERTY_NAME      VARCHAR2(255) not null,
    PROPERTY_VALUE     CLOB,
    constraint PK_DC_MSG_LANE_PROP
        primary key (DC_MESSAGE_LANE_ID, PROPERTY_NAME),
    primary key (DC_MESSAGE_LANE_ID, PROPERTY_NAME)
)
/

create table DC_MESSAGE_LANE_PROPERTY
(
    DC_MESSAGE_LANE_ID NUMBER(19)         not null,
    PROPERTY_VALUE     VARCHAR2(255 char),
    PROPERTY_NAME      VARCHAR2(255 char) not null,
    constraint FK8I4LMHLSFPWB2I9SRBUBYRHB4
        foreign key () references DC_MESSAGE_LANE
)
/

create index SYS_C007074
    on DC_MESSAGE_LANE_PROPERTY ()
/

create unique index PK_DC_SERVICE
    on DC_MESSAGE_LANE_PROPERTY (ID)
/

-- Cyclic dependencies found

create table DC_PMODE_SET
(
    ID                NUMBER(10)   not null
        constraint PK_DC_PMODE_SET
            primary key
        primary key,
    FK_MESSAGE_LANE   NUMBER(10)
        constraint FK_DC_PMODE_SET_01
            references DC_MESSAGE_LANE (),
    CREATED           TIMESTAMP(6) not null,
    DESCRIPTION       CLOB,
    ACTIVE            NUMBER(1)    not null,
    PMODES            BLOB,
    FK_CONNECTORSTORE NUMBER(10)
        constraint FK_DC_PMODE_SET_02
            references DC_KEYSTORE ()
)
/

create table DC_PMODE_SET
(
    ID                NUMBER(19) not null,
    ACTIVE            NUMBER(1),
    CREATED           TIMESTAMP(6),
    DESCRIPTION       VARCHAR2(255 char),
    PMODES            RAW(255),
    FK_CONNECTORSTORE NUMBER(19),
    FK_MESSAGE_LANE   NUMBER(19),
    constraint FKAWKFBEJUOOFU1IJHXHPQQJWDJ
        foreign key () references DC_KEYSTORE,
    constraint FKLNOIC3SOYNW9BPED4Y6IQXJPK
        foreign key () references DC_MESSAGE_LANE
)
/

create index SYS_C007078
    on DC_PMODE_SET ()
/

-- Cyclic dependencies found

create table DC_TRANSPORT_STEP_STATUS
(
    TRANSPORT_STEP_ID NUMBER(10)   not null
        constraint FK_DC_TRANS_STEP_STATUS_01
            references DC_TRANSPORT_STEP (),
    STATE             VARCHAR2(40) not null,
    CREATED           TIMESTAMP(6),
    TEXT              CLOB,
    constraint PK_DC_TRANS_STEP_STATUS
        primary key (TRANSPORT_STEP_ID, STATE),
    primary key (STATE, TRANSPORT_STEP_ID)
)
/

create table DC_TRANSPORT_STEP_STATUS
(
    STATE             VARCHAR2(255 char) not null,
    TRANSPORT_STEP_ID NUMBER(19)         not null,
    CREATED           TIMESTAMP(6),
    TEXT              CLOB,
    constraint FK5G1JNGH3F82IALBTNQQ99H418
        foreign key () references DC_TRANSPORT_STEP
)
/


-- Domibus

create table DOMIBUS_CONNECTOR_PARTY
(
    ID            NUMBER(19) not null
        primary key,
    PARTY_ID      VARCHAR2(255 char),
    PARTY_ID_TYPE VARCHAR2(255 char),
    IDENTIFIER    VARCHAR2(255 char),
    ROLE          VARCHAR2(255 char),
    ROLE_TYPE     VARCHAR2(255 char),
    FK_PMODE_SET  NUMBER(19)
        constraint FKAL7YNDGAIAPSLNDRUUU48G34
            references DC_PMODE_SET
)
/

create table DOMIBUS_CONNECTOR_PROPERTY
(
    ID             NUMBER(10) not null
        primary key,
    PROPERTY_NAME  VARCHAR2(255 char),
    PROPERTY_VALUE VARCHAR2(255 char)
)
/

create table DOMIBUS_CONNECTOR_SERVICE
(
    ID           NUMBER(19) not null
        primary key,
    SERVICE      VARCHAR2(255 char),
    SERVICE_TYPE VARCHAR2(255 char),
    FK_PMODE_SET NUMBER(19)
        constraint FKBJ0847CSNU0CBI0U92J81LRN0
            references DC_PMODE_SET
)
/

-- Cyclic dependencies found

create table DOMIBUS_CONNECTOR_ACTION
(
    ID           NUMBER(10)         not null
        constraint PK_DC_ACTION
            primary key
        primary key,
    FK_PMODE_SET NUMBER(10)         not null
        constraint FK_DC_ACTION_PMODE_SET_01
            references DC_PMODE_SET,
    ACTION       VARCHAR2(255 char) not null
)
/

create table DOMIBUS_CONNECTOR_ACTION
(
    ID           NUMBER(19) not null,
    ACTION       VARCHAR2(255 char),
    FK_PMODE_SET NUMBER(19),
    constraint FK249380R1RR1KT886ABX7EXJ7G
        foreign key () references DC_PMODE_SET
)
/

create index SYS_C007085
    on DOMIBUS_CONNECTOR_ACTION ()
/

-- Cyclic dependencies found

create table DOMIBUS_CONNECTOR_BIGDATA
(
    ID                   VARCHAR2(255 char) not null
        constraint PK_DC_BIGDATA
            primary key
        primary key,
    CHECKSUM             CLOB,
    CREATED              TIMESTAMP(6)       not null,
    CONNECTOR_MESSAGE_ID VARCHAR2(255 char),
    LAST_ACCESS          TIMESTAMP(6),
    NAME                 CLOB,
    CONTENT              BLOB,
    MIMETYPE             VARCHAR2(255 char)
)
/

create table DOMIBUS_CONNECTOR_BIGDATA
(
    ID                   NUMBER(19) not null,
    CHECKSUM             VARCHAR2(255 char),
    CONNECTOR_MESSAGE_ID VARCHAR2(255 char),
    CONTENT              BLOB,
    CREATED              TIMESTAMP(6),
    LAST_ACCESS          TIMESTAMP(6),
    MIMETYPE             VARCHAR2(255 char),
    NAME                 VARCHAR2(255 char)
)
/

create index SYS_C007087
    on DOMIBUS_CONNECTOR_BIGDATA ()
/

-- Cyclic dependencies found

create table DOMIBUS_CONNECTOR_MESSAGE
(
    ID                   NUMBER(10)   not null
        constraint PK_DC_MESSAGE
            primary key
        primary key,
    EBMS_MESSAGE_ID      VARCHAR2(255 char)
        constraint UK_E71RH4N71M4MPGCOKHENGR592
            unique,
    BACKEND_MESSAGE_ID   VARCHAR2(255 char)
        constraint UK_81O66LN4TXUJH8P62A6G6LQX9
            unique
        constraint UQ_DC_MSG_BCK_MSG_ID
            unique,
    BACKEND_NAME         VARCHAR2(255 char),
    CONNECTOR_MESSAGE_ID VARCHAR2(255 char)
        constraint UK_S9Y5AJQYJNJB7GJF2NA4AE7UR
            unique
        constraint UQ_DC_MSG_CON_MSG_ID
            unique,
    CONVERSATION_ID      VARCHAR2(255 char),
    HASH_VALUE           CLOB,
    CONFIRMED            TIMESTAMP(6),
    REJECTED             TIMESTAMP(6),
    DELIVERED_BACKEND    TIMESTAMP(6),
    DELIVERED_GW         TIMESTAMP(6),
    UPDATED              TIMESTAMP(6) not null,
    CREATED              TIMESTAMP(6) not null,
    DIRECTION_SOURCE     VARCHAR2(20),
    DIRECTION_TARGET     VARCHAR2(20),
    GATEWAY_NAME         VARCHAR2(255)
)
/

create table DOMIBUS_CONNECTOR_MESSAGE
(
    ID                   NUMBER(19)         not null,
    BACKEND_MESSAGE_ID   VARCHAR2(255 char),
    BACKEND_NAME         VARCHAR2(255 char),
    CONFIRMED            TIMESTAMP(6),
    CONNECTOR_MESSAGE_ID VARCHAR2(255 char) not null,
    CONVERSATION_ID      VARCHAR2(255 char),
    CREATED              TIMESTAMP(6)       not null,
    DELIVERED_GW         TIMESTAMP(6),
    DELIVERED_BACKEND    TIMESTAMP(6),
    DIRECTION_SOURCE     VARCHAR2(10 char),
    DIRECTION_TARGET     VARCHAR2(10 char),
    EBMS_MESSAGE_ID      VARCHAR2(255 char),
    GATEWAY_NAME         VARCHAR2(255 char),
    HASH_VALUE           VARCHAR2(255 char),
    REJECTED             TIMESTAMP(6),
    UPDATED              TIMESTAMP(6)       not null
)
/

create index SYS_C007096
    on DOMIBUS_CONNECTOR_MESSAGE ()
/

create index UK_81O66LN4TXUJH8P62A6G6LQX9
    on DOMIBUS_CONNECTOR_MESSAGE ()
/

create index UK_S9Y5AJQYJNJB7GJF2NA4AE7UR
    on DOMIBUS_CONNECTOR_MESSAGE ()
/

create index UK_E71RH4N71M4MPGCOKHENGR592
    on DOMIBUS_CONNECTOR_MESSAGE ()
/

create table DOMIBUS_CONNECTOR_MSG_ERROR
(
    ID            NUMBER(19)   not null
        primary key,
    CREATED       TIMESTAMP(6) not null,
    DETAILED_TEXT VARCHAR2(255 char),
    ERROR_MESSAGE VARCHAR2(2048 char),
    ERROR_SOURCE  VARCHAR2(255 char),
    MESSAGE_ID    NUMBER(19)   not null
        constraint FKI0WRARSE6I0T5NJ4R82P1E4N
            references DOMIBUS_CONNECTOR_MESSAGE
)
/

-- Cyclic dependencies found

create table DOMIBUS_CONNECTOR_EVIDENCE
(
    ID                   NUMBER(10)   not null
        constraint PK_DC_EVIDENCE
            primary key
        primary key,
    MESSAGE_ID           NUMBER(10)   not null
        constraint FK_DC_EVIDENCES_01
            references DOMIBUS_CONNECTOR_MESSAGE,
    CONNECTOR_MESSAGE_ID VARCHAR2(255),
    TYPE                 VARCHAR2(255 char),
    EVIDENCE             CLOB,
    DELIVERED_NAT        TIMESTAMP(6),
    DELIVERED_GW         TIMESTAMP(6),
    UPDATED              TIMESTAMP(6) not null
)
/

create table DOMIBUS_CONNECTOR_EVIDENCE
(
    ID            NUMBER(19)   not null,
    DELIVERED_NAT TIMESTAMP(6),
    DELIVERED_GW  TIMESTAMP(6),
    EVIDENCE      VARCHAR2(255 char),
    TYPE          VARCHAR2(255 char),
    UPDATED       TIMESTAMP(6) not null,
    MESSAGE_ID    NUMBER(19)   not null,
    constraint FK4JXG7XYFGFL8TXAY9SLWCAFJ1
        foreign key () references DOMIBUS_CONNECTOR_MESSAGE
)
/

create index SYS_C007091
    on DOMIBUS_CONNECTOR_EVIDENCE ()
/

-- Cyclic dependencies found

create table DOMIBUS_CONNECTOR_MESSAGE_INFO
(
    ID               NUMBER(10)   not null
        constraint PK_DC_MESSAGE_INFO
            primary key
        primary key,
    MESSAGE_ID       NUMBER(10)   not null
        constraint FK_DC_MSG_INFO_I
            references DOMIBUS_CONNECTOR_MESSAGE,
    FK_FROM_PARTY_ID NUMBER(10)
        constraint FK_DC_MSG_INFO_F_PARTY
            references DOMIBUS_CONNECTOR_PARTY (),
    FK_TO_PARTY_ID   NUMBER(10)
        constraint FK_DC_MSG_INFO_T_PARTY
            references DOMIBUS_CONNECTOR_PARTY (),
    ORIGINAL_SENDER  VARCHAR2(2048 char),
    FINAL_RECIPIENT  VARCHAR2(2048 char),
    FK_SERVICE       NUMBER(10)
        constraint FK_DC_MSG_INFO_SERVICE
            references DOMIBUS_CONNECTOR_SERVICE (),
    FK_ACTION        NUMBER(10)
        constraint FK_DC_MSG_INFO_ACTION
            references DOMIBUS_CONNECTOR_ACTION,
    CREATED          TIMESTAMP(6) not null,
    UPDATED          TIMESTAMP(6) not null
)
/

create table DOMIBUS_CONNECTOR_MESSAGE_INFO
(
    ID               NUMBER(19)   not null,
    CREATED          TIMESTAMP(6) not null,
    FINAL_RECIPIENT  VARCHAR2(255 char),
    ORIGINAL_SENDER  VARCHAR2(255 char),
    UPDATED          TIMESTAMP(6) not null,
    FK_ACTION        NUMBER(19),
    FK_FROM_PARTY_ID NUMBER(19),
    MESSAGE_ID       NUMBER(19)   not null,
    FK_SERVICE       NUMBER(19),
    FK_TO_PARTY_ID   NUMBER(19),
    constraint FKA5OHEQMHN4EU4J1YUYI3FEMSH
        foreign key () references DOMIBUS_CONNECTOR_PARTY,
    constraint FKADKW4KU0O3A3X80FELPTLTNFR
        foreign key () references DOMIBUS_CONNECTOR_ACTION,
    constraint FKHBVKHB64LTJR9PJPVDS09T6H7
        foreign key () references DOMIBUS_CONNECTOR_PARTY,
    constraint FKOLTSH7WSH3A0PJG7AAGLTLBBO
        foreign key () references DOMIBUS_CONNECTOR_SERVICE,
    constraint FKUVD19003OB697V6E8OVGW140
        foreign key () references DOMIBUS_CONNECTOR_MESSAGE
)
/

create index SYS_C007101
    on DOMIBUS_CONNECTOR_MESSAGE_INFO ()
/

-- Cyclic dependencies found

create table DOMIBUS_CONNECTOR_MSG_CONT
(
    ID                    NUMBER(10)   not null
        constraint PK_DC_MSG_CONT
            primary key
        primary key,
    MESSAGE_ID            NUMBER(10)   not null
        constraint FK_DC_CON_01
            references DOMIBUS_CONNECTOR_MESSAGE,
    CONTENT_TYPE          VARCHAR2(255 char),
    CONTENT               BLOB,
    CHECKSUM              CLOB,
    CREATED               TIMESTAMP(6) not null,
    STORAGE_PROVIDER_NAME VARCHAR2(255),
    STORAGE_REFERENCE_ID  VARCHAR2(512),
    DIGEST                VARCHAR2(512),
    PAYLOAD_NAME          VARCHAR2(512),
    PAYLOAD_IDENTIFIER    VARCHAR2(512),
    PAYLOAD_DESCRIPTION   CLOB,
    PAYLOAD_MIMETYPE      VARCHAR2(255),
    PAYLOAD_SIZE          NUMBER(10),
    DETACHED_SIGNATURE_ID NUMBER(10)
        constraint FK_DC_MSG_CONT_02
            references DC_MSGCNT_DETSIG (),
    DELETED               TIMESTAMP(6),
    CONNECTOR_MESSAGE_ID  VARCHAR2(512 char)
)
/

create table DOMIBUS_CONNECTOR_MSG_CONT
(
    ID                    NUMBER(19) not null,
    CHECKSUM              VARCHAR2(255 char),
    CONNECTOR_MESSAGE_ID  VARCHAR2(255 char),
    CONTENT               BLOB,
    CONTENT_TYPE          VARCHAR2(255 char),
    CREATED               TIMESTAMP(6),
    DELETED               TIMESTAMP(6),
    DIGEST                VARCHAR2(255 char),
    PAYLOAD_DESCRIPTION   VARCHAR2(255 char),
    PAYLOAD_IDENTIFIER    VARCHAR2(255 char),
    PAYLOAD_MIMETYPE      VARCHAR2(255 char),
    PAYLOAD_NAME          VARCHAR2(255 char),
    PAYLOAD_SIZE          NUMBER(19),
    STORAGE_PROVIDER_NAME VARCHAR2(255 char),
    STORAGE_REFERENCE_ID  VARCHAR2(255 char),
    DETACHED_SIGNATURE_ID NUMBER(19),
    MESSAGE_ID            NUMBER(19),
    constraint FK7EMYMOIGDT3QSPLYRI0DQ1XOW
        foreign key () references DC_MSGCNT_DETSIG,
    constraint FKDA043M9H695OGLA2SG58KXKB1
        foreign key () references DOMIBUS_CONNECTOR_MESSAGE
)
/

create index SYS_C007103
    on DOMIBUS_CONNECTOR_MSG_CONT ()
/

-- Cyclic dependencies found

create table DOMIBUS_CONNECTOR_SEQ_STORE
(
    SEQ_NAME  VARCHAR2(255 char) not null
        constraint PK_DC_SEQ_STORE
            primary key
        primary key,
    SEQ_VALUE NUMBER(10)         not null
)
/

create table DOMIBUS_CONNECTOR_SEQ_STORE
(
    SEQ_NAME  VARCHAR2(255 char) not null,
    SEQ_VALUE NUMBER(19)
)
/

create index SYS_C007113
    on DOMIBUS_CONNECTOR_SEQ_STORE ()
/

-- Cyclic dependencies found

create table DOMIBUS_CONNECTOR_USER
(
    ID                     NUMBER(10)          not null
        constraint PK_DC_USER
            primary key
        primary key,
    USERNAME               VARCHAR2(50)        not null
        constraint USERNAME_UNIQUE
            unique,
    ROLE                   VARCHAR2(50)        not null,
    LOCKED                 NUMBER(1) default 0 not null,
    NUMBER_OF_GRACE_LOGINS NUMBER(2) default 5 not null,
    GRACE_LOGINS_USED      NUMBER(2) default 0 not null,
    CREATED                TIMESTAMP(6)        not null
)
/

create table DOMIBUS_CONNECTOR_USER
(
    ID                     NUMBER(19)         not null,
    CREATED                TIMESTAMP(6)       not null,
    GRACE_LOGINS_USED      NUMBER(19)         not null,
    LOCKED                 NUMBER(1),
    NUMBER_OF_GRACE_LOGINS NUMBER(19)         not null,
    ROLE                   VARCHAR2(255 char) not null,
    USERNAME               VARCHAR2(255 char) not null
)
/

create index SYS_C007122
    on DOMIBUS_CONNECTOR_USER ()
/

create table DOMIBUS_CONNECTOR_USER_PWD
(
    ID          NUMBER(19)         not null
        primary key,
    CREATED     TIMESTAMP(6)       not null,
    CURRENT_PWD NUMBER(1),
    INITIAL_PWD NUMBER(1),
    PASSWORD    VARCHAR2(255 char) not null,
    SALT        VARCHAR2(255 char) not null,
    USER_ID     NUMBER(19)         not null
        constraint FK62DOE366DLQ21RV9YSF7HFK4E
            references DOMIBUS_CONNECTOR_USER
)
/

-- QUARTZ

create table QRTZ_CALENDARS
(
    SCHED_NAME    VARCHAR2(120) not null,
    CALENDAR_NAME VARCHAR2(200) not null,
    CALENDAR      BLOB          not null,
    constraint QRTZ_CALENDARS_PK
        primary key (SCHED_NAME, CALENDAR_NAME)
)
/

create table QRTZ_PAUSED_TRIGGER_GRPS
(
    SCHED_NAME    VARCHAR2(120) not null,
    TRIGGER_GROUP VARCHAR2(200) not null,
    constraint QRTZ_PAUSED_TRIG_GRPS_PK
        primary key (SCHED_NAME, TRIGGER_GROUP)
)
/

create table QRTZ_FIRED_TRIGGERS
(
    SCHED_NAME        VARCHAR2(120) not null,
    ENTRY_ID          VARCHAR2(95)  not null,
    TRIGGER_NAME      VARCHAR2(200) not null,
    TRIGGER_GROUP     VARCHAR2(200) not null,
    INSTANCE_NAME     VARCHAR2(200) not null,
    FIRED_TIME        NUMBER(13)    not null,
    SCHED_TIME        NUMBER(13)    not null,
    PRIORITY          NUMBER(13)    not null,
    STATE             VARCHAR2(16)  not null,
    JOB_NAME          VARCHAR2(200),
    JOB_GROUP         VARCHAR2(200),
    IS_NONCONCURRENT  VARCHAR2(1),
    REQUESTS_RECOVERY VARCHAR2(1),
    constraint QRTZ_FIRED_TRIGGER_PK
        primary key (SCHED_NAME, ENTRY_ID)
)
/

create index IDX_QRTZ_FT_TRIG_INST_NAME
    on QRTZ_FIRED_TRIGGERS (SCHED_NAME, INSTANCE_NAME)
/

create index IDX_QRTZ_FT_INST_JOB_REQ_RCVRY
    on QRTZ_FIRED_TRIGGERS (SCHED_NAME, INSTANCE_NAME, REQUESTS_RECOVERY)
/

create index IDX_QRTZ_FT_J_G
    on QRTZ_FIRED_TRIGGERS (SCHED_NAME, JOB_NAME, JOB_GROUP)
/

create index IDX_QRTZ_FT_JG
    on QRTZ_FIRED_TRIGGERS (SCHED_NAME, JOB_GROUP)
/

create index IDX_QRTZ_FT_T_G
    on QRTZ_FIRED_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
/

create index IDX_QRTZ_FT_TG
    on QRTZ_FIRED_TRIGGERS (SCHED_NAME, TRIGGER_GROUP)
/

create table QRTZ_SCHEDULER_STATE
(
    SCHED_NAME        VARCHAR2(120) not null,
    INSTANCE_NAME     VARCHAR2(200) not null,
    LAST_CHECKIN_TIME NUMBER(13)    not null,
    CHECKIN_INTERVAL  NUMBER(13)    not null,
    constraint QRTZ_SCHEDULER_STATE_PK
        primary key (SCHED_NAME, INSTANCE_NAME)
)
/

create table QRTZ_LOCKS
(
    SCHED_NAME VARCHAR2(120) not null,
    LOCK_NAME  VARCHAR2(40)  not null,
    constraint QRTZ_LOCKS_PK
        primary key (SCHED_NAME, LOCK_NAME)
)
/

create unique index PK_DC_TRANSPORT_STEP
    on QRTZ_LOCKS (ID)
/

-- Cyclic dependencies found

create table QRTZ_JOB_DETAILS
(
    SCHED_NAME        VARCHAR2(120) not null,
    JOB_NAME          VARCHAR2(200) not null,
    JOB_GROUP         VARCHAR2(200) not null,
    DESCRIPTION       VARCHAR2(250),
    JOB_CLASS_NAME    VARCHAR2(250) not null,
    IS_DURABLE        VARCHAR2(1)   not null,
    IS_NONCONCURRENT  VARCHAR2(1)   not null,
    IS_UPDATE_DATA    VARCHAR2(1)   not null,
    REQUESTS_RECOVERY VARCHAR2(1)   not null,
    JOB_DATA          BLOB,
    constraint QRTZ_JOB_DETAILS_PK
        primary key (SCHED_NAME, JOB_NAME, JOB_GROUP)
)
/

create table QRTZ_JOB_DETAILS
(
    SCHED_NAME        VARCHAR2(120) not null,
    JOB_NAME          VARCHAR2(200) not null,
    JOB_GROUP         VARCHAR2(200) not null,
    DESCRIPTION       VARCHAR2(250),
    JOB_CLASS_NAME    VARCHAR2(250) not null,
    IS_DURABLE        VARCHAR2(1)   not null,
    IS_NONCONCURRENT  VARCHAR2(1)   not null,
    IS_UPDATE_DATA    VARCHAR2(1)   not null,
    REQUESTS_RECOVERY VARCHAR2(1)   not null,
    JOB_DATA          BLOB,
    constraint QRTZ_JOB_DETAILS_PK
        primary key (SCHED_NAME, JOB_NAME, JOB_GROUP)
)
/

create index IDX_QRTZ_J_REQ_RECOVERY
    on QRTZ_JOB_DETAILS (SCHED_NAME, REQUESTS_RECOVERY)
/

create index IDX_QRTZ_J_GRP
    on QRTZ_JOB_DETAILS (SCHED_NAME, JOB_GROUP)
/

-- Cyclic dependencies found

create table QRTZ_TRIGGERS
(
    SCHED_NAME     VARCHAR2(120) not null,
    TRIGGER_NAME   VARCHAR2(200) not null,
    TRIGGER_GROUP  VARCHAR2(200) not null,
    JOB_NAME       VARCHAR2(200) not null,
    JOB_GROUP      VARCHAR2(200) not null,
    DESCRIPTION    VARCHAR2(250),
    NEXT_FIRE_TIME NUMBER(13),
    PREV_FIRE_TIME NUMBER(13),
    PRIORITY       NUMBER(13),
    TRIGGER_STATE  VARCHAR2(16)  not null,
    TRIGGER_TYPE   VARCHAR2(8)   not null,
    START_TIME     NUMBER(13)    not null,
    END_TIME       NUMBER(13),
    CALENDAR_NAME  VARCHAR2(200),
    MISFIRE_INSTR  NUMBER(2),
    JOB_DATA       BLOB,
    constraint QRTZ_TRIGGERS_PK
        primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint QRTZ_TRIGGER_TO_JOBS_FK
        foreign key (SCHED_NAME, JOB_NAME, JOB_GROUP) references QRTZ_JOB_DETAILS
)
/

create table QRTZ_TRIGGERS
(
    SCHED_NAME     VARCHAR2(120) not null,
    TRIGGER_NAME   VARCHAR2(200) not null,
    TRIGGER_GROUP  VARCHAR2(200) not null,
    JOB_NAME       VARCHAR2(200) not null,
    JOB_GROUP      VARCHAR2(200) not null,
    DESCRIPTION    VARCHAR2(250),
    NEXT_FIRE_TIME NUMBER(13),
    PREV_FIRE_TIME NUMBER(13),
    PRIORITY       NUMBER(13),
    TRIGGER_STATE  VARCHAR2(16)  not null,
    TRIGGER_TYPE   VARCHAR2(8)   not null,
    START_TIME     NUMBER(13)    not null,
    END_TIME       NUMBER(13),
    CALENDAR_NAME  VARCHAR2(200),
    MISFIRE_INSTR  NUMBER(2),
    JOB_DATA       BLOB,
    constraint QRTZ_TRIGGERS_PK
        primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint QRTZ_TRIGGER_TO_JOBS_FK
        foreign key (SCHED_NAME, JOB_NAME, JOB_GROUP) references QRTZ_JOB_DETAILS
)
/

create index IDX_QRTZ_T_J
    on QRTZ_TRIGGERS (SCHED_NAME, JOB_NAME, JOB_GROUP)
/

create index IDX_QRTZ_T_JG
    on QRTZ_TRIGGERS (SCHED_NAME, JOB_GROUP)
/

create index IDX_QRTZ_T_C
    on QRTZ_TRIGGERS (SCHED_NAME, CALENDAR_NAME)
/

create index IDX_QRTZ_T_G
    on QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_GROUP)
/

create index IDX_QRTZ_T_STATE
    on QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_STATE)
/

create index IDX_QRTZ_T_N_STATE
    on QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP, TRIGGER_STATE)
/

create index IDX_QRTZ_T_N_G_STATE
    on QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_GROUP, TRIGGER_STATE)
/

create index IDX_QRTZ_T_NEXT_FIRE_TIME
    on QRTZ_TRIGGERS (SCHED_NAME, NEXT_FIRE_TIME)
/

create index IDX_QRTZ_T_NFT_ST
    on QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_STATE, NEXT_FIRE_TIME)
/

create index IDX_QRTZ_T_NFT_MISFIRE
    on QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME)
/

create index IDX_QRTZ_T_NFT_ST_MISFIRE
    on QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_STATE)
/

create index IDX_QRTZ_T_NFT_ST_MISFIRE_GRP
    on QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_GROUP, TRIGGER_STATE)
/

create unique index PK_DC_LINK_CONF_PROP
    on QRTZ_TRIGGERS (DC_LINK_CONFIGURATION_ID, PROPERTY_NAME)
/

-- Cyclic dependencies found

create table QRTZ_BLOB_TRIGGERS
(
    SCHED_NAME    VARCHAR2(120) not null,
    TRIGGER_NAME  VARCHAR2(200) not null,
    TRIGGER_GROUP VARCHAR2(200) not null,
    BLOB_DATA     BLOB,
    constraint QRTZ_BLOB_TRIG_PK
        primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint QRTZ_BLOB_TRIG_TO_TRIG_FK
        foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references QRTZ_TRIGGERS
)
/

create table QRTZ_BLOB_TRIGGERS
(
    SCHED_NAME    VARCHAR2(120) not null,
    TRIGGER_NAME  VARCHAR2(200) not null,
    TRIGGER_GROUP VARCHAR2(200) not null,
    BLOB_DATA     BLOB,
    constraint QRTZ_BLOB_TRIG_PK
        primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint QRTZ_BLOB_TRIG_TO_TRIG_FK
        foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references QRTZ_TRIGGERS
)
/

create unique index PK_DC_MSG_LANE
    on QRTZ_BLOB_TRIGGERS (ID)
/

-- Cyclic dependencies found

create table QRTZ_CRON_TRIGGERS
(
    SCHED_NAME      VARCHAR2(120) not null,
    TRIGGER_NAME    VARCHAR2(200) not null,
    TRIGGER_GROUP   VARCHAR2(200) not null,
    CRON_EXPRESSION VARCHAR2(120) not null,
    TIME_ZONE_ID    VARCHAR2(80),
    constraint QRTZ_CRON_TRIG_PK
        primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint QRTZ_CRON_TRIG_TO_TRIG_FK
        foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references QRTZ_TRIGGERS
)
/

create table QRTZ_CRON_TRIGGERS
(
    SCHED_NAME      VARCHAR2(120) not null,
    TRIGGER_NAME    VARCHAR2(200) not null,
    TRIGGER_GROUP   VARCHAR2(200) not null,
    CRON_EXPRESSION VARCHAR2(120) not null,
    TIME_ZONE_ID    VARCHAR2(80),
    constraint QRTZ_CRON_TRIG_TO_TRIG_FK
        foreign key () references QRTZ_TRIGGERS
)
/

create index QRTZ_CRON_TRIG_PK
    on QRTZ_CRON_TRIGGERS ()
/

create index QRTZ_CRON_TRIG_PK
    on QRTZ_CRON_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
/

-- Cyclic dependencies found

create table QRTZ_SIMPLE_TRIGGERS
(
    SCHED_NAME      VARCHAR2(120) not null,
    TRIGGER_NAME    VARCHAR2(200) not null,
    TRIGGER_GROUP   VARCHAR2(200) not null,
    REPEAT_COUNT    NUMBER(7)     not null,
    REPEAT_INTERVAL NUMBER(12)    not null,
    TIMES_TRIGGERED NUMBER(10)    not null,
    constraint QRTZ_SIMPLE_TRIG_PK
        primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint QRTZ_SIMPLE_TRIG_TO_TRIG_FK
        foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references QRTZ_TRIGGERS
)
/

create table QRTZ_SIMPLE_TRIGGERS
(
    SCHED_NAME      VARCHAR2(120) not null,
    TRIGGER_NAME    VARCHAR2(200) not null,
    TRIGGER_GROUP   VARCHAR2(200) not null,
    REPEAT_COUNT    NUMBER(7)     not null,
    REPEAT_INTERVAL NUMBER(12)    not null,
    TIMES_TRIGGERED NUMBER(10)    not null,
    constraint QRTZ_SIMPLE_TRIG_TO_TRIG_FK
        foreign key () references QRTZ_TRIGGERS
)
/

create index QRTZ_SIMPLE_TRIG_PK
    on QRTZ_SIMPLE_TRIGGERS ()
/

-- Cyclic dependencies found

create table QRTZ_SIMPROP_TRIGGERS
(
    SCHED_NAME    VARCHAR2(120) not null,
    TRIGGER_NAME  VARCHAR2(200) not null,
    TRIGGER_GROUP VARCHAR2(200) not null,
    STR_PROP_1    VARCHAR2(512),
    STR_PROP_2    VARCHAR2(512),
    STR_PROP_3    VARCHAR2(512),
    INT_PROP_1    NUMBER(10),
    INT_PROP_2    NUMBER(10),
    LONG_PROP_1   NUMBER(13),
    LONG_PROP_2   NUMBER(13),
    DEC_PROP_1    NUMBER(13, 4),
    DEC_PROP_2    NUMBER(13, 4),
    BOOL_PROP_1   VARCHAR2(1),
    BOOL_PROP_2   VARCHAR2(1),
    constraint QRTZ_SIMPROP_TRIG_PK
        primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint QRTZ_SIMPROP_TRIG_TO_TRIG_FK
        foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references QRTZ_TRIGGERS
)
/

create table QRTZ_SIMPROP_TRIGGERS
(
    SCHED_NAME    VARCHAR2(120) not null,
    TRIGGER_NAME  VARCHAR2(200) not null,
    TRIGGER_GROUP VARCHAR2(200) not null,
    STR_PROP_1    VARCHAR2(512),
    STR_PROP_2    VARCHAR2(512),
    STR_PROP_3    VARCHAR2(512),
    INT_PROP_1    NUMBER(10),
    INT_PROP_2    NUMBER(10),
    LONG_PROP_1   NUMBER(13),
    LONG_PROP_2   NUMBER(13),
    DEC_PROP_1    NUMBER(13, 4),
    DEC_PROP_2    NUMBER(13, 4),
    BOOL_PROP_1   VARCHAR2(1),
    BOOL_PROP_2   VARCHAR2(1),
    constraint QRTZ_SIMPROP_TRIG_TO_TRIG_FK
        foreign key () references QRTZ_TRIGGERS
)
/

create index QRTZ_SIMPROP_TRIG_PK
    on QRTZ_SIMPROP_TRIGGERS ()
/

create index QRTZ_SIMPROP_TRIG_PK
    on QRTZ_SIMPROP_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
/
