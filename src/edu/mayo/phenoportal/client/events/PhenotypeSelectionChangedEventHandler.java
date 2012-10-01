package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Interface for PhenotypeSelectionChangedEventHandler
 */
public interface PhenotypeSelectionChangedEventHandler extends EventHandler {
    void onPhenotypeSelectionChanged(PhenotypeSelectionChangedEvent phenotypeSelectionChangedEvent);
}
