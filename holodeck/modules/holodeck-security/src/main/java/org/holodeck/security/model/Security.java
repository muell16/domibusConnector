package org.holodeck.security.model;

import org.simpleframework.xml.*;
import org.apache.neethi.Policy;
import org.apache.rampart.policy.model.RampartConfig;
import org.apache.rampart.policy.model.CryptoConfig;

import java.util.Properties;
import java.io.File;

import org.holodeck.security.module.Configuration;

/**
 * @author Hamid Ben Malek
 */
@Root(name="security", strict=false)
public class Security implements java.io.Serializable
{
  private static final long serialVersionUID = -8309029384765717779L;

  @Attribute
  protected String name;

  @Element(name="policy")
  protected String policyFile;

  @Element
  protected Initiator initiator;

  @Element
  protected Recipient recipient;

  // These fields are transient (should not be serialized)
  protected Policy initiatorPolicy;
  protected RampartConfig initiatorRampartConfig;
  protected Policy recipientPolicy;
  protected RampartConfig recipientRampartConfig;

  public Security() {}

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getPolicyFile() { return policyFile; }
  public void setPolicyFile(String policy) { this.policyFile = policy; }

  public Initiator getInitiator() { return initiator; }
  public void setInitiator(Initiator initiator) { this.initiator = initiator; }

  public Recipient getRecipient() { return recipient; }
  public void setRecipient(Recipient recipient) { this.recipient = recipient; }

  public Policy getInitiatorPolicy() { return initiatorPolicy; }
  public void setInitiatorPolicy(Policy policy)
  {
    this.initiatorPolicy = policy;
    if ( initiatorPolicy != null )
         initiatorPolicy.addAssertion( getInitiatorRampartConfig() );
  }

  public Policy getRecipientPolicy() { return recipientPolicy; }
  public void setRecipientPolicy(Policy policy)
  {
    this.recipientPolicy = policy;
    if ( recipientPolicy != null )
         recipientPolicy.addAssertion( getRecipientRampartConfig() );
  }

  protected RampartConfig getInitiatorRampartConfig()
  {
    if ( initiatorRampartConfig != null ) return initiatorRampartConfig;
    if ( initiator == null || initiator.getKeystore() == null ) return null;
    String keyStoreType = initiator.getKeystore().getType();
    String keyStoreFile = initiator.getKeystore().getValue();
    if ( !new File(keyStoreFile).exists() )
         keyStoreFile =
             Configuration.getKeysFolder() + File.separator + keyStoreFile;
    String keyStorePassword = initiator.getKeystore().getPassword();
    String pwCbClass = initiator.getCallbackClass();
    String user = initiator.getUser().getValue();
    String encryptionUser = initiator.getEncryptionUser();
    initiatorRampartConfig = new RampartConfig();
    CryptoConfig crypto = new CryptoConfig();
    crypto.setProvider("org.apache.ws.security.components.crypto.Merlin");
    Properties props = new Properties();
    props.setProperty("org.apache.ws.security.crypto.merlin.keystore.type",
                       keyStoreType);
    props.setProperty("org.apache.ws.security.crypto.merlin.file",
                       keyStoreFile);
    props.setProperty("org.apache.ws.security.crypto.merlin.keystore.password",
                       keyStorePassword);
    crypto.setProp(props);
    initiatorRampartConfig.setSigCryptoConfig(crypto);
    initiatorRampartConfig.setEncrCryptoConfig(crypto);
    initiatorRampartConfig.setPwCbClass(pwCbClass);
    initiatorRampartConfig.setUser(user);
    initiatorRampartConfig.setEncryptionUser(encryptionUser);
    return initiatorRampartConfig;
  }
  protected RampartConfig getRecipientRampartConfig()
  {
    if ( recipientRampartConfig != null ) return recipientRampartConfig;
    if ( recipient == null || recipient.getKeystore() == null ) return null;
    String keyStoreType = recipient.getKeystore().getType();
    String keyStoreFile = recipient.getKeystore().getValue();
    if ( !new File(keyStoreFile).exists() )
         keyStoreFile =
             Configuration.getKeysFolder() + File.separator + keyStoreFile;
    String keyStorePassword = recipient.getKeystore().getPassword();
    String pwCbClass = recipient.getCallbackClass();
    String user = recipient.getUser().getValue();
    String encryptionUser = recipient.getEncryptionUser();
    recipientRampartConfig = new RampartConfig();
    CryptoConfig crypto = new CryptoConfig();
    crypto.setProvider("org.apache.ws.security.components.crypto.Merlin");
    Properties props = new Properties();
    props.setProperty("org.apache.ws.security.crypto.merlin.keystore.type",
                       keyStoreType);
    props.setProperty("org.apache.ws.security.crypto.merlin.file",
                       keyStoreFile);
    props.setProperty("org.apache.ws.security.crypto.merlin.keystore.password",
                       keyStorePassword);
    crypto.setProp(props);
    recipientRampartConfig.setSigCryptoConfig(crypto);
    recipientRampartConfig.setEncrCryptoConfig(crypto);
    recipientRampartConfig.setPwCbClass(pwCbClass);
    recipientRampartConfig.setUser(user);
    recipientRampartConfig.setEncryptionUser(encryptionUser);
    return recipientRampartConfig;
  }
}