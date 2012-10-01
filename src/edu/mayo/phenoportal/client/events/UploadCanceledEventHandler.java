package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface UploadCanceledEventHandler extends EventHandler {
    void onUploadCanceled(UploadCanceledEvent uploadCanceledEvent);
}
