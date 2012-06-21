package org.holodeck.common.logging.pattern;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.PatternParser;

public class MyPatternLayout extends PatternLayout {
	@Override
	protected PatternParser createPatternParser(String pattern) {
		return new MessagePatternParser(pattern);
	}
}