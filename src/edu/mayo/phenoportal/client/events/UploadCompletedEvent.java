package edu.mayo.phenoportal.client.events;

import com.google.gwt.event.shared.GwtEvent;

import edu.mayo.phenoportal.client.core.AlgorithmData;

public class UploadCompletedEvent extends GwtEvent<UploadCompletedEventHandler> {

	private AlgorithmData i_algorithmData;
	
	public static Type<UploadCompletedEventHandler> TYPE = new Type<UploadCompletedEventHandler>();
	
	public UploadCompletedEvent(AlgorithmData algorithmData) {
		super();
		i_algorithmData = algorithmData;
	}

	@Override
	public Type<UploadCompletedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(UploadCompletedEventHandler handler) {
		handler.onUploadCompleted(this);
	}

	public AlgorithmData getAlgorithmData() {
		return i_algorithmData;
	}
}
