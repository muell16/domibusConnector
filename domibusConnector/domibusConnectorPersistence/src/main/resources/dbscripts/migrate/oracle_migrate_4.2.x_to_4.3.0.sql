-- *********************************************************************
-- Update Database Script - from domibusConnector 4.2 to 4.3
-- *********************************************************************
-- change message timestamps in DOMIBUS_CONNECTOR_EVIDENCE and DOMIBUS_CONNECTOR_MESSAGE
-- remove not null constraint on service_type in DOMIBUS_CONNECTOR_SERVICE
--

-- #################### 1/6 RENAME tables that need to be recreated ####################

rename DOMIBUS_CONNECTOR_SERVICE to BKP_DC_SERVICE;
RENAME DOMIBUS_CONNECTOR_SEQ_STORE TO BKP_DC_SEQ_STORE;
RENAME DOMIBUS_CONNECTOR_ACTION TO BKP_DC_ACTION;
RENAME DC_PMODE_SET TO BKP_PMODE_SET;
RENAME DOMIBUS_CONNECTOR_MESSAGE TO BKP_DC_MESSAGE;

-- #################### 2/6 CREATE tables, structural changes ####################

-- delayed to 4.4
-- alter table DOMIBUS_CONNECTOR_ACTION drop column PDF_REQUIRED;

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
    UPDATED              TIMESTAMP NOT NULL,
    CREATED              TIMESTAMP NOT NULL,
    DIRECTION_SOURCE     VARCHAR2(20),
    DIRECTION_TARGET     VARCHAR2(20),
    GATEWAY_NAME         VARCHAR2(255)
);


CREATE TABLE DOMIBUS_CONNECTOR_SEQ_STORE
(
    SEQ_NAME  VARCHAR2(255 CHAR) NOT NULL,
    SEQ_VALUE DECIMAL(10, 0)     NOT NULL
);


CREATE TABLE DOMIBUS_CONNECTOR_ACTION
(
    ID           DECIMAL(10, 0) NOT NULL,
    FK_PMODE_SET DECIMAL(10, 0) NOT NULL,
    ACTION       VARCHAR2(255)  NOT NULL,
    PDF_REQUIRED DECIMAL(1, 0)  NOT NULL
);


alter table DOMIBUS_CONNECTOR_MSG_CONT
    add CONNECTOR_MESSAGE_ID VARCHAR2(512 CHAR);

CREATE TABLE DOMIBUS_CONNECTOR_SERVICE
(
    ID           DECIMAL(10, 0) NOT NULL,
    FK_PMODE_SET DECIMAL(10, 0) NOT NULL,
    SERVICE      VARCHAR2(255)  NOT NULL,
    SERVICE_TYPE VARCHAR2(512)
);

CREATE TABLE DC_PMODE_SET
(
    ID              DECIMAL(10, 0) NOT NULL,
    FK_MESSAGE_LANE DECIMAL(10, 0),
    CREATED         TIMESTAMP      NOT NULL,
    DESCRIPTION     CLOB,
    ACTIVE          DECIMAL(1, 0)  NOT NULL,
    CONSTRAINT PK_DC_PMODE_SET PRIMARY KEY (ID)
);


alter table DC_TRANSPORT_STEP rename column CONNECTOR_MESSAGE_ID to bkp_cmid;
alter table DC_TRANSPORT_STEP add           CONNECTOR_MESSAGE_ID VARCHAR2(255);
alter table DC_TRANSPORT_STEP add           TRANSPORTED_MESSAGE CLOB;
update      DC_TRANSPORT_STEP set           CONNECTOR_MESSAGE_ID=to_char(bkp_cmid);
alter table DC_TRANSPORT_STEP drop column   bkp_cmid;
alter table DC_TRANSPORT_STEP modify       ("CONNECTOR_MESSAGE_ID" not null);
alter table DC_TRANSPORT_STEP add FINAL_STATE_REACHED TIMESTAMP;

alter table DOMIBUS_CONNECTOR_EVIDENCE modify ("UPDATED" not null);

alter table DOMIBUS_CONNECTOR_MESSAGE modify ("CREATED" not null);
alter table DOMIBUS_CONNECTOR_MESSAGE modify ("UPDATED" not null);

alter table DOMIBUS_CONNECTOR_MESSAGE_INFO modify ("CREATED" not null);
alter table DOMIBUS_CONNECTOR_MESSAGE_INFO modify ("UPDATED" not null);

alter table DOMIBUS_CONNECTOR_MSG_CONT modify ("MESSAGE_ID" not null);
alter table DOMIBUS_CONNECTOR_MSG_CONT modify ("CREATED" not null);

alter table DOMIBUS_CONNECTOR_BIGDATA modify ("CREATED" not null);



-- FK_MESSAGESTEP_MESSAGE is very likely already deleted, because the connector does not work, if it still exists.
-- To prevent errors I wrapped the deletion in a psql try catch
-- DECLARE
--     DC_TRANSPORT_STEP       VARCHAR2(250):= 'DC_TRANSPORT_STEP';
--     FK_MESSAGESTEP_MESSAGE           VARCHAR2(250):= 'FK_MESSAGESTEP_MESSAGE';
-- BEGIN
--     EXECUTE IMMEDIATE 'alter table ' || DC_TRANSPORT_STEP || ' drop constraint ' || FK_MESSAGESTEP_MESSAGE;
-- EXCEPTION
--     WHEN OTHERS THEN
--         -- "ORA-02443: Cannot drop constraint  - nonexistent constraint"
--         -- exceptions are ignored, anything else is raised
--         IF SQLCODE != -2443 THEN
--             RAISE;
--         END IF;
-- END;

-- #################### 3/6 TRANSFER & UPDATE data ####################

insert into DOMIBUS_CONNECTOR_SEQ_STORE select * from BKP_DC_SEQ_STORE;


update DOMIBUS_CONNECTOR_SEQ_STORE
set SEQ_NAME='DOMIBUS_CONNECTOR_MESSAGE.ID'
where SEQ_NAME = 'DOMIBUS_CONNECTOR_MESSAGES.ID';

update DOMIBUS_CONNECTOR_SEQ_STORE
set SEQ_NAME='DOMIBUS_CONNECTOR_EVIDENCE.ID'
where SEQ_NAME = 'DOMIBUS_CONNECTOR_EVIDENCES.ID';



insert into DC_PMODE_SET select * from BKP_PMODE_SET;
insert into DOMIBUS_CONNECTOR_ACTION select * from BKP_DC_ACTION;
insert into DOMIBUS_CONNECTOR_SERVICE select * from BKP_DC_SERVICE;
insert into DOMIBUS_CONNECTOR_MESSAGE select * from BKP_DC_MESSAGE;

-- update timestamps

update (select e.type, e.delivered_gw, e.updated, m.direction_source, m.direction_target, m.confirmed
        from DOMIBUS_CONNECTOR_EVIDENCE e
                 join DOMIBUS_CONNECTOR_MESSAGE m on m.id = e.MESSAGE_ID) x
set x.DELIVERED_GW=x.updated
where x.DELIVERED_GW is null
  and x.TYPE in ('DELIVERY', 'RETRIVAL', 'RELAY_REMMD_ACCEPTANCE')
  and x.updated > to_date('01.06.2020', 'dd.mm.yyyy')
  and x.DIRECTION_SOURCE = 'GATEWAY'
  and x.DIRECTION_TARGET = 'BACKEND'
  and x.confirmed is not null;


update (select e.type, e.delivered_nat, e.updated, m.direction_source, m.direction_target, m.confirmed
        from DOMIBUS_CONNECTOR_EVIDENCE e
                 join DOMIBUS_CONNECTOR_MESSAGE m on m.id = e.MESSAGE_ID) x
set x.DELIVERED_NAT=x.updated
where x.DELIVERED_NAT is null
  and x.TYPE in ('DELIVERY', 'RETRIVAL', 'RELAY_REMMD_ACCEPTANCE')
  and x.updated > to_date('01.06.2020', 'dd.mm.yyyy')
  and x.DIRECTION_SOURCE = 'BACKEND'
  and x.DIRECTION_TARGET = 'GATEWAY'
  and x.confirmed is not null;

-- #################### 4/6 DELETE temporary tables, frees fk names ####################

drop table BKP_DC_MESSAGE cascade constraints;
drop table BKP_DC_ACTION cascade constraints;
drop table BKP_DC_SERVICE cascade constraints;
drop table BKP_PMODE_SET cascade constraints;
drop table BKP_DC_SEQ_STORE cascade constraints;

-- #################### 5/6 ADD the constraints ####################

ALTER TABLE DOMIBUS_CONNECTOR_SERVICE ADD CONSTRAINT PK_DC_SERVICE PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_SERVICE ADD CONSTRAINT FK_SERVICE_PMODE_SET_ID FOREIGN KEY (FK_PMODE_SET) REFERENCES DC_PMODE_SET(ID);
ALTER TABLE DOMIBUS_CONNECTOR_BIGDATA ADD CONSTRAINT FK_DC_BIGDATA_MESSAGE_01 FOREIGN KEY (MESSAGE_ID) REFERENCES DOMIBUS_CONNECTOR_MESSAGE (ID);

ALTER TABLE DOMIBUS_CONNECTOR_ACTION
    ADD CONSTRAINT PK_DC_ACTION PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_ACTION
    ADD CONSTRAINT FK_DC_ACTION_PMODE_SET_01 FOREIGN KEY (FK_PMODE_SET) REFERENCES DC_PMODE_SET (ID);

ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE
    ADD CONSTRAINT PK_DC_MESSAGE PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE
    ADD CONSTRAINT UQ_DC_MSG_CON_MSG_ID UNIQUE (CONNECTOR_MESSAGE_ID);
ALTER TABLE DOMIBUS_CONNECTOR_MESSAGE
    ADD CONSTRAINT UQ_DC_MSG_BCK_MSG_ID UNIQUE (BACKEND_MESSAGE_ID);

ALTER TABLE DOMIBUS_CONNECTOR_SEQ_STORE
    ADD CONSTRAINT PK_DC_SEQ_STORE PRIMARY KEY (SEQ_NAME);

-- #################### 6/6 UPDATE Version ####################

update DC_DB_VERSION set TAG='V4.3';