package org.holodeck.ebms3.web
{
  [Bindable]
  [RemoteClass(alias="org.holodeck.ebms3.web.OutgoingMessage")]
  public class OutgoingMessage
  {
    //public var metadata:MsgMetadata;
    //public var toParties:Array = new Array(); 
    public var fromParty:String;
    public var toParty:String;
    public var legNumber:int;
    public var pmode:String;
    public var mep:String;
    public var mpc:String;
    public var service:String;
    public var action:String;
    public var toURL:String;
    public var sent:Boolean;
    
    public var envelope:String;
  }
}