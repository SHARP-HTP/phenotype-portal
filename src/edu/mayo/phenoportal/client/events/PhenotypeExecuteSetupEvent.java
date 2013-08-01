package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class PhenotypeExecuteSetupEvent extends GwtEvent<PhenotypeExecuteSetupEventHandler> {
	public static Type<PhenotypeExecuteSetupEventHandler> TYPE = new GwtEvent.Type<PhenotypeExecuteSetupEventHandler>();

	@Override
	public Type<PhenotypeExecuteSetupEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PhenotypeExecuteSetupEventHandler handler) {
		handler.onExecuteSetup(this);
	}
}
