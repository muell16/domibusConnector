create table dc_db_version
(
    tag varchar(255) not null
        constraint pk_dc_db_version
        primary key
);

alter table dc_db_version
    owner to postgres;

create table dc_keystore
(
    id          numeric(10)  not null
        constraint pk_dc_keystore
        primary key,
    uuid        varchar(255) not null
        constraint uq_dc_keystore
        unique,
    keystore    bytea        not null,
    password    varchar(1024),
    uploaded    timestamp    not null,
    description varchar(512),
    type        varchar(50)
);

alter table dc_keystore
    owner to postgres;

create table dc_link_configuration
(
    id          numeric(10)  not null
        constraint pk_dc_link_configuration
        primary key,
    config_name varchar(255) not null
        constraint un_dc_link_conf_name_01
        unique,
    link_impl   varchar(255)
);

alter table dc_link_configuration
    owner to postgres;

create table dc_link_config_property
(
    dc_link_configuration_id numeric(10)  not null
        constraint fk_dc_link_conf_prop_01
        references dc_link_configuration,
    property_name            varchar(255) not null,
    property_value           text,
    constraint pk_dc_link_conf_prop
        primary key (dc_link_configuration_id, property_name)
);

alter table dc_link_config_property
    owner to postgres;

create table dc_link_partner
(
    id             numeric(10) not null
        constraint pk_dc_link_p
        primary key,
    name           varchar(50) not null
        constraint un_dc_link_p_01
        unique,
    description    text,
    enabled        boolean,
    link_config_id numeric(10)
        constraint fk_dc_link_p_01
        references dc_link_configuration,
    link_type      varchar(20),
    link_mode      varchar(20)
);

alter table dc_link_partner
    owner to postgres;

create table dc_link_partner_property
(
    dc_link_partner_id numeric(10)  not null
        constraint fk_dc_link_p_prop_01
        references dc_link_partner,
    property_name      varchar(255) not null,
    property_value     text,
    constraint pk_dc_link_p_prop
        primary key (dc_link_partner_id, property_name)
);

alter table dc_link_partner_property
    owner to postgres;

create table dc_message_lane
(
    id          numeric(10)  not null
        constraint pk_dc_msg_lane
        primary key,
    name        varchar(255) not null
        constraint un_dc_msg_lane_01
        unique,
    description text
);

alter table dc_message_lane
    owner to postgres;

create table dc_message_lane_property
(
    dc_message_lane_id numeric(10)  not null
        constraint fk_dc_msg_lane_prop_01
        references dc_message_lane,
    property_name      varchar(255) not null,
    property_value     text,
    constraint pk_dc_msg_lane_prop
        primary key (dc_message_lane_id, property_name)
);

alter table dc_message_lane_property
    owner to postgres;

create table dc_msgcnt_detsig
(
    id             numeric(10) not null
        constraint pk_detsig
        primary key,
    signature      text,
    signature_name varchar(255),
    signature_type varchar(255)
);

alter table dc_msgcnt_detsig
    owner to postgres;

create table dc_pmode_set
(
    id                numeric(10) not null
        constraint pk_dc_pmode_set
        primary key,
    fk_message_lane   numeric(10)
        constraint fk_dc_pmode_set_01
        references dc_message_lane,
    created           timestamp   not null,
    description       text,
    active            numeric(1)  not null,
    pmodes            bytea,
    fk_connectorstore numeric(10)
);

alter table dc_pmode_set
    owner to postgres;

create table dc_transport_step
(
    id                          numeric(10)  not null
        constraint pk_dc_transport_step
        primary key,
    connector_message_id        varchar(255) not null,
    link_partner_name           varchar(255) not null,
    attempt                     numeric(2)   not null,
    transport_id                varchar(255),
    transport_system_message_id varchar(255),
    remote_message_id           varchar(255),
    created                     timestamp,
    transported_message         text,
    final_state_reached         timestamp
);

alter table dc_transport_step
    owner to postgres;

create table dc_transport_step_status
(
    transport_step_id numeric(10) not null
        constraint fk_dc_trans_step_status_01
        references dc_transport_step,
    state             varchar(40) not null,
    created           timestamp,
    text              text,
    constraint pk_dc_trans_step_status
        primary key (transport_step_id, state)
);

alter table dc_transport_step_status
    owner to postgres;

create table domibus_connector_action
(
    id           numeric(10)  not null
        constraint pk_dc_action
        primary key,
    fk_pmode_set numeric(10)  not null
        constraint fk_dc_action_pmode_set_01
        references dc_pmode_set,
    action       varchar(255) not null
);

alter table domibus_connector_action
    owner to postgres;

create table domibus_connector_bigdata
(
    id                   varchar(255) not null
        constraint pk_dc_bigdata
        primary key,
    checksum             text,
    created              timestamp    not null,
    connector_message_id varchar(255),
    last_access          timestamp,
    name                 text,
    content              bytea,
    mimetype             varchar(255)
);

alter table domibus_connector_bigdata
    owner to postgres;

create table domibus_connector_message
(
    id                   numeric(10) not null
        constraint pk_dc_message
        primary key,
    ebms_message_id      varchar(255),
    backend_message_id   varchar(255)
        constraint uq_dc_msg_bck_msg_id
        unique,
    backend_name         varchar(255),
    connector_message_id varchar(255)
        constraint uq_dc_msg_con_msg_id
        unique,
    conversation_id      varchar(255),
    hash_value           text,
    confirmed            timestamp,
    rejected             timestamp,
    delivered_backend    timestamp,
    delivered_gw         timestamp,
    updated              timestamp   not null,
    created              timestamp   not null,
    direction_source     varchar(20),
    direction_target     varchar(20),
    gateway_name         varchar(255)
);

alter table domibus_connector_message
    owner to postgres;

create table domibus_connector_evidence
(
    id                   numeric(10) not null
        constraint pk_dc_evidence
        primary key,
    message_id           numeric(10) not null
        constraint fk_dc_evidences_01
        references domibus_connector_message,
    connector_message_id varchar(255),
    type                 varchar(255),
    evidence             text,
    delivered_nat        timestamp,
    delivered_gw         timestamp,
    updated              timestamp   not null
);

alter table domibus_connector_evidence
    owner to postgres;

create table domibus_connector_msg_cont
(
    id                    numeric(10) not null
        constraint pk_dc_msg_cont
        primary key,
    message_id            numeric(10) not null
        constraint fk_dc_con_01
        references domibus_connector_message,
    content_type          varchar(255),
    content               bytea,
    checksum              text,
    created               timestamp   not null,
    storage_provider_name varchar(255),
    storage_reference_id  varchar(512),
    digest                varchar(512),
    payload_name          varchar(512),
    payload_identifier    varchar(512),
    payload_description   text,
    payload_mimetype      varchar(255),
    payload_size          numeric(10),
    detached_signature_id numeric(10)
        constraint fk_dc_msg_cont_02
        references dc_msgcnt_detsig,
    deleted               timestamp,
    connector_message_id  varchar(512)
);

alter table domibus_connector_msg_cont
    owner to postgres;

create table domibus_connector_msg_error
(
    id            numeric(10)   not null
        constraint pk_dc_msg_error
        primary key,
    message_id    numeric(10)   not null
        constraint fk_dc_msg_error_01
        references domibus_connector_message,
    error_message varchar(2048) not null,
    detailed_text text,
    error_source  text,
    created       timestamp     not null
);

alter table domibus_connector_msg_error
    owner to postgres;

create table domibus_connector_party
(
    id            numeric(10)  not null
        constraint pk_dc_party
        primary key,
    fk_pmode_set  numeric(10)  not null
        constraint fk_dc_service_pmode_set_01
        references dc_pmode_set,
    identifier    varchar(255),
    party_id      varchar(255) not null,
    role          varchar(255) not null,
    party_id_type varchar(512) not null
);

alter table domibus_connector_party
    owner to postgres;

create table domibus_connector_property
(
    id             numeric(10)   not null
        constraint pk_domibus_conn_03
        primary key,
    property_name  varchar(2048) not null,
    property_value varchar(2048)
);

alter table domibus_connector_property
    owner to postgres;

create table domibus_connector_seq_store
(
    seq_name  varchar(255) not null
        constraint pk_dc_seq_store
        primary key,
    seq_value numeric(10)  not null
);

alter table domibus_connector_seq_store
    owner to postgres;

create table domibus_connector_service
(
    id           numeric(10)  not null
        constraint pk_dc_service
        primary key,
    fk_pmode_set numeric(10)  not null
        constraint fk_dc_service_pmode_set_01
        references dc_pmode_set,
    service      varchar(255) not null,
    service_type varchar(512)
);

alter table domibus_connector_service
    owner to postgres;

create table domibus_connector_message_info
(
    id               numeric(10) not null
        constraint pk_dc_message_info
        primary key,
    message_id       numeric(10) not null
        constraint fk_dc_msg_info_i
        references domibus_connector_message,
    fk_from_party_id numeric(10)
        constraint fk_dc_msg_info_f_party
        references domibus_connector_party,
    fk_to_party_id   numeric(10)
        constraint fk_dc_msg_info_t_party
        references domibus_connector_party,
    original_sender  varchar(2048),
    final_recipient  varchar(2048),
    fk_service       numeric(10)
        constraint fk_dc_msg_info_service
        references domibus_connector_service,
    fk_action        numeric(10)
        constraint fk_dc_msg_info_action
        references domibus_connector_action,
    created          timestamp   not null,
    updated          timestamp   not null
);

alter table domibus_connector_message_info
    owner to postgres;

create table domibus_connector_user
(
    id                     numeric(10)          not null
        constraint pk_dc_user
        primary key,
    username               varchar(50)          not null
        constraint username_unique
        unique,
    role                   varchar(50)          not null,
    locked                 numeric(1) default 0 not null,
    number_of_grace_logins numeric(2) default 5 not null,
    grace_logins_used      numeric(2) default 0 not null,
    created                timestamp            not null
);

alter table domibus_connector_user
    owner to postgres;

create table domibus_connector_user_pwd
(
    id          numeric(10)          not null
        constraint pk_dc_user_pwd
        primary key,
    user_id     numeric(10)          not null
        constraint fk_dc_user_pwd_01
        references domibus_connector_user,
    password    varchar(1024)        not null,
    salt        varchar(512)         not null,
    current_pwd numeric(1) default 0 not null,
    initial_pwd numeric(1) default 0 not null,
    created     timestamp            not null
);

alter table domibus_connector_user_pwd
    owner to postgres;

create table qrtz_job_details
(
    sched_name        varchar(120) not null,
    job_name          varchar(200) not null,
    job_group         varchar(200) not null,
    description       varchar(250),
    job_class_name    varchar(250) not null,
    is_durable        boolean      not null,
    is_nonconcurrent  boolean      not null,
    is_update_data    boolean      not null,
    requests_recovery boolean      not null,
    job_data          bytea,
    primary key (sched_name, job_name, job_group)
);

alter table qrtz_job_details
    owner to postgres;

create index idx_qrtz_j_req_recovery
    on qrtz_job_details (sched_name, requests_recovery);

create index idx_qrtz_j_grp
    on qrtz_job_details (sched_name, job_group);

create table qrtz_triggers
(
    sched_name     varchar(120) not null,
    trigger_name   varchar(200) not null,
    trigger_group  varchar(200) not null,
    job_name       varchar(200) not null,
    job_group      varchar(200) not null,
    description    varchar(250),
    next_fire_time bigint,
    prev_fire_time bigint,
    priority       integer,
    trigger_state  varchar(16)  not null,
    trigger_type   varchar(8)   not null,
    start_time     bigint       not null,
    end_time       bigint,
    calendar_name  varchar(200),
    misfire_instr  smallint,
    job_data       bytea,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, job_name, job_group) references qrtz_job_details
);

alter table qrtz_triggers
    owner to postgres;

create index idx_qrtz_t_j
    on qrtz_triggers (sched_name, job_name, job_group);

create index idx_qrtz_t_jg
    on qrtz_triggers (sched_name, job_group);

create index idx_qrtz_t_c
    on qrtz_triggers (sched_name, calendar_name);

create index idx_qrtz_t_g
    on qrtz_triggers (sched_name, trigger_group);

create index idx_qrtz_t_state
    on qrtz_triggers (sched_name, trigger_state);

create index idx_qrtz_t_n_state
    on qrtz_triggers (sched_name, trigger_name, trigger_group, trigger_state);

create index idx_qrtz_t_n_g_state
    on qrtz_triggers (sched_name, trigger_group, trigger_state);

create index idx_qrtz_t_next_fire_time
    on qrtz_triggers (sched_name, next_fire_time);

create index idx_qrtz_t_nft_st
    on qrtz_triggers (sched_name, trigger_state, next_fire_time);

create index idx_qrtz_t_nft_misfire
    on qrtz_triggers (sched_name, misfire_instr, next_fire_time);

create index idx_qrtz_t_nft_st_misfire
    on qrtz_triggers (sched_name, misfire_instr, next_fire_time, trigger_state);

create index idx_qrtz_t_nft_st_misfire_grp
    on qrtz_triggers (sched_name, misfire_instr, next_fire_time, trigger_group, trigger_state);

create table qrtz_simple_triggers
(
    sched_name      varchar(120) not null,
    trigger_name    varchar(200) not null,
    trigger_group   varchar(200) not null,
    repeat_count    bigint       not null,
    repeat_interval bigint       not null,
    times_triggered bigint       not null,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group) references qrtz_triggers
);

alter table qrtz_simple_triggers
    owner to postgres;

create table qrtz_cron_triggers
(
    sched_name      varchar(120) not null,
    trigger_name    varchar(200) not null,
    trigger_group   varchar(200) not null,
    cron_expression varchar(120) not null,
    time_zone_id    varchar(80),
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group) references qrtz_triggers
);

alter table qrtz_cron_triggers
    owner to postgres;

create table qrtz_simprop_triggers
(
    sched_name    varchar(120) not null,
    trigger_name  varchar(200) not null,
    trigger_group varchar(200) not null,
    str_prop_1    varchar(512),
    str_prop_2    varchar(512),
    str_prop_3    varchar(512),
    int_prop_1    integer,
    int_prop_2    integer,
    long_prop_1   bigint,
    long_prop_2   bigint,
    dec_prop_1    numeric(13, 4),
    dec_prop_2    numeric(13, 4),
    bool_prop_1   boolean,
    bool_prop_2   boolean,
    primary key (sched_name, trigger_name, trigger_group),
    constraint qrtz_simprop_triggers_sched_name_trigger_name_trigger_grou_fkey
        foreign key (sched_name, trigger_name, trigger_group) references qrtz_triggers
);

alter table qrtz_simprop_triggers
    owner to postgres;

create table qrtz_blob_triggers
(
    sched_name    varchar(120) not null,
    trigger_name  varchar(200) not null,
    trigger_group varchar(200) not null,
    blob_data     bytea,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group) references qrtz_triggers
);

alter table qrtz_blob_triggers
    owner to postgres;

create table qrtz_calendars
(
    sched_name    varchar(120) not null,
    calendar_name varchar(200) not null,
    calendar      bytea        not null,
    primary key (sched_name, calendar_name)
);

alter table qrtz_calendars
    owner to postgres;

create table qrtz_paused_trigger_grps
(
    sched_name    varchar(120) not null,
    trigger_group varchar(200) not null,
    primary key (sched_name, trigger_group)
);

alter table qrtz_paused_trigger_grps
    owner to postgres;

create table qrtz_fired_triggers
(
    sched_name        varchar(120) not null,
    entry_id          varchar(95)  not null,
    trigger_name      varchar(200) not null,
    trigger_group     varchar(200) not null,
    instance_name     varchar(200) not null,
    fired_time        bigint       not null,
    sched_time        bigint       not null,
    priority          integer      not null,
    state             varchar(16)  not null,
    job_name          varchar(200),
    job_group         varchar(200),
    is_nonconcurrent  boolean,
    requests_recovery boolean,
    primary key (sched_name, entry_id)
);

alter table qrtz_fired_triggers
    owner to postgres;

create index idx_qrtz_ft_trig_inst_name
    on qrtz_fired_triggers (sched_name, instance_name);

create index idx_qrtz_ft_inst_job_req_rcvry
    on qrtz_fired_triggers (sched_name, instance_name, requests_recovery);

create index idx_qrtz_ft_j_g
    on qrtz_fired_triggers (sched_name, job_name, job_group);

create index idx_qrtz_ft_jg
    on qrtz_fired_triggers (sched_name, job_group);

create index idx_qrtz_ft_t_g
    on qrtz_fired_triggers (sched_name, trigger_name, trigger_group);

create index idx_qrtz_ft_tg
    on qrtz_fired_triggers (sched_name, trigger_group);

create table qrtz_scheduler_state
(
    sched_name        varchar(120) not null,
    instance_name     varchar(200) not null,
    last_checkin_time bigint       not null,
    checkin_interval  bigint       not null,
    primary key (sched_name, instance_name)
);

alter table qrtz_scheduler_state
    owner to postgres;

create table qrtz_locks
(
    sched_name varchar(120) not null,
    lock_name  varchar(40)  not null,
    primary key (sched_name, lock_name)
);

alter table qrtz_locks
    owner to postgres;

