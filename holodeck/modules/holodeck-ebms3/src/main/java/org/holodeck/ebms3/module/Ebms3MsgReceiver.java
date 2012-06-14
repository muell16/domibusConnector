package org.holodeck.ebms3.module;

import org.apache.axis2.AxisFault;
//import org.apache.axis2.description.AxisOperation;
//import org.apache.axis2.description.InOutAxisOperation;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.engine.AxisEngine;
//import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.util.MessageContextBuilder;
import org.apache.axis2.receivers.*;
import org.apache.axiom.soap.*;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.holodeck.ebms3.config.PMode;


//import javax.xml.namespace.QName;
import java.lang.reflect.Method;

/**
 * @author Hamid Ben Malek
 */
public class Ebms3MsgReceiver extends AbstractMessageReceiver
{
  private static final Log log =
                  LogFactory.getLog(Ebms3MsgReceiver.class.getName());

  public final void receive(MessageContext msgContext) throws AxisFault
  {
    MessageContext outMsgContext =
         MessageContextBuilder.createOutMessageContext(msgContext);
    SOAPFactory factory = (SOAPFactory)msgContext.getEnvelope().getOMFactory();
    outMsgContext.setEnvelope( factory.getDefaultEnvelope() );
    outMsgContext.getOperationContext().addMessageContext(outMsgContext);

    OMElement ebMess = (OMElement)msgContext.getProperty(Constants.IN_MESSAGING);
    if ( ebMess != null ) msgContext.getEnvelope().getHeader().addChild(ebMess);

    ThreadContextDescriptor tc = setThreadContext(msgContext);
    boolean sendOut = false;
    try
    {
      PMode pmode = (PMode)msgContext.getProperty(Constants.IN_PMODE);
      if ( pmode == null )
      {
        log.debug("Received message does not have a corresponding PMode");
        return;
      }
      String mep = pmode.getMep(); //getMep(msgContext);
      sendOut = handle(msgContext, outMsgContext, mep);
    }
    finally
    {
      restoreThreadContext(tc);
    }

    if ( sendOut )
    {
      //AxisEngine engine = new AxisEngine(msgContext.getConfigurationContext());
      //engine.send(outMsgContext);
      replicateState(msgContext);
      AxisEngine.send(outMsgContext);
    }
  }
/*
  private String getMep(MessageContext msgCtx)
  {
    return EbUtil.getMep(msgCtx);
  }
*/
  private boolean handle(MessageContext msgCtx, MessageContext outMsgContext,
                         String mep)
                  throws AxisFault
  {
    log.debug("Ebms3MsgReceiver::handle() is called");
    Object obj;
    try { obj = getTheImplementationObject(msgCtx); }
    catch(Exception ex) { throw new AxisFault("Service class does not exist"); }

    Class implClass = obj.getClass();
    Method pullMethod = getPullOperation(implClass);
    Method pushMethod = getPushOperation(implClass);
    MsgInfo msgInfo = //EbUtil.createMsgInfo(msgCtx);
           (MsgInfo)msgCtx.getProperty(Constants.IN_MSG_INFO);
    if ( msgInfo == null ) msgInfo = EbUtil.createMsgInfo(msgCtx);
    log.debug("Ebms3MsgReceiver::handle(): mep is " + mep);

    // check if need to send receipt or ack on back channel
    boolean expectReceipt =
      (Boolean)msgCtx.getProperty(org.holodeck.common.Constants.EXPECT_RECEIPT);
    boolean expectAck =
      (Boolean)msgCtx.getProperty(org.holodeck.common.Constants.EXPECT_ACK);

    if ( mep.equals(Constants.ONE_WAY_PULL) )
    {
      invokeBusiness(pullMethod, msgInfo, outMsgContext);
      return true;
    }
    else if ( mep.equals(Constants.ONE_WAY_PUSH) )
    {
      invokeBusiness(pushMethod, msgInfo, outMsgContext);
      return expectReceipt || expectAck;
    }
    else if ( mep.equals(Constants.TWO_WAY_SYNC) )
    {
      invokeBusiness(pushMethod, msgInfo, outMsgContext);
      return true;
    }
    else if ( mep.equals(Constants.TWO_WAY_PUSH_AND_PUSH) )
    {
      invokeBusiness(pushMethod, msgInfo, outMsgContext);
      // need to store the outMsgContext in the database
      // to be pushed out later by a background thread...

      return expectReceipt || expectAck;
    }
    else if ( mep.equals(Constants.TWO_WAY_PUSH_AND_PULL) )
    {
      if ( msgInfo.getService() != null )
      {
        invokeBusiness(pushMethod, msgInfo, outMsgContext);
        // need to store the outMsgContext in the database
        // to be pulled out later...
        return expectReceipt || expectAck;
      }
      else
      {
        // normally we should not be here, as the pulled message
        // was already store previously in the database and ready
        // for pulling
        invokeBusiness(pullMethod, msgInfo, outMsgContext);
        return true;
      }
    }
    else if ( mep.equals(Constants.TWO_WAY_PULL_AND_PUSH) )
    {
      if ( msgInfo.getService() != null )
      {
        invokeBusiness(pushMethod, msgInfo, outMsgContext);
        return expectReceipt || expectAck;
      }
      else
      {
        invokeBusiness(pullMethod, msgInfo, outMsgContext);
        return true;
      }
    }
    else if ( mep.equals(Constants.TWO_WAY_PULL_AND_Pull) )
    {
      invokeBusiness(pullMethod, msgInfo, outMsgContext);
      return true;
    }
    return false;
  }

  private void invokeBusiness(Method method, MsgInfo msgInfo,
                              MessageContext msgContext)
               throws AxisFault
  {
    try
    {
      Object obj = getTheImplementationObject(msgContext);
      if ( method != null )
      {
        //method.invoke(obj, new Object[]{msgInfo, msgContext});
        method.invoke(obj, msgInfo, msgContext);
      }
      else
      {
        throw new AxisFault("method " + method.getName() + " Does Not Exist");
      }
    }
    catch (Exception e) { throw AxisFault.makeFault(e); }
  }

  private Method getPushOperation(Class implClass)
  {
    String methodName = "push";
    try
    {
      Method method =
          implClass.getMethod(methodName,
                  new Class [] {MsgInfo.class, MessageContext.class});
      if ( method != null ) return method;
    }
    catch(Exception ex) { ex.printStackTrace(); }

    Method[] methods = implClass.getMethods();
    for (int i = 0; i < methods.length; i++)
    {
      if (methods[i].getName().equals(methodName) &&
          methods[i].getParameterTypes().length == 2 &&
          MsgInfo.class.getName().equals(
                            methods[i].getParameterTypes()[0].getName() ) &&
          MessageContext.class.getName().equals(
                            methods[i].getParameterTypes()[1].getName() ) &&
          "void".equals(methods[i].getReturnType().getName()) )
      {
        return methods[i];
      }
    }
    return null;
  }

  private Method getPullOperation(Class implClass)
  {
    String methodName = "pull";
    Method[] methods = implClass.getMethods();
    for (int i = 0; i < methods.length; i++)
    {
      if (methods[i].getName().equals(methodName) &&
          methods[i].getParameterTypes().length == 2 &&
          MsgInfo.class.getName().equals(
                            methods[i].getParameterTypes()[0].getName() ) &&
          MessageContext.class.getName().equals(
                            methods[i].getParameterTypes()[1].getName() ) &&
          "void".equals(methods[i].getReturnType().getName()) )
      {
        return methods[i];
      }
    }
    return null;
  }

  protected void invokeBusinessLogic(MessageContext msgContext)
                 throws AxisFault
  {
  }
}