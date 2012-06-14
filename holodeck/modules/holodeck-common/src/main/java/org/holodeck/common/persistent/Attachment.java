package org.holodeck.common.persistent;

import java.io.*;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.holodeck.common.soap.Util;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.InternetHeaders;

/**
 * @author Hamid Ben Malek
 */
@Entity
@Table(name = "Attachments")
public class Attachment implements Serializable
{
  private static final long serialVersionUID = 9118287153028725539L;  

  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy="uuid")
  @Column(name = "ID")
  protected String id = null;

  //@Lob
  //@Column(name = "Contents", length = 1999999999)
  //private byte[] contents;

  @Column(name = "ContentType")
  private String contentType;

  @Column(name = "ContentID")
  private String contentID;

  //@Column(name = "Encoding")
  //private String transferEncoding;

  @Column(name = "FilePath")
  private String filePath;

  public Attachment() {}
  public Attachment(String file)
  {
    this.filePath = file;
    String mimeType = Util.mimeType(file);
    if ( mimeType != null ) setContentType(mimeType);
  }
  public Attachment(String file, String cid)
  {
    this(file);
    if ( cid != null && !cid.trim().equals("") )
         setContentID(cid);
  }
  /*
  public Attachment(DataHandler dh, String cid)
  {
    if (dh == null) return;
    try
    {
      setContentID(cid);
      setContentType(dh.getContentType());
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      dh.writeTo(baos);
      setContents(baos.toByteArray());
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }
  */
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  //public byte[] getContents() { return contents; }
  //public void setContents(byte[] contents) { this.contents = contents; }

  public String getContentType() { return contentType; }
  public void setContentType(String contentType)
  {
    this.contentType = contentType;
  }

  public String getContentID() { return contentID; }
  public void setContentID(String contentID) { this.contentID = contentID; }
  /*
  public String getTransferEncoding() { return transferEncoding; }

  public void setTransferEncoding(String transferEncoding)
  {
    this.transferEncoding = transferEncoding;
  } */

  public String getFilePath() { return filePath; }
  public void setFilePath(String filePath) { this.filePath = filePath; }

  public DataHandler getDataHandler(String storageFolder)
  {
    if ( filePath == null || filePath.trim().equals("") ) return null;
    File f = new File(filePath);
    if ( f.exists() )
    {
      FileDataSource fileDataSource = new FileDataSource(f);
      fileDataSource.setFileTypeMap(Util.getMimeTypes());
      return new DataHandler(fileDataSource);
    }
    else if ( storageFolder != null && !storageFolder.trim().equals("") )
    {
      String path = storageFolder + File.separator +
                    f.getParentFile().getName() + File.separator +
                    f.getName();
      File file = new File(path);
      if ( file.exists() )
      {
        FileDataSource fileDataSource = new FileDataSource(file);
        fileDataSource.setFileTypeMap(Util.getMimeTypes());
        return new DataHandler(fileDataSource);
      }
    }
    return null;
  }
  /*
  public DataHandler getDataHandler()
  {
    if ( contents != null && contents.length > 0 )
    {
      try
      {
        InternetHeaders headers = new InternetHeaders();
        headers.addHeader("Content-Type", contentType);
        if (contentID != null && !contentID.trim().equals(""))
            headers.addHeader("Content-ID", contentID);
        if (transferEncoding != null && !transferEncoding.trim().equals(""))
            headers.addHeader("Content-Transfer-Encoding", transferEncoding);
        MimeBodyPart mimePart = new MimeBodyPart(headers, contents);
        return mimePart.getDataHandler();
      }
      catch(Exception e) { e.printStackTrace(); }
      return null;
    }
    else if ( filePath != null )
    {
      File f = new File(filePath);
      if ( f.exists() )
      {
         FileDataSource fileDataSource = new FileDataSource(f);
         fileDataSource.setFileTypeMap(Util.getMimeTypes());
         return new DataHandler(fileDataSource);
      }
    }
    return null;
  } */
}