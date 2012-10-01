package edu.mayo.phenoportal.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DemographicsCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	List<DemographicStat> demoStatList = new ArrayList<DemographicStat>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DemographicStat> getDemoStatList() {
		return demoStatList;
	}

	public void setDemoStatList(List<DemographicStat> demoStatList) {
		this.demoStatList = demoStatList;
	}

}