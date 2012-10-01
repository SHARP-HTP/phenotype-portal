package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class UploadCanceledEvent extends GwtEvent<UploadCanceledEventHandler> {

	public static Type<UploadCanceledEventHandler> TYPE = new Type<UploadCanceledEventHandler>();
	
	@Override
	public Type<UploadCanceledEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(UploadCanceledEventHandler handler) {
		handler.onUploadCanceled(this);
	}

}
