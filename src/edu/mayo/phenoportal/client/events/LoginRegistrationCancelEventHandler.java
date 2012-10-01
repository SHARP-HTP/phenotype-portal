package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface LoginRegistrationCancelEventHandler extends EventHandler {
    void onCancel(LoginRegistrationCancelEvent cancelEvent);
}