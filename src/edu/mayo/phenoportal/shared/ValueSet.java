package edu.mayo.phenoportal.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ValueSet implements IsSerializable {
	public String name;
	public String description;
	public String version;
	public String comment;
	public String changeSetId;
	public String documentUri;

	public ValueSet() { }

	public ValueSet(String name, String description, String version, String comment) {
		this.name = name;
		this.description = description;
		this.version = version;
		this.comment = comment;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof ValueSet &&
		  ((ValueSet) other).name.equalsIgnoreCase(name) &&
		  ((ValueSet) other).version.equalsIgnoreCase(version);
	}

}
