package com.tmassasin.jsf.constants;

/**
 * This class will hold all the global variables using in views.
 * 
 * @author gauravg
 */
public final class JSFConstants {
	
	private JSFConstants() {
	}
	
	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	public static final String INVALID_EMAIL_MSG = "Invalid pattern for email.";
	
	public static final String DATE_ONLY_FORMAT = "dd/MM/yyyy";
	public static final String TIME_ONLY_FORMAT = "HH:mm";
	public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";
}
