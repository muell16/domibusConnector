package org.holodeck.ebms3.submit
{
  [Bindable]
  [RemoteClass(alias="org.holodeck.ebms3.config.Party")]
  public class Party
  {
    public var partyId:String = null;
    public var type:String = null;

    public function Party() {}
  }
}