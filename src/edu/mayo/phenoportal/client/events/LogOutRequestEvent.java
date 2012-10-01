package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class LogOutRequestEvent extends GwtEvent<LogOutRequestEventHandler> {

	public static Type<LogOutRequestEventHandler> TYPE = new Type<LogOutRequestEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LogOutRequestEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LogOutRequestEventHandler handler) {
		handler.onLogOutRequest(this);
	}

}
