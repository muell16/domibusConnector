package eu.domibus.webadmin.commons;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

    private String[] emailList;
    private String emailFromAddress;
    private String smtpHostName;
    private String nameFrom;

    public String getNameFrom() {
        return nameFrom;
    }

    public void setNameFrom(String nameFrom) {
        this.nameFrom = nameFrom;
    }

    public void postMail(String emailMsgTxt, String emailSubject, org.apache.commons.logging.Log logger) {
        try {
            postMail(emailMsgTxt, emailSubject);
        } catch (MessagingException e) {
            logger.error(e.toString());
        }
    }

    public void postMail(String emailMsgTxt, String emailSubject) throws MessagingException {
        boolean debug = false;
        Properties props = new Properties();
        // Set the host smtp address
        props.put("mail.smtp.host", getSmtpHostName());
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(debug);
        // create a message
        Message msg = new MimeMessage(session);
        // set the from and to address
        InternetAddress addressFrom = new InternetAddress(getEmailFromAddress());
        try {
            addressFrom.setPersonal(nameFrom);
            // CHECKSTYLE.OFF: EmptyBlock
        } catch (UnsupportedEncodingException e) {
        }
        // CHECKSTYLE.ON: EmptyBlock
        InternetAddress[] addressTo = null;
        msg.setFrom(addressFrom);
        if (emailList != null) {
            addressTo = new InternetAddress[emailList.length];
            for (int i = 0; i < emailList.length; i++) {
                addressTo[i] = new InternetAddress(emailList[i]);
            }
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);
        // Setting the Subject and Content Type
        msg.setSubject(emailSubject);
        msg.setContent(emailMsgTxt, "text/html");
        Transport.send(msg);
    }

    /**
     * @return the emailFromAddress
     */
    public String getEmailFromAddress() {
        return emailFromAddress;
    }

    /**
     * @param emailFromAddress
     *            the emailFromAddress to set
     */
    public void setEmailFromAddress(String emailFromAddress) {
        this.emailFromAddress = emailFromAddress;
    }

    /**
     * @return the emailList
     */
    public String[] getEmailList() {
        return emailList;
    }

    /**
     * @param emailList
     *            the emailList to set
     */
    public void setEmailList(String[] emailList) {
        this.emailList = emailList;
    }

    /**
     * @return the smtpHostName
     */
    public String getSmtpHostName() {
        return smtpHostName;
    }

    /**
     * @param smtpHostName
     *            the smtpHostName to set
     */
    public void setSmtpHostName(String smtpHostName) {
        this.smtpHostName = smtpHostName;
    }
}
