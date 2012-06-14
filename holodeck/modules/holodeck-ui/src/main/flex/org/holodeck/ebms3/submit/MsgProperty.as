package org.holodeck.ebms3.submit
{
  [Bindable]
  [RemoteClass(alias="org.holodeck.ebms3.module.Property")]
  public class MsgProperty
  {
    public var name:String;
    public var value:String;

    public function MsgProperty() {}
  }
}