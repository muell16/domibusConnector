package org.holodeck.security.model;

import org.apache.rampart.policy.model.RampartConfig;
import org.apache.rampart.policy.model.CryptoConfig;
import org.apache.rampart.RampartMessageData;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

import java.util.Properties;

/**
 * This is used only for testing
 *
 * @author Hamid Ben Malek
 */
public class Config extends RampartConfig
{
  protected Policy policy = null;
  protected Properties props = new Properties();

  protected String policyFile;
  protected String user;
  protected String encryptionUser;
  protected String pwCbClass;
  protected String keyStoreFile;
  protected String keyStoreType;
  protected String keyStorePassword;

  public Config()
  {
    CryptoConfig sigCrypto = new CryptoConfig();
    sigCrypto.setProvider("org.apache.ws.security.components.crypto.Merlin");
    sigCrypto.setProp(props);
    setSigCryptoConfig(sigCrypto);
  }
  public Config(String policyFile, String user, String encryptionUser,
                String passCallbackHandler,
                String keyStoreFile, String keyStoreType, String keyStorePass)
  {
    this();
    setUser(user);
    setEncryptionUser(encryptionUser);
    setPwCbClass(passCallbackHandler);
    setKeyStoreFile(keyStoreFile);
    setKeyStoreType(keyStoreType);
    setKeyStorePassword(keyStorePass);
    //init();
    System.out.println("---- Config constructer: about to call loadPolicy(" +
                       policyFile + ")");
    setPolicyFile(policyFile);
  }

  public Policy getPolicy() { return policy; }
  public void setPolicy(Policy policy) { this.policy = policy; }

  public String getPolicyFile() { return policyFile; }
  public void setPolicyFile(String policyFile)
  {
    this.policyFile = policyFile;
    loadPolicy(policyFile);
  }

  public String getUser() { return user; }
  public void setUser(String user) { this.user = user; }

  public String getEncryptionUser() { return encryptionUser; }
  public void setEncryptionUser(String encryptionUser)
  {
    this.encryptionUser = encryptionUser;
  }

  public String getPwCbClass() { return pwCbClass; }
  public void setPwCbClass(String pwCbClass) { this.pwCbClass = pwCbClass; }

  public String getKeyStoreFile() { return keyStoreFile; }
  public void setKeyStoreFile(String keyStoreFile)
  {
    this.keyStoreFile = keyStoreFile;
    props.setProperty("org.apache.ws.security.crypto.merlin.file",
                       keyStoreFile);
  }

  public String getKeyStoreType() { return keyStoreType; }
  public void setKeyStoreType(String keyStoreType)
  {
    this.keyStoreType = keyStoreType;
    props.setProperty("org.apache.ws.security.crypto.merlin.keystore.type",
                       keyStoreType);
  }

  public String getKeyStorePassword() { return keyStorePassword; }
  public void setKeyStorePassword(String keyStorePassword)
  {
    this.keyStorePassword = keyStorePassword;
    props.setProperty("org.apache.ws.security.crypto.merlin.keystore.password",
                       keyStorePassword);
  }

  public void attachPolicy(ServiceClient sc)
  {
    if ( sc == null ) return;
    Options options = sc.getOptions();
    if ( options == null ) options = new Options();
    options.setProperty(RampartMessageData.KEY_RAMPART_POLICY, policy);
    sc.setOptions(options);
    System.out.println("--- attachPolicy()...");
    try { sc.engageModule("rampart"); System.out.println("rampart is engaged"); }
    catch(Exception ex) { ex.printStackTrace(); }
  }

  private void loadPolicy(String policyFile)
  {
    if ( policyFile == null || policyFile.trim().equals("") ) return;
    try
    {
      System.out.println("=========loading policy file: " + policyFile);
      StAXOMBuilder builder = new StAXOMBuilder(policyFile);
      policy = PolicyEngine.getPolicy(builder.getDocumentElement());
      System.out.println("==== policy file loaded");
      if ( policy != null ) policy.addAssertion(this);
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }
  private void init()
  {
    CryptoConfig sigCrypto = new CryptoConfig();
    sigCrypto.setProvider("org.apache.ws.security.components.crypto.Merlin");
    Properties props = new Properties();
    props.setProperty("org.apache.ws.security.crypto.merlin.keystore.type",
                       keyStoreType);
    props.setProperty("org.apache.ws.security.crypto.merlin.file",
                       keyStoreFile);
    props.setProperty("org.apache.ws.security.crypto.merlin.keystore.password",
                       keyStorePassword);
    sigCrypto.setProp(props);

    setSigCryptoConfig(sigCrypto);
    setPwCbClass(pwCbClass);
    setUser(user);
    this.setEncryptionUser(encryptionUser);
  }
}