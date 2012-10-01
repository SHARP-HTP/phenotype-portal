package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface LogInRequestEventHandler extends EventHandler {
	void onLogInRequest(LogInRequestEvent logInRequestEvent);
}
