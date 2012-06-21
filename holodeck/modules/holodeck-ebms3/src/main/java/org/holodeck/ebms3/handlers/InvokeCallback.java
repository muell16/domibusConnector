package org.holodeck.ebms3.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.async.*;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axiom.om.*;
import org.apache.axiom.soap.*;

import org.holodeck.ebms3.persistent.*;
import org.holodeck.ebms3.module.*;
import org.holodeck.common.soap.Util;

import java.lang.reflect.Constructor;

/**
 *  This handler, when it receives an incoming UserMessage
 *  that has an eb:RefToMessageId in its ebMS headers, looks up the database
 *  table MsgIdCallback to see if there is a callback class registered to
 *  handle such a response message.
 *
 * @author Hamid Ben Malek
 */
public class InvokeCallback extends AbstractHandler
{
//  private static final Log log = LogFactory.getLog(InvokeCallback.class.getName());
  private static final Log log = LogFactory.getLog(InvokeCallback.class.getName());
  
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( msgCtx.getFLOW() != MessageContext.IN_FLOW )
         return InvocationResponse.CONTINUE;
    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return InvocationResponse.CONTINUE;

    OMElement ebMess =
       Util.getGrandChildNameNS(header, Constants.MESSAGING, Constants.NS);
    if ( ebMess == null ) return InvocationResponse.CONTINUE;
    OMElement userMessage =
      Util.getGrandChildNameNS(header, Constants.USER_MESSAGE, Constants.NS);
    if ( userMessage == null ) return InvocationResponse.CONTINUE;
    String refToMessageId =
          Util.getGrandChildValue(userMessage, Constants.REF_TO_MESSAGE_ID);
    if ( refToMessageId == null || refToMessageId.trim().equals("") )
         return InvocationResponse.CONTINUE;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    log.info(logPrefix +
      "looking the database table MsgIdCallback for a registered callback class");
    MsgIdCallback micb = Constants.store.getMsgIdCallback(refToMessageId);
    if ( micb == null )
    {
      log.info(logPrefix + " no registered callback class was found");
      return InvocationResponse.CONTINUE;
    }
    String callbackClass = micb.getCallbackClass();
    if ( callbackClass == null || callbackClass.trim().equals("") )
         return InvocationResponse.CONTINUE;

    Callback cb = createCallback(callbackClass);
    if ( cb != null )
    {
      log.info("Invoking the callback class " + callbackClass + " ...");
      AsyncResult result = new AsyncResult(msgCtx);
      cb.onComplete(result);
    }
    return InvocationResponse.CONTINUE;
  }

  private Callback createCallback(String callbackClassName)
  {
    Callback callbackInstance = null;
    try
    {
      int dollarPos = callbackClassName.indexOf("$");
      if ( dollarPos < 0 )
      {
        //instantiate it as a normal class
        Class callbackClass = Class.forName(callbackClassName);
        callbackInstance = (Callback) callbackClass.newInstance();
      }
      else
      {
        //instantiate it as a normal class
        String containerClassName = callbackClassName.substring(0, dollarPos);
        Class containerClass = Class.forName(containerClassName);
        Class innerClass = Class.forName(callbackClassName);
        Object containerInstance = containerClass.newInstance();
        Constructor innerConstructor =
        innerClass.getDeclaredConstructor(new Class[] {containerClass});
        callbackInstance =
          (Callback)innerConstructor.newInstance(containerInstance);
      }
    }
    catch (Exception e)
    {
      String message =
        "Cannot instantiate the Callback class " + callbackClassName +
        ". Make sure that it has a default constructor";
      log.error(message,e);
    }
    return callbackInstance;
  }
}