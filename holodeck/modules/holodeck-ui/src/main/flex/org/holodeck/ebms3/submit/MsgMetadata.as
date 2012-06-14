package org.holodeck.ebms3.submit
{
   import org.holodeck.pmode.Payload;
	
  [Bindable]
  [RemoteClass(alias="org.holodeck.ebms3.submit.MsgInfoSet")]
  public class MsgMetadata
  {
    public var pmode:String;
    public var agreementRef:String = null;
    public var conversationId:String = null;
    public var refToMessageId:String = null;
    public var producer:Producer = null;
    //public var fromParties:Array = new Array();
    //public var messageProperties:Array = new Array();
    public var properties:Properties = null;
    public var callbackClass:String = null;
    //public var bodyPayload:String;	
    public var payloads:Payloads = null;
      
    public function MsgMetadata() {}
    
    public function setBodyPayload(bodyPayload:String):void
    {
      if ( bodyPayload == null || bodyPayload == "" ) return;
      if ( payloads == null ) payloads = new Payloads();
      payloads.bodyPayload = bodyPayload;
    }
  }
}