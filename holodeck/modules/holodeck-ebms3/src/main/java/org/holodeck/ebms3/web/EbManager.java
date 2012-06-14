package org.holodeck.ebms3.web;

//import javax.servlet.http.HttpServlet;
//import javax.servlet.*;
import java.util.*;
import java.io.*;

import org.apache.axis2.context.*;
//import org.apache.axis2.description.*;
//import org.apache.axis2.transport.http.AxisServlet;
import org.holodeck.ebms3.module.Constants;
import org.holodeck.ebms3.module.Configuration;
import org.holodeck.ebms3.submit.MsgInfoSet;
import org.holodeck.ebms3.persistent.ReceivedUserMsg;
import org.holodeck.ebms3.persistent.UserMsgToPush;


/**
 * @author Hamid Ben Malek
 */
public class EbManager //extends HttpServlet
{
  //static String submittedMsgsDirectory = null;
  //static ServletContext application = null;
  //static ConfigurationContext configCtx = null;
/*
  public void init(ServletConfig config) throws ServletException
  {
    application = config.getServletContext();
    //System.out.println("================== Got the ServletContext =========");
  }
*/
  public EbManager()
  {
    //System.out.println("============================================");
    //System.out.println("        EbManager Constructor being called");
    //System.out.println("============================================");
  }

  public static ConfigurationContext getConfigurationContext()
  {
    return Configuration.configContext;
/*
    if ( configCtx != null ) return configCtx;
    if ( application == null ) return null;
    configCtx =
      (ConfigurationContext)application.getAttribute(AxisServlet.CONFIGURATION_CONTEXT);
    return configCtx;
*/
  }

  public static String getSubmitFolder()
  {
    return Configuration.getSubmitFolder();
/*
    if ( getConfigurationContext() == null )
    {
      if ( submittedMsgsDirectory == null ) initRes();
      return submittedMsgsDirectory;
    }

    String submitFolder = null;
    Parameter submitFolderParam = getConfigurationContext().getAxisConfiguration()
                                       .getParameter("SUBMITTED_MESSAGES_FOLDER");
    if ( submitFolderParam != null )
    {
      submitFolder = (String)submitFolderParam.getValue();
      System.out.println("========= submitFolder as defined module.xml is " +
                         submitFolder);
      if ( submitFolder != null && !new File(submitFolder).exists() )
      {
        File baseFolder = getConfigurationContext()
                .getRealPath("/modules/" + Constants.getAxisModule().getName());
        submitFolder = //getConfigurationContext().getRealPath("/WEB-INF") +
                baseFolder.getAbsolutePath() +
                            File.separator + submitFolder;
        File sub = new File(submitFolder);
        if ( !sub.exists() ) sub.mkdirs();
      }
    }
    return submitFolder;
*/
  }
/*
  static void initRes()
  {
    ResourceBundle bundle = ResourceBundle.getBundle("configuration");
    submittedMsgsDirectory = bundle.getString("Submitted.Messages.Folder");
  }
*/
  public static void writeMetadata(MsgInfoSet meta, String directory)
  {
    String fileName = getSubmitFolder() + File.separator + directory +
                      File.separator + "metadata.xml";
    meta.writeToFile(fileName);
  }

  public static void writeToFile(String content, File file)
  {
    try
    {
      FileWriter out = new FileWriter(file);
      out.write(content);
      out.close();
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }

  public static String[] pmodeList()
  {
    /*
    if ( Constants.pmodes == null ) return null;
    Set<String> pmodes = Constants.pmodes.keySet();
    if ( pmodes == null || pmodes.size() == 0 ) return null;
    Iterator<String> it = pmodes.iterator();
    List<String> res = new ArrayList<String>();
    while ( it != null && it.hasNext() ) res.add(it.next());
    String[] pm = new String[res.size()];
    for (int i = 0; i < res.size(); i++) pm[i] = res.get(i);
    return pm;
    */
    Set<String> pmodeNames = Configuration.pmodes.keySet();
    String[] names = new String[pmodeNames.size()];
    pmodeNames.toArray(names);
    return names;
  }

  public ReceivedMessage[] getReceivedMessages()
  {
    List<ReceivedUserMsg> messages = Constants.store.getReceivedUserMsg();
    if ( messages == null || messages.size() == 0 ) return null;
    ReceivedMessage[] rmessages = new ReceivedMessage[messages.size()];
    int i = 0;
    for (ReceivedUserMsg msg : messages)
    {
      ReceivedMessage m = new ReceivedMessage(msg);
      rmessages[i] = m;
      i++;
    }
    return rmessages;
  }

  public OutgoingMessage[] getOutgoingMessages()
  {
    List<UserMsgToPush> messages = Constants.store.getAllOutgoingMessages();
    if ( messages == null || messages.size() == 0 ) return null;
    OutgoingMessage[] omessages = new OutgoingMessage[messages.size()];
    int i = 0;
    for (UserMsgToPush msg : messages)
    {
      OutgoingMessage m = new OutgoingMessage(msg);
      omessages[i] = m;
      i++;
    }
    return omessages;
  }
}