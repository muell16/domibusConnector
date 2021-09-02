package eu.domibus.connector.link.impl.gwjmsplugin;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TestJmsMessage implements MapMessage {

    private String jmsMessageId;
    private long jmsTimestamp;
    private byte[] jmsCorrelationBytes;
    private String correlationId;
    private Destination replyTo;
    private Destination destination;
    private int deliveryMode;

    private Map<String, Object> properties = new HashMap<>();
    private Map<String, Object> map = new HashMap<>();

    @Override
    public String getJMSMessageID() throws JMSException {
        return jmsMessageId;
    }

    @Override
    public void setJMSMessageID(String id) throws JMSException {
        jmsMessageId = id;
    }

    @Override
    public long getJMSTimestamp() throws JMSException {
        return jmsTimestamp;
    }

    @Override
    public void setJMSTimestamp(long timestamp) throws JMSException {
        this.jmsTimestamp = timestamp;
    }

    @Override
    public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
        return jmsCorrelationBytes;
    }

    @Override
    public void setJMSCorrelationIDAsBytes(byte[] correlationID) throws JMSException {
        this.jmsCorrelationBytes = correlationID;
    }

    @Override
    public void setJMSCorrelationID(String correlationID) throws JMSException {
        this.correlationId = correlationID;
    }

    @Override
    public String getJMSCorrelationID() throws JMSException {
        return correlationId;
    }

    @Override
    public Destination getJMSReplyTo() throws JMSException {
        return replyTo;
    }

    @Override
    public void setJMSReplyTo(Destination replyTo) throws JMSException {
        this.replyTo = replyTo;
    }

    @Override
    public Destination getJMSDestination() throws JMSException {
        return destination;
    }

    @Override
    public void setJMSDestination(Destination destination) throws JMSException {
        this.destination = destination;
    }

    @Override
    public int getJMSDeliveryMode() throws JMSException {
        return deliveryMode;
    }

    @Override
    public void setJMSDeliveryMode(int deliveryMode) throws JMSException {
        this.deliveryMode = deliveryMode;
    }

    @Override
    public boolean getJMSRedelivered() throws JMSException {
        return false;
    }

    @Override
    public void setJMSRedelivered(boolean redelivered) throws JMSException {

    }

    @Override
    public String getJMSType() throws JMSException {
        return null;
    }

    @Override
    public void setJMSType(String type) throws JMSException {

    }

    @Override
    public long getJMSExpiration() throws JMSException {
        return 0;
    }

    @Override
    public void setJMSExpiration(long expiration) throws JMSException {

    }

    @Override
    public long getJMSDeliveryTime() throws JMSException {
        return 0;
    }

    @Override
    public void setJMSDeliveryTime(long deliveryTime) throws JMSException {

    }

    @Override
    public int getJMSPriority() throws JMSException {
        return 0;
    }

    @Override
    public void setJMSPriority(int priority) throws JMSException {

    }

    @Override
    public void clearProperties() throws JMSException {

    }

    @Override
    public boolean propertyExists(String name) throws JMSException {
        return properties.containsKey(name);
    }

    @Override
    public boolean getBooleanProperty(String name) throws JMSException {
        return (boolean) properties.get(name);
    }

    @Override
    public byte getByteProperty(String name) throws JMSException {
        return (byte) properties.get(name);
    }

    @Override
    public short getShortProperty(String name) throws JMSException {
        return (short) properties.get(name);
    }

    @Override
    public int getIntProperty(String name) throws JMSException {
        return (int) properties.get(name);
    }

    @Override
    public long getLongProperty(String name) throws JMSException {
        return (long) properties.get(name);
    }

    @Override
    public float getFloatProperty(String name) throws JMSException {
        return (float) properties.get(name);
    }

    @Override
    public double getDoubleProperty(String name) throws JMSException {
        return (double) properties.get(name);
    }

    @Override
    public String getStringProperty(String name) throws JMSException {
        return (String) properties.get(name);
    }

    @Override
    public Object getObjectProperty(String name) throws JMSException {
        return properties.get(name);
    }

    @Override
    public Enumeration getPropertyNames() throws JMSException {
        return Collections.enumeration(properties.keySet());
    }

    @Override
    public void setBooleanProperty(String name, boolean value) throws JMSException {
        properties.put(name, value);
    }

    @Override
    public void setByteProperty(String name, byte value) throws JMSException {
        properties.put(name, value);
    }

    @Override
    public void setShortProperty(String name, short value) throws JMSException {
        properties.put(name, value);
    }

    @Override
    public void setIntProperty(String name, int value) throws JMSException {
        properties.put(name, value);
    }

    @Override
    public void setLongProperty(String name, long value) throws JMSException {
        properties.put(name, value);
    }

    @Override
    public void setFloatProperty(String name, float value) throws JMSException {
        properties.put(name, value);
    }

    @Override
    public void setDoubleProperty(String name, double value) throws JMSException {
        properties.put(name, value);
    }

    @Override
    public void setStringProperty(String name, String value) throws JMSException {
        properties.put(name, value);
    }

    @Override
    public void setObjectProperty(String name, Object value) throws JMSException {
        properties.put(name, value);
    }

    @Override
    public void acknowledge() throws JMSException {

    }

    @Override
    public void clearBody() throws JMSException {

    }

    @Override
    public <T> T getBody(Class<T> c) throws JMSException {
        return null;
    }

    @Override
    public boolean isBodyAssignableTo(Class c) throws JMSException {
        return false;
    }

    @Override
    public boolean getBoolean(String name) throws JMSException {
        return false;
    }

    @Override
    public byte getByte(String name) throws JMSException {
        return 0;
    }

    @Override
    public short getShort(String name) throws JMSException {
        return 0;
    }

    @Override
    public char getChar(String name) throws JMSException {
        return 0;
    }

    @Override
    public int getInt(String name) throws JMSException {
        return (int) map.get(name);
    }

    @Override
    public long getLong(String name) throws JMSException {
        return (long) map.get(name);
    }

    @Override
    public float getFloat(String name) throws JMSException {
        return (float) map.get(name);
    }

    @Override
    public double getDouble(String name) throws JMSException {
        return (double) map.get(name);
    }

    @Override
    public String getString(String name) throws JMSException {
        return (String) map.get(name);
    }

    @Override
    public byte[] getBytes(String name) throws JMSException {
        return (byte[]) map.get(name);
    }

    @Override
    public Object getObject(String name) throws JMSException {
        return map.get(name);
    }

    @Override
    public Enumeration getMapNames() throws JMSException {
        return Collections.enumeration(map.keySet());
    }

    @Override
    public void setBoolean(String name, boolean value) throws JMSException {
        map.put(name, value);
    }

    @Override
    public void setByte(String name, byte value) throws JMSException {
        map.put(name, value);
    }

    @Override
    public void setShort(String name, short value) throws JMSException {
        map.put(name, value);
    }

    @Override
    public void setChar(String name, char value) throws JMSException {
        map.put(name, value);
    }

    @Override
    public void setInt(String name, int value) throws JMSException {
        map.put(name, value);
    }

    @Override
    public void setLong(String name, long value) throws JMSException {
        map.put(name, value);
    }

    @Override
    public void setFloat(String name, float value) throws JMSException {
        map.put(name, value);
    }

    @Override
    public void setDouble(String name, double value) throws JMSException {
        map.put(name, value);
    }

    @Override
    public void setString(String name, String value) throws JMSException {
        map.put(name, value);
    }

    @Override
    public void setBytes(String name, byte[] value) throws JMSException {
        map.put(name, value);
    }

    @Override
    public void setBytes(String name, byte[] value, int offset, int length) throws JMSException {
        map.put(name, value);
    }

    @Override
    public void setObject(String name, Object value) throws JMSException {
        map.put(name, value);
    }

    @Override
    public boolean itemExists(String name) throws JMSException {
        return map.get(name) != null;
    }
}
