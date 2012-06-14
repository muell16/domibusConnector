// ActionScript file behind SelectPayload.mxml:

import mx.controls.*;
import mx.effects.SoundEffect;
import mx.collections.ArrayCollection;
import flash.events.*;
import mx.events.CollectionEvent;
import flash.net.FileReferenceList;
import flash.net.FileFilter;
import flash.net.FileReference;

[Bindable] public var _files:ArrayCollection = new ArrayCollection();
[Bindable] public var _fileref:FileReferenceList = new FileReferenceList();
[Bindable] public var _file:FileReference = new FileReference();
[Bindable] public var _progressbar:ProgressBar = new ProgressBar();
[Bindable] public var _totalbytes:Number = 0;
[Bindable] public var _uploadbutton:Button = new Button();

[Bindable] public var payloads:Array = new Array();
public function updatePayloadList():void
{
  if ( payloads.length > 0 ) payloads.splice(0, payloads.length);
  if ( _files.length == 0 ) 
  { 
  	dispatchEvent(new Event("payloadSelectionChange"));
    return; 
  }
  payloads.push("NONE");
  for (var i:int = 0; i < _files.length; i++)
  {
    payloads.push(_files[i].name);
  }
  dispatchEvent(new Event("payloadSelectionChange"));
}

public function init():void
{
  _progressbar.mode = "manual";
  _progressbar.label = "";
  
  _delButton.enabled = false;
  _clearAllButton.enabled = false;
  
  _fileref.addEventListener(Event.SELECT, selectHandler);
  _files.addEventListener(CollectionEvent.COLLECTION_CHANGE, popDataGrid);
}

/**
 * Browse for files
 */
private function browseFiles(event:Event):void
{       
  _fileref.browse(null);
}

private function clearFileCue(event:Event):void
{
  _files.removeAll();
  updatePayloadList();
}

//Remove Selected File From Cue
private function removeSelectedFileFromCue(event:Event):void
{
  if ( _filesDG.selectedIndex >= 0 )
  {
    _files.removeItemAt( _filesDG.selectedIndex);
  }
  updatePayloadList();
}

//  called after user selected files form the browse dialouge box.
private function selectHandler(event:Event):void 
{
  var i:int;
  var msg:String ="";                         
  for ( i=0; i < event.currentTarget.fileList.length; i ++)
  {
	_files.addItem(event.currentTarget.fileList[i]);
  }
  updatePayloadList();	    
}

/** 
 * whenever the _files arraycollection changes this function is called 
 * to make sure the datagrid data jives
 */        
public function popDataGrid(event:CollectionEvent):void
{                
  getByteCount();
  checkCue();
} 

// Feed the progress bar a meaningful label
public function getByteCount():void
{
  var i:int;
  _totalbytes = 0;
  for( i = 0; i < _files.length; i++ )
  {
    _totalbytes +=  _files[i].size;
  }
  _progressbar.label = "Total Files: "+  _files.length+ 
                       "    Total Size: " + 
                        Math.round(_totalbytes/1024) + " kb"
}   

/**
 *  enable or disable upload and remove controls based on files in the cue;
 */        
public function checkCue():void
{
  if (_files.length > 0)
  {
    _uploadbutton.enabled = true;
    _delButton.enabled = true;
    _clearAllButton.enabled = true;            
  }
  else
  {
    resetProgressBar();
    //_uploadbutton.enabled = false;     
  }    
} 

// restores progress bar back to normal
public function resetProgressBar():void
{
  _progressbar.label = "";
  _progressbar.maximum = 0;
  _progressbar.minimum = 0;
} 

public function bytesToKilobytes(data:Object,blank:Object):String 
{
  var kilobytes:String;
  kilobytes = String(Math.round(data.size/ 1024)) + ' kb';
  return kilobytes
} 
