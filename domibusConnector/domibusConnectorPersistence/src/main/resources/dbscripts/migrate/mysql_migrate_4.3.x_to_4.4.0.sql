-- *********************************************************************
-- Update Database Script - from domibusConnector 4.3 to 4.4
-- *********************************************************************

SET FOREIGN_KEY_CHECKS=0;
-- requires MySQL >= 5.6.6, default since MySQL 8.0.2
SET EXPLICIT_DEFAULTS_FOR_TIMESTAMP = ON;
-- fixes UUID bug: https://bugs.mysql.com/bug.php?id=101820es UUID bug
-- also see: https://stackoverflow.com/questions/36296558/mysql-generating-duplicate-uuid
SET names utf8;

-- #################### 1/6 RENAME tables that need to be recreated ####################



-- #################### 2/6 CREATE tables, structural changes ####################

alter table DOMIBUS_CONNECTOR_ACTION drop column PDF_REQUIRED;

ALTER TABLE DC_PMODE_SET ADD COLUMN PMODES BLOB;
ALTER TABLE DC_PMODE_SET ADD COLUMN FK_CONNECTORSTORE NUMBER(10);

ALTER TABLE DOMIBUS_CONNECTOR_PARTY ADD COLUMN ROLE_TYPE VARCHAR(50);

CREATE TABLE `DC_KEYSTORE`
(
	`ID` DECIMAL(10,0) NOT NULL,
	`UUID` VARCHAR(255) NOT NULL,
	`KEYSTORE` BLOB NOT NULL,
	`PASSWORD` VARCHAR(1024),
	`UPLOADED` TIMESTAMP(6) NOT NULL,
	`DESCRIPTION` VARCHAR(512),
	`TYPE` VARCHAR(50),
	CONSTRAINT `PK_DC_KEYSTORE` PRIMARY KEY (`ID` ASC)
)

;

ALTER TABLE `DC_KEYSTORE` 
 ADD CONSTRAINT `UQ_DC_KEYSTORE` UNIQUE (`UUID` ASC)
;

-- if not done with last upgrade 4.2.x to 4.3.x
-- REMOVE FK and replace with CONNECTOR_MESSAGE_ID
ALTER TABLE "DOMIBUS_CONNECTOR_BIGDATA"
    ADD "CONNECTOR_MESSAGE_ID" VARCHAR2(255 CHAR);

ALTER TABLE "DOMIBUS_CONNECTOR_BIGDATA"
    DROP COLUMN "MESSAGE_ID";

-- #################### 3/6 TRANSFER & UPDATE data ####################

-- Set rejected timestamp to all messages not rejected or confirmed and older than 5 days.
-- Number of days may be changed if the 5 at the end of the query is changed.
UPDATE DOMIBUS_CONNECTOR_MESSAGE SET REJECTED = curdate() WHERE REJECTED IS NULL AND CONFIRMED IS NULL AND CONNECTOR_MESSAGE_ID IS NULL 
AND CREATED < DATE_ADD(curdate(), INTERVAL -5 DAY);

-- Set a CONNECTOR_MESSAGE_ID to every message that does not have one yet by using the technical ID.
UPDATE DOMIBUS_CONNECTOR_MESSAGE SET CONNECTOR_MESSAGE_ID=ID WHERE CONNECTOR_MESSAGE_ID is null;

-- #################### 4/6 DELETE temporary tables, frees fk names ####################


-- #################### 5/6 ADD the constraints ####################

ALTER TABLE `DC_PMODE_SET` 
 ADD CONSTRAINT `FK_DC_PMODE_SET_02`
	FOREIGN KEY (`FK_CONNECTORSTORE`) REFERENCES `DC_KEYSTORE` (`ID`) ON DELETE No Action ON UPDATE No Action
;

-- #################### 6/6 UPDATE Version ####################

update DC_DB_VERSION set TAG='V4.4';



SET FOREIGN_KEY_CHECKS = 1;