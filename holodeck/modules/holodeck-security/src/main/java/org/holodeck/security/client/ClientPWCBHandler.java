package org.holodeck.security.client;

import org.apache.ws.security.WSPasswordCallback;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

/**
 * This is used only for testing
 *
 * @author Hamid Ben Malek
 */
public class ClientPWCBHandler implements CallbackHandler
{
  public void handle(Callback[] callbacks) throws IOException,
                                                  UnsupportedCallbackException
  {
    for (Callback callback : callbacks)
    {
      WSPasswordCallback pwcb = (WSPasswordCallback) callback;
      if(pwcb.getIdentifer().equals("client") )
      {
        pwcb.setPassword("ClientPW");
        return;
      }
    }
  }
}
