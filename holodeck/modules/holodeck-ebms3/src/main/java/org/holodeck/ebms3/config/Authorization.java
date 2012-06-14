package org.holodeck.ebms3.config;

import org.simpleframework.xml.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="Authorization", strict=false)
public class Authorization implements java.io.Serializable
{
  private static final long serialVersionUID = 746582737443013947L;

  public static final String USERNAME_TOKEN = "UsernameToken";
  public static final String SIGNATURE = "Signature";

  @Attribute
  protected String type;

  @Attribute(required=false)
  protected String username;

  @Attribute(required=false)
  protected String password;

  public Authorization() {}
  public Authorization(String type, String username, String password)
  {
    if ( type != null && type.equalsIgnoreCase(USERNAME_TOKEN) )
         this.type = USERNAME_TOKEN;
    else if ( type != null && type.equalsIgnoreCase(SIGNATURE) )
              this.type = SIGNATURE;
    this.username = username;
    this.password = password;
  }

  public String getType() { return type; }
  public void setType(String type)
  {
    if ( type != null && type.equalsIgnoreCase(USERNAME_TOKEN) )
         this.type = USERNAME_TOKEN;
    else if ( type != null && type.equalsIgnoreCase(SIGNATURE) )
         this.type = SIGNATURE;
  }

  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }

  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }

  public boolean isUsernameToken()
  {
    return type.equalsIgnoreCase(USERNAME_TOKEN);
  }
}