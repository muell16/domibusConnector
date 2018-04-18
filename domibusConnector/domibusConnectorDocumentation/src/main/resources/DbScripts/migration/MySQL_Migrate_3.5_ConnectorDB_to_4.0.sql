--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: db/changelog/v004/upgrade-3to4.xml
--  Ran at: 11.04.18 12:19
--  Against: domibus@localhost@jdbc:mysql://localhost/domibusconnector
--  Liquibase version: 3.5.3
--  *********************************************************************
-- This database migration script is based on LIQUIBASE
-- It updates a 3.5 Connector Database to the 4.0 Database Schema
-- Before you execute this script make sure you have created a BACKUP of your current database
-- The script assumes, that there have been no changes made to the 3.5 database!
--
--


--  Create Database Lock Table
CREATE TABLE connectormigrate.DATABASECHANGELOGLOCK (ID INT NOT NULL, LOCKED BIT(1) NOT NULL, LOCKGRANTED datetime NULL, LOCKEDBY VARCHAR(255) NULL, CONSTRAINT PK_DATABASECHANGELOGLOCK PRIMARY KEY (ID));

--  Initialize Database Lock Table
DELETE FROM connectormigrate.DATABASECHANGELOGLOCK;

INSERT INTO connectormigrate.DATABASECHANGELOGLOCK (ID, LOCKED) VALUES (1, 0);


--  Create Database Change Log Table
CREATE TABLE connectormigrate.DATABASECHANGELOG (ID VARCHAR(255) NOT NULL, AUTHOR VARCHAR(255) NOT NULL, FILENAME VARCHAR(255) NOT NULL, DATEEXECUTED datetime NOT NULL, ORDEREXECUTED INT NOT NULL, EXECTYPE VARCHAR(10) NOT NULL, MD5SUM VARCHAR(35) NULL, DESCRIPTION VARCHAR(255) NULL, COMMENTS VARCHAR(255) NULL, TAG VARCHAR(255) NULL, LIQUIBASE VARCHAR(20) NULL, CONTEXTS VARCHAR(255) NULL, LABELS VARCHAR(255) NULL, DEPLOYMENT_ID VARCHAR(10) NULL);

--  Changeset /db/changelog/v004/upgrade_qrtz_tables.xml::01_drop_all_old_qrtz_tables_DCON_QRTZ_BLOB_TRIGGERS::StephanSpindler
--  dropping all old quartz trigger tables to create them again under control of liquibase
DROP TABLE connectormigrate.DCON_QRTZ_BLOB_TRIGGERS;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('01_drop_all_old_qrtz_tables_DCON_QRTZ_BLOB_TRIGGERS', 'StephanSpindler', '/db/changelog/v004/upgrade_qrtz_tables.xml', NOW(), 1, '7:5316e6fa660dacd43c35ad3211d807f2', 'dropTable tableName=DCON_QRTZ_BLOB_TRIGGERS', 'dropping all old quartz trigger tables to create them again under control of liquibase', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/v004/upgrade_qrtz_tables.xml::02_drop_all_old_qrtz_tables_DCON_QRTZ_CALENDARS::StephanSpindler
DROP TABLE connectormigrate.DCON_QRTZ_CALENDARS;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('02_drop_all_old_qrtz_tables_DCON_QRTZ_CALENDARS', 'StephanSpindler', '/db/changelog/v004/upgrade_qrtz_tables.xml', NOW(), 2, '7:71a62acb5e9f929d4fe3405a7dce0c50', 'dropTable tableName=DCON_QRTZ_CALENDARS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/v004/upgrade_qrtz_tables.xml::03_drop_all_old_qrtz_tables_DCON_QRTZ_CRON_TRIGGERS::StephanSpindler
DROP TABLE connectormigrate.DCON_QRTZ_CRON_TRIGGERS;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('03_drop_all_old_qrtz_tables_DCON_QRTZ_CRON_TRIGGERS', 'StephanSpindler', '/db/changelog/v004/upgrade_qrtz_tables.xml', NOW(), 3, '7:0d16afcce3d42c399af12c132ffbb265', 'dropTable tableName=DCON_QRTZ_CRON_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/v004/upgrade_qrtz_tables.xml::04_drop_all_old_qrtz_tables_DCON_QRTZ_FIRED_TRIGGERS::StephanSpindler
DROP TABLE connectormigrate.DCON_QRTZ_FIRED_TRIGGERS;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('04_drop_all_old_qrtz_tables_DCON_QRTZ_FIRED_TRIGGERS', 'StephanSpindler', '/db/changelog/v004/upgrade_qrtz_tables.xml', NOW(), 4, '7:90894d0c0d3b9945c5ab96298a20744f', 'dropTable tableName=DCON_QRTZ_FIRED_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/v004/upgrade_qrtz_tables.xml::05_drop_all_old_qrtz_tables_DCON_QRTZ_LOCKS::StephanSpindler
DROP TABLE connectormigrate.DCON_QRTZ_LOCKS;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('05_drop_all_old_qrtz_tables_DCON_QRTZ_LOCKS', 'StephanSpindler', '/db/changelog/v004/upgrade_qrtz_tables.xml', NOW(), 5, '7:be18c37d668b2cf01f4cbea94312c149', 'dropTable tableName=DCON_QRTZ_LOCKS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/v004/upgrade_qrtz_tables.xml::06_drop_all_old_qrtz_tables_DCON_QRTZ_PAUSED_TRIGGER_GRPS::StephanSpindler
DROP TABLE connectormigrate.DCON_QRTZ_PAUSED_TRIGGER_GRPS;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('06_drop_all_old_qrtz_tables_DCON_QRTZ_PAUSED_TRIGGER_GRPS', 'StephanSpindler', '/db/changelog/v004/upgrade_qrtz_tables.xml', NOW(), 6, '7:6974fb97c59f8be3b899503c4338db58', 'dropTable tableName=DCON_QRTZ_PAUSED_TRIGGER_GRPS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/v004/upgrade_qrtz_tables.xml::07_drop_all_old_qrtz_tables_DCON_QRTZ_SCHEDULER_STATE::StephanSpindler
DROP TABLE connectormigrate.DCON_QRTZ_SCHEDULER_STATE;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('07_drop_all_old_qrtz_tables_DCON_QRTZ_SCHEDULER_STATE', 'StephanSpindler', '/db/changelog/v004/upgrade_qrtz_tables.xml', NOW(), 7, '7:279421c54862418a1b5c660fadf49f02', 'dropTable tableName=DCON_QRTZ_SCHEDULER_STATE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/v004/upgrade_qrtz_tables.xml::08_drop_all_old_qrtz_tables_DCON_QRTZ_SIMPLE_TRIGGERS::StephanSpindler
DROP TABLE connectormigrate.DCON_QRTZ_SIMPLE_TRIGGERS;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('08_drop_all_old_qrtz_tables_DCON_QRTZ_SIMPLE_TRIGGERS', 'StephanSpindler', '/db/changelog/v004/upgrade_qrtz_tables.xml', NOW(), 8, '7:37ccc044d987c9e1659bd5dfe465e8aa', 'dropTable tableName=DCON_QRTZ_SIMPLE_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/v004/upgrade_qrtz_tables.xml::09_drop_all_old_qrtz_tables_DCON_QRTZ_SIMPROP_TRIGGERS::StephanSpindler
DROP TABLE connectormigrate.DCON_QRTZ_SIMPROP_TRIGGERS;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('09_drop_all_old_qrtz_tables_DCON_QRTZ_SIMPROP_TRIGGERS', 'StephanSpindler', '/db/changelog/v004/upgrade_qrtz_tables.xml', NOW(), 9, '7:902ff6fb14e9fbe25cc4bedc205ac18f', 'dropTable tableName=DCON_QRTZ_SIMPROP_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/v004/upgrade_qrtz_tables.xml::10_drop_all_old_qrtz_tables_DCON_QRTZ_TRIGGERS::StephanSpindler
DROP TABLE connectormigrate.DCON_QRTZ_TRIGGERS;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('10_drop_all_old_qrtz_tables_DCON_QRTZ_TRIGGERS', 'StephanSpindler', '/db/changelog/v004/upgrade_qrtz_tables.xml', NOW(), 10, '7:90e08fcb7607fe9572529ea131a73e74', 'dropTable tableName=DCON_QRTZ_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/v004/upgrade_qrtz_tables.xml::11_drop_all_old_qrtz_tables_DCON_QRTZ_JOB_DETAILS::StephanSpindler
DROP TABLE connectormigrate.DCON_QRTZ_JOB_DETAILS;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('11_drop_all_old_qrtz_tables_DCON_QRTZ_JOB_DETAILS', 'StephanSpindler', '/db/changelog/v004/upgrade_qrtz_tables.xml', NOW(), 11, '7:b35429aa93105fc7233d2cec24b80a4c', 'dropTable tableName=DCON_QRTZ_JOB_DETAILS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-1::StephanSpindler
CREATE TABLE connectormigrate.DCON_QRTZ_JOB_DETAILS (SCHED_NAME VARCHAR(120) NOT NULL, JOB_NAME VARCHAR(200) NOT NULL, JOB_GROUP VARCHAR(200) NOT NULL, DESCRIPTION VARCHAR(250) NULL, JOB_CLASS_NAME VARCHAR(250) NOT NULL, IS_DURABLE BIT(1) NOT NULL, IS_NONCONCURRENT BIT(1) NOT NULL, IS_UPDATE_DATA BIT(1) NOT NULL, REQUESTS_RECOVERY BIT(1) NOT NULL, JOB_DATA BLOB NULL, CONSTRAINT PK_DCON_QRTZ_JOB_DETAILS PRIMARY KEY (SCHED_NAME, JOB_NAME, JOB_GROUP));

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-1', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 12, '7:6894318992d3a73f450e8b1086afb840', 'createTable tableName=DCON_QRTZ_JOB_DETAILS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-2::StephanSpindler
CREATE TABLE connectormigrate.DCON_QRTZ_TRIGGERS (SCHED_NAME VARCHAR(120) NOT NULL, TRIGGER_NAME VARCHAR(200) NOT NULL, TRIGGER_GROUP VARCHAR(200) NOT NULL, JOB_NAME VARCHAR(200) NOT NULL, JOB_GROUP VARCHAR(200) NOT NULL, DESCRIPTION VARCHAR(250) NULL, NEXT_FIRE_TIME BIGINT NULL, PREV_FIRE_TIME BIGINT NULL, PRIORITY INT NULL, TRIGGER_STATE VARCHAR(16) NOT NULL, TRIGGER_TYPE VARCHAR(8) NOT NULL, START_TIME BIGINT NOT NULL, END_TIME BIGINT NULL, CALENDAR_NAME VARCHAR(200) NULL, MISFIRE_INSTR SMALLINT NULL, JOB_DATA BLOB NULL);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-2', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 13, '7:539e3dfe79119381bf27285bb77f124e', 'createTable tableName=DCON_QRTZ_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-2.1::StephanSpindler
ALTER TABLE connectormigrate.DCON_QRTZ_TRIGGERS ADD PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-2.1', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 14, '7:7716981b9630467ef2715f42537c57e2', 'addPrimaryKey tableName=DCON_QRTZ_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-2.2::StephanSpindler
ALTER TABLE connectormigrate.DCON_QRTZ_TRIGGERS ADD CONSTRAINT fk_sched_to_job_details FOREIGN KEY (SCHED_NAME, JOB_NAME, JOB_GROUP) REFERENCES connectormigrate.DCON_QRTZ_JOB_DETAILS (SCHED_NAME, JOB_NAME, JOB_GROUP);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-2.2', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 15, '7:c21ee8df7e25cca1fa0c37af97476ee6', 'addForeignKeyConstraint baseTableName=DCON_QRTZ_TRIGGERS, constraintName=fk_sched_to_job_details, referencedTableName=DCON_QRTZ_JOB_DETAILS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-3::StephanSpindler
CREATE TABLE connectormigrate.DCON_QRTZ_SIMPLE_TRIGGERS (SCHED_NAME VARCHAR(120) NOT NULL, TRIGGER_NAME VARCHAR(200) NOT NULL, TRIGGER_GROUP VARCHAR(200) NOT NULL, REPEAT_COUNT BIGINT NOT NULL, REPEAT_INTERVAL BIGINT NOT NULL, TIMES_TRIGGERED BIGINT NOT NULL);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-3', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 16, '7:a38afb2aee37dee6b09fec22a0f910d4', 'createTable tableName=DCON_QRTZ_SIMPLE_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-3.1::StephanSpindler
ALTER TABLE connectormigrate.DCON_QRTZ_SIMPLE_TRIGGERS ADD PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-3.1', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 17, '7:21f48283a32a3dec4cf1abd2dbf742c0', 'addPrimaryKey tableName=DCON_QRTZ_SIMPLE_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-3.2::StephanSpindler
ALTER TABLE connectormigrate.DCON_QRTZ_SIMPLE_TRIGGERS ADD CONSTRAINT fk_schedtrigger_qrtz_trig FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES connectormigrate.DCON_QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-3.2', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 18, '7:6df2b464ee405f7f12ff1e1abcd0c47c', 'addForeignKeyConstraint baseTableName=DCON_QRTZ_SIMPLE_TRIGGERS, constraintName=fk_schedtrigger_qrtz_trig, referencedTableName=DCON_QRTZ_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-4::StephanSpindler
CREATE TABLE connectormigrate.DCON_QRTZ_CRON_TRIGGERS (SCHED_NAME VARCHAR(120) NOT NULL, TRIGGER_NAME VARCHAR(200) NOT NULL, TRIGGER_GROUP VARCHAR(200) NOT NULL, CRON_EXPRESSION VARCHAR(120) NOT NULL, TIME_ZONE_ID VARCHAR(80) NULL);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-4', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 19, '7:8debd5cadfd8ffb4f0e73f2ffe33d2df', 'createTable tableName=DCON_QRTZ_CRON_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-4.1::StephanSpindler
ALTER TABLE connectormigrate.DCON_QRTZ_CRON_TRIGGERS ADD PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-4.1', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 20, '7:ae455f628acdb815d8ded9a5c9a89763', 'addPrimaryKey tableName=DCON_QRTZ_CRON_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-4.2::StephanSpindler
ALTER TABLE connectormigrate.DCON_QRTZ_CRON_TRIGGERS ADD CONSTRAINT fk_crontrig_to_qrtztrig FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES connectormigrate.DCON_QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-4.2', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 21, '7:4f2ad9ab325cf3208fddcec3e4bc8a1f', 'addForeignKeyConstraint baseTableName=DCON_QRTZ_CRON_TRIGGERS, constraintName=fk_crontrig_to_qrtztrig, referencedTableName=DCON_QRTZ_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-5::StephanSpindler
CREATE TABLE connectormigrate.DCON_QRTZ_SIMPROP_TRIGGERS (SCHED_NAME VARCHAR(120) NOT NULL, TRIGGER_NAME VARCHAR(200) NOT NULL, TRIGGER_GROUP VARCHAR(200) NOT NULL, STR_PROP_1 VARCHAR(512) NULL, STR_PROP_2 VARCHAR(512) NULL, STR_PROP_3 VARCHAR(512) NULL, INT_PROP_1 INT NULL, INT_PROP_2 INT NULL, LONG_PROP_1 BIGINT NULL, LONG_PROP_2 BIGINT NULL, DEC_PROP_1 DECIMAL(13, 4) NULL, DEC_PROP_2 DECIMAL(13, 4) NULL, BOOL_PROP_1 VARCHAR(5) NULL, BOOL_PROP_2 VARCHAR(5) NULL);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-5', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 22, '7:66ee4c4d3135bbee96aa34173f0e2d49', 'createTable tableName=DCON_QRTZ_SIMPROP_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-5.1::StephanSpindler
ALTER TABLE connectormigrate.DCON_QRTZ_SIMPROP_TRIGGERS ADD PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-5.1', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 23, '7:02b1bd28f4c895ccb829900500c7eb78', 'addPrimaryKey tableName=DCON_QRTZ_SIMPROP_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-5.2::StephanSpindler
ALTER TABLE connectormigrate.DCON_QRTZ_SIMPROP_TRIGGERS ADD CONSTRAINT fk_simproptrig_to_qrtztrig FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES connectormigrate.DCON_QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-5.2', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 24, '7:62ab3b9cf77e44e123ea783294920f39', 'addForeignKeyConstraint baseTableName=DCON_QRTZ_SIMPROP_TRIGGERS, constraintName=fk_simproptrig_to_qrtztrig, referencedTableName=DCON_QRTZ_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-6::StephanSpindler
CREATE TABLE connectormigrate.DCON_QRTZ_BLOB_TRIGGERS (SCHED_NAME VARCHAR(120) NOT NULL, TRIGGER_NAME VARCHAR(200) NOT NULL, TRIGGER_GROUP VARCHAR(200) NOT NULL, BLOB_DATA BLOB NULL);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-6', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 25, '7:5fd25df814449af5e22a060edc1321cb', 'createTable tableName=DCON_QRTZ_BLOB_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-6.1::StephanSpindler
ALTER TABLE connectormigrate.DCON_QRTZ_BLOB_TRIGGERS ADD PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-6.1', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 26, '7:566db542d801eee7819fbadebcac1eee', 'addPrimaryKey tableName=DCON_QRTZ_BLOB_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-6.2::StephanSpindler
ALTER TABLE connectormigrate.DCON_QRTZ_BLOB_TRIGGERS ADD CONSTRAINT fk_blobtrig_to_trig FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES connectormigrate.DCON_QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-6.2', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 27, '7:a412e84929455094e67a7e2aaba2b106', 'addForeignKeyConstraint baseTableName=DCON_QRTZ_BLOB_TRIGGERS, constraintName=fk_blobtrig_to_trig, referencedTableName=DCON_QRTZ_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-7::StephanSpindler
CREATE TABLE connectormigrate.DCON_QRTZ_CALENDARS (SCHED_NAME VARCHAR(120) NOT NULL, CALENDAR_NAME VARCHAR(200) NOT NULL, CALENDAR BLOB NOT NULL);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-7', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 28, '7:8f95cf8890d2f3ff3d3c83ad5dd15099', 'createTable tableName=DCON_QRTZ_CALENDARS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-7.1::StephanSpindler
ALTER TABLE connectormigrate.DCON_QRTZ_CALENDARS ADD PRIMARY KEY (SCHED_NAME, CALENDAR_NAME);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-7.1', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 29, '7:c72e09a57afa5cf5b4c01d28466c92c2', 'addPrimaryKey tableName=DCON_QRTZ_CALENDARS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-8::StephanSpindler
CREATE TABLE connectormigrate.DCON_QRTZ_PAUSED_TRIGGER_GRPS (SCHED_NAME VARCHAR(120) NOT NULL, TRIGGER_GROUP VARCHAR(200) NOT NULL);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-8', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 30, '7:b4476fbcf8de45d511c82056e676b68d', 'createTable tableName=DCON_QRTZ_PAUSED_TRIGGER_GRPS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-8.1::StephanSpindler
ALTER TABLE connectormigrate.DCON_QRTZ_PAUSED_TRIGGER_GRPS ADD PRIMARY KEY (SCHED_NAME, TRIGGER_GROUP);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-8.1', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 31, '7:580d53a416ab28b2a95ba461f540f73b', 'addPrimaryKey tableName=DCON_QRTZ_PAUSED_TRIGGER_GRPS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-9::StephanSpindler
CREATE TABLE connectormigrate.DCON_QRTZ_FIRED_TRIGGERS (SCHED_NAME VARCHAR(120) NOT NULL, ENTRY_ID VARCHAR(95) NOT NULL, TRIGGER_NAME VARCHAR(200) NOT NULL, TRIGGER_GROUP VARCHAR(200) NOT NULL, INSTANCE_NAME VARCHAR(200) NOT NULL, FIRED_TIME BIGINT NOT NULL, SCHED_TIME BIGINT NOT NULL, PRIORITY INT NOT NULL, STATE VARCHAR(16) NOT NULL, JOB_NAME VARCHAR(200) NULL, JOB_GROUP VARCHAR(200) NULL, IS_NONCONCURRENT BIT(1) NULL, REQUESTS_RECOVERY BIT(1) NULL);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-9', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 32, '7:5231b128ad234361291c2f3e9ca356ea', 'createTable tableName=DCON_QRTZ_FIRED_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-9.1::StephanSpindler
ALTER TABLE connectormigrate.DCON_QRTZ_FIRED_TRIGGERS ADD PRIMARY KEY (SCHED_NAME, ENTRY_ID);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-9.1', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 33, '7:1e0b8f34085a929ec25ba251a9461bf7', 'addPrimaryKey tableName=DCON_QRTZ_FIRED_TRIGGERS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-10::StephanSpindler
CREATE TABLE connectormigrate.DCON_QRTZ_SCHEDULER_STATE (SCHED_NAME VARCHAR(120) NOT NULL, INSTANCE_NAME VARCHAR(200) NOT NULL, LAST_CHECKIN_TIME BIGINT NOT NULL, CHECKIN_INTERVAL BIGINT NOT NULL);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-10', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 34, '7:a00b396e75af85fc13849873bb250f8d', 'createTable tableName=DCON_QRTZ_SCHEDULER_STATE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-10.1::StephanSpindler
ALTER TABLE connectormigrate.DCON_QRTZ_SCHEDULER_STATE ADD PRIMARY KEY (SCHED_NAME, INSTANCE_NAME);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-10.1', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 35, '7:1b5429580d7f93c001cc97f32920e65b', 'addPrimaryKey tableName=DCON_QRTZ_SCHEDULER_STATE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-11::StephanSpindler
CREATE TABLE connectormigrate.DCON_QRTZ_LOCKS (SCHED_NAME VARCHAR(120) NOT NULL, LOCK_NAME VARCHAR(40) NOT NULL);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-11', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 36, '7:66b900e0c7e794e5270e7a0d852b4446', 'createTable tableName=DCON_QRTZ_LOCKS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::init-quartz-11.1::StephanSpindler
ALTER TABLE connectormigrate.DCON_QRTZ_LOCKS ADD PRIMARY KEY (SCHED_NAME, LOCK_NAME);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('init-quartz-11.1', 'StephanSpindler', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 37, '7:6c1aea2f594dcd2acaa9ab33fc45e3a6', 'addPrimaryKey tableName=DCON_QRTZ_LOCKS', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset /db/changelog/install/initial-changelog-quartz.xml::StephanSpindler::init-quartz-finished
INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID, TAG) VALUES ('StephanSpindler', 'init-quartz-finished', '/db/changelog/install/initial-changelog-quartz.xml', NOW(), 38, '7:b20200175c5324a76b29c1e64e146046', 'tagDatabase', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608', 'QUARTZ_DB ');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_001.0_pre_setup::StephanSpindler
--  WARNING The following SQL may change each run and therefore is possibly incorrect and/or invalid:
--  WARNING The following SQL may change each run and therefore is possibly incorrect and/or invalid:
--  WARNING The following SQL may change each run and therefore is possibly incorrect and/or invalid:
--  WARNING The following SQL may change each run and therefore is possibly incorrect and/or invalid:
ALTER TABLE connectormigrate.domibus_connector_evidences DROP FOREIGN KEY domibus_connector_evidences_ibfk_1;

--  WARNING The following SQL may change each run and therefore is possibly incorrect and/or invalid:
ALTER TABLE connectormigrate.domibus_connector_message_info DROP FOREIGN KEY domibus_connector_message_info_ibfk_1;

ALTER TABLE connectormigrate.domibus_connector_message_info DROP FOREIGN KEY domibus_connector_message_info_ibfk_2;

ALTER TABLE connectormigrate.domibus_connector_message_info DROP FOREIGN KEY domibus_connector_message_info_ibfk_3;

ALTER TABLE connectormigrate.domibus_connector_message_info DROP FOREIGN KEY domibus_connector_message_info_ibfk_4;

ALTER TABLE connectormigrate.domibus_connector_message_info DROP FOREIGN KEY domibus_connector_message_info_ibfk_5;

--  WARNING The following SQL may change each run and therefore is possibly incorrect and/or invalid:
--  WARNING The following SQL may change each run and therefore is possibly incorrect and/or invalid:
ALTER TABLE connectormigrate.domibus_connector_msg_error DROP FOREIGN KEY domibus_connector_msg_error_ibfk_1;

--  WARNING The following SQL may change each run and therefore is possibly incorrect and/or invalid:
INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_001.0_pre_setup', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 39, '7:60d088bad0f7515adbff6bd46353a67d', 'dropAllForeignKeyConstraints baseTableName=DOMIBUS_CONNECTOR_SERVICE; dropAllForeignKeyConstraints baseTableName=DOMIBUS_CONNECTOR_PARTY; dropAllForeignKeyConstraints baseTableName=DOMIBUS_CONNECTOR_ACTION; dropAllForeignKeyConstraints baseTableNa...', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_001.1_pre_setup_drop_primary_keys::StephanSpindler
--  Drop all primary keys, they are regenerated with named constraints
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_ACTION DROP PRIMARY KEY;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_001.1_pre_setup_drop_primary_keys', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 40, '7:8372c81b9e194590c62106a8cd7b4a3a', 'dropPrimaryKey tableName=DOMIBUS_CONNECTOR_ACTION', 'Drop all primary keys, they are regenerated with named constraints', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_001.2_drop_pk_evidences::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_EVIDENCES DROP PRIMARY KEY;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_001.2_drop_pk_evidences', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 41, '7:089581b46664568927b1db08090ebe55', 'dropPrimaryKey tableName=DOMIBUS_CONNECTOR_EVIDENCES', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_001.3_drop_pk_msg_info::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO DROP PRIMARY KEY;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_001.3_drop_pk_msg_info', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 42, '7:c3bfa97f02ab26457bca94d1059b1d76', 'dropPrimaryKey tableName=DOMIBUS_CONNECTOR_MESSAGE_INFO', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_001.4_drop_pk_messages::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGES DROP PRIMARY KEY;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_001.4_drop_pk_messages', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 43, '7:270743d2ca856fd8c34bd04d72b67460', 'dropPrimaryKey tableName=DOMIBUS_CONNECTOR_MESSAGES', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_001.5_pre_setup_drop_pk_msg_error::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MSG_ERROR DROP PRIMARY KEY;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_001.5_pre_setup_drop_pk_msg_error', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 44, '7:758ad4e8eeb59b5124052d6b865c477b', 'dropPrimaryKey tableName=DOMIBUS_CONNECTOR_MSG_ERROR', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_001.6_drop_pk_party::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_PARTY DROP PRIMARY KEY;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_001.6_drop_pk_party', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 45, '7:caf261ceacbf902e5364aa90e5d5e77d', 'dropPrimaryKey tableName=DOMIBUS_CONNECTOR_PARTY', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_001.7_drop_pk_seq_store::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_SEQ_STORE DROP PRIMARY KEY;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_001.7_drop_pk_seq_store', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 46, '7:cc41b40165700d67293b41332296c208', 'dropPrimaryKey tableName=DOMIBUS_CONNECTOR_SEQ_STORE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_001.8_drop_pk_service::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_SERVICE DROP PRIMARY KEY;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_001.8_drop_pk_service', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 47, '7:6d600648afbf6b883369a0c6a7262f31', 'dropPrimaryKey tableName=DOMIBUS_CONNECTOR_SERVICE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_001.9_drop_pk_webadmin_users::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_WEBADMIN_USER DROP PRIMARY KEY;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_001.9_drop_pk_webadmin_users', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 48, '7:8b516881cf50f0f410ea103fcc103043', 'dropPrimaryKey tableName=DOMIBUS_WEBADMIN_USER', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_001.32_pk_create_action::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_ACTION ADD PRIMARY KEY (ACTION);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_001.32_pk_create_action', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 49, '7:a3b013a6e03c1b7f051bc6491947cbe8', 'addPrimaryKey constraintName=PK_DOMIBUS_CONNECTOR_ACTION, tableName=DOMIBUS_CONNECTOR_ACTION', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_001.33_pk_create_message_info::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO ADD PRIMARY KEY (ID);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_001.33_pk_create_message_info', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 50, '7:9995e77a158b64e5582b66d152db1d7e', 'addPrimaryKey constraintName=PK_MSG_INFO, tableName=DOMIBUS_CONNECTOR_MESSAGE_INFO', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_001.34_pk_create_msg_error::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MSG_ERROR ADD PRIMARY KEY (ID);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_001.34_pk_create_msg_error', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 51, '7:daa0ac5929965ede105df3a3f98a4bac', 'addPrimaryKey constraintName=PK_MSG_ERROR, tableName=DOMIBUS_CONNECTOR_MSG_ERROR', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_001.35_pk_create_party::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_PARTY ADD PRIMARY KEY (PARTY_ID, ROLE);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_001.35_pk_create_party', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 52, '7:81497e82994f11ae105a249210914ce2', 'addPrimaryKey constraintName=PK_DOMIBUS_CONNECTOR_PARTY, tableName=DOMIBUS_CONNECTOR_PARTY', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_001.36_pk_create_seq_store::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_SEQ_STORE ADD PRIMARY KEY (SEQ_NAME);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_001.36_pk_create_seq_store', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 53, '7:3c70d1f8396c63018c2a93187603168c', 'addPrimaryKey constraintName=PK_DOMIBUS_CONNECTOR_SEQ_STORE, tableName=DOMIBUS_CONNECTOR_SEQ_STORE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_001.37_pk_create_service::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_SERVICE ADD PRIMARY KEY (SERVICE);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_001.37_pk_create_service', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 54, '7:3ec08b12032ca0cf9e89db044e66e8c1', 'addPrimaryKey constraintName=PK_DOMIBUS_CONNECTOR_SERVICE, tableName=DOMIBUS_CONNECTOR_SERVICE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_001_tb_DOMIBUS_CONNECTOR_SERVICE::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_SERVICE MODIFY SERVICE VARCHAR(255);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_SERVICE MODIFY SERVICE_TYPE VARCHAR(512);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_001_tb_DOMIBUS_CONNECTOR_SERVICE', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 55, '7:cc419bfe193d3a061879f75ad35b8c95', 'modifyDataType columnName=SERVICE, tableName=DOMIBUS_CONNECTOR_SERVICE; modifyDataType columnName=SERVICE_TYPE, tableName=DOMIBUS_CONNECTOR_SERVICE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_002_tb_DOMIBUS_CONNECTOR_PARTY::StephanSpinlder
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_PARTY MODIFY PARTY_ID VARCHAR(255);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_PARTY MODIFY ROLE VARCHAR(255);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_PARTY MODIFY PARTY_ID_TYPE VARCHAR(512);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_002_tb_DOMIBUS_CONNECTOR_PARTY', 'StephanSpinlder', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 56, '7:cd1591ffd545beccca7fd236fd8eb6d4', 'modifyDataType columnName=PARTY_ID, tableName=DOMIBUS_CONNECTOR_PARTY; modifyDataType columnName=ROLE, tableName=DOMIBUS_CONNECTOR_PARTY; modifyDataType columnName=PARTY_ID_TYPE, tableName=DOMIBUS_CONNECTOR_PARTY', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_003_tb_DOMIBUS_CONNECTOR_MSG_ERROR::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MSG_ERROR MODIFY ID numeric(10, 0);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MSG_ERROR MODIFY MESSAGE_ID numeric(10, 0);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MSG_ERROR MODIFY ERROR_MESSAGE VARCHAR(512);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MSG_ERROR MODIFY CREATED timestamp;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_003_tb_DOMIBUS_CONNECTOR_MSG_ERROR', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 57, '7:15b4e1cd399978d03658bac0a929253b', 'modifyDataType columnName=ID, tableName=DOMIBUS_CONNECTOR_MSG_ERROR; modifyDataType columnName=MESSAGE_ID, tableName=DOMIBUS_CONNECTOR_MSG_ERROR; modifyDataType columnName=ERROR_MESSAGE, tableName=DOMIBUS_CONNECTOR_MSG_ERROR; modifyDataType column...', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_003_tb_DOMIBUS_CONNECTOR_MSG_ERROR_mysql::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MSG_ERROR MODIFY DETAILED_TEXT LONGTEXT;

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MSG_ERROR MODIFY ERROR_SOURCE LONGTEXT;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_003_tb_DOMIBUS_CONNECTOR_MSG_ERROR_mysql', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 58, '7:75076db8079bf98471509f6b12b82bcb', 'modifyDataType columnName=DETAILED_TEXT, tableName=DOMIBUS_CONNECTOR_MSG_ERROR; modifyDataType columnName=ERROR_SOURCE, tableName=DOMIBUS_CONNECTOR_MSG_ERROR', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_004_tb_create_DOMIBUS_CONNECTOR_MSG_CONT::StephanSpindler
CREATE TABLE connectormigrate.DOMIBUS_CONNECTOR_MSG_CONT (ID numeric(10, 0) NOT NULL, MESSAGE_ID numeric(10, 0) NOT NULL, CONTENT_TYPE VARCHAR(255) NULL, CONTENT BLOB NULL, CHECKSUM LONGTEXT NULL, CREATED datetime NULL);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MSG_CONT ADD PRIMARY KEY (ID);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_004_tb_create_DOMIBUS_CONNECTOR_MSG_CONT', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 59, '7:e32d7a68984fca1676ce2e9c5b114671', 'createTable tableName=DOMIBUS_CONNECTOR_MSG_CONT; addPrimaryKey constraintName=PK_DOMIBUS_CONNECTOR_MSG__01, tableName=DOMIBUS_CONNECTOR_MSG_CONT', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_005_tb_update_DOMIBUS_CONNECTOR_MESSAGE_INFO::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO MODIFY ID numeric(10, 0);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO MODIFY MESSAGE_ID numeric(10, 0);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO MODIFY FROM_PARTY_ID VARCHAR(255);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO MODIFY FROM_PARTY_ROLE VARCHAR(255);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO MODIFY TO_PARTY_ID VARCHAR(255);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO MODIFY TO_PARTY_ROLE VARCHAR(255);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO MODIFY ORIGINAL_SENDER VARCHAR(255);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO MODIFY FINAL_RECIPIENT VARCHAR(255);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO MODIFY SERVICE VARCHAR(255);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO MODIFY ACTION VARCHAR(255);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO MODIFY CREATED timestamp;

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO MODIFY UPDATED timestamp;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_005_tb_update_DOMIBUS_CONNECTOR_MESSAGE_INFO', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 60, '7:6a593648e94572f00d485e2d59ba2b18', 'modifyDataType columnName=ID, tableName=DOMIBUS_CONNECTOR_MESSAGE_INFO; modifyDataType columnName=MESSAGE_ID, tableName=DOMIBUS_CONNECTOR_MESSAGE_INFO; modifyDataType columnName=FROM_PARTY_ID, tableName=DOMIBUS_CONNECTOR_MESSAGE_INFO; modifyDataTy...', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_005_tb_update_DOMIBUS_CONNECTOR_MESSAGE_INFO_add_message_id_unique::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT MSG_INFO_UNIQ_MSG_ID UNIQUE (MESSAGE_ID);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_005_tb_update_DOMIBUS_CONNECTOR_MESSAGE_INFO_add_message_id_unique', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 61, '7:e20525c0b67e6a8b43daa95a14b8af69', 'addUniqueConstraint constraintName=MSG_INFO_UNIQ_MSG_ID, tableName=DOMIBUS_CONNECTOR_MESSAGE_INFO', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_006_tb_rename_DOMIBUS_CONNECTOR_MESSAGE::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGES RENAME connectormigrate.DOMIBUS_CONNECTOR_MESSAGE;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_006_tb_rename_DOMIBUS_CONNECTOR_MESSAGE', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 62, '7:c1c6c87542e8303354a39c8a82d5e17a', 'renameTable newTableName=DOMIBUS_CONNECTOR_MESSAGE, oldTableName=DOMIBUS_CONNECTOR_MESSAGES', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_006_pk_create_message::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE ADD PRIMARY KEY (ID);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_006_pk_create_message', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 63, '7:745d4a6137ea5ff535a08864a96eeecf', 'addPrimaryKey constraintName=PK_DOMIBUS_CONNECTOR_MESSAGE, tableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_modify_id::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE MODIFY ID numeric(10, 0);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_modify_id', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 64, '7:68042aedb648b02064a266fc923f7de9', 'modifyDataType columnName=ID, tableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_rename_nat_message_id::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE CHANGE NAT_MESSAGE_ID BACKEND_MESSAGE_ID VARCHAR(255);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_rename_nat_message_id', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 65, '7:e1439e91951aef2a2e007ff7daea87a0', 'renameColumn newColumnName=BACKEND_MESSAGE_ID, oldColumnName=NAT_MESSAGE_ID, tableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_confirmed_timestamp::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE MODIFY CONFIRMED timestamp;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_confirmed_timestamp', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 66, '7:b6a79e3a75ba442373fc7338d47619d1', 'modifyDataType columnName=CONFIRMED, tableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_rejected_timestamp::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE MODIFY REJECTED timestamp;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_rejected_timestamp', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 67, '7:5515e7832911faab1f53915932e3a22f', 'modifyDataType columnName=REJECTED, tableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_delivered_gw_timestamp::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE MODIFY DELIVERED_GW timestamp;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_delivered_gw_timestamp', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 68, '7:1a6d6632f45b0e7c056ab28323a8b84f', 'modifyDataType columnName=DELIVERED_GW, tableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_updated_timestamp::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE MODIFY UPDATED timestamp;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_updated_timestamp', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 69, '7:4db6e0fb41c39b1b334359081bb1690f', 'modifyDataType columnName=UPDATED, tableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_delivered_backend_timestamp::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE CHANGE DELIVERED_NAT DELIVERED_BACKEND timestamp;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_delivered_backend_timestamp', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 70, '7:8abb8c2ac61d7eb1fa5437a2a4e365ec', 'renameColumn newColumnName=DELIVERED_BACKEND, oldColumnName=DELIVERED_NAT, tableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_add_Column_backend_name::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE ADD BACKEND_NAME VARCHAR(255) NULL;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_add_Column_backend_name', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 71, '7:f2ae16169673f3aa4d8ffee7dee4acc2', 'addColumn tableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_add_Column_connector_id::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE ADD CONNECTOR_MESSAGE_ID VARCHAR(255) NULL;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_add_Column_connector_id', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 72, '7:a6416c46ea06c214aba82ae2da25dad1', 'addColumn tableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_hashValue_mysql::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE MODIFY HASH_VALUE LONGTEXT;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_007_tb_update_DOMIBUS_CONNECTOR_MESSAGE_hashValue_mysql', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 73, '7:b558e4813485491e53ae861694695987', 'modifyDataType columnName=HASH_VALUE, tableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_008_tb_ren_DOMIBUS_CONNECTOR_EVIDENCE::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_EVIDENCES RENAME connectormigrate.DOMIBUS_CONNECTOR_EVIDENCE;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_008_tb_ren_DOMIBUS_CONNECTOR_EVIDENCE', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 74, '7:e380300222773aac95002fbc9e4d5919', 'renameTable newTableName=DOMIBUS_CONNECTOR_EVIDENCE, oldTableName=DOMIBUS_CONNECTOR_EVIDENCES', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_008_pk_create_evidence::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_EVIDENCE ADD PRIMARY KEY (ID);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_008_pk_create_evidence', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 75, '7:9d449e4665fcdb39bb818f2d741ebda8', 'addPrimaryKey constraintName=PK_DOMIBUS_CONNECTOR_EVIDENCE, tableName=DOMIBUS_CONNECTOR_EVIDENCE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_009_tb_update_DOMIBUS_CONNECTOR_EVIDENCE_mod_id_type::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_EVIDENCE MODIFY ID numeric(10, 0);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_009_tb_update_DOMIBUS_CONNECTOR_EVIDENCE_mod_id_type', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 76, '7:56d4b2142e837c6999e45dbcb1a0db3f', 'modifyDataType columnName=ID, tableName=DOMIBUS_CONNECTOR_EVIDENCE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_009_tb_update_DOMIBUS_CONNECTOR_EVIDENCE_mod_message_id_type::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_EVIDENCE MODIFY MESSAGE_ID numeric(10, 0);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_009_tb_update_DOMIBUS_CONNECTOR_EVIDENCE_mod_message_id_type', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 77, '7:6d8f4164680edbc2f1d3458d5884df3e', 'modifyDataType columnName=MESSAGE_ID, tableName=DOMIBUS_CONNECTOR_EVIDENCE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_009_tb_update_DOMIBUS_CONNECTOR_EVIDENCE_mode_evidence_mysql::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_EVIDENCE MODIFY EVIDENCE LONGTEXT;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_009_tb_update_DOMIBUS_CONNECTOR_EVIDENCE_mode_evidence_mysql', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 78, '7:f2b797d8d4ebe0efa5531f1abca8a58c', 'modifyDataType columnName=EVIDENCE, tableName=DOMIBUS_CONNECTOR_EVIDENCE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_009_tb_update_DOMIBUS_CONNECTOR_EVIDENCE_delivered_nat_timestamp::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_EVIDENCE MODIFY DELIVERED_NAT timestamp;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_009_tb_update_DOMIBUS_CONNECTOR_EVIDENCE_delivered_nat_timestamp', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 79, '7:1db1c26b51d0218f6e1db95f9ad8ea0a', 'modifyDataType columnName=DELIVERED_NAT, tableName=DOMIBUS_CONNECTOR_EVIDENCE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_009_tb_update_DOMIBUS_CONNECTOR_EVIDENCE_delivered_gw_timestamp::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_EVIDENCE MODIFY DELIVERED_GW timestamp;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_009_tb_update_DOMIBUS_CONNECTOR_EVIDENCE_delivered_gw_timestamp', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 80, '7:74953e5d599597a08ae2f6323fd85255', 'modifyDataType columnName=DELIVERED_GW, tableName=DOMIBUS_CONNECTOR_EVIDENCE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_011_tb_create_DOMIBUS_CONNECTOR_BACKEND_INFO::StephanSpindler
CREATE TABLE connectormigrate.DOMIBUS_CONNECTOR_BACKEND_INFO (ID numeric(10, 0) NOT NULL, BACKEND_NAME VARCHAR(255) NOT NULL, BACKEND_KEY_ALIAS VARCHAR(255) NOT NULL, BACKEND_KEY_PASS VARCHAR(255) NULL, BACKEND_SERVICE_TYPE VARCHAR(512) NULL, BACKEND_ENABLED BIT(1) DEFAULT 1 NULL, BACKEND_DEFAULT BIT(1) DEFAULT 0 NULL, BACKEND_DESCRIPTION LONGTEXT NULL, BACKEND_PUSH_ADDRESS LONGTEXT NULL);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_BACKEND_INFO ADD PRIMARY KEY (ID);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_BACKEND_INFO ADD CONSTRAINT UN_DOMIBUS_CONNECTOR_BACK_01 UNIQUE (BACKEND_NAME);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_BACKEND_INFO ADD CONSTRAINT UN_DOMIBUS_CONNECTOR_BACK_02 UNIQUE (BACKEND_KEY_ALIAS);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_011_tb_create_DOMIBUS_CONNECTOR_BACKEND_INFO', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 81, '7:a6b62dc64207f45e46e35c9e71db7984', 'createTable tableName=DOMIBUS_CONNECTOR_BACKEND_INFO; addPrimaryKey constraintName=PK_DOMIBUS_CONNECTOR_BACK_01, tableName=DOMIBUS_CONNECTOR_BACKEND_INFO; addUniqueConstraint constraintName=UN_DOMIBUS_CONNECTOR_BACK_01, tableName=DOMIBUS_CONNECTOR...', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_012_tb_create_DOMIBUS_CONNECTOR_BACK_2_S::StephanSpindler
CREATE TABLE connectormigrate.DOMIBUS_CONNECTOR_BACK_2_S (DOMIBUS_CONNECTOR_SERVICE_ID VARCHAR(255) NOT NULL, DOMIBUS_CONNECTOR_BACKEND_ID numeric(10, 0) NOT NULL);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_012_tb_create_DOMIBUS_CONNECTOR_BACK_2_S', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 82, '7:6738c564ddf1050b2293afcaa665476e', 'createTable tableName=DOMIBUS_CONNECTOR_BACK_2_S', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_012_tb_create_DOMIBUS_CONNECTOR_BACK_2_S_connector_to_service_rel_primary_key::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_BACK_2_S ADD PRIMARY KEY (DOMIBUS_CONNECTOR_SERVICE_ID, DOMIBUS_CONNECTOR_BACKEND_ID);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_012_tb_create_DOMIBUS_CONNECTOR_BACK_2_S_connector_to_service_rel_primary_key', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 83, '7:04dbbfcbaada5f2946fca49a6f3f33ff', 'addPrimaryKey constraintName=PK_DOMIBUS_CONN_01, tableName=DOMIBUS_CONNECTOR_BACK_2_S', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_013_tb_update_DOMIBUS_CONNECTOR_ACTION::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_ACTION MODIFY ACTION VARCHAR(255);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_013_tb_update_DOMIBUS_CONNECTOR_ACTION', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 84, '7:73a2f99a837c4e1bebda8e5bf9752624', 'modifyDataType columnName=ACTION, tableName=DOMIBUS_CONNECTOR_ACTION', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_013.1_tb_update_DOMIBUS_CONNECTOR_ACTION_pdf_required::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_ACTION ALTER PDF_REQUIRED SET DEFAULT 0;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_013.1_tb_update_DOMIBUS_CONNECTOR_ACTION_pdf_required', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 85, '7:41cc297db71acacdde9d22084b2d657f', 'addDefaultValue columnName=PDF_REQUIRED, tableName=DOMIBUS_CONNECTOR_ACTION', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_014_tb_create_DOMIBUS_CONNECTOR_CONT_TYPE::StephanSpindler
--  Create Content Type Table
CREATE TABLE connectormigrate.DOMIBUS_CONNECTOR_CONT_TYPE (MESSAGE_CONTENT_TYPE VARCHAR(255) NOT NULL);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_014_tb_create_DOMIBUS_CONNECTOR_CONT_TYPE', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 86, '7:e3afb29f162a824dd4dee01c440cb30c', 'createTable tableName=DOMIBUS_CONNECTOR_CONT_TYPE', 'Create Content Type Table', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_014_tb_create_DOMIBUS_CONNECTOR_CONT_TYPE_primary_key::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_CONT_TYPE ADD PRIMARY KEY (MESSAGE_CONTENT_TYPE);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_014_tb_create_DOMIBUS_CONNECTOR_CONT_TYPE_primary_key', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 87, '7:f46db93aa5eb5a2f0c591a34e82177f1', 'addPrimaryKey constraintName=PK_MSG_CONT_TYPE, tableName=DOMIBUS_CONNECTOR_CONT_TYPE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_014_tb_create_DOMIBUS_CONNECTOR_BIGDATA::StephanSpindler
CREATE TABLE connectormigrate.DOMIBUS_CONNECTOR_BIGDATA (ID VARCHAR(255) NOT NULL, CHECKSUM LONGTEXT NULL, CREATED datetime NULL, MESSAGE_ID DECIMAL(10, 0) NULL, LAST_ACCESS datetime NULL, NAME LONGTEXT NULL, CONTENT LONGBLOB NULL, MIMETYPE VARCHAR(255) NULL);

ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_BIGDATA ADD PRIMARY KEY (ID);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_014_tb_create_DOMIBUS_CONNECTOR_BIGDATA', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 88, '7:468c70ed80843f07fe1d2cc15a92449a', 'createTable tableName=DOMIBUS_CONNECTOR_BIGDATA; addPrimaryKey tableName=DOMIBUS_CONNECTOR_BIGDATA', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_014_tb_create_DOMIBUS_CONNECTOR_BIGDATA_constraints::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_BIGDATA ADD CONSTRAINT FK_DOMIBUS_CONNECTOR_BIGDATA FOREIGN KEY (MESSAGE_ID) REFERENCES connectormigrate.DOMIBUS_CONNECTOR_MESSAGE (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_014_tb_create_DOMIBUS_CONNECTOR_BIGDATA_constraints', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 89, '7:34f4a9d6269a7dfa4e99b88a0fa71ee7', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_BIGDATA, constraintName=FK_DOMIBUS_CONNECTOR_BIGDATA, referencedTableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_015_tb_modify_WEBADMIN_USER::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_WEBADMIN_USER MODIFY USERNAME VARCHAR(512);

ALTER TABLE connectormigrate.DOMIBUS_WEBADMIN_USER MODIFY PASSWORD VARCHAR(512);

ALTER TABLE connectormigrate.DOMIBUS_WEBADMIN_USER MODIFY SALT VARCHAR(255);

ALTER TABLE connectormigrate.DOMIBUS_WEBADMIN_USER MODIFY ROLE VARCHAR(255);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_015_tb_modify_WEBADMIN_USER', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 90, '7:46b4fb403df690ab3da9c2f2792a2059', 'modifyDataType columnName=USERNAME, tableName=DOMIBUS_WEBADMIN_USER; modifyDataType columnName=PASSWORD, tableName=DOMIBUS_WEBADMIN_USER; modifyDataType columnName=SALT, tableName=DOMIBUS_WEBADMIN_USER; modifyDataType columnName=ROLE, tableName=DO...', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_015_tb_modify_WEBADMIN_USER_primary_key::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_WEBADMIN_USER ADD PRIMARY KEY (USERNAME);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_015_tb_modify_WEBADMIN_USER_primary_key', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 91, '7:ffe8fb04b3fe31cf2c7d0eb0b32eb8ab', 'addPrimaryKey constraintName=PK_WEBADMIN_USER, tableName=DOMIBUS_WEBADMIN_USER', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_016_tb_rename_WEBADMIN_PROPERTIES_drop_pk::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_WEBADMIN_PROPERTIES DROP PRIMARY KEY;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_016_tb_rename_WEBADMIN_PROPERTIES_drop_pk', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 92, '7:a3585dc730e5a91a88038294a50dadd7', 'dropPrimaryKey tableName=DOMIBUS_WEBADMIN_PROPERTIES', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_016_tb_rename_WEBADMIN_PROPERTIES::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_WEBADMIN_PROPERTIES RENAME connectormigrate.DOMIBUS_WEBADMIN_PROPERTY;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_016_tb_rename_WEBADMIN_PROPERTIES', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 93, '7:5c82e5c5d0eec7e28f1adf5e26658ae2', 'renameTable newTableName=DOMIBUS_WEBADMIN_PROPERTY, oldTableName=DOMIBUS_WEBADMIN_PROPERTIES', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_016_tb_modify_WEBADMIN_PROPERTY_key_rename::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_WEBADMIN_PROPERTY CHANGE PROPERTIES_KEY `KEY` VARCHAR(512);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_016_tb_modify_WEBADMIN_PROPERTY_key_rename', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 94, '7:f18b6cb61006984d673c4daa5e26dcc2', 'renameColumn newColumnName=KEY, oldColumnName=PROPERTIES_KEY, tableName=DOMIBUS_WEBADMIN_PROPERTY', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_016_tb_modify_WEBADMIN_PROPERTY_key_mod::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_WEBADMIN_PROPERTY MODIFY `KEY` VARCHAR(512);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_016_tb_modify_WEBADMIN_PROPERTY_key_mod', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 95, '7:ce79445debb9c84c58f214e07b0be55a', 'modifyDataType columnName=KEY, tableName=DOMIBUS_WEBADMIN_PROPERTY', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_016_tb_modify_WEBADMIN_PROPERTY_value_rename::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_WEBADMIN_PROPERTY CHANGE PROPERTIES_VALUE VALUE VARCHAR(512);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_016_tb_modify_WEBADMIN_PROPERTY_value_rename', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 96, '7:8062dc7353c50c2201795dd223ec33e9', 'renameColumn newColumnName=VALUE, oldColumnName=PROPERTIES_VALUE, tableName=DOMIBUS_WEBADMIN_PROPERTY', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_016_tb_modify_WEBADMIN_PROPERTY_value_modify::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_WEBADMIN_PROPERTY MODIFY VALUE VARCHAR(512);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_016_tb_modify_WEBADMIN_PROPERTY_value_modify', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 97, '7:714434872462047a595ab79664d20357', 'modifyDataType columnName=VALUE, tableName=DOMIBUS_WEBADMIN_PROPERTY', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_016_tb_WEBADMIN_PROPERTY_add_primary_key::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_WEBADMIN_PROPERTY ADD PRIMARY KEY (`KEY`);

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_016_tb_WEBADMIN_PROPERTY_add_primary_key', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 98, '7:25128fd9a150c71e70370f18b56a6fae', 'addPrimaryKey constraintName=PK_WEBAMDIN_PROPERTY, tableName=DOMIBUS_WEBADMIN_PROPERTY', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_020_fk_create_msg_error_message::StephanSpindler
--  Create Foreign Key Message Error to Message
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MSG_ERROR ADD CONSTRAINT FK_DOMIBUS_CONNECTOR_MSG_ERROR FOREIGN KEY (MESSAGE_ID) REFERENCES connectormigrate.DOMIBUS_CONNECTOR_MESSAGE (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_020_fk_create_msg_error_message', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 99, '7:df71593e1116807cd89bc97b3b6269f9', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_MSG_ERROR, constraintName=FK_DOMIBUS_CONNECTOR_MSG_ERROR, referencedTableName=DOMIBUS_CONNECTOR_MESSAGE', 'Create Foreign Key Message Error to Message', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_022_fk_create_msg_cont_message::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MSG_CONT ADD CONSTRAINT FK_DOMIBUS_CONN_DOMIBUS_CON_04 FOREIGN KEY (MESSAGE_ID) REFERENCES connectormigrate.DOMIBUS_CONNECTOR_MESSAGE (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_022_fk_create_msg_cont_message', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 101, '7:79940f7647fa5b32ba163f93ec7dc443', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_MSG_CONT, constraintName=FK_DOMIBUS_CONN_DOMIBUS_CON_04, referencedTableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_023_fk_create_message_info_action_to_action_table::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_MSGI_ACTION FOREIGN KEY (ACTION) REFERENCES connectormigrate.DOMIBUS_CONNECTOR_ACTION (ACTION) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_023_fk_create_message_info_action_to_action_table', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 102, '7:453ab5dce64c3c230cdb60a1ccfbdc92', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_MESSAGE_INFO, constraintName=FK_MSGI_ACTION, referencedTableName=DOMIBUS_CONNECTOR_ACTION', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_024_fk_create_message_info_service_to_service_table::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_MSGI_SERVICE FOREIGN KEY (SERVICE) REFERENCES connectormigrate.DOMIBUS_CONNECTOR_SERVICE (service) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_024_fk_create_message_info_service_to_service_table', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 103, '7:41e846ad4a8eca879465fa8198b0c9ca', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_MESSAGE_INFO, constraintName=FK_MSGI_SERVICE, referencedTableName=DOMIBUS_CONNECTOR_SERVICE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_025_fk_create_message_info_from_party_to_party_table::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_MSGI_FROM_PARTY FOREIGN KEY (FROM_PARTY_ID, FROM_PARTY_ROLE) REFERENCES connectormigrate.DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_025_fk_create_message_info_from_party_to_party_table', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 104, '7:d77fccb07745b574ca4ca6677b5022cf', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_MESSAGE_INFO, constraintName=FK_MSGI_FROM_PARTY, referencedTableName=DOMIBUS_CONNECTOR_PARTY', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_026_fk_create_message_info_to_party_to_party_table::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_MSGI_TO_PARTY FOREIGN KEY (TO_PARTY_ID, TO_PARTY_ROLE) REFERENCES connectormigrate.DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_026_fk_create_message_info_to_party_to_party_table', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 105, '7:98fdcade76edbde1efea922f6517811e', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_MESSAGE_INFO, constraintName=FK_MSGI_TO_PARTY, referencedTableName=DOMIBUS_CONNECTOR_PARTY', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_027_fk_create_message_info_to_message::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_MESSAGE_INFO ADD CONSTRAINT FK_MSGI_MSGID FOREIGN KEY (MESSAGE_ID) REFERENCES connectormigrate.DOMIBUS_CONNECTOR_MESSAGE (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_027_fk_create_message_info_to_message', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 106, '7:589b37dcc0c9147a8994f2d6e449bf96', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_MESSAGE_INFO, constraintName=FK_MSGI_MSGID, referencedTableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_028_fk_create_evidence_to_message::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_EVIDENCE ADD CONSTRAINT FK_DOMIBUS_CONNECTOR_EVIDENCES FOREIGN KEY (MESSAGE_ID) REFERENCES connectormigrate.DOMIBUS_CONNECTOR_MESSAGE (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_028_fk_create_evidence_to_message', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 107, '7:78539cc81c1e1b40db24b041e763ece6', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_EVIDENCE, constraintName=FK_DOMIBUS_CONNECTOR_EVIDENCES, referencedTableName=DOMIBUS_CONNECTOR_MESSAGE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_029_fk_create_b2s_to_backend::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_BACK_2_S ADD CONSTRAINT FK_DOMIBUS_CONN_DOMIBUS_CON_01 FOREIGN KEY (DOMIBUS_CONNECTOR_BACKEND_ID) REFERENCES connectormigrate.DOMIBUS_CONNECTOR_BACKEND_INFO (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_029_fk_create_b2s_to_backend', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 108, '7:36227a8cd1e0cdb690047bac2a6c5a21', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_BACK_2_S, constraintName=FK_DOMIBUS_CONN_DOMIBUS_CON_01, referencedTableName=DOMIBUS_CONNECTOR_BACKEND_INFO', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::3to4_upgrade_030_fk_create_b2s_to_service::StephanSpindler
ALTER TABLE connectormigrate.DOMIBUS_CONNECTOR_BACK_2_S ADD CONSTRAINT FK_DOMIBUS_CONN_DOMIBUS_CON_02 FOREIGN KEY (DOMIBUS_CONNECTOR_SERVICE_ID) REFERENCES connectormigrate.DOMIBUS_CONNECTOR_SERVICE (SERVICE) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('3to4_upgrade_030_fk_create_b2s_to_service', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 109, '7:49f051a349b138a5e072780022152398', 'addForeignKeyConstraint baseTableName=DOMIBUS_CONNECTOR_BACK_2_S, constraintName=FK_DOMIBUS_CONN_DOMIBUS_CON_02, referencedTableName=DOMIBUS_CONNECTOR_SERVICE', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608');

--  Changeset db/changelog/v004/upgrade-3to4.xml::tag-db-domibus-4.0::StephanSpindler
INSERT INTO connectormigrate.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID, TAG) VALUES ('tag-db-domibus-4.0', 'StephanSpindler', 'db/changelog/v004/upgrade-3to4.xml', NOW(), 110, '7:9154cc53e107d3186ebc05f34ddc6844', 'tagDatabase', '', 'EXECUTED', NULL, NULL, '3.5.3', '3518077608', 'DOMIBUS_DB_V4.0');
