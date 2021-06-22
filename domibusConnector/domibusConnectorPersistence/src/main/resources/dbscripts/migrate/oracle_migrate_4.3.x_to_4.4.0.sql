-- *********************************************************************
-- Update Database Script - from domibusConnector 4.3 to 4.4
-- *********************************************************************
-- change message timestamps in DOMIBUS_CONNECTOR_EVIDENCE and DOMIBUS_CONNECTOR_MESSAGE
-- remove not null constraint on service_type in DOMIBUS_CONNECTOR_SERVICE
--

-- #################### 1/6 RENAME tables that need to be recreated ####################


-- #################### 2/6 CREATE tables, structural changes ####################

alter table DOMIBUS_CONNECTOR_ACTION drop column PDF_REQUIRED;


-- #################### 3/6 TRANSFER & UPDATE data ####################


-- #################### 4/6 DELETE temporary tables, frees fk names ####################


-- #################### 5/6 ADD the constraints ####################

-- #################### 6/6 UPDATE Version ####################

