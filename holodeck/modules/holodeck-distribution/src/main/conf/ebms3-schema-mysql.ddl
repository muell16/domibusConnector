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
