package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class PhenotypeExecuteStartedEvent extends GwtEvent<PhenotypeExecuteStartedEventHandler> {

	public static Type<PhenotypeExecuteStartedEventHandler> TYPE = new Type<PhenotypeExecuteStartedEventHandler>();
	
	@Override
	public Type<PhenotypeExecuteStartedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PhenotypeExecuteStartedEventHandler handler) {
		handler.onPhenotypeExecuteStart(this);
	}

}