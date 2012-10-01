package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.GwtEvent;

import edu.mayo.phenoportal.shared.User;

public class LoggedInEvent extends GwtEvent<LoggedInEventHandler> {

	public static Type<LoggedInEventHandler> TYPE = new Type<LoggedInEventHandler>();
	
	private User i_user;
	
	public LoggedInEvent(User user) {
		super();
		i_user = user;
	}

	@Override
	public Type<LoggedInEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoggedInEventHandler handler) {
		handler.onLoggedIn(this);
	}

	public User getUser() {
		return i_user;
	}
}
