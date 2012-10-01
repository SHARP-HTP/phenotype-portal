package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class LogInRequestEvent extends GwtEvent<LogInRequestEventHandler> {

    private final boolean i_isRegister;

    public static Type<LogInRequestEventHandler> TYPE = new Type<LogInRequestEventHandler>();

    public LogInRequestEvent() {
        this(false);
    }

    public LogInRequestEvent(boolean isRegister) {
        super();
        i_isRegister = isRegister;
    }

    @Override
    public Type<LogInRequestEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(LogInRequestEventHandler handler) {
        handler.onLogInRequest(this);
    }

    /**
     * determine if its the usual login request or a request to register.
     * 
     * @return
     */
    public boolean getIsRegister() {
        return i_isRegister;
    }
}
