package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.GwtEvent;
import edu.mayo.phenoportal.shared.Execution;

public class PhenotypeExecuteCompletedEvent extends GwtEvent<PhenotypeExecuteCompletedEventHandler> {
    private final boolean i_executeSuccess;
    private Execution execution;

    public static Type<PhenotypeExecuteCompletedEventHandler> TYPE = new Type<PhenotypeExecuteCompletedEventHandler>();

    public PhenotypeExecuteCompletedEvent(boolean executeSuccess, Execution execution) {
        super();
        i_executeSuccess = executeSuccess;
        this.execution = execution;
    }

    public PhenotypeExecuteCompletedEvent(boolean i_executeSuccess) {
        super();
        this.i_executeSuccess = i_executeSuccess;
    }

    @Override
    public Type<PhenotypeExecuteCompletedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PhenotypeExecuteCompletedEventHandler handler) {
        handler.onPhenotypeExecuteCompleted(this);
    }

    public boolean getExecuteSuccess() {
        return i_executeSuccess;
    }

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

}
