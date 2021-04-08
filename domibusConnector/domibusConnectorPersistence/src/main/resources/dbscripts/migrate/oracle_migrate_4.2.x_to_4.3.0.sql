-- *********************************************************************
-- Update Database Script - from domibusConnector 4.1 to 4.2
-- *********************************************************************
-- adds quartz tables
--

-- 1/X) delete foreign key constraint
-- rename DC_TRANSPORT_STEP to bkp_dc_transtep;
-- create table DC_TRANSPORT_STEP
-- (
--     ID DECIMAL(10,0) not null,
--     CONNECTOR_MESSAGE_ID VARCHAR2(255) not null,
--     LINK_PARTNER_NAME VARCHAR2(255) not null,
--     ATTEMPT INT not null,
--     TRANSPORT_ID VARCHAR2(255),
--     TRANSPORT_SYSTEM_MESSAGE_ID VARCHAR2(255),
--     REMOTE_MESSAGE_ID VARCHAR2(255),
--     CREATED TIMESTAMP,
--     constraint PK_DC_TRANSPORT_STEP
--         primary key (ID)
-- );

alter table DC_TRANSPORT_STEP rename column CONNECTOR_MESSAGE_ID to bkp_cmid;
alter table DC_TRANSPORT_STEP add CONNECTOR_MESSAGE_ID VARCHAR2(255);
update DC_TRANSPORT_STEP set CONNECTOR_MESSAGE_ID=to_char(bkp.cmid);
alter table DC_TRANSPORT_STEP drop column bkp_cmid;
alter table DC_TRANSPORT_STEP modify ("CONNECTOR_MESSAGE_ID" not null);
alter table DC_TRANSPORT_STEP drop constraint FK_MESSAGESTEP_MESSAGE;

-- 2/X) update name of jpa sequence
update DOMIBUS_CONNECTOR_SEQ_STORE
set SEQ_NAME='DOMIBUS_CONNECTOR_MESSAGE.ID'
where SEQ_NAME = 'DOMIBUS_CONNECTOR_MESSAGES.ID';

update DOMIBUS_CONNECTOR_SEQ_STORE
set SEQ_NAME='DOMIBUS_CONNECTOR_EVIDENCE.ID'
where SEQ_NAME = 'DOMIBUS_CONNECTOR_EVIDENCES.ID';

-- 3/X) remove not null constraint on service type
rename DOMIBUS_CONNECTOR_SERVICE to BKP_DC_SERVICE;

CREATE TABLE DOMIBUS_CONNECTOR_SERVICE
(
    ID           DECIMAL(10, 0) NOT NULL,
    FK_PMODE_SET DECIMAL(10, 0) NOT NULL,
    SERVICE      VARCHAR2(255)  NOT NULL,
    SERVICE_TYPE VARCHAR2(255)
);
ALTER TABLE DOMIBUS_CONNECTOR_SERVICE ADD CONSTRAINT PK_DOMIBUS_CONNECTOR_SERVICE PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_SERVICE ADD CONSTRAINT FK_SERVICE_PMODE_SET_ID FOREIGN KEY (FK_PMODE_SET) REFERENCES DC_PMODE_SET(ID);

insert into DOMIBUS_CONNECTOR_SERVICE select * from BKP_DC_SERVICE;