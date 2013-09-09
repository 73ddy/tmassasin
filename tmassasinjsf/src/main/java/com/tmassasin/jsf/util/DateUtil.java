package com.tmassasin.jsf.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is a utility class for handling dates.
 * 
 * @author gauravg
 */
public final class DateUtil {

	public static final String TIME_ONLY_FORMAT = "HH:mm";
	public static final String DATE_ONLY_FORMAT = "MM/dd/yy";
	public static final String DATE_TIME_FORMAT = "MM/dd/yy HH:mm";

	/**
	 * Private constructor.
	 */
	private DateUtil() {
	}

	/**
	 * This method converts date to string using the given format.
	 * 
	 * @param date
	 *            {@link Date}
	 * @param dateFormat
	 *            {@link String}
	 * @return {@link String}
	 */
	public static String dateToString(Date date, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.format(date);
	}
}
