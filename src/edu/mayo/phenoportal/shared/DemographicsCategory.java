package edu.mayo.phenoportal.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

public class DemographicsCategory implements IsSerializable {

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