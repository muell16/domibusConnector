package org.holodeck.common.logging.level;

import org.apache.log4j.Level;

@SuppressWarnings("serial")
public class MessageLevel extends Level {
	
	public static final int MESSAGE_INT = DEBUG_INT + 10;
	
	public static final Level MESSAGE = new MessageLevel(MESSAGE_INT, "MESSAGE", 7);
	
	protected MessageLevel(int level, String levelStr, int syslogEquivalent) {
		super(level, levelStr, syslogEquivalent);
	}


	public static Level toLevel(String level) {
		if(level != null && level.toUpperCase().equals("MESSAGE")) {
			return MESSAGE;
		}
		return (Level)toLevel(level, Level.DEBUG);
	}
	
	
	public static Level toLevel(int value) {
		if(value == MESSAGE_INT) {
			return MESSAGE;
		}
		return (Level)toLevel(value, Level.DEBUG);
	}
	
	
	public static Level toLevel(int value, Level defaultLevel) {
		if (value == MESSAGE_INT) {
			return MESSAGE;
		}
		return Level.toLevel(value, defaultLevel);
	}
	
	
	public static Level toLevel(String level, Level defaultLevel) {
		if(level != null && level.toUpperCase().equals("MESSAGE")) {
			return MESSAGE;
		}
		return Level.toLevel(level, defaultLevel);
	}
	
}
