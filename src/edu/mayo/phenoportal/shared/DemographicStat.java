package edu.mayo.phenoportal.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DemographicStat implements IsSerializable {

	String label;
	int value;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
