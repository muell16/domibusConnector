package org.holodeck.ebms3.submit
{
  [Bindable]
  [RemoteClass(alias="org.holodeck.ebms3.submit.Properties")]
  public class Properties
  {
    public var propertyArray:Array = new Array();

    public function Properties() {}
    
    public function addProperty(name:String, value:String):void
    {
      var property:MsgProperty = new MsgProperty();
      property.name = name;
      property.value = value;
      propertyArray.push(property);
    }
    
    public function clear():void
    {
      if (propertyArray != null && propertyArray.length > 0)
          propertyArray.splice(0, propertyArray.length);
    }
  }
}