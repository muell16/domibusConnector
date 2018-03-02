--liquibase formatted sql

--changeset StephanSpindler:initialBackendData_1
----------------------- Values for DOMIBUS_CONNECTOR_BACKEND_INFO ------------------------------

INSERT INTO DOMIBUS_CONNECTOR_BACKEND_INFO (ID, BACKEND_NAME, BACKEND_KEY_ALIAS, BACKEND_KEY_PASS, BACKEND_SERVICE_TYPE, BACKEND_DESCRIPTION, BACKEND_PUSH_ADDRESS) VALUES (1, 'CN=bob', 'bob', '', 'EPO', 'a epo backend', null);