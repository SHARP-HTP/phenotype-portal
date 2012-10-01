package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface UploadStartedEventHandler extends EventHandler {
    void onUploadStarted(UploadStartedEvent uploadStartedEvent);
}
