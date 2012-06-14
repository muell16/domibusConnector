package org.holodeck.reliability.module;

import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.context.ConfigurationContext;

import java.io.File;

/**
 * @author Hamid Ben Malek
 */
public class Constants
{
  public static final String GROUP_NAME = "GROUP_NAME";
  // The value of this property is to be set
  // dynamically at the MessageContext level:
  public static String QUALITY = "QUALITY";
  // possible values for the QUALITY property:
  public static final String ORDER_ANON = "ORDER_ANON";
  public static final String ORDER_CALLBACK = "ORDER_CALLBACK";
  public static final String ORDER_POLL = "ORDER_POLL";
  public static final String ANON = "ANON";
  public static final String CALLBACK = "CALLBACK";
  public static final String POLL = "POLL";

  public static String NS =
         "http://docs.oasis-open.org/wsrm/2004/06/ws-reliability-1.1.xsd";
  public static final String PREFIX = "wsrm";
  public static final String REQUEST = "Request";
  public static final String MESSAGE_ID = "MessageId";
  public static final String SEQUENCE_NUM = "SequenceNum";
  public static final String EXPIRY_TIME = "ExpiryTime";
  public static final String REPLY_PATTERN = "ReplyPattern";
  public static final String VALUE = "Value";
  public static final String REPLY_TO = "ReplyTo";
  public static final String BARE_URI = "BareURI";
  public static final String ACK_REQUESTED = "AckRequested";
  public static final String DUPLICATE_ELIMINATION = "DuplicateElimination";
  public static final String MESSAGE_ORDER = "MessageOrder";
  public static final String SEQUENCE_NUM_RANGE = "SequenceNumRange";
  public static final String REPLY_RANGE = "ReplyRange";
  public static final String NON_SEQUENCE_REPLY = "NonSequenceReply";
  public static final String SEQUENCE_REPLIES = "SequenceReplies";
  public static final String RESPONSE = "Response";
  public static final String REF_TO_MESSAGE_IDS = "RefToMessageIds";
  public static final String POLL_REQUEST = "PollRequest";

  public static final String MODULE_CLASS_LOADER = "RELIABILITY_MODULE_CLASS_LOADER";
  public static final String STORE = "RELIABILITY_STORE";
  public static final String IN_REQUEST = "IN_REQUEST";
  public static final String IN_POLL_REQUEST = "IN_POLL_REQUEST";
  public static final String RECEIVER_GROUP = "RECEIVER_GROUP";
  public static final String SCHEDULED_EXECUTOR = "SCHEDULED_EXECUTOR";
  public static final String RETRANSMIT_CALLBACK_CLASS = "RETRANSMIT_CALLBACK_CLASS";

  public static ConfigurationContext configContext;
  public static AxisModule module;

  public static OrderedDeliveryWorker orderedDeliveryWorker;
  public static CallbackAckWorker callbackAckWorker;
  public static PollReqSenderWorker pollReqSenderWorker;
  public static CleanupWorker cleanupWorker;
  public static SenderWorker senderWorker;

  public static final String MESSAGE_STORAGE_FOLDER =
                                    "MESSAGE_STORAGE_FOLDER";
  public static final String RELIABILITY_CONFIG_FILE = "RELIABILITY_CONFIG_FILE";

  public static DbStore store;

  public static void setAxisModule(AxisModule m) { module = m; }
  public static AxisModule getAxisModule() { return module; }

  public static String getStorageFolder()
  {
    if ( configContext == null ) return null;
    String storageFolder = null;
    Parameter storageFolderParam =
         configContext.getAxisConfiguration()
                      .getParameter(MESSAGE_STORAGE_FOLDER);
    if ( storageFolderParam != null )
    {
      storageFolder = (String)storageFolderParam.getValue();
      if ( storageFolder != null && !new File(storageFolder).exists() )
      {
        File baseFolder = configContext.getRealPath("/modules/" + module.getName());
        storageFolder = baseFolder.getAbsolutePath() +
                            File.separator + storageFolder;
        File sub = new File(storageFolder);
        if ( !sub.exists() ) sub.mkdirs();
      }
    }
    //System.out.println("========== Reliability StorageFolder is: "
    //                   + storageFolder);
    return storageFolder;
  }
}