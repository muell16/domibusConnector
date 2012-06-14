package org.holodeck.ebms3.module;

import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.context.ConfigurationContext;
import org.holodeck.ebms3.workers.WorkerPool;

//import org.holodeck.ebms3.pmodes.PMode;
//import org.holodeck.ebms3.config.PMode;

import java.io.File;
import java.util.Map;

/**
 * @author Hamid Ben Malek
 */
public class Constants
{
  public static final String GROUP_NAME = "GROUP_NAME";
  // The value of this property is to be set
  // dynamically at the MessageContext level:
  public static String PMODE = "PMODE";
  // possible values for the QUALITY property:
  public static final String ORDER_ANON = "ORDER_ANON";
  public static final String ORDER_CALLBACK = "ORDER_CALLBACK";
  public static final String ORDER_POLL = "ORDER_POLL";
  public static final String ANON = "ANON";
  public static final String CALLBACK = "CALLBACK";
  public static final String POLL = "POLL";

  // Simple MEPs:
  public static String ONE_WAY_PULL = "One-Way/Pull";
  public static String ONE_WAY_PUSH = "One-Way/Push";
  public static String TWO_WAY_SYNC = "Two-Way/Sync";
  public static String TWO_WAY_PUSH_AND_PUSH = "Two-Way/Push-And-Push";
  public static String TWO_WAY_PUSH_AND_PULL = "Two-Way/Push-And-Pull";
  public static String TWO_WAY_PULL_AND_PUSH = "Two-Way/Pull-And-Push";
  public static String TWO_WAY_PULL_AND_Pull = "Two-Way/Pull-And-Pull";

  public static String NS =
      "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/";
  public static final String PREFIX = "eb";
  public static final String MESSAGING = "Messaging";
  public static final String USER_MESSAGE = "UserMessage";
  public static final String MESSAGE_INFO = "MessageInfo";
  public static final String TIMESTAMP = "Timestamp";
  public static final String MESSAGE_ID = "MessageId";
  public static final String REF_TO_MESSAGE_ID = "RefToMessageId";
  public static final String PARTY_INFO = "PartyInfo";
  public static final String FROM = "From";
  public static final String PARTY_ID = "PartyId";
  public static final String ROLE = "Role";
  public static final String TO = "To";
  public static final String COLLABORATION_INFO = "CollaborationInfo";
  public static final String AGREEMENT_REF = "AgreementRef";
  public static final String SERVICE = "Service";
  public static final String ACTION = "Action";
  public static final String CONVERSATION_ID = "ConversationId";
  public static final String MESSAGE_PROPERTIES = "MessageProperties";
  public static final String PROPERTY = "Property";
  public static final String PAYLOAD_INFO = "PayloadInfo";
  public static final String PART_INFO = "PartInfo";
  public static final String SCHEMA = "Schema";
  public static final String DESCRIPTION = "Description";
  public static final String PART_PROPERTIES = "PartProperties";
  public static final String SIGNAL_MESSAGE = "SignalMessage";
  public static final String ERROR = "Error";
  public static final String PULL_REQUEST = "PullRequest";
  public static final String RECEIPT = "Receipt";
  public static final String NON_REPUDIATION_INFORMATION =
                                  "NonRepudiationInformation";
  public static final String MESSAGE_PART_NR_INFORMATION =
                                  "MessagePartNRInformation";
  public static String ebbpNS =
      "http://docs.oasis-open.org/ebxml-bp/ebbp-2.0";
  public static final String ebbp_PREFIX = "ebbp";
  public static final String dsigNS = "http://www.w3.org/2000/09/xmldsig#";

  public static final String MODULE_CLASS_LOADER = "EBMS3_MODULE_CLASS_LOADER";
  public static final String STORE = "EBMS3_STORE";
  public static final String USER_SERVICES_MAP = "UserServicesMap";
  public static final String BINDINGS_MAP = "BindingsMap";
  public static final String PMODES_MAP = "PMODES_MAP";
  public static final String IN_PULL_REQUEST = "IN_PULL_REQUEST";
  public static final String IN_MESSAGING = "IN_MESSAGING";
  public static final String TO_ADDRESS = "TO_ADDRESS";
  public static final String IN_MSG_INFO = "IN_MSG_INFO";
  public static final String MESSAGE_INFO_SET = "MESSAGE_INFO_SET";
  public static final String MODULES = "MODULES";
  public static final String IMMEDIATE = "IMMEDIATE";
  public static final String SCHEDULED_EXECUTOR = "EBMS_SCHEDULED_EXECUTOR";
  public static final String RETRANSMIT_CALLBACK_CLASS = "RETRANSMIT_CALLBACK_CLASS";

  public static final String SUBMITTED_MESSAGES_FOLDER =
                                    "SUBMITTED_MESSAGES_FOLDER";
  public static final String RECEIVED_MESSAGES_FOLDER =
                                    "RECEIVED_MESSAGES_FOLDER";
  public static final String LOCAL_MACHINE = "LOCAL_MACHINE";
  public static final String STORE_ATTACHMENTS_IN_DB =
                                      "STORE_ATTACHMENTS_IN_DB";
  public static final String GATEWAY_CONFIG_FILE = "GATEWAY_CONFIG_FILE";

  // This parameter holds the received soap header at the server side:
  public static final String IN_SOAP_HEADER = "IN_SOAP_HEADER";

  // This parameter holds the PMode of the received msg at the server side:
  public static final String IN_PMODE = "IN_PMODE";

  // This parameter holds the Leg of the received msg at the server side:
  public static final String IN_LEG = "IN_LEG";
    
  // This parameter holds the address to which a callback AS4 receipt will be
  // sent to: in case of a one-way/pull or two-way/sync mep, the reeipt for
  // the response message (the message on leg #2 that is coming on the back channel)
  // is obviously a callback receipt. In this case the receiptTo url is taken from
  // the endpoint url of the request message which store its endpoint url in this
  // key for later retrieval
  public static final String RECEIPT_TO = "RECEIPT_TO";

  public static AxisModule module;
  public static String[] engagedModules = new String[]{"holodeck-ebms3"};

  public static void setAxisModule(AxisModule m) { module = m; }
  public static AxisModule getAxisModule() { return module; }

  public static ConfigurationContext configContext;
  public static DbStore store;
  /* change */
  //public static Map<String, PMode> pmodesMap;

  public static Map<String, org.holodeck.ebms3.config.PMode> pmodes;
  public static boolean attachmentsInDB;

  public static WorkerPool workerPool;
  public static final String WORKERS_FILE = "Workers";
  public static File workersFile;

  // This may be a convenience method (not sure yet):
  public static String getSubmitFolder()
  {
    if ( configContext == null ) return null;
    String submitFolder = null;
    Parameter submitFolderParam = configContext.getAxisConfiguration()
                                    .getParameter("SUBMITTED_MESSAGES_FOLDER");
    if ( submitFolderParam != null )
    {
      submitFolder = (String)submitFolderParam.getValue();
      if ( submitFolder != null && !new File(submitFolder).exists() )
      {
        File baseFolder = configContext.getRealPath("/modules/" + module.getName());
        submitFolder = baseFolder.getAbsolutePath() +
                            File.separator + submitFolder;
        File sub = new File(submitFolder);
        if ( !sub.exists() ) sub.mkdirs();
      }
    }
    
    return submitFolder;
  }

  // This may be a convenience method (not sure yet):
  public static String getReceivedFolder()
  {
    if ( configContext == null ) return null;
    String receivedFolder = null;
    Parameter receivedFolderParam = configContext.getAxisConfiguration()
                                    .getParameter("RECEIVED_MESSAGES_FOLDER");
    if ( receivedFolderParam != null )
    {
      receivedFolder = (String)receivedFolderParam.getValue();
      if ( receivedFolder != null && !new File(receivedFolder).exists() )
      {
        File baseFolder = configContext.getRealPath("/modules/" + module.getName());
        receivedFolder = baseFolder.getAbsolutePath() +
                            File.separator + receivedFolder;
        File sub = new File(receivedFolder);
        if ( !sub.exists() ) sub.mkdirs();
      }
    }

    return receivedFolder;
  }

  public static String getAttachmentDir()
  {
    if ( configContext == null ) return null;
    Parameter attachDirParam =
      configContext.getAxisConfiguration()
                   .getParameter(org.apache.axis2.Constants.Configuration.ATTACHMENT_TEMP_DIR);
    File base = configContext.getRealPath("/");
    String attachmentFolder = base.getAbsolutePath() + "attachments";
    File sub = new File(attachmentFolder);
    if ( !sub.exists() ) sub.mkdirs();
    if ( attachDirParam != null ) attachDirParam.setValue(attachmentFolder);
    else
    {
      Parameter p = new Parameter(org.apache.axis2.Constants.Configuration.ATTACHMENT_TEMP_DIR, attachmentFolder);
      try { configContext.getAxisConfiguration().addParameter(p); }
      catch(Exception e) { e.printStackTrace(); }
    }
    return attachmentFolder;
  }

  public static File getWorkersFile()
  {
    if ( workersFile != null ) return workersFile;
    if ( module == null ) return null;
    Parameter workersParam = module.getParameter(WORKERS_FILE);
    File base = configContext.getRealPath("/modules/" + module.getName());
    String file = base.getAbsolutePath() + File.separator + "workers.xml";
    if ( workersParam == null ) return new File(file);
    String wFile = (String)workersParam.getValue();
    workersFile = new File(wFile);
    if ( workersFile.exists() ) return workersFile;
    else workersFile =
        new File( base.getAbsolutePath() + File.separator + wFile );
    
    return workersFile;
  }
}