-- *********************************************************************
-- Update Database Script - from domibusConnector 4.1 to 4.1.8
-- *********************************************************************
--
--
--
--

START TRANSACTION;
CREATE SEQUENCE dc_default_seq;
ALTER TABLE DOMIBUS_CONNECTOR_PROPERTY DROP PRIMARY KEY DROP INDEX;
ALTER TABLE DOMIBUS_CONNECTOR_PROPERTY ADD COLUMN ID DECIMAL(10,0) NOT NULL DEFAULT dc_default_seq.NEXTVAL;

UPDATE DOMIBUS_CONNECTOR_SEQ_STORE SET SEQ_VALUE = (SELECT MAX(ID) FROM DOMIBUS_CONNECTOR_PROPERTY)
WHERE SEQ_NAME = 'DOMIBUS_CONNECTOR_PROPERTY.ID';

ALTER TABLE DOMIBUS_CONNECTOR_PROPERTY MODIFY COLUMN ID DECIMAL(10,0) NOT NULL;

DROP SEQUENCE dc_default_seq;

COMMIT;