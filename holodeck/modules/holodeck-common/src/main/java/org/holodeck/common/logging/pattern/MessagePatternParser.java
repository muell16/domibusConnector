package org.holodeck.common.logging.pattern;

import org.apache.log4j.helpers.PatternParser;

public class MessagePatternParser extends PatternParser {
	private static final char SENDER_CHAR = 's';
	private static final char RECIPIENT_CHAR = 'r';
	private static final char MESSAGEID_CHAR = 'm';
	
	public MessagePatternParser(String pattern) {
		super(pattern);
	}
	
	@Override
	protected void finalizeConverter(char c) {
		switch(c) {
			case MESSAGEID_CHAR: currentLiteral.setLength(0);
								 addConverter(new MessageIdPatternConverter());
								 break;
			case SENDER_CHAR:	 currentLiteral.setLength(0);
								 addConverter(new SenderPatternConverter());
								 break;
			case RECIPIENT_CHAR: currentLiteral.setLength(0);
								 addConverter(new RecipientPatternConverter());
								 break;
			default: super.finalizeConverter(c);
		}
	}
}
