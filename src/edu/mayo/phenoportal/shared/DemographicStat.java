package edu.mayo.phenoportal.shared;

import java.io.Serializable;

public class DemographicStat implements Serializable {

	private static final long serialVersionUID = 1L;

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
