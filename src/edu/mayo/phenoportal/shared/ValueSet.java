package edu.mayo.phenoportal.shared;

import java.io.Serializable;

public class ValueSet implements Serializable {
	public String name;
	public String version;

	public ValueSet() { }

	public ValueSet(String name, String version) {
		this.name = name;
		this.version = version;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof ValueSet &&
		  ((ValueSet) other).name.equalsIgnoreCase(name) &&
		  ((ValueSet) other).version.equalsIgnoreCase(version);
	}
}
