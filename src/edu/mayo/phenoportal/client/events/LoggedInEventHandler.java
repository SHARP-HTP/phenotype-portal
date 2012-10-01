package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface LoggedInEventHandler extends EventHandler {
	void onLoggedIn(LoggedInEvent loggedInEvent);
}
