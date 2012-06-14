create table Acks (
        CallbackAck_ID varchar(255) not null,
        AckTo varchar(255),
        GroupId varchar(255),
        Sent bit,
        Seq_Number varchar(255),
        primary key (CallbackAck_ID)
    );

    create table Attachments (
        ID varchar(255) not null,
        ContentID varchar(255),
        ContentType varchar(255),
        FilePath varchar(255),
        primary key (ID)
    );

    create table Business_Responses (
        BusinessResponse_ID varchar(255) not null,
        Content_Type varchar(255),
        Mime_File varchar(255),
        Msg_Context longblob,
        GroupId varchar(255),
        Sequence_Number varchar(255),
        primary key (BusinessResponse_ID)
    );

    create table Business_Responses_Attachments (
        Business_Responses_BusinessResponse_ID varchar(255) not null,
        attachments_ID varchar(255) not null,
        unique (attachments_ID)
    );

    create table Delivered_Ranges (
        DeliveredRange_ID varchar(255) not null,
        GroupId varchar(255),
        Max_Seq integer,
        Min_Seq integer,
        primary key (DeliveredRange_ID)
    );

    create table Ordered_Msg (
        OMessage_ID varchar(255) not null,
        Content_Type varchar(255),
        Mime_File varchar(255),
        Msg_Context longblob,
        Delivered bit,
        Expirytime datetime,
        Faulted bit,
        GroupId varchar(255),
        Seq_Number integer,
        Service_URL varchar(255),
        primary key (OMessage_ID)
    );

    create table Ordered_Msg_Attachments (
        Ordered_Msg_OMessage_ID varchar(255) not null,
        attachments_ID varchar(255) not null,
        unique (attachments_ID)
    );

    create table Received_Ranges (
        ReceivedRange_ID varchar(255) not null,
        GroupId varchar(255),
        Max_Seq integer,
        Min_Seq integer,
        primary key (ReceivedRange_ID)
    );

    create table Receiver_Groups (
        Group_ID varchar(255) not null,
        Closed bit,
        Complete bit,
        Delivered_Count integer,
        Group_Expritytime_UTC varchar(255),
        GroupId varchar(255),
        Max_Idle_Duration varchar(255),
        Highest_Seq_Received integer,
        Last_Delivered_Seq integer,
        Last_Msg_timestamp datetime,
        Max_Msg_ExpiryTime datetime,
        Ordered bit,
        Removed bit,
        primary key (Group_ID)
    );

    create table Reliability (
        Reliability_ID varchar(255) not null,
        AckReply varchar(255),
        ackReplyElement tinyblob,
        AckTo varchar(255),
        AtLeastOnce bit,
        AtMostOnce bit,
        Exponential_Backoff bit,
        InOrder bit,
        Maximum_Retransmission_Count integer,
        Name varchar(255),
        Retransmission_Interval integer,
        Retransmit_Callback varchar(255),
        primary key (Reliability_ID)
    );

    create table Retransmit_Msg (
        GMessage_ID varchar(255) not null,
        Content_Type varchar(255),
        Mime_File varchar(255),
        Msg_Context longblob,
        Acknowledged bit,
        Delivery_Failed bit,
        Expirytime datetime,
        Exp_Backoff bit,
        Faulted bit,
        GroupId varchar(255),
        Max_Retrans_Count integer,
        Send_To varchar(255),
        Reply_Pattern varchar(255),
        Resend_Count integer,
        Retrans_Interval integer,
        Retransmit_Callback varchar(255),
        Seq_Number integer,
        Time_To_Send bigint,
        primary key (GMessage_ID)
    );

    create table Retransmit_Msg_Attachments (
        Retransmit_Msg_GMessage_ID varchar(255) not null,
        attachments_ID varchar(255) not null,
        unique (attachments_ID)
    );

    create table Sender_Groups (
        Group_ID varchar(255) not null,
        Acknowledged_Count integer,
        Capacity integer,
        Closed bit,
        Current_Seq integer,
        Date_Started datetime,
        Failed_Count integer,
        GroupId varchar(255),
        Group_Lifetime varchar(255),
        Max_Idle_Duration varchar(255),
        Last_Msg_timestamp datetime,
        Max_Msg_ExpiryTime datetime,
        Mesg_Lifetime varchar(255),
        Name varchar(255),
        quality_Reliability_ID varchar(255),
        primary key (Group_ID)
    );

    alter table Business_Responses_Attachments 
        add index FK8BF9E7E45885AD52 (attachments_ID), 
        add constraint FK8BF9E7E45885AD52 
        foreign key (attachments_ID) 
        references Attachments (ID);

    alter table Business_Responses_Attachments 
        add index FK8BF9E7E4550C94C0 (Business_Responses_BusinessResponse_ID), 
        add constraint FK8BF9E7E4550C94C0 
        foreign key (Business_Responses_BusinessResponse_ID) 
        references Business_Responses (BusinessResponse_ID);

    alter table Ordered_Msg_Attachments 
        add index FKFD00A3C0112E73C (Ordered_Msg_OMessage_ID), 
        add constraint FKFD00A3C0112E73C 
        foreign key (Ordered_Msg_OMessage_ID) 
        references Ordered_Msg (OMessage_ID);

    alter table Ordered_Msg_Attachments 
        add index FKFD00A3C05885AD52 (attachments_ID), 
        add constraint FKFD00A3C05885AD52 
        foreign key (attachments_ID) 
        references Attachments (ID);

    alter table Retransmit_Msg_Attachments 
        add index FK63ABC176768643B2 (Retransmit_Msg_GMessage_ID), 
        add constraint FK63ABC176768643B2 
        foreign key (Retransmit_Msg_GMessage_ID) 
        references Retransmit_Msg (GMessage_ID);

    alter table Retransmit_Msg_Attachments 
        add index FK63ABC1765885AD52 (attachments_ID), 
        add constraint FK63ABC1765885AD52 
        foreign key (attachments_ID) 
        references Attachments (ID);

    alter table Sender_Groups 
        add index FKA6DEE31E894F77F3 (quality_Reliability_ID), 
        add constraint FKA6DEE31E894F77F3 
        foreign key (quality_Reliability_ID) 
        references Reliability (Reliability_ID);
