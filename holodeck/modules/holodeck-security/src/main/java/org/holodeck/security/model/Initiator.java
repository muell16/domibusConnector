package org.holodeck.security.model;

import org.simpleframework.xml.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="initiator", strict=false)
public class Initiator implements java.io.Serializable
{
  private static final long serialVersionUID = -8309019657483917779L;

  @Element
  protected User user;

  @Element  
  protected String encryptionUser;

  @Element
  protected Keystore keystore;

  public Initiator() {}

  public User getUser() { return user; }
  public void setUser(User user) { this.user = user; }

  public String getEncryptionUser() { return encryptionUser; }
  public void setEncryptionUser(String encryptionUser)
  {
    this.encryptionUser = encryptionUser;
  }

  public Keystore getKeystore() { return keystore; }
  public void setKeystore(Keystore keystore) { this.keystore = keystore; }

  public String getCallbackClass()
  {
    if ( user == null ) return null;
    return user.getCallbackClass();
  }
}