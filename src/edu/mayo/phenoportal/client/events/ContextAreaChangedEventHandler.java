package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Interface for ContextAreaChangedEventHandler
 *
 */
public interface ContextAreaChangedEventHandler extends EventHandler {
	void onContextAreaChanged(ContextAreaChangedEvent contextAreaChangedEvent);
}
