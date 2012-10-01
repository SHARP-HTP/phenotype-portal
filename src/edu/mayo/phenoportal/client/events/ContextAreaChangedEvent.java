package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.GwtEvent;

import edu.mayo.phenoportal.client.core.ContextAreas;

/**
 * Event to fire when the context area changes.
 *
 */
public class ContextAreaChangedEvent extends GwtEvent<ContextAreaChangedEventHandler> {

	public static Type<ContextAreaChangedEventHandler> TYPE = new Type<ContextAreaChangedEventHandler>();
	
	private ContextAreas.types i_contextType;
	private Object i_data;
		
	public ContextAreaChangedEvent(ContextAreas.types type) {
		super();
		i_contextType = type;
	}

	public ContextAreaChangedEvent(ContextAreas.types type, Object data) {
		super();
		i_contextType = type;
		i_data = data;
	}
	
	@Override
	public Type<ContextAreaChangedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ContextAreaChangedEventHandler handler) {
		handler.onContextAreaChanged(this);
	}
	
	public ContextAreas.types getContextType() {
		return i_contextType;
	}
	
	public Object getData() {
		return i_data;
	}
}
