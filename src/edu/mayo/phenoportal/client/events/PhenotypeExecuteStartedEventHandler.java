package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface PhenotypeExecuteStartedEventHandler extends EventHandler {
	void onPhenotypeExecuteStart(PhenotypeExecuteStartedEvent phenotypeExecuteStartedEvent);
}
