package org.holodeck.ebms3.web
{
  [Bindable]
  [RemoteClass(alias="org.holodeck.ebms3.web.ReceivedMessage")]
  public class ReceivedMessage
  {
    public function ReceivedMessage()
    {
      public var messageId:String = null;
      public var refToMessageId:String = null;
      public var mpc:String = null;
      public var fromParty:String = null;
      public var toParty:String = null;
      public var service:String = null;
      public var action:String = null;
      public var envelope:String;

      public function ReceivedMessage() {}
    }
  }
}