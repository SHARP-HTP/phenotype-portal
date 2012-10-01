package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class LoginRegistrationCancelEvent extends GwtEvent<LoginRegistrationCancelEventHandler> {

    public static Type<LoginRegistrationCancelEventHandler> TYPE = new Type<LoginRegistrationCancelEventHandler>();

    @Override
    public Type<LoginRegistrationCancelEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(LoginRegistrationCancelEventHandler handler) {
        handler.onCancel(this);
    }

}
