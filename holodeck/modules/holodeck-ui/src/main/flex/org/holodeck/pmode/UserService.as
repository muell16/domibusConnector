package org.holodeck.pmode
{
  public class UserService
  {
    public var name:String;
    public var toParty:ToParty;
    public var service:String;
    public var action:String;
    public var properties:Array = new Array();
    public var payloads:Array = new Array();
    
    public function addProperty(p:Property):void
    {
      properties.push(p);
    }
    
    public function addProperty(propName:String, propType:String, 
                                propRequired:Boolean=false, propDesc:String):void
    {
      var prop:Property = new Property();
      prop.name = propName;
      prop.type = propType;
      prop.required = propRequired;
      prop.description = propDesc;
      properties.push(prop);
    }
    
    public function addPayload(p:Payload):void
    {
      payloads.push(p);
    }
    
    public function addPayload(label:String, parts:Array):void
    {
      var payload:Payload = new Payload();
      payload.label = label;
      payload.parts = parts;
      payloads.push(payload);
    }
  }
}