package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface LoggedOutEventHandler extends EventHandler {
	void onLoggedOut(LoggedOutEvent loggedOutEvent);
}
