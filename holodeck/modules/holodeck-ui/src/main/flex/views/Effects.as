package views
{
  import mx.controls.*;
  	
  public class Effects
  {
	[Embed(source="/assets/audio/tng-doorbell.mp3")]
   	public static var sound1:Class;
   	
   	[Embed(source="/assets/audio/ccv.mp3")]
   	public static var sound2:Class;
   	
   	[Embed(source="/assets/audio/zap5.mp3")]
   	public static var sound3:Class;
   	
   	[Embed(source="/assets/audio/command-codes-verified.mp3")]
   	public static var sound4:Class;
   	
   	[Embed(source="/assets/audio/tas_sound_button_03.mp3")]
   	public static var sound5:Class;
   	
   	[Embed(source="/assets/audio/voy-doc-plzstate.mp3")]
   	public static var sound6:Class;
   	
   	[Embed(source="/assets/audio/voy-kes-activate.mp3")]
   	public static var sound7:Class;
   	
   	[Embed(source="/assets/audio/c809.mp3")]
   	public static var sound8:Class;
   	
   	[Embed(source="/assets/audio/laser.mp3")]
   	public static var sound9:Class;
   	
   	[Embed(source="/assets/audio/Transfer-of-data-is-complete.mp3")]
   	public static var sound10:Class;
   	
   	[Embed(source="/assets/audio/Ding.mp3")]
   	public static var sound11:Class;
   	
   	[Embed(source="/assets/audio/Affirmative.mp3")]
   	public static var sound12:Class;
   	
   	[Embed(source="/assets/audio/Standby.mp3")]
   	public static var sound13:Class;
   	
   	[Embed(source="/assets/audio/Access-Denied.mp3")]
   	public static var sound14:Class;
   	
   	[Embed(source="/assets/audio/indus058.mp3")]
   	public static var sound15:Class;
   	
   	public static function bringToFront(field:TextInput):void
    {
      field.styleName = "textInputFront"; 
    }  
      
    public static function bringBack(field:TextInput):void
    {
      //field.styleName = "textInputBack"; 
      field.styleName = null;
    }
  }
}