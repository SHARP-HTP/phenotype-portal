package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.GwtEvent;

import edu.mayo.phenoportal.client.core.AlgorithmData;

/**
 * Event to indicate that a user selected a phenotyp from the phenotype tree.
 */
public class PhenotypeSelectionChangedEvent extends GwtEvent<PhenotypeSelectionChangedEventHandler> {

    private final AlgorithmData i_algorithmData;

    public static Type<PhenotypeSelectionChangedEventHandler> TYPE = new Type<PhenotypeSelectionChangedEventHandler>();

    public PhenotypeSelectionChangedEvent(AlgorithmData data) {
        super();
        i_algorithmData = data;
    }

    @Override
    protected void dispatch(PhenotypeSelectionChangedEventHandler handler) {
        handler.onPhenotypeSelectionChanged(this);
    }

    @Override
    public Type<PhenotypeSelectionChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

    public AlgorithmData getAlgorithmData() {
        return i_algorithmData;
    }

}
