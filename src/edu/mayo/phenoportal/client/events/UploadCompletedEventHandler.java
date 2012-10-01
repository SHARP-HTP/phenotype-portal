package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface UploadCompletedEventHandler extends EventHandler {
    void onUploadCompleted(UploadCompletedEvent uploadCompletedEvent);
}
