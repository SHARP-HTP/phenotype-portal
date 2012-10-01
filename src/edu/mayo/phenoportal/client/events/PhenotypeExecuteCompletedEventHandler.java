package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface PhenotypeExecuteCompletedEventHandler  extends EventHandler {
	void onPhenotypeExecuteCompleted(PhenotypeExecuteCompletedEvent phenotypeExecuteCompletedEvent);
}
