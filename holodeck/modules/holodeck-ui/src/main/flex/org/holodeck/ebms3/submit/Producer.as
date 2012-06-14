package org.holodeck.ebms3.submit
{
  [Bindable]
  [RemoteClass(alias="org.holodeck.ebms3.config.Producer")]
  public class Producer
  {
    public var name:String;
    public var partiesArray:Array = new Array();
    public var _role:String;

    public function Producer() {}
    
    public function addParty(type:String, partyId:String):void
    {
  	  var p:Party = new Party();
      if (type != null && type != "") p.type = type;
      p.partyId = partyId;
      partiesArray.push(p);
    }
    public function clear():void
    {
      _role = null;
      if (partiesArray.length > 0)
          partiesArray.splice(0, partiesArray.length);
    }
    
    public function set role(r:String):void
    {
      if ( r != null && r != "" ) _role = r;
    }
    public function get role():String { return _role; }
    
    public function isEmpty():Boolean
    {
      if ( _role != null && _role != "" ) return false;
      if ( partiesArray == null || partiesArray.length <= 0 ) return true;
      else return false;
    }
  }
}