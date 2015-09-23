package eu.domibus.connector.common.message;

public class MessageError {

    private Message message;
    private String text;
    private String details;
    private String source;

    public MessageError() {
    }

    public MessageError(Message message, String text, String details, String source) {
        super();
        this.message = message;
        this.text = text;
        this.details = details;
        this.source = source;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
