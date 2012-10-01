package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface LogOutRequestEventHandler extends EventHandler {
	void onLogOutRequest(LogOutRequestEvent logOutRequestEvent);
}
