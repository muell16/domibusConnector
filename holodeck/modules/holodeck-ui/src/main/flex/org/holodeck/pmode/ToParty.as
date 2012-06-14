package org.holodeck.pmode
{
  public class ToParty
  {
    public var role:String;
    public var parties:Array = new Array();

    public function ToParty() {}  

    /*
    public function addParty(party:Party):void
    {
      parties.push(party);
    }
    */
    public function addParty(partyId:String, type:String):void
    {
      var party:Party = new Party();
      party.partyId = partyId;
      party.type = type;
      parties.push(party);
    }
  }
}