--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: db/changelog/install/initial-4.0.xml
--  Ran at: 12.04.18 09:22
--  Against: domibus@localhost@jdbc:mysql://localhost/connectornew
--  Liquibase version: 3.5.3
--  *********************************************************************

--  Create Database Lock Table
CREATE TABLE connectornew.DATABASECHANGELOGLOCK (ID INT NOT NULL, LOCKED BIT(1) NOT NULL, LOCKGRANTED datetime NULL, LOCKEDBY VARCHAR(255) NULL, CONSTRAINT PK_DATABASECHANGELOGLOCK PRIMARY KEY (ID));

--  Initialize Database Lock Table
DELETE FROM connectornew.DATABASECHANGELOGLOCK;

INSERT INTO connectornew.DATABASECHANGELOGLOCK (ID, LOCKED) VALUES (1, 0);


--  Create Database Change Log Table
CREATE TABLE connectornew.DATABASECHANGELOG (ID VARCHAR(255) NOT NULL, AUTHOR VARCHAR(255) NOT NULL, FILENAME VARCHAR(255) NOT NULL, DATEEXECUTED datetime NOT NULL, ORDEREXECUTED INT NOT NULL, EXECTYPE VARCHAR(10) NOT NULL, MD5SUM VARCHAR(35) NULL, DESCRIPTION VARCHAR(255) NULL, COMMENTS VARCHAR(255) NULL, TAG VARCHAR(255) NULL, LIQUIBASE VARCHAR(20) NULL, CONTEXTS VARCHAR(255) NULL, LABELS VARCHAR(255) NULL, DEPLOYMENT_ID VARCHAR(10) NULL);

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_1::StephanSpindler
CREATE TABLE connectornew.DOMIBUS_CONNECTOR_SEQ_STORE (SEQ_NAME VARCHAR(255) NOT NULL, SEQ_VALUE numeric(10, 0) NOT NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_1', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 1, '7:ef8cb8392b6a12b87ca0dd433de4cca7', 'createTable tableName=DOMIBUS_CONNECTOR_SEQ_STORE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_1_primary_key_for_seq_store::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_SEQ_STORE ADD PRIMARY KEY (SEQ_NAME);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_1_primary_key_for_seq_store', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 2, '7:3c70d1f8396c63018c2a93187603168c', 'addPrimaryKey constraintName=PK_DOMIBUS_CONNECTOR_SEQ_STORE, tableName=DOMIBUS_CONNECTOR_SEQ_STORE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_2::StephanSpindler
CREATE TABLE connectornew.DOMIBUS_CONNECTOR_SERVICE (SERVICE VARCHAR(255) NOT NULL, SERVICE_TYPE VARCHAR(512) NOT NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_2', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 3, '7:b268693155debb7626af011d9cd7b6c0', 'createTable tableName=DOMIBUS_CONNECTOR_SERVICE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_2_primary_key_for_connector_service::StephanSpindler
--  Creating Primary Key for DOMIBUS_CONNECTOR_SERVICE
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_SERVICE ADD PRIMARY KEY (SERVICE);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_2_primary_key_for_connector_service', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 4, '7:3ec08b12032ca0cf9e89db044e66e8c1', 'addPrimaryKey constraintName=PK_DOMIBUS_CONNECTOR_SERVICE, tableName=DOMIBUS_CONNECTOR_SERVICE', 'Creating Primary Key for DOMIBUS_CONNECTOR_SERVICE', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_3::StephanSpindler
CREATE TABLE connectornew.DOMIBUS_CONNECTOR_PARTY (PARTY_ID VARCHAR(255) NOT NULL, ROLE VARCHAR(255) NOT NULL, PARTY_ID_TYPE VARCHAR(512) NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_3', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 5, '7:089f4d99a33fb049648da24da78e0637', 'createTable tableName=DOMIBUS_CONNECTOR_PARTY', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_3_primary_key::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_PARTY ADD PRIMARY KEY (PARTY_ID, ROLE);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_3_primary_key', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 6, '7:81497e82994f11ae105a249210914ce2', 'addPrimaryKey constraintName=PK_DOMIBUS_CONNECTOR_PARTY, tableName=DOMIBUS_CONNECTOR_PARTY', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_4::StephanSpindler
CREATE TABLE connectornew.DOMIBUS_CONNECTOR_MSG_ERROR (ID DECIMAL(10, 0) NOT NULL, MESSAGE_ID DECIMAL(10, 0) NOT NULL, ERROR_MESSAGE VARCHAR(512) NOT NULL, DETAILED_TEXT LONGTEXT NULL, ERROR_SOURCE LONGTEXT NULL, CREATED datetime NOT NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_4', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 7, '7:de0f207c962adb98ee5bbe7f8d257baf', 'createTable tableName=DOMIBUS_CONNECTOR_MSG_ERROR', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_4_primary_key::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_MSG_ERROR ADD PRIMARY KEY (ID);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_4_primary_key', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 8, '7:daa0ac5929965ede105df3a3f98a4bac', 'addPrimaryKey constraintName=PK_MSG_ERROR, tableName=DOMIBUS_CONNECTOR_MSG_ERROR', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_5::StephanSpindler
CREATE TABLE connectornew.DOMIBUS_CONNECTOR_MSG_CONT (ID DECIMAL(10, 0) NOT NULL, MESSAGE_ID DECIMAL(10, 0) NOT NULL, CONTENT_TYPE VARCHAR(255) NULL, CONTENT LONGBLOB NULL, CHECKSUM LONGTEXT NULL, CREATED datetime NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_5', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 9, '7:f0e3a8f0bf54f6ade1a69b65bc8a5888', 'createTable tableName=DOMIBUS_CONNECTOR_MSG_CONT', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_5_primary_key::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_MSG_CONT ADD PRIMARY KEY (ID);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_5_primary_key', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 10, '7:3525496d2334c790f3b36ef273c79500', 'addPrimaryKey constraintName=PK_DOMIBUS_CONNECTOR_MSG__01, tableName=DOMIBUS_CONNECTOR_MSG_CONT', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_6::StephanSpindler
CREATE TABLE connectornew.DOMIBUS_CONNECTOR_BIGDATA (ID VARCHAR(255) NOT NULL, CHECKSUM LONGTEXT NULL, CREATED datetime NULL, MESSAGE_ID DECIMAL(10, 0) NULL, LAST_ACCESS datetime NULL, NAME LONGTEXT NULL, CONTENT LONGBLOB NULL, MIMETYPE VARCHAR(255) NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_6', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 11, '7:7b83c53d21d588160549fce98b9a702d', 'createTable tableName=DOMIBUS_CONNECTOR_BIGDATA', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_6_primary_key::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_BIGDATA ADD PRIMARY KEY (ID);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_6_primary_key', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 12, '7:b62590318c3dead4ef843aefedada329', 'addPrimaryKey tableName=DOMIBUS_CONNECTOR_BIGDATA', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_7::StephanSpindler
CREATE TABLE connectornew.DOMIBUS_CONNECTOR_MESSAGE_INFO (ID DECIMAL(10, 0) NOT NULL, MESSAGE_ID DECIMAL(10, 0) NOT NULL, FROM_PARTY_ID VARCHAR(255) NULL, FROM_PARTY_ROLE VARCHAR(255) NULL, TO_PARTY_ID VARCHAR(255) NULL, TO_PARTY_ROLE VARCHAR(255) NULL, ORIGINAL_SENDER VARCHAR(255) NULL, FINAL_RECIPIENT VARCHAR(255) NULL, SERVICE VARCHAR(255) NULL, ACTION VARCHAR(255) NULL, CREATED datetime NOT NULL, UPDATED datetime NOT NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_7', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 13, '7:eca1aaf73203279734e0cb3854a40dd6', 'createTable tableName=DOMIBUS_CONNECTOR_MESSAGE_INFO', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_7_primary_key::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_MESSAGE_INFO ADD PRIMARY KEY (ID);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_7_primary_key', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 14, '7:99f8a96cb81260b5e616809b7dbb00cc', 'addPrimaryKey constraintName=PK_CONNECTOR_MESSAGE_INFO, tableName=DOMIBUS_CONNECTOR_MESSAGE_INFO', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_7_message_id_unique::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT MSG_INFO_UNIQ_MSG_ID UNIQUE (MESSAGE_ID);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_7_message_id_unique', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 15, '7:e20525c0b67e6a8b43daa95a14b8af69', 'addUniqueConstraint constraintName=MSG_INFO_UNIQ_MSG_ID, tableName=DOMIBUS_CONNECTOR_MESSAGE_INFO', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_8::StephanSpindler
CREATE TABLE connectornew.DOMIBUS_CONNECTOR_MESSAGE (ID DECIMAL(10, 0) NOT NULL, EBMS_MESSAGE_ID VARCHAR(255) NULL, BACKEND_MESSAGE_ID VARCHAR(255) NULL, BACKEND_NAME VARCHAR(255) NULL, CONNECTOR_MESSAGE_ID VARCHAR(255) NULL, CONVERSATION_ID VARCHAR(255) NULL, DIRECTION VARCHAR(10) NULL, HASH_VALUE LONGTEXT NULL, CONFIRMED datetime NULL, REJECTED datetime NULL, DELIVERED_BACKEND datetime NULL, DELIVERED_GW datetime NULL, UPDATED datetime NULL, CREATED datetime NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_8', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 16, '7:8a1a5dfbcbc001fe72565df09bf8a4d0', 'createTable tableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_8_primary_key::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_MESSAGE ADD PRIMARY KEY (ID);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_8_primary_key', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 17, '7:745d4a6137ea5ff535a08864a96eeecf', 'addPrimaryKey constraintName=PK_DOMIBUS_CONNECTOR_MESSAGE, tableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_8_unique_connector_message_id::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_MESSAGE ADD CONSTRAINT UNIQUE_CONNECTOR_MESSAGE_ID UNIQUE (CONNECTOR_MESSAGE_ID);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_8_unique_connector_message_id', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 18, '7:253a9b998f9541cb5180ef56832d5b50', 'addUniqueConstraint constraintName=UNIQUE_CONNECTOR_MESSAGE_ID, tableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_8_unique_ebms_id::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_MESSAGE ADD CONSTRAINT UQ_DOMIBUS_CONNE_EBMS_MESSAGE UNIQUE (EBMS_MESSAGE_ID);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_8_unique_ebms_id', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 19, '7:11e839c979fed04b7e831960ce9569a9', 'addUniqueConstraint constraintName=UQ_DOMIBUS_CONNE_EBMS_MESSAGE, tableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_8_unique_backend_message_id::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_MESSAGE ADD CONSTRAINT UQ_DOMIBUS_CONNE_NAT_MESSAGE_ UNIQUE (BACKEND_MESSAGE_ID);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_8_unique_backend_message_id', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 20, '7:be24b37dfe5644309be893d916775c43', 'addUniqueConstraint constraintName=UQ_DOMIBUS_CONNE_NAT_MESSAGE_, tableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_9::StephanSpindler
CREATE TABLE connectornew.DOMIBUS_CONNECTOR_EVIDENCE (ID DECIMAL(10, 0) NOT NULL, MESSAGE_ID DECIMAL(10, 0) NOT NULL, TYPE VARCHAR(255) NULL, EVIDENCE LONGTEXT NULL, DELIVERED_NAT datetime NULL, DELIVERED_GW datetime NULL, UPDATED datetime NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_9', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 21, '7:c420433995410790cdebdaa4e3e87a7e', 'createTable tableName=DOMIBUS_CONNECTOR_EVIDENCE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_9_primary_key::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_EVIDENCE ADD PRIMARY KEY (ID);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_9_primary_key', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 22, '7:9d449e4665fcdb39bb818f2d741ebda8', 'addPrimaryKey constraintName=PK_DOMIBUS_CONNECTOR_EVIDENCE, tableName=DOMIBUS_CONNECTOR_EVIDENCE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_10::StephanSpindler
--  Create Content Type Table
CREATE TABLE connectornew.DOMIBUS_CONNECTOR_CONT_TYPE (MESSAGE_CONTENT_TYPE VARCHAR(255) NOT NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_10', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 23, '7:e3afb29f162a824dd4dee01c440cb30c', 'createTable tableName=DOMIBUS_CONNECTOR_CONT_TYPE', 'Create Content Type Table', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_10_primary_key::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_CONT_TYPE ADD PRIMARY KEY (MESSAGE_CONTENT_TYPE);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_10_primary_key', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 24, '7:7161f1d63c7cad88d8c5bc387ca61edd', 'addPrimaryKey constraintName=PK_DOMIBUS_CONN_02, tableName=DOMIBUS_CONNECTOR_CONT_TYPE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_10_1::StephanSpindler
CREATE TABLE connectornew.DOMIBUS_CONNECTOR_ACTION (ACTION VARCHAR(255) NOT NULL, PDF_REQUIRED BIT(1) DEFAULT 0 NOT NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_10_1', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 25, '7:d2c5c34331ea2373cddfab1506196fb0', 'createTable tableName=DOMIBUS_CONNECTOR_ACTION', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_10_1_primary_key::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_ACTION ADD PRIMARY KEY (ACTION);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_10_1_primary_key', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 26, '7:a3b013a6e03c1b7f051bc6491947cbe8', 'addPrimaryKey constraintName=PK_DOMIBUS_CONNECTOR_ACTION, tableName=DOMIBUS_CONNECTOR_ACTION', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_11_backend_info::StephanSpindler
CREATE TABLE connectornew.DOMIBUS_CONNECTOR_BACKEND_INFO (ID DECIMAL(10, 0) NOT NULL, BACKEND_NAME VARCHAR(255) NOT NULL, BACKEND_KEY_ALIAS VARCHAR(255) NOT NULL, BACKEND_KEY_PASS VARCHAR(255) NULL, BACKEND_SERVICE_TYPE VARCHAR(512) NULL, BACKEND_ENABLED BIT(1) DEFAULT 1 NULL, BACKEND_DEFAULT BIT(1) DEFAULT 0 NULL, BACKEND_DESCRIPTION LONGTEXT NULL, BACKEND_PUSH_ADDRESS LONGTEXT NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_11_backend_info', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 27, '7:fdf86bbfa87c284e49c8f73fd8083d64', 'createTable tableName=DOMIBUS_CONNECTOR_BACKEND_INFO', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_11_primary_key_backend_info::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_BACKEND_INFO ADD PRIMARY KEY (ID);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_11_primary_key_backend_info', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 28, '7:c531fabf667ba185c74b4b0898183f3f', 'addPrimaryKey constraintName=PK_DOMIBUS_CONNECTOR_BACK_01, tableName=DOMIBUS_CONNECTOR_BACKEND_INFO', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_11_backend_info_unique_backend_name::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_BACKEND_INFO ADD CONSTRAINT UN_DOMIBUS_CONNECTOR_BACK_01 UNIQUE (BACKEND_NAME);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_11_backend_info_unique_backend_name', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 29, '7:e1b4cf49399f86d83ac0d5b8fc21bfa9', 'addUniqueConstraint constraintName=UN_DOMIBUS_CONNECTOR_BACK_01, tableName=DOMIBUS_CONNECTOR_BACKEND_INFO', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_11_backend_info_unique_key_alias::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_BACKEND_INFO ADD CONSTRAINT UN_DOMIBUS_CONNECTOR_BACK_02 UNIQUE (BACKEND_KEY_ALIAS);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_11_backend_info_unique_key_alias', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 30, '7:6b213ba445f6111d1d98a651ce68baf3', 'addUniqueConstraint constraintName=UN_DOMIBUS_CONNECTOR_BACK_02, tableName=DOMIBUS_CONNECTOR_BACKEND_INFO', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_12_connector_to_service_rel::StephanSpindler
CREATE TABLE connectornew.DOMIBUS_CONNECTOR_BACK_2_S (DOMIBUS_CONNECTOR_SERVICE_ID VARCHAR(255) NOT NULL, DOMIBUS_CONNECTOR_BACKEND_ID DECIMAL(10, 0) NOT NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_12_connector_to_service_rel', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 31, '7:b71e326c1ff94c5fcb74345efc55d694', 'createTable tableName=DOMIBUS_CONNECTOR_BACK_2_S', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_12_connector_to_service_rel_primary_key::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_BACK_2_S ADD PRIMARY KEY (DOMIBUS_CONNECTOR_SERVICE_ID, DOMIBUS_CONNECTOR_BACKEND_ID);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_12_connector_to_service_rel_primary_key', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 32, '7:04dbbfcbaada5f2946fca49a6f3f33ff', 'addPrimaryKey constraintName=PK_DOMIBUS_CONN_01, tableName=DOMIBUS_CONNECTOR_BACK_2_S', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_13::StephanSpindler
--  Foreign Key between Message error and Message
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_MSG_ERROR ADD CONSTRAINT FK_DOMIBUS_CONNECTOR_MSG_ERROR FOREIGN KEY (MESSAGE_ID) REFERENCES connectornew.DOMIBUS_CONNECTOR_MESSAGE (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_13', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 33, '7:df71593e1116807cd89bc97b3b6269f9', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_MSG_ERROR, constraintName=FK_DOMIBUS_CONNECTOR_MSG_ERROR, referencedTableName=DOMIBUS_CONNECTOR_MESSAGE', 'Foreign Key between Message error and Message', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_14::StephanSpindler
--  Foreign key between message content and message
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_MSG_CONT ADD CONSTRAINT FK_DOMIBUS_CONN_DOMIBUS_CON_04 FOREIGN KEY (MESSAGE_ID) REFERENCES connectornew.DOMIBUS_CONNECTOR_MESSAGE (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_14', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 34, '7:79940f7647fa5b32ba163f93ec7dc443', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_MSG_CONT, constraintName=FK_DOMIBUS_CONN_DOMIBUS_CON_04, referencedTableName=DOMIBUS_CONNECTOR_MESSAGE', 'Foreign key between message content and message', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');


--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_15::StephanSpindler
--  Foreign key between message info and message
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_MSGI_ACTION FOREIGN KEY (ACTION) REFERENCES connectornew.DOMIBUS_CONNECTOR_ACTION (ACTION) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_15', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 36, '7:453ab5dce64c3c230cdb60a1ccfbdc92', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_MESSAGE_INFO, constraintName=FK_MSGI_ACTION, referencedTableName=DOMIBUS_CONNECTOR_ACTION', 'Foreign key between message info and message', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_16::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_MSGI_SERVICE FOREIGN KEY (SERVICE) REFERENCES connectornew.DOMIBUS_CONNECTOR_SERVICE (service) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_16', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 37, '7:41e846ad4a8eca879465fa8198b0c9ca', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_MESSAGE_INFO, constraintName=FK_MSGI_SERVICE, referencedTableName=DOMIBUS_CONNECTOR_SERVICE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_17::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_MSGI_FROM_PARTY FOREIGN KEY (FROM_PARTY_ID, FROM_PARTY_ROLE) REFERENCES connectornew.DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_17', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 38, '7:d77fccb07745b574ca4ca6677b5022cf', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_MESSAGE_INFO, constraintName=FK_MSGI_FROM_PARTY, referencedTableName=DOMIBUS_CONNECTOR_PARTY', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_18::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_MSGI_TO_PARTY FOREIGN KEY (TO_PARTY_ID, TO_PARTY_ROLE) REFERENCES connectornew.DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_18', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 39, '7:98fdcade76edbde1efea922f6517811e', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_MESSAGE_INFO, constraintName=FK_MSGI_TO_PARTY, referencedTableName=DOMIBUS_CONNECTOR_PARTY', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_19::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_MSGI_MSGID FOREIGN KEY (MESSAGE_ID) REFERENCES connectornew.DOMIBUS_CONNECTOR_MESSAGE (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_19', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 40, '7:589b37dcc0c9147a8994f2d6e449bf96', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_MESSAGE_INFO, constraintName=FK_MSGI_MSGID, referencedTableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_20::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_EVIDENCE ADD CONSTRAINT FK_DOMIBUS_CONNECTOR_EVIDENCES FOREIGN KEY (MESSAGE_ID) REFERENCES connectornew.DOMIBUS_CONNECTOR_MESSAGE (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_20', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 41, '7:78539cc81c1e1b40db24b041e763ece6', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_EVIDENCE, constraintName=FK_DOMIBUS_CONNECTOR_EVIDENCES, referencedTableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_21::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_BACK_2_S ADD CONSTRAINT FK_DOMIBUS_CONN_DOMIBUS_CON_01 FOREIGN KEY (DOMIBUS_CONNECTOR_BACKEND_ID) REFERENCES connectornew.DOMIBUS_CONNECTOR_BACKEND_INFO (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_21', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 42, '7:36227a8cd1e0cdb690047bac2a6c5a21', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_BACK_2_S, constraintName=FK_DOMIBUS_CONN_DOMIBUS_CON_01, referencedTableName=DOMIBUS_CONNECTOR_BACKEND_INFO', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_22::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_BACK_2_S ADD CONSTRAINT FK_DOMIBUS_CONN_DOMIBUS_CON_02 FOREIGN KEY (DOMIBUS_CONNECTOR_SERVICE_ID) REFERENCES connectornew.DOMIBUS_CONNECTOR_SERVICE (SERVICE) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_22', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 43, '7:49f051a349b138a5e072780022152398', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_BACK_2_S, constraintName=FK_DOMIBUS_CONN_DOMIBUS_CON_02, referencedTableName=DOMIBUS_CONNECTOR_SERVICE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::changelog-domibus-4.0_23::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_CONNECTOR_BIGDATA ADD CONSTRAINT FK_DOMIBUS_CONNECTOR_BIGDATA FOREIGN KEY (MESSAGE_ID) REFERENCES connectornew.DOMIBUS_CONNECTOR_MESSAGE (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-domibus-4.0_23', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 44, '7:34f4a9d6269a7dfa4e99b88a0fa71ee7', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_BIGDATA, constraintName=FK_DOMIBUS_CONNECTOR_BIGDATA, referencedTableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset /db/changelog/install/initial-changelog-domibus.xml::tag-db-domibus-4.0::StephanSpindler
INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID, TAG) VALUES ('tag-db-domibus-4.0', 'StephanSpindler', '/db/changelog/install/initial-changelog-domibus.xml', NOW(), 45, '7:9154cc53e107d3186ebc05f34ddc6844', 'tagDatabase', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427', 'DOMIBUS_DB_V4.0');

--  Changeset db/changelog/install/initial-changelog-webadmin.xml::changelog-1.0_create_tbl_webadmin_user_pk::StephanSpindler
CREATE TABLE connectornew.DOMIBUS_WEBADMIN_USER (USERNAME VARCHAR(512) NOT NULL, PASSWORD VARCHAR(512) NOT NULL, SALT VARCHAR(255) NOT NULL, ROLE VARCHAR(255) NOT NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-1.0_create_tbl_webadmin_user_pk', 'StephanSpindler', 'db/changelog/install/initial-changelog-webadmin.xml', NOW(), 46, '7:3963e11ce59f1979015828d8b06a40f7', 'createTable tableName=DOMIBUS_WEBADMIN_USER', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-webadmin.xml::changelog-1.0_create_webadmin_user_pk::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_WEBADMIN_USER ADD PRIMARY KEY (USERNAME);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-1.0_create_webadmin_user_pk', 'StephanSpindler', 'db/changelog/install/initial-changelog-webadmin.xml', NOW(), 47, '7:ffe8fb04b3fe31cf2c7d0eb0b32eb8ab', 'addPrimaryKey constraintName=PK_WEBADMIN_USER, tableName=DOMIBUS_WEBADMIN_USER', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-webadmin.xml::changelog-1.1_create_tbl_webadmin_property::StephanSpindler
CREATE TABLE connectornew.DOMIBUS_WEBADMIN_PROPERTY (`KEY` VARCHAR(512) NULL, VALUE VARCHAR(512) NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-1.1_create_tbl_webadmin_property', 'StephanSpindler', 'db/changelog/install/initial-changelog-webadmin.xml', NOW(), 48, '7:3a70634c5e236f95b1380ebb1d76fb23', 'createTable tableName=DOMIBUS_WEBADMIN_PROPERTY', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-webadmin.xml::changelog-1.1_create_tbl_webadmin_property_pk::StephanSpindler
ALTER TABLE connectornew.DOMIBUS_WEBADMIN_PROPERTY ADD PRIMARY KEY (`KEY`);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-1.1_create_tbl_webadmin_property_pk', 'StephanSpindler', 'db/changelog/install/initial-changelog-webadmin.xml', NOW(), 49, '7:25128fd9a150c71e70370f18b56a6fae', 'addPrimaryKey constraintName=PK_WEBAMDIN_PROPERTY, tableName=DOMIBUS_WEBADMIN_PROPERTY', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-webadmin.xml::changelog-1.0_create_default_user_password::StephanSpindler
--  Create default admin user with username admin and password admin
INSERT INTO connectornew.DOMIBUS_WEBADMIN_USER (USERNAME, PASSWORD, SALT, ROLE) VALUES ('admin', '2bf5e637d0d82a75ca43e3be85df2c23febffc0cc221f5e010937005df478a19b5eaab59fe7e4e97f6b43ba648c169effd432e19817f386987d058c239236306', '5b424031616564356639', 'admin');

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('changelog-1.0_create_default_user_password', 'StephanSpindler', 'db/changelog/install/initial-changelog-webadmin.xml', NOW(), 50, '7:5e3c78856caa32b875f0ab821d71604f', 'insert tableName=DOMIBUS_WEBADMIN_USER', 'Create default admin user with username admin and password admin', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-1::StephanSpindler
CREATE TABLE connectornew.DCON_QRTZ_JOB_DETAILS (SCHED_NAME VARCHAR(120) NOT NULL, JOB_NAME VARCHAR(200) NOT NULL, JOB_GROUP VARCHAR(200) NOT NULL, DESCRIPTION VARCHAR(250) NULL, JOB_CLASS_NAME VARCHAR(250) NOT NULL, IS_DURABLE BIT(1) NOT NULL, IS_NONCONCURRENT BIT(1) NOT NULL, IS_UPDATE_DATA BIT(1) NOT NULL, REQUESTS_RECOVERY BIT(1) NOT NULL, JOB_DATA BLOB NULL, CONSTRAINT PK_DCON_QRTZ_JOB_DETAILS PRIMARY KEY (SCHED_NAME, JOB_NAME, JOB_GROUP));

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-1', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 51, '7:6894318992d3a73f450e8b1086afb840', 'createTable tableName=DCON_QRTZ_JOB_DETAILS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-2::StephanSpindler
CREATE TABLE connectornew.DCON_QRTZ_TRIGGERS (SCHED_NAME VARCHAR(120) NOT NULL, TRIGGER_NAME VARCHAR(200) NOT NULL, TRIGGER_GROUP VARCHAR(200) NOT NULL, JOB_NAME VARCHAR(200) NOT NULL, JOB_GROUP VARCHAR(200) NOT NULL, DESCRIPTION VARCHAR(250) NULL, NEXT_FIRE_TIME BIGINT NULL, PREV_FIRE_TIME BIGINT NULL, PRIORITY INT NULL, TRIGGER_STATE VARCHAR(16) NOT NULL, TRIGGER_TYPE VARCHAR(8) NOT NULL, START_TIME BIGINT NOT NULL, END_TIME BIGINT NULL, CALENDAR_NAME VARCHAR(200) NULL, MISFIRE_INSTR SMALLINT NULL, JOB_DATA BLOB NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-2', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 52, '7:539e3dfe79119381bf27285bb77f124e', 'createTable tableName=DCON_QRTZ_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-2.1::StephanSpindler
ALTER TABLE connectornew.DCON_QRTZ_TRIGGERS ADD PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-2.1', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 53, '7:7716981b9630467ef2715f42537c57e2', 'addPrimaryKey tableName=DCON_QRTZ_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-2.2::StephanSpindler
ALTER TABLE connectornew.DCON_QRTZ_TRIGGERS ADD CONSTRAINT fk_sched_to_job_details FOREIGN KEY (SCHED_NAME, JOB_NAME, JOB_GROUP) REFERENCES connectornew.DCON_QRTZ_JOB_DETAILS (SCHED_NAME, JOB_NAME, JOB_GROUP);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-2.2', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 54, '7:c21ee8df7e25cca1fa0c37af97476ee6', 'addForeignKeyConstraint baseTableName=DCON_QRTZ_TRIGGERS, constraintName=fk_sched_to_job_details, referencedTableName=DCON_QRTZ_JOB_DETAILS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-3::StephanSpindler
CREATE TABLE connectornew.DCON_QRTZ_SIMPLE_TRIGGERS (SCHED_NAME VARCHAR(120) NOT NULL, TRIGGER_NAME VARCHAR(200) NOT NULL, TRIGGER_GROUP VARCHAR(200) NOT NULL, REPEAT_COUNT BIGINT NOT NULL, REPEAT_INTERVAL BIGINT NOT NULL, TIMES_TRIGGERED BIGINT NOT NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-3', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 55, '7:a38afb2aee37dee6b09fec22a0f910d4', 'createTable tableName=DCON_QRTZ_SIMPLE_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-3.1::StephanSpindler
ALTER TABLE connectornew.DCON_QRTZ_SIMPLE_TRIGGERS ADD PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-3.1', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 56, '7:21f48283a32a3dec4cf1abd2dbf742c0', 'addPrimaryKey tableName=DCON_QRTZ_SIMPLE_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-3.2::StephanSpindler
ALTER TABLE connectornew.DCON_QRTZ_SIMPLE_TRIGGERS ADD CONSTRAINT fk_schedtrigger_qrtz_trig FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES connectornew.DCON_QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-3.2', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 57, '7:6df2b464ee405f7f12ff1e1abcd0c47c', 'addForeignKeyConstraint baseTableName=DCON_QRTZ_SIMPLE_TRIGGERS, constraintName=fk_schedtrigger_qrtz_trig, referencedTableName=DCON_QRTZ_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-4::StephanSpindler
CREATE TABLE connectornew.DCON_QRTZ_CRON_TRIGGERS (SCHED_NAME VARCHAR(120) NOT NULL, TRIGGER_NAME VARCHAR(200) NOT NULL, TRIGGER_GROUP VARCHAR(200) NOT NULL, CRON_EXPRESSION VARCHAR(120) NOT NULL, TIME_ZONE_ID VARCHAR(80) NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-4', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 58, '7:8debd5cadfd8ffb4f0e73f2ffe33d2df', 'createTable tableName=DCON_QRTZ_CRON_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-4.1::StephanSpindler
ALTER TABLE connectornew.DCON_QRTZ_CRON_TRIGGERS ADD PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-4.1', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 59, '7:ae455f628acdb815d8ded9a5c9a89763', 'addPrimaryKey tableName=DCON_QRTZ_CRON_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-4.2::StephanSpindler
ALTER TABLE connectornew.DCON_QRTZ_CRON_TRIGGERS ADD CONSTRAINT fk_crontrig_to_qrtztrig FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES connectornew.DCON_QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-4.2', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 60, '7:4f2ad9ab325cf3208fddcec3e4bc8a1f', 'addForeignKeyConstraint baseTableName=DCON_QRTZ_CRON_TRIGGERS, constraintName=fk_crontrig_to_qrtztrig, referencedTableName=DCON_QRTZ_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-5::StephanSpindler
CREATE TABLE connectornew.DCON_QRTZ_SIMPROP_TRIGGERS (SCHED_NAME VARCHAR(120) NOT NULL, TRIGGER_NAME VARCHAR(200) NOT NULL, TRIGGER_GROUP VARCHAR(200) NOT NULL, STR_PROP_1 VARCHAR(512) NULL, STR_PROP_2 VARCHAR(512) NULL, STR_PROP_3 VARCHAR(512) NULL, INT_PROP_1 INT NULL, INT_PROP_2 INT NULL, LONG_PROP_1 BIGINT NULL, LONG_PROP_2 BIGINT NULL, DEC_PROP_1 DECIMAL(13, 4) NULL, DEC_PROP_2 DECIMAL(13, 4) NULL, BOOL_PROP_1 VARCHAR(5) NULL, BOOL_PROP_2 VARCHAR(5) NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-5', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 61, '7:66ee4c4d3135bbee96aa34173f0e2d49', 'createTable tableName=DCON_QRTZ_SIMPROP_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-5.1::StephanSpindler
ALTER TABLE connectornew.DCON_QRTZ_SIMPROP_TRIGGERS ADD PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-5.1', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 62, '7:02b1bd28f4c895ccb829900500c7eb78', 'addPrimaryKey tableName=DCON_QRTZ_SIMPROP_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-5.2::StephanSpindler
ALTER TABLE connectornew.DCON_QRTZ_SIMPROP_TRIGGERS ADD CONSTRAINT fk_simproptrig_to_qrtztrig FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES connectornew.DCON_QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-5.2', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 63, '7:62ab3b9cf77e44e123ea783294920f39', 'addForeignKeyConstraint baseTableName=DCON_QRTZ_SIMPROP_TRIGGERS, constraintName=fk_simproptrig_to_qrtztrig, referencedTableName=DCON_QRTZ_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-6::StephanSpindler
CREATE TABLE connectornew.DCON_QRTZ_BLOB_TRIGGERS (SCHED_NAME VARCHAR(120) NOT NULL, TRIGGER_NAME VARCHAR(200) NOT NULL, TRIGGER_GROUP VARCHAR(200) NOT NULL, BLOB_DATA BLOB NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-6', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 64, '7:5fd25df814449af5e22a060edc1321cb', 'createTable tableName=DCON_QRTZ_BLOB_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-6.1::StephanSpindler
ALTER TABLE connectornew.DCON_QRTZ_BLOB_TRIGGERS ADD PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-6.1', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 65, '7:566db542d801eee7819fbadebcac1eee', 'addPrimaryKey tableName=DCON_QRTZ_BLOB_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-6.2::StephanSpindler
ALTER TABLE connectornew.DCON_QRTZ_BLOB_TRIGGERS ADD CONSTRAINT fk_blobtrig_to_trig FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES connectornew.DCON_QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-6.2', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 66, '7:a412e84929455094e67a7e2aaba2b106', 'addForeignKeyConstraint baseTableName=DCON_QRTZ_BLOB_TRIGGERS, constraintName=fk_blobtrig_to_trig, referencedTableName=DCON_QRTZ_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-7::StephanSpindler
CREATE TABLE connectornew.DCON_QRTZ_CALENDARS (SCHED_NAME VARCHAR(120) NOT NULL, CALENDAR_NAME VARCHAR(200) NOT NULL, CALENDAR BLOB NOT NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-7', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 67, '7:8f95cf8890d2f3ff3d3c83ad5dd15099', 'createTable tableName=DCON_QRTZ_CALENDARS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-7.1::StephanSpindler
ALTER TABLE connectornew.DCON_QRTZ_CALENDARS ADD PRIMARY KEY (SCHED_NAME, CALENDAR_NAME);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-7.1', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 68, '7:c72e09a57afa5cf5b4c01d28466c92c2', 'addPrimaryKey tableName=DCON_QRTZ_CALENDARS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-8::StephanSpindler
CREATE TABLE connectornew.DCON_QRTZ_PAUSED_TRIGGER_GRPS (SCHED_NAME VARCHAR(120) NOT NULL, TRIGGER_GROUP VARCHAR(200) NOT NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-8', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 69, '7:b4476fbcf8de45d511c82056e676b68d', 'createTable tableName=DCON_QRTZ_PAUSED_TRIGGER_GRPS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-8.1::StephanSpindler
ALTER TABLE connectornew.DCON_QRTZ_PAUSED_TRIGGER_GRPS ADD PRIMARY KEY (SCHED_NAME, TRIGGER_GROUP);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-8.1', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 70, '7:580d53a416ab28b2a95ba461f540f73b', 'addPrimaryKey tableName=DCON_QRTZ_PAUSED_TRIGGER_GRPS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-9::StephanSpindler
CREATE TABLE connectornew.DCON_QRTZ_FIRED_TRIGGERS (SCHED_NAME VARCHAR(120) NOT NULL, ENTRY_ID VARCHAR(95) NOT NULL, TRIGGER_NAME VARCHAR(200) NOT NULL, TRIGGER_GROUP VARCHAR(200) NOT NULL, INSTANCE_NAME VARCHAR(200) NOT NULL, FIRED_TIME BIGINT NOT NULL, SCHED_TIME BIGINT NOT NULL, PRIORITY INT NOT NULL, STATE VARCHAR(16) NOT NULL, JOB_NAME VARCHAR(200) NULL, JOB_GROUP VARCHAR(200) NULL, IS_NONCONCURRENT BIT(1) NULL, REQUESTS_RECOVERY BIT(1) NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-9', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 71, '7:5231b128ad234361291c2f3e9ca356ea', 'createTable tableName=DCON_QRTZ_FIRED_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-9.1::StephanSpindler
ALTER TABLE connectornew.DCON_QRTZ_FIRED_TRIGGERS ADD PRIMARY KEY (SCHED_NAME, ENTRY_ID);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-9.1', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 72, '7:1e0b8f34085a929ec25ba251a9461bf7', 'addPrimaryKey tableName=DCON_QRTZ_FIRED_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-10::StephanSpindler
CREATE TABLE connectornew.DCON_QRTZ_SCHEDULER_STATE (SCHED_NAME VARCHAR(120) NOT NULL, INSTANCE_NAME VARCHAR(200) NOT NULL, LAST_CHECKIN_TIME BIGINT NOT NULL, CHECKIN_INTERVAL BIGINT NOT NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-10', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 73, '7:a00b396e75af85fc13849873bb250f8d', 'createTable tableName=DCON_QRTZ_SCHEDULER_STATE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-10.1::StephanSpindler
ALTER TABLE connectornew.DCON_QRTZ_SCHEDULER_STATE ADD PRIMARY KEY (SCHED_NAME, INSTANCE_NAME);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-10.1', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 74, '7:1b5429580d7f93c001cc97f32920e65b', 'addPrimaryKey tableName=DCON_QRTZ_SCHEDULER_STATE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-11::StephanSpindler
CREATE TABLE connectornew.DCON_QRTZ_LOCKS (SCHED_NAME VARCHAR(120) NOT NULL, LOCK_NAME VARCHAR(40) NOT NULL);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-11', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 75, '7:66b900e0c7e794e5270e7a0d852b4446', 'createTable tableName=DCON_QRTZ_LOCKS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::init-quartz-11.1::StephanSpindler
ALTER TABLE connectornew.DCON_QRTZ_LOCKS ADD PRIMARY KEY (SCHED_NAME, LOCK_NAME);

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-11.1', 'StephanSpindler', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 76, '7:6c1aea2f594dcd2acaa9ab33fc45e3a6', 'addPrimaryKey tableName=DCON_QRTZ_LOCKS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');

--  Changeset db/changelog/install/initial-changelog-quartz.xml::StephanSpindler::init-quartz-finished
INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID, TAG) VALUES ('StephanSpindler', 'init-quartz-finished', 'db/changelog/install/initial-changelog-quartz.xml', NOW(), 77, '7:b20200175c5324a76b29c1e64e146046', 'tagDatabase', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427', 'QUARTZ_DB ');

--  Changeset /db/changelog/install/initial-changelog-data.sql::initialData_1::StephanSpindler
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE VALUES ('DOMIBUS_CONNECTOR_MESSAGES.ID', 0);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE VALUES ('DOMIBUS_CONNECTOR_EVIDENCES.ID', 0);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE VALUES ('DOMIBUS_CONNECTOR_MESSAGE_INFO.ID', 0);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE VALUES ('DOMIBUS_CONNECTOR_MSG_ERROR.ID', 0);
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('ARHS', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('AT', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('CTP', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('CZ', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('DE', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('EC', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('EE', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('ES', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('FR', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('GR', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('IT', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('ITIC', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('MT', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('NL', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('PL', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_A', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_B', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_C', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_D', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_E', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_F', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_G', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('FreeFormLetter', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('FreeFormLetterIn', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('FreeFormLetterOut', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('SubmissionAcceptanceRejection', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('RelayREMMDAcceptanceRejection', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('RelayREMMDFailure', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('DeliveryNonDeliveryToRecipient', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('RetrievalNonRetrievalToRecipient', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Test_Form', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('TEST-ping-connector', 0);
INSERT INTO DOMIBUS_CONNECTOR_SERVICE VALUES ('EPO', 'urn:e-codex:services:');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE VALUES ('SmallClaims', 'urn:e-codex:services:');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE VALUES ('Connector-TEST', 'urn:e-codex:services:');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE VALUES ('TEST-ping-connector', 'urn:e-codex:services:');

INSERT INTO connectornew.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('initialData_1', 'StephanSpindler', '/db/changelog/install/initial-changelog-data.sql', NOW(), 78, '7:d299ac405108642b0b8b4dbb75cfbb29', 'sql', '', 'EXECUTED', NULL, NULL, '3.5.3', '3517749427');
