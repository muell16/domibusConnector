-- backend

create table Message (
        idMessage number(10,0) not null,
        deleted number(1,0),
        directory varchar2(1024 char),
        downloaded number(1,0),
        messageDate timestamp,
        messageUID varchar2(128 char),
        pmode varchar2(128 char),
        primary key (idMessage)
    );

    create table Payload (
        idPayload number(10,0) not null,
        fileName varchar2(256 char),
        idMessage number(10,0),
        primary key (idPayload)
    );

    alter table Payload 
        add constraint FK3454796EA6FCC7E9 
        foreign key (idMessage) 
        references Message;

    create table SEQUENCE_TABLE (
         SEQ_NAME varchar2(255 char),
         SEQ_COUNT number(10,0) 
    ) ;

-- ebms3

create table Attachments (
        ID varchar2(255 char) not null,
        ContentID varchar2(255 char),
        ContentType varchar2(255 char),
        FilePath varchar2(255 char),
        primary key (ID)
    );

    create table Msg_Callback (
        ID varchar2(255 char) not null,
        Callback_Class varchar2(255 char),
        LegNumber number(10,0),
        MessageId varchar2(255 char),
        pmode varchar2(255 char),
        primary key (ID)
    );

    create table Receipt_Tracking (
        ID varchar2(255 char) not null,
        MessageId varchar2(255 char) unique,
        PMode varchar2(255 char),
        Receipt_Received number(1,0),
        Receipt clob,
        toURL varchar2(255 char),
        request_ID varchar2(255 char),
        primary key (ID)
    );

    create table Receipts (
        Receipt_ID varchar2(255 char) not null,
        messageId varchar2(255 char),
        Non_Repudiation_Info clob,
        refToMessaeId varchar2(255 char),
        Sent number(1,0),
        timestamp timestamp,
        To_URL varchar2(255 char),
        primary key (Receipt_ID)
    );

    create table Received_UserMsg (
        ID varchar2(255 char) not null,
        Content_Type varchar2(255 char),
        Mime_File varchar2(255 char),
        Msg_Context blob,
        ebms3_Action varchar2(255 char),
        From_Party varchar2(255 char),
        MessageId varchar2(255 char),
        MPC varchar2(255 char),
        Msg_InfoSet blob,
        RefToMessageId varchar2(255 char),
        ebms3_Service varchar2(255 char),
        To_Party varchar2(255 char),
        primary key (ID)
    );

    create table Received_UserMsg_Attachments (
        Received_UserMsg_ID varchar2(255 char) not null,
        attachments_ID varchar2(255 char) not null,
        unique (attachments_ID)
    );

    create table Sync_Responses (
        ID varchar2(255 char) not null,
        Content_Type varchar2(255 char),
        Mime_File varchar2(255 char),
        Msg_Context blob,
        MEP varchar2(255 char),
        MPC varchar2(255 char),
        Msg_InfoSet blob,
        PMode varchar2(255 char),
        Sent number(1,0),
        Time_In_Millis number(19,0),
        primary key (ID)
    );

    create table Sync_Responses_Attachments (
        Sync_Responses_ID varchar2(255 char) not null,
        attachments_ID varchar2(255 char) not null,
        unique (attachments_ID)
    );

    create table UserMsg_Pull (
        ID varchar2(255 char) not null,
        Content_Type varchar2(255 char),
        Mime_File varchar2(255 char),
        Msg_Context blob,
        MPC varchar2(255 char),
        Msg_InfoSet blob,
        PMode varchar2(255 char),
        Pulled number(1,0),
        Time_In_Millis number(19,0),
        primary key (ID)
    );

    create table UserMsg_Pull_Attachments (
        UserMsg_Pull_ID varchar2(255 char) not null,
        attachments_ID varchar2(255 char) not null,
        unique (attachments_ID)
    );

    create table UserMsg_Push (
        ID varchar2(255 char) not null,
        Content_Type varchar2(255 char),
        Mime_File varchar2(255 char),
        Msg_Context blob,
        Callback_Class varchar2(255 char),
        LegNumber number(10,0),
        MEP varchar2(255 char),
        Msg_InfoSet blob,
        PMode varchar2(255 char),
        Pushed number(1,0),
        Time_In_Millis number(19,0),
        toURL varchar2(255 char),
        primary key (ID)
    );

    create table UserMsg_Push_Attachments (
        UserMsg_Push_ID varchar2(255 char) not null,
        attachments_ID varchar2(255 char) not null,
        unique (attachments_ID)
    );

    alter table Receipt_Tracking 
        add constraint FK78EC851E85558B8E 
        foreign key (request_ID) 
        references UserMsg_Push;

    alter table Received_UserMsg_Attachments 
        add constraint FK84D1352918A1EF0F 
        foreign key (Received_UserMsg_ID) 
        references Received_UserMsg;

    alter table Received_UserMsg_Attachments 
        add constraint FK84D135295885AD52 
        foreign key (attachments_ID) 
        references Attachments;

    alter table Sync_Responses_Attachments 
        add constraint FK6084EC7F10131110 
        foreign key (Sync_Responses_ID) 
        references Sync_Responses;

    alter table Sync_Responses_Attachments 
        add constraint FK6084EC7F5885AD52 
        foreign key (attachments_ID) 
        references Attachments;

    alter table UserMsg_Pull_Attachments 
        add constraint FK4E9D823F5885AD52 
        foreign key (attachments_ID) 
        references Attachments;

    alter table UserMsg_Pull_Attachments 
        add constraint FK4E9D823FDAD13FDA 
        foreign key (UserMsg_Pull_ID) 
        references UserMsg_Pull;

    alter table UserMsg_Push_Attachments 
        add constraint FKC1A1EB945885AD52 
        foreign key (attachments_ID) 
        references Attachments;

    alter table UserMsg_Push_Attachments 
        add constraint FKC1A1EB94DB3213BA 
        foreign key (UserMsg_Push_ID) 
        references UserMsg_Push;


-- logging

	create table LoggerEvent (
        Id varchar2(255 char) not null,
        LOGDate timestamp,
        Log_ClassName varchar2(255 char),
        Log_LineNumber varchar2(255 char),
        Log_MethodName varchar2(255 char),
        Logger varchar2(255 char),
        Msg varchar2(2000 char),
        Priority varchar2(255 char),
        primary key (Id)
    );

    create table LoggerMessage (
        id varchar2(255 char) not null,
        action varchar2(255 char),
        conversationId varchar2(255 char),
        fromRole varchar2(255 char),
        messageId varchar2(255 char),
        pmode varchar2(255 char),
        recipient varchar2(255 char),
        sender varchar2(255 char),
        service varchar2(255 char),
        status varchar2(255 char),
        timestamp timestamp,
        toRole varchar2(255 char),
        primary key (id)
    );

-- reliability
    
    create table Acks (
        CallbackAck_ID varchar2(255 char) not null,
        AckTo varchar2(255 char),
        GroupId varchar2(255 char),
        Sent number(1,0),
        Seq_Number varchar2(255 char),
        primary key (CallbackAck_ID)
    );

    create table Business_Responses (
        BR_ID varchar2(255 char) not null,
        Content_Type varchar2(255 char),
        Mime_File varchar2(255 char),
        Msg_Context blob,
        GroupId varchar2(255 char),
        Sequence_Number varchar2(255 char),
        primary key (BR_ID)
    );

    create table Business_Responses_Attachments (
        Business_Responses_BR_ID varchar2(255 char) not null,
        attachments_ID varchar2(255 char) not null,
        unique (attachments_ID)
    );

    create table Delivered_Ranges (
        DeliveredRange_ID varchar2(255 char) not null,
        GroupId varchar2(255 char),
        Max_Seq number(10,0),
        Min_Seq number(10,0),
        primary key (DeliveredRange_ID)
    );

    create table Ordered_Msg (
        OMessage_ID varchar2(255 char) not null,
        Content_Type varchar2(255 char),
        Mime_File varchar2(255 char),
        Msg_Context blob,
        Delivered number(1,0),
        Expirytime timestamp,
        Faulted number(1,0),
        GroupId varchar2(255 char),
        Seq_Number number(10,0),
        Service_URL varchar2(255 char),
        primary key (OMessage_ID)
    );

    create table Ordered_Msg_Attachments (
        Ordered_Msg_OMessage_ID varchar2(255 char) not null,
        attachments_ID varchar2(255 char) not null,
        unique (attachments_ID)
    );

    create table Received_Ranges (
        ReceivedRange_ID varchar2(255 char) not null,
        GroupId varchar2(255 char),
        Max_Seq number(10,0),
        Min_Seq number(10,0),
        primary key (ReceivedRange_ID)
    );

    create table Receiver_Groups (
        Group_ID varchar2(255 char) not null,
        Closed number(1,0),
        Complete number(1,0),
        Delivered_Count number(10,0),
        Group_Expritytime_UTC varchar2(255 char),
        GroupId varchar2(255 char),
        Max_Idle_Duration varchar2(255 char),
        Highest_Seq_Received number(10,0),
        Last_Delivered_Seq number(10,0),
        Last_Msg_timestamp timestamp,
        Max_Msg_ExpiryTime timestamp,
        Ordered number(1,0),
        Removed number(1,0),
        primary key (Group_ID)
    );

    create table Reliability (
        Reliability_ID varchar2(255 char) not null,
        AckReply varchar2(255 char),
        ackReplyElement raw(255),
        AckTo varchar2(255 char),
        AtLeastOnce number(1,0),
        AtMostOnce number(1,0),
        Exponential_Backoff number(1,0),
        InOrder number(1,0),
        Maximum_Retransmission_Count number(10,0),
        Name varchar2(255 char),
        Retransmission_Interval number(10,0),
        Retransmit_Callback varchar2(255 char),
        primary key (Reliability_ID)
    );

    create table Retransmit_Msg (
        GMessage_ID varchar2(255 char) not null,
        Content_Type varchar2(255 char),
        Mime_File varchar2(255 char),
        Msg_Context blob,
        Acknowledged number(1,0),
        Delivery_Failed number(1,0),
        Expirytime timestamp,
        Exp_Backoff number(1,0),
        Faulted number(1,0),
        GroupId varchar2(255 char),
        Max_Retrans_Count number(10,0),
        Send_To varchar2(255 char),
        Reply_Pattern varchar2(255 char),
        Resend_Count number(10,0),
        Retrans_Interval number(10,0),
        Retransmit_Callback varchar2(255 char),
        Seq_Number number(10,0),
        Time_To_Send number(19,0),
        primary key (GMessage_ID)
    );

    create table Retransmit_Msg_Attachments (
        Retransmit_Msg_GMessage_ID varchar2(255 char) not null,
        attachments_ID varchar2(255 char) not null,
        unique (attachments_ID)
    );

    create table Sender_Groups (
        Group_ID varchar2(255 char) not null,
        Acknowledged_Count number(10,0),
        Capacity number(10,0),
        Closed number(1,0),
        Current_Seq number(10,0),
        Date_Started timestamp,
        Failed_Count number(10,0),
        GroupId varchar2(255 char),
        Group_Lifetime varchar2(255 char),
        Max_Idle_Duration varchar2(255 char),
        Last_Msg_timestamp timestamp,
        Max_Msg_ExpiryTime timestamp,
        Mesg_Lifetime varchar2(255 char),
        Name varchar2(255 char),
        quality_Reliability_ID varchar2(255 char),
        primary key (Group_ID)
    );

    alter table Business_Responses_Attachments 
        add constraint FK8BF9E7E45BCEE911 
        foreign key (Business_Responses_BR_ID) 
        references Business_Responses;

    alter table Business_Responses_Attachments 
        add constraint FK8BF9E7E45885AD52 
        foreign key (attachments_ID) 
        references Attachments;

    alter table Ordered_Msg_Attachments 
        add constraint FKFD00A3C0112E73C 
        foreign key (Ordered_Msg_OMessage_ID) 
        references Ordered_Msg;

    alter table Ordered_Msg_Attachments 
        add constraint FKFD00A3C05885AD52 
        foreign key (attachments_ID) 
        references Attachments;

    alter table Retransmit_Msg_Attachments 
        add constraint FK63ABC176768643B2 
        foreign key (Retransmit_Msg_GMessage_ID) 
        references Retransmit_Msg;

    alter table Retransmit_Msg_Attachments 
        add constraint FK63ABC1765885AD52 
        foreign key (attachments_ID) 
        references Attachments;

    alter table Sender_Groups 
        add constraint FKA6DEE31E894F77F3 
        foreign key (quality_Reliability_ID) 
        references Reliability;
