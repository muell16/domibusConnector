-- *********************************************************************
-- Update Database Script - from domibusConnector 4.1 to 4.1.8
-- *********************************************************************
--
--
--


START TRANSACTION;
ALTER TABLE DOMIBUS_CONNECTOR_PROPERTY ADD COLUMN ID int NOT NULL AUTO_INCREMENT UNIQUE;

UPDATE DOMIBUS_CONNECTOR_SEQ_STORE SET SEQ_VALUE = (SELECT MAX(ID) FROM DOMIBUS_CONNECTOR_PROPERTY)
WHERE SEQ_NAME = 'DOMIBUS_CONNECTOR_PROPERTY.ID';

ALTER TABLE DOMIBUS_CONNECTOR_PROPERTY MODIFY COLUMN ID int NOT NULL;

COMMIT;