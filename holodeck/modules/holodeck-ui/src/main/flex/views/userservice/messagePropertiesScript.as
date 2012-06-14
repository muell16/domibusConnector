// ActionScript file behind MessageProperties.mxml:

import org.holodeck.pmode.*;

[Bindable] public var properties:Array = new Array();

public function addProperty():void
{
  if ( _propertyName.text != null && _propertyName.text != "" )
  {
    var property:Property = new Property();
    property.name = _propertyName.text;
    property.type = _type.text;
    property.required = _required.selected;
    property.description = _description.text;
    properties.push(property);
    _propertiesGrid.dataProvider = properties;
  }
}

public function deleteProperty():void
{
  var index:int = _propertiesGrid.selectedIndex;
  if ( index >= 0 )
  {
    properties.splice(index, 1);
    _propertiesGrid.dataProvider = properties;
  }
}

public function reset():void
{
  // finish this method...
}