package org.holodeck.ebms3.submit
{
  [Bindable]
  [RemoteClass(alias="org.holodeck.ebms3.submit.Payload")]
  public class Payload
  {
    public var cid:String;
    public var payload:String;

    public function Payload() {}
  }
}