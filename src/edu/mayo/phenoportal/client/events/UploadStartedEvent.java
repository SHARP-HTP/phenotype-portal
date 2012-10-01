package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class UploadStartedEvent extends GwtEvent<UploadStartedEventHandler> {

    public static Type<UploadStartedEventHandler> TYPE = new Type<UploadStartedEventHandler>();

    public UploadStartedEvent() {
        super();
    }

    @Override
    public Type<UploadStartedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UploadStartedEventHandler handler) {
        handler.onUploadStarted(this);
    }

}
