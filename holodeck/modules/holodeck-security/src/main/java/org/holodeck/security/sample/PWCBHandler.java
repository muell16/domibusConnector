package org.holodeck.security.sample;

import org.apache.ws.security.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import java.io.IOException;

public class PWCBHandler implements CallbackHandler
{
  public void handle(Callback[] callbacks) throws IOException,
                                                    UnsupportedCallbackException
  {
    for (Callback callback : callbacks)
    {
      WSPasswordCallback pwcb = (WSPasswordCallback) callback;
      String id = pwcb.getIdentifer();
      if ("client".equals(id))
      {
        pwcb.setPassword("apache");
        return;
      }
      else if ("service".equals(id))
      {
        pwcb.setPassword("apache");
        return;
      }

      if (pwcb.getUsage() == WSPasswordCallback.USERNAME_TOKEN_UNKNOWN)
      {
        if (pwcb.getIdentifer().equals("alice") &&
            pwcb.getPassword().equals("bobPW"))
            return;
        else throw new UnsupportedCallbackException(callback, "check failed");
      }
      pwcb.setPassword("bobPW");
    }
  }
}