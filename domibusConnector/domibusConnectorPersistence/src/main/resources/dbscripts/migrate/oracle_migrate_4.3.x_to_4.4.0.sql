-- *********************************************************************
-- Update Database Script - from domibusConnector 4.3 to 4.4
-- *********************************************************************
-- create new entity DC_KEYSTORE
-- extend entity DC_PMODE_SET by PMODES and FK_CONNECTORSTORE
--

-- #################### 1/6 RENAME tables that need to be recreated ####################


-- #################### 2/6 CREATE tables, structural changes ####################

alter table DOMIBUS_CONNECTOR_ACTION drop column PDF_REQUIRED;

ALTER TABLE DC_PMODE_SET ADD "PMODES" BLOB;
ALTER TABLE DC_PMODE_SET ADD "FK_CONNECTORSTORE" NUMBER(10);

ALTER TABLE DOMIBUS_CONNECTOR_PARTY ADD "ROLE_TYPE" VARCHAR2(50 CHAR);

CREATE TABLE  "DC_KEYSTORE"
(
	"ID" NUMBER(10) NOT NULL,
	"UUID" VARCHAR2(255 CHAR) NOT NULL,
	"KEYSTORE" BLOB NOT NULL,
	"PASSWORD" VARCHAR2(1024 BYTE),
	"UPLOADED" TIMESTAMP NOT NULL,
	"DESCRIPTION" VARCHAR2(512 CHAR),
	"TYPE" VARCHAR2(50 CHAR)
)
;

ALTER TABLE  "DC_KEYSTORE" 
 ADD CONSTRAINT "PK_DC_KEYSTORE"
	PRIMARY KEY ("ID") 
 USING INDEX
;

ALTER TABLE  "DC_KEYSTORE" 
 ADD CONSTRAINT "UQ_DC_KEYSTORE" UNIQUE ("UUID") 
 USING INDEX
;


-- #################### 3/6 TRANSFER & UPDATE data ####################


-- #################### 4/6 DELETE temporary tables, frees fk names ####################


-- #################### 5/6 ADD the constraints ####################


ALTER TABLE  "DC_PMODE_SET" 
 ADD CONSTRAINT "FK_DC_PMODE_SET_02"
	FOREIGN KEY ("FK_CONNECTORSTORE") REFERENCES  "DC_KEYSTORE" ("ID")
;

-- #################### 6/6 UPDATE Version ####################

update DC_DB_VERSION set TAG='V4.4';

