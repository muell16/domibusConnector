// ActionScript file behind MessagePayloadInfo.mxml:

import org.holodeck.pmode.*;

[Bindable] public var payloads:Object = new Object();

[Bindable] public var mimeTypes:Array = 
               [
                 {label: "application/envoy evy", data: "application/envoy evy"}, 
                 {label: "application/fractals", data: "application/fractals"}, 
                 {label: "application/futuresplash", data: "application/futuresplash"}, 
                 {label: "application/hta", data: "application/hta"}, 
                 {label: "application/internet-property-stream", data: "application/internet-property-stream"}, 
                 {label: "application/mac-binhex40", data: "application/mac-binhex40"}, 
                 {label: "application/msword", data: "application/msword"},  
                 {label: "application/octet-stream", data: "application/octet-stream"}, 
                 {label: "application/oda", data: "application/oda"}, 
                 {label: "application/olescript", data: "application/olescript"}, 
                 {label: "application/pdf", data: "application/pdf"}, 
                 {label: "application/pics-rules", data: "application/pics-rules"}, 
                 {label: "application/pkcs10", data: "application/pkcs10"}, 
                 {label: "application/pkix-crl", data: "application/pkix-crl"}, 
                 {label: "application/postscript", data: "application/postscript"}, 
                 {label: "application/rtf", data: "application/rtf"}, 
                 {label: "application/set-payment-initiation", data: "application/set-payment-initiation"}, 
                 {label: "application/set-registration-initiation", data: "application/set-registration-initiation"}, 
                 {label: "application/vnd.ms-excel", data: "application/vnd.ms-excel"}, 
                 {label: "application/vnd.ms-outlook", data: "application/vnd.ms-outlook"}, 
                 {label: "application/vnd.ms-pkicertstore", data: ""}, 
                 {label: "application/vnd.ms-pkiseccat", data: ""}, 
                 {label: "application/vnd.ms-pkistl", data: ""}, 
                 {label: "application/vnd.ms-powerpoint", data: ""}, 
                 {label: "application/vnd.ms-project", data: ""}, 
                 {label: "application/vnd.ms-works", data: ""},  
                 {label: "application/winhlp", data: ""}, 
                 {label: "application/x-bcpio", data: ""}, 
                 {label: "application/x-cdf", data: ""}, 
                 {label: "application/x-compress", data: ""}, 
                 {label: "application/x-compressed", data: ""}, 
                 {label: "application/x-cpio", data: ""}, 
                 {label: "application/x-csh", data: ""}, 
                 {label: "application/x-director", data: ""}, 
                 {label: "application/x-dvi", data: ""}, 
                 {label: "application/x-gtar", data: ""}, 
                 {label: "application/x-gzip", data: ""}, 
                 {label: "application/x-hdf", data: ""}, 
                 {label: "application/x-internet-signup", data: ""},  
                 {label: "application/x-iphone", data: ""}, 
                 {label: "application/x-javascript", data: ""}, 
                 {label: "application/x-latex", data: ""}, 
                 {label: "application/x-msaccess", data: ""}, 
                 {label: "application/x-mscardfile", data: ""}, 
                 {label: "application/x-msclip", data: ""}, 
                 {label: "application/x-msdownload", data: ""}, 
                 {label: "application/x-msmediaview", data: ""}, 
                 {label: "application/x-msmetafile", data: ""}, 
                 {label: "application/x-msmoney", data: ""}, 
                 {label: "application/x-mspublisher", data: ""}, 
                 {label: "application/x-msschedule", data: ""}, 
                 {label: "application/x-msterminal", data: ""}, 
                 {label: "application/x-mswrite", data: ""}, 
                 {label: "application/x-netcdf", data: ""},  
                 {label: "application/x-perfmon", data: ""}, 
                 {label: "application/x-pkcs12", data: ""}, 
                 {label: "application/x-pkcs7-certificates", data: ""},  
                 {label: "application/x-pkcs7-mime", data: ""}, 
                 {label: "application/x-pkcs7-signature", data: ""}, 
                 {label: "application/x-sh", data: ""}, 
                 {label: "application/x-shar", data: ""}, 
                 {label: "application/x-shockwave-flash", data: ""}, 
                 {label: "application/x-stuffit", data: ""}, 
                 {label: "application/x-sv4cpio", data: ""}, 
                 {label: "application/x-sv4crc", data: ""}, 
                 {label: "application/x-tar", data: ""}, 
                 {label: "application/x-tcl", data: ""}, 
                 {label: "application/x-tex", data: ""}, 
                 {label: "application/x-texinfo", data: ""},  
                 {label: "application/x-troff", data: ""}, 
                 {label: "application/x-troff-man", data: ""}, 
                 {label: "application/x-troff-me", data: ""}, 
                 {label: "application/x-troff-ms", data: ""}, 
                 {label: "application/x-ustar", data: ""}, 
                 {label: "application/x-wais-source", data: ""}, 
                 {label: "application/x-x509-ca-cert", data: ""}, 
                 {label: "application/ynd.ms-pkipko", data: ""}, 
                 {label: "application/zip", data: ""}, 
                 {label: "audio/basic", data: ""}, 
                 {label: "audio/mid", data: ""}, 
                 {label: "audio/mpeg", data: ""}, 
                 {label: "audio/x-aiff", data: ""}, 
                 {label: "audio/x-mpegurl", data: ""}, 
                 {label: "audio/x-pn-realaudio", data: ""}, 
                 {label: "audio/x-wav", data: ""}, 
                 {label: "image/bmp", data: ""}, 
                 {label: "image/cis-cod", data: ""}, 
                 {label: "image/gif", data: ""}, 
                 {label: "image/ief", data: ""}, 
                 {label: "image/jpeg", data: ""}, 
                 {label: "image/pipeg", data: ""}, 
                 {label: "image/svg+xml", data: ""}, 
                 {label: "image/tiff", data: ""},  
                 {label: "image/x-cmu-raster", data: ""}, 
                 {label: "image/x-cmx", data: ""}, 
                 {label: "image/x-icon", data: ""}, 
                 {label: "image/x-portable-anymap", data: ""}, 
                 {label: "image/x-portable-bitmap", data: ""}, 
                 {label: "image/x-portable-graymap", data: ""}, 
                 {label: "image/x-portable-pixmap", data: ""}, 
                 {label: "image/x-rgb", data: ""}, 
                 {label: "image/x-xbitmap", data: ""}, 
                 {label: "image/x-xwindowdump", data: ""}, 
                 {label: "message/rfc822", data: ""}, 
                 {label: "text/css", data: ""}, 
                 {label: "text/h323", data: ""}, 
                 {label: "text/html", data: ""}, 
                 {label: "text/iuls", data: ""}, 
                 {label: "text/plain", data: ""}, 
                 {label: "text/richtext", data: ""}, 
                 {label: "text/scriptlet", data: ""}, 
                 {label: "text/tab-separated-values", data: ""}, 
                 {label: "text/webviewhtml", data: ""}, 
                 {label: "text/x-component", data: ""}, 
                 {label: "text/x-setext", data: ""}, 
                 {label: "text/x-vcard", data: ""}, 
                 {label: "video/mpeg", data: "video/mpeg"}, 
                 {label: "video/quicktime", data: "video/quicktime"},  
                 {label: "video/x-la-asf", data: "video/x-la-asf"}, 
                 {label: "video/x-ms-asf", data: "video/x-ms-asf"},  
                 {label: "video/x-msvideo", data: "video/x-msvideo"}, 
                 {label: "video/x-sgi-movie", data: "video/x-sgi-movie"}, 
                 {label: "x-world/x-vrml", data: "x-world/x-vrml"},  
               ];

public function addPart():void
{
  if ( _payloadLabel.text != null && _payloadLabel.text != "" )
  {
    var payload:Payload = payload[_payloadLabel.text] as Payload;
    if ( payload == null ) 
    {
      payload = new Payload();
      payload.label = _payloadLabel.text;
    }
    payload.addPayloadPart(_partCID.text, _mimeType.selectedLabel, 
                           _schemaLocation.text, _description.text);
    payloads[_payloadLabel.text] = payload;
    _payloadGrid.dataProvider = payload.parts;
  }
}

public function deletePart():void
{
  var index:int = _payloadGrid.selectedIndex;
  if ( index >= 0 )
  {
  	var part:PayloadPart = _payloadGrid.selectedItem as PayloadPart;
  	var payload:Payload = payload[part.label] as Payload;
    payload.parts.splice(index, 1);
    _payloadGrid.dataProvider = payload.parts;
  }
}

public function reset():void
{
  // finish this method...
}
