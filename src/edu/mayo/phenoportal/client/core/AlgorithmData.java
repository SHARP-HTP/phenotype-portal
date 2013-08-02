package edu.mayo.phenoportal.client.core;

import edu.mayo.phenoportal.shared.ValueSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to encapsulate a selected algorithm.
 *
 */
public class AlgorithmData implements Serializable {

	private int id;
	private String i_categoryId;
	private String i_parentId;
	private String i_algorithmVersion;
	private String i_algorithmUser;
	private String i_algorithmDescription;
	private String i_algorithmName;
	private Map<String, ValueSet> valueSets = new HashMap<String, ValueSet>();
	private Map<String, ValueSet> supplementalValueSets = new HashMap<String, ValueSet>();

	public AlgorithmData() {
		id = -1;
		i_categoryId = "";
		i_parentId = "";
		i_algorithmVersion = "";
		i_algorithmUser = "";
		i_algorithmDescription = "";
		i_algorithmName = "";
	}
	
	public AlgorithmData(int id, String categoryId, String parentId,
			String algorithmVersion, String algorithmUser,
			String algorithmDescription, String algorithmName) {
		this.id = id;
		i_categoryId = categoryId;
		i_parentId = parentId;
		i_algorithmVersion = algorithmVersion;
		i_algorithmUser = algorithmUser;
		i_algorithmDescription = algorithmDescription;
		i_algorithmName = algorithmName;
	}

	public String getCategoryId() {
		return i_categoryId;
	}

	public void setCategoryId(String categoryId) {
		i_categoryId = categoryId;
	}

	public String getParentId() {
		return i_parentId;
	}

	public void setParentId(String parentId) {
		i_parentId = parentId;
	}

	public String getAlgorithmVersion() {
		return i_algorithmVersion;
	}


	public void setAlgorithmVersion(String algorithmVersion) {
		i_algorithmVersion = algorithmVersion;
	}


	public String getAlgorithmUser() {
		return i_algorithmUser;
	}


	public void setAlgorithmUser(String algorithmUser) {
		i_algorithmUser = algorithmUser;
	}

	public String getAlgorithmDescription() {
		return i_algorithmDescription;
	}

	public void setAlgorithmDescription(String algorithmDescription) {
		i_algorithmDescription = algorithmDescription;
	}

	public String getAlgorithmName() {
		return i_algorithmName;
	}

	public void setAlgorithmName(String algorithmName) {
		i_algorithmName = algorithmName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void addValueSet(ValueSet vs) {
		valueSets.put(vs.name, vs);
	}

	public void addValueSet(String name, String desc, String version) {
		addValueSet(new ValueSet(name, desc, version, ""));
	}

	public void setValueSets(List<ValueSet> valueSets) {
		for (ValueSet vs : valueSets) {
			this.valueSets.put(vs.name, vs);
		}
	}

	public Collection<ValueSet> getValueSets() {
		return this.valueSets.values();
	}

	public void addSupplementalValueSet(ValueSet vs) {
		supplementalValueSets.put(vs.name, vs);
	}

	public void addSupplementalValueSet(String name, String desc, String version) {
		addSupplementalValueSet(new ValueSet(name, desc, version, ""));
	}

	public void setSupplementalValueSets(List<ValueSet> valueSets) {
		for (ValueSet vs : valueSets) {
			this.supplementalValueSets.put(vs.name, vs);
		}
	}

	public Collection<ValueSet> getSupplementalValueSets() {
		return this.supplementalValueSets.values();
	}
}
