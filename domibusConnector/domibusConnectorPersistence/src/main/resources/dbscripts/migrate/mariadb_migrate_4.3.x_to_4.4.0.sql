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

CREATE TABLE `DC_KEYSTORE`
(
	`ID` DECIMAL(10,0) NOT NULL,
	`KEYSTORE` BLOB NOT NULL,
	`PASSWORD` VARCHAR(1024),
	`PW_SALT` VARCHAR(512),
	`UPLOADED` TIMESTAMP NOT NULL,
	CONSTRAINT `PK_DC_CONNECTORSTORE` PRIMARY KEY (`ID` ASC)
)

;

-- #################### 3/6 TRANSFER & UPDATE data ####################


-- #################### 4/6 DELETE temporary tables, frees fk names ####################


-- #################### 5/6 ADD the constraints ####################

ALTER TABLE `DC_PMODE_SET` 
 ADD CONSTRAINT `FK_DC_PMODE_SET_02`
	FOREIGN KEY (`FK_CONNECTORSTORE`) REFERENCES `DC_KEYSTORE` (`ID`) ON DELETE No Action ON UPDATE No Action
;

-- #################### 6/6 UPDATE Version ####################

update DC_DB_VERSION set TAG='V4.4';



SET FOREIGN_KEY_CHECKS = 1;