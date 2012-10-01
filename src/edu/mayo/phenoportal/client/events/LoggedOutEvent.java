package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class LoggedOutEvent extends GwtEvent<LoggedOutEventHandler> {

	public static Type<LoggedOutEventHandler> TYPE = new Type<LoggedOutEventHandler>();
	
	@Override
	public Type<LoggedOutEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoggedOutEventHandler handler) {
		handler.onLoggedOut(this);
	}

}
