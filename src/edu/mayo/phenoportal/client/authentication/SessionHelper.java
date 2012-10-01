package edu.mayo.phenoportal.client.authentication;

import java.util.Date;

import com.google.gwt.user.client.Cookies;

/**
 * Common client side session management class.
 */
public class SessionHelper {
	
	private static final long DURATION_1_WEEK = 1000 * 60 * 60 * 24 * 14;
	private static final long DURATION_10_MIN = 1000 * 60 * 10;
	
	public static final String SESSION_ID = "sid";	
	
	/**
	 * Set the user's session id
	 * 
	 * @param sessionId
	 */
	public static void storeSessionIdOnClient(String sessionId) {
		Date expires = new Date(System.currentTimeMillis() + DURATION_10_MIN);
		Cookies.setCookie(SESSION_ID, sessionId, expires, null, "/", false);

	}
	
	/**
	 * Remove the user's session on the client.
	 */
	public static void removeSessionIdOnClient() {
		Cookies.removeCookie(SESSION_ID);	
	}
	
		
}
