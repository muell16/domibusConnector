package org.holodeck.pmode
{
  public class Payload
  {
    public var label:String;
    public var parts:Array = new Array();

    public function Payload() {}
    
    public function addPayloadPart(partCID:String, mimeType:String, 
                                   schemaLocation:String, description:String):void
    {
      var part:PayloadPart = new PayloadPart();
      part.partCID = partCID;
      part.mimeType = mimeType;
      part.schemaLocation = schemaLocation;
      part.description = description;
      part.label = label;
      parts.push(part);
    }
  }
}