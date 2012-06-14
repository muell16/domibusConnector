package org.holodeck.security.client;

import org.holodeck.common.client.Client;
import org.holodeck.security.model.Config;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.client.ServiceClient;

/**
 * This is used only for testing
 *
 * @author Hamid Ben Malek
 */
public class Msg extends Client
{
  public Msg(String axis2Repo)
  {
    super(axis2Repo);
  }

  public MessageContext inOut(String url, String action)
  {
    ServiceClient sc = createServiceClient(url, action, new String[]{"rampart"});
    System.out.println("----- Constructed ServiceClient");
    Config cfg = new Config(policyFile(), "client", "service", 
                            "org.holodeck.security.client.ClientPWCBHandler",
                            "F:/Dev/Servers/rampart-1.4/samples/keys/client.jks", 
                            "apache", "JKS");
    System.out.println("------ Constructed Config object");
    cfg.attachPolicy(sc);
    System.out.println("---- about to add payload " + payloadFile());
    this.addToBody(payloadFile());
    System.out.println("==== added payload to SOAP Body: ");
    System.out.println(getMessageContext().getEnvelope().toString());
    return inOut(sc);
  }

  private String policyFile()
  {
/*
    java.net.URL url = Msg.class.getClassLoader().getResource("policy.xml");
    System.out.println("-------- policy file is: " + url.getPath());
    return url.getPath();
*/
    return "F:\\Dev\\Projects\\Jacques\\holodeck\\security\\src\\test\\resources\\policy.xml";
  }
  private String payloadFile()
  {
/*
    java.net.URL url = Msg.class.getClassLoader()
                                .getResource("payload/add.xml");
    System.out.println("-------- payload file is: " + url.getPath());
    return url.getPath();
*/
    return "F:\\Dev\\Projects\\Jacques\\holodeck\\security\\target\\classes\\pyaload\\add.xml";
  }

  public static void main(String[] args)
  {
    String url = "http://localhost:9999/holodeck/services/SecureService";
    String axis2Repo = "F:\\Dev\\Projects\\Jacques\\holodeck\\ebms3\\target\\holodeck-ebms3\\WEB-INF";
    Msg msg = new Msg(axis2Repo);

    System.out.println("==== About to call SecureService ...");  
    MessageContext response =
            msg.inOut(url, "http://service.rampart.tutorial");
    System.out.println("==== Received response:");
    try { System.out.println(response.getEnvelope().toStringWithConsume()); }
    catch(Exception e) { e.printStackTrace(); }
  }
}