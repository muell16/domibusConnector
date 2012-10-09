-- backend

create table Message (
        idMessage integer not null,
        deleted bit,
        directory longtext,
        downloaded bit,
        messageDate datetime,
        messageUID varchar(128),
        pmode varchar(128),
        primary key (idMessage)
    );

    create table Payload (
        idPayload integer not null,
        fileName longtext,
        idMessage integer,
        primary key (idPayload)
    );

    alter table Payload 
        add index FK3454796EA6FCC7E9 (idMessage), 
        add constraint FK3454796EA6FCC7E9 
        foreign key (idMessage) 
        references Message (idMessage);

    create table SEQUENCE_TABLE (
         SEQ_NAME varchar(255),
         SEQ_COUNT integer 
    ) ;

-- ebms3

 create table Attachments (
        ID varchar(255) not null,
        ContentID varchar(255),
        ContentType varchar(255),
        FilePath varchar(255),
        primary key (ID)
    );

    create table Msg_Callback (
        ID varchar(255) not null,
        Callback_Class varchar(255),
        LegNumber integer,
        MessageId varchar(255),
        pmode varchar(255),
        primary key (ID)
    );

    create table Receipt_Tracking (
        ID varchar(255) not null,
        MessageId varchar(255) unique,
        PMode varchar(255),
        Receipt_Received bit,
        Receipt longtext,
        toURL varchar(255),
        request_ID varchar(255),
        primary key (ID)
    );

    create table Receipts (
        Receipt_ID varchar(255) not null,
        messageId varchar(255),
        Non_Repudiation_Info longtext,
        refToMessaeId varchar(255),
        Sent bit,
        timestamp datetime,
        To_URL varchar(255),
        primary key (Receipt_ID)
    );

    create table Received_UserMsg (
        ID varchar(255) not null,
        Content_Type varchar(255),
        Mime_File varchar(255),
        Msg_Context longblob,
        ebms3_Action varchar(255),
        From_Party varchar(255),
        MessageId varchar(255),
        MPC varchar(255),
        Msg_InfoSet longblob,
        RefToMessageId varchar(255),
        ebms3_Service varchar(255),
        To_Party varchar(255),
        primary key (ID)
    );

    create table Received_UserMsg_Attachments (
        Received_UserMsg_ID varchar(255) not null,
        attachments_ID varchar(255) not null,
        unique (attachments_ID)
    );

    create table Sync_Responses (
        ID varchar(255) not null,
        Content_Type varchar(255),
        Mime_File varchar(255),
        Msg_Context longblob,
        MEP varchar(255),
        MPC varchar(255),
        Msg_InfoSet longblob,
        PMode varchar(255),
        Sent bit,
        Time_In_Millis bigint,
        primary key (ID)
    );

    create table Sync_Responses_Attachments (
        Sync_Responses_ID varchar(255) not null,
        attachments_ID varchar(255) not null,
        unique (attachments_ID)
    );

    create table UserMsg_Pull (
        ID varchar(255) not null,
        Content_Type varchar(255),
        Mime_File varchar(255),
        Msg_Context longblob,
        MPC varchar(255),
        Msg_InfoSet longblob,
        PMode varchar(255),
        Pulled bit,
        Time_In_Millis bigint,
        primary key (ID)
    );

    create table UserMsg_Pull_Attachments (
        UserMsg_Pull_ID varchar(255) not null,
        attachments_ID varchar(255) not null,
        unique (attachments_ID)
    );

    create table UserMsg_Push (
        ID varchar(255) not null,
        Content_Type varchar(255),
        Mime_File varchar(255),
        Msg_Context longblob,
        Callback_Class varchar(255),
        LegNumber integer,
        MEP varchar(255),
        Msg_InfoSet longblob,
        PMode varchar(255),
        Pushed bit,
        Time_In_Millis bigint,
        toURL varchar(255),
        primary key (ID)
    );

    create table UserMsg_Push_Attachments (
        UserMsg_Push_ID varchar(255) not null,
        attachments_ID varchar(255) not null,
        unique (attachments_ID)
    );

    alter table Receipt_Tracking 
        add index FK78EC851E85558B8E (request_ID), 
        add constraint FK78EC851E85558B8E 
        foreign key (request_ID) 
        references UserMsg_Push (ID);

    alter table Received_UserMsg_Attachments 
        add index FK84D1352918A1EF0F (Received_UserMsg_ID), 
        add constraint FK84D1352918A1EF0F 
        foreign key (Received_UserMsg_ID) 
        references Received_UserMsg (ID);

    alter table Received_UserMsg_Attachments 
        add index FK84D135295885AD52 (attachments_ID), 
        add constraint FK84D135295885AD52 
        foreign key (attachments_ID) 
        references Attachments (ID);

    alter table Sync_Responses_Attachments 
        add index FK6084EC7F10131110 (Sync_Responses_ID), 
        add constraint FK6084EC7F10131110 
        foreign key (Sync_Responses_ID) 
        references Sync_Responses (ID);

    alter table Sync_Responses_Attachments 
        add index FK6084EC7F5885AD52 (attachments_ID), 
        add constraint FK6084EC7F5885AD52 
        foreign key (attachments_ID) 
        references Attachments (ID);

    alter table UserMsg_Pull_Attachments 
        add index FK4E9D823F5885AD52 (attachments_ID), 
        add constraint FK4E9D823F5885AD52 
        foreign key (attachments_ID) 
        references Attachments (ID);

    alter table UserMsg_Pull_Attachments 
        add index FK4E9D823FDAD13FDA (UserMsg_Pull_ID), 
        add constraint FK4E9D823FDAD13FDA 
        foreign key (UserMsg_Pull_ID) 
        references UserMsg_Pull (ID);

    alter table UserMsg_Push_Attachments 
        add index FKC1A1EB945885AD52 (attachments_ID), 
        add constraint FKC1A1EB945885AD52 
        foreign key (attachments_ID) 
        references Attachments (ID);

    alter table UserMsg_Push_Attachments 
        add index FKC1A1EB94DB3213BA (UserMsg_Push_ID), 
        add constraint FKC1A1EB94DB3213BA 
        foreign key (UserMsg_Push_ID) 
        references UserMsg_Push (ID);

-- logging

create table LoggerEvent (
        Id varchar(255) not null,
        LOGDate datetime,
        Log_ClassName varchar(255),
        Log_LineNumber varchar(255),
        Log_MethodName varchar(255),
        Logger varchar(255),
        Msg longtext,
        Priority varchar(255),
        primary key (Id)
    );

    create table LoggerMessage (
        id varchar(255) not null,
        action varchar(255),
        conversationId varchar(255),
        fromRole varchar(255),
        messageId varchar(255),
        pmode varchar(255),
        recipient varchar(255),
        sender varchar(255),
        service varchar(255),
        status varchar(255),
        timestamp datetime,
        toRole varchar(255),
        primary key (id)
    );

-- reliability

create table Acks (
        CallbackAck_ID varchar(255) not null,
        AckTo varchar(255),
        GroupId varchar(255),
        Sent bit,
        Seq_Number varchar(255),
        primary key (CallbackAck_ID)
    );

    create table Business_Responses (
        BR_ID varchar(255) not null,
        Content_Type varchar(255),
        Mime_File varchar(255),
        Msg_Context longblob,
        GroupId varchar(255),
        Sequence_Number varchar(255),
        primary key (BR_ID)
    );

    create table Business_Responses_Attachments (
        Business_Responses_BR_ID varchar(255) not null,
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
        add index FK8BF9E7E45BCEE911 (Business_Responses_BR_ID), 
        add constraint FK8BF9E7E45BCEE911 
        foreign key (Business_Responses_BR_ID) 
        references Business_Responses (BR_ID);

    alter table Business_Responses_Attachments 
        add index FK8BF9E7E45885AD52 (attachments_ID), 
        add constraint FK8BF9E7E45885AD52 
        foreign key (attachments_ID) 
        references Attachments (ID);

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