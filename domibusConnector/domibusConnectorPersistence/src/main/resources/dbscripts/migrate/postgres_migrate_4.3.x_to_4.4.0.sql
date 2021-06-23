-- *********************************************************************
-- Update Database Script - from domibusConnector 4.3 to 4.4
-- *********************************************************************


-- #################### 1/6 RENAME tables that need to be recreated ####################



-- #################### 2/6 CREATE tables, structural changes ####################

alter table DOMIBUS_CONNECTOR_ACTION drop column PDF_REQUIRED;

ALTER TABLE DC_PMODE_SET ADD COLUMN PMODES bytea;
ALTER TABLE DC_PMODE_SET ADD COLUMN FK_CONNECTORSTORE numeric(10);

CREATE TABLE DC_KEYSTORE
(
	ID numeric(10) NOT NULL,
	KEYSTORE bytea NOT NULL,
	PASSWORD varchar(1024),
	PW_SALT varchar(512),
	UPLOADED timestamp without time zone NOT NULL
)
;

ALTER TABLE DC_KEYSTORE ADD CONSTRAINT PK_DC_CONNECTORSTORE
	PRIMARY KEY (ID)
;

-- #################### 3/6 TRANSFER & UPDATE data ####################


-- #################### 4/6 DELETE temporary tables, frees fk names ####################


-- #################### 5/6 ADD the constraints ####################

ALTER TABLE DC_PMODE_SET ADD CONSTRAINT FK_DC_PMODE_SET_02
	FOREIGN KEY (FK_CONNECTORSTORE) REFERENCES DC_KEYSTORE (ID) ON DELETE No Action ON UPDATE No Action
;

-- #################### 6/6 UPDATE Version ####################

update DC_DB_VERSION set TAG='V4.4';

