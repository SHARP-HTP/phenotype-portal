package edu.mayo.phenoportal.client.login;

import com.smartgwt.client.widgets.tab.Tab;

public class TabRequestNewPassword extends Tab {
	
	private static final String TITLE = "Request New Password";
	private static final String ICON = "password_reset.png";  
	
	public TabRequestNewPassword() {
		super(TITLE, ICON);
		
		setWidth(190);
	}
}
