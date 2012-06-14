// ActionScript file behind ToParty.mxml:

import org.holodeck.pmode.*;

[Bindable] public var _toParty:org.holodeck.pmode.ToParty = 
                             new org.holodeck.pmode.ToParty();

public function addParty():void
{
  if ( _toPartyId.text != null && _toPartyId.text != "" )
  {
    _toParty.addParty(_toPartyId.text, _type.text);
    _toPartyGrid.dataProvider = _toParty.parties;
  }
}

public function deleteParty():void
{
  var index:int = _toPartyGrid.selectedIndex;
  if ( index >= 0 )
  {
    _toParty.parties.splice(index, 1);
    _toPartyGrid.dataProvider = _toParty.parties;
  }
}

public function get toParty():org.holodeck.pmode.ToParty
{
  _toParty.role = _role.text;
  return _toParty;
}

public function reset():void
{
  _toPartyId.text = null;
  // finish this method...
}
