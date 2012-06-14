package org.holodeck.ebms3.submit
{
  [Bindable]
  [RemoteClass(alias="org.holodeck.ebms3.submit.Payloads")]
  public class Payloads
  {
    public var bodyPayload:String = null;
    public var payloadArray:Array = new Array();

    public function Payloads() {}  

    public function addPayload(cid:String, payloadURI:String):void
    {
      var p:Payload = new Payload();
      if (cid != null && cid != "") p.cid = cid;
      p.payload = payloadURI;
      payloadArray.push(p);
    }
    
    public function clear():void
    {
      bodyPayload = null;
      if (payloadArray != null && payloadArray.length > 0)
          payloadArray.splice(0, payloadArray.length);
    }
    
    public function isEmpty():Boolean
    {
      if ( payloadArray != null && payloadArray.length > 0 ) return false;
      if ( bodyPayload == null || bodyPayload == "" ||
           bodyPayload == "NONE" ) return true;
      else return false;
    }
  }
}