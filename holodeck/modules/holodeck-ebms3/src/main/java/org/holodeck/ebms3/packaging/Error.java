package org.holodeck.ebms3.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.ebms3.module.*;

/**
 * @author Hamid Ben Malek
 */
public class Error extends Element
{
  private static final long serialVersionUID = -5795434364197382100L;  

  public Error()
  {
    super(Constants.ERROR, Constants.NS, Constants.PREFIX);
  }

  public Error(String errorCode, String severity)
  {
    this();
    if ( errorCode != null && !errorCode.trim().equals("") )
         addAttribute("errorCode", errorCode);
    if ( severity != null && !severity.trim().equals("") )
         addAttribute("severity", severity);
  }

  public Error(String errorCode, String severity, String refToMessageInError)
  {
    this(errorCode, severity);
    if ( refToMessageInError != null &&
         !refToMessageInError.trim().equals("") )
         addAttribute("refToMessageInError", refToMessageInError);
  }

  public Error(String origin, String category, String errorCode,
               String severity, String refToMessageInError)
  {
    this(errorCode, severity, refToMessageInError);
    setOrigin(origin);
    setCategory(category);
  }

  public Error(String origin, String category, String errorCode,
               String severity, String refToMessageInError,
               String shortDescription)
  {
    this(origin, category, errorCode, severity, refToMessageInError);
    addAttribute("shortDescription", shortDescription);
  }

  public String getOrigin() { return getAttributeValue("origin"); }
  public void setOrigin(String origin)
  {
    if ( origin != null && !origin.trim().equals("") )
        addAttribute("origin", origin);
  }

  public String getCategory() { return getAttributeValue("category"); }
  public void setCategory(String category)
  {
    if ( category != null && !category.trim().equals("") )
        addAttribute("category", category);
  }

  public String getErrorCode() { return getAttributeValue("errorCode"); }
  public void setErrorCode(String errorCode)
  {
    if ( errorCode != null && !errorCode.trim().equals("") )
         addAttribute("errorCode", errorCode);
  }

  public String getSeverity() { return getAttributeValue("severity"); }
  public void setSeverity(String severity)
  {
    if ( severity != null && !severity.trim().equals("") )
         addAttribute("severity", severity);
  }

  public String getRefToMessageInError()
  {
    return getAttributeValue("refToMessageInError");
  }
  public void setRefToMessageInError(String refToMessageInError)
  {
    if ( refToMessageInError != null &&
         !refToMessageInError.trim().equals("") )
         addAttribute("refToMessageInError", refToMessageInError);
  }

  public static Error getEmptyPartitionError(String refToMessageInError)
  {
    return new Error("ebMS", "Communication", "EBMS:0006",
            "warning", refToMessageInError, "EmptyMessagePartitionChannel");
  }

  public static Error getValueNotRecognizedError(String refToMessageInError)
  {
    return new Error("ebMS", "Content", "EBMS:0001", "failure", refToMessageInError);
  }

  public static Error getFeatureNotSupportedError(String refToMessageInError)
  {
    return new Error("ebMS", "Content", "EBMS:0002", "warning", refToMessageInError);
  }

  public static Error getValueInconsistentError(String refToMessageInError)
  {
    return new Error("ebMS", "Content", "EBMS:0003", "failure", refToMessageInError);
  }

  public static Error getConnectionFailureError(String refToMessageInError)
  {
    return new Error("ebMS", "Communication", "EBMS:0005", "failure", refToMessageInError);
  }

  public static Error getMimeInconsistencyError(String refToMessageInError)
  {
    return new Error("ebMS", "Unpackaging", "EBMS:0007", "failure", refToMessageInError);
  }

  public static Error getInvalidHeaderError(String refToMessageInError)
  {
    return new Error("ebMS", "Unpackaging", "EBMS:0009", "failure", refToMessageInError);
  }

  public static Error getProcessingModeMismatchError(String refToMessageInError)
  {
    return new Error("ebMS", "Processing", "EBMS:0010", "failure",
            refToMessageInError, "ProcessingModeMismatch");
  }

  public static Error getFailedAuthenticationError(String refToMessageInError)
  {
    return new Error("security", "Processing", "EBMS:0101", "failure", refToMessageInError);
  }

  public static Error getDeliveryFailureError(String refToMessageInError)
  {
    return new Error("reliability", "Communication", "EBMS:0202", "failure", refToMessageInError);
  }

  public static Error getDysfunctionalReliabilityError(String refToMessageInError)
  {
    return new Error("reliability", "Processing", "EBMS:0201", "failure", refToMessageInError);
  }
}