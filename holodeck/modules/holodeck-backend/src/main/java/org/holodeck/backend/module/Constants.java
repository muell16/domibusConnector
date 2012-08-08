/*
 * 
 */
package org.holodeck.backend.module;

import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.Parameter;
import org.holodeck.backend.util.StringUtils;

/**
 * The Class Constants.
 */
public class Constants {
	
	/** The Constant PERSISTENCE_UNIT_PROPERTY_KEY. */
	public static final String PERSISTENCE_UNIT_PROPERTY_KEY = "PersistenceUnit";

	/** The Constant EVIDENCES_PROPERTY_KEY. */
	public static final String EVIDENCES_PROPERTY_KEY = "Evidences";
	
	/** The Constant MESSAGES_FOLDER_PROPERTY_KEY. */
	public static final String MESSAGES_FOLDER_PROPERTY_KEY = "MessagesFolder";
	
	/** The Constant MESSAGES_TIME_LIVE_PROPERTY_KEY. */
	public static final String MESSAGES_TIME_LIVE_PROPERTY_KEY = "MessagesTimeLive";
	
	/** The Constant DELETE_MESSAGES_CRON_PROPERTY_KEY. */
	public static final String DELETE_MESSAGES_CRON_PROPERTY_KEY = "DeleteMessagesCron";

	/** The Constant DEFAULT_TIME_LIVE. */
	public static final int DEFAULT_TIME_LIVE = 60;// In days

	/** The Constant PAYLOAD_FILE_NAME_FORMAT. */
	public static final String PAYLOAD_FILE_NAME_FORMAT = "payload_{0}.bin";
	
	/** The Constant MESSAGING_FILE_NAME. */
	public static final String MESSAGING_FILE_NAME = "messaging.xml";
	
	/** The Constant METADATA_FILE_NAME. */
	public static final String METADATA_FILE_NAME = "metadata.xml";

	/** The Constant PATTERN_DATE_FORMAT. */
	public static final String PATTERN_DATE_FORMAT = "yyyy_MM_dd__HH_mm_ss";

	/** The module. */
	public static AxisModule module;

	/**
	 * Checks if is evidences actived.
	 *
	 * @return true, if is evidences actived
	 */
	public static boolean isEvidencesActived() {
		if (module == null)
			return false;
		Parameter param = module.getParameter(EVIDENCES_PROPERTY_KEY);
		String value = (String) param.getValue();
		if (value == null)
			return false;
		return Boolean.parseBoolean((String) param.getValue());
	}

	/**
	 * Gets the persistence unit.
	 *
	 * @return the persistence unit
	 */
	public static String getPersistenceUnit() {
		if (module == null)
			return null;
		Parameter param = module.getParameter(PERSISTENCE_UNIT_PROPERTY_KEY);
		String value = (String) param.getValue();
		if (value == null)
			return null;
		return value;
	}

	/**
	 * Gets the messages folder.
	 *
	 * @return the messages folder
	 */
	public static String getMessagesFolder() {
		if (module == null)
			return null;
		Parameter param = module.getParameter(MESSAGES_FOLDER_PROPERTY_KEY);
		String value = (String) param.getValue();
		if (value == null)
			return null;
		return value;
	}

	/**
	 * Gets the messages time live.
	 *
	 * @return the messages time live
	 */
	public static int getMessagesTimeLive() {
		if (module == null)
			return DEFAULT_TIME_LIVE;
		Parameter param = module.getParameter(MESSAGES_TIME_LIVE_PROPERTY_KEY);
		String value = (String) param.getValue();
		if (param == null || !StringUtils.isNumeric(value))
			return DEFAULT_TIME_LIVE;
		return Integer.parseInt(value);
	}

	/**
	 * Gets the delete messages cron.
	 *
	 * @return the delete messages cron
	 */
	public static String getDeleteMessagesCron() {
		if (module == null)
			return null;
		Parameter param = module.getParameter(DELETE_MESSAGES_CRON_PROPERTY_KEY);
		String value = (String) param.getValue();
		if (value == null)
			return null;
		return value;
	}
}