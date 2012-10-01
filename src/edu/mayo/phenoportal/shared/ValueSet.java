package edu.mayo.phenoportal.shared;

import java.io.Serializable;
import java.util.Map;

public class ValueSet implements Serializable {
	private String developer;
	private String oid;
	private String name;
	private String qdmCategory;
	private String codeSystem;
	private String codeSystemVersion;
	private Map<String, String> values;

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQdmCategory() {
		return qdmCategory;
	}

	public void setQdmCategory(String qdmCategory) {
		this.qdmCategory = qdmCategory;
	}

	public String getCodeSystem() {
		return codeSystem;
	}

	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}

	public String getCodeSystemVersion() {
		return codeSystemVersion;
	}

	public void setCodeSystemVersion(String codeSystemVersion) {
		this.codeSystemVersion = codeSystemVersion;
	}

	/**
	 * Key: Code, Value: Descriptor
	 * @return
	 */
	public Map<String, String> getValues() {
		return values;
	}

	/**
	 * Key: Code, Value: Descriptor
	 * @param values
	 */
	public void setValues(Map<String, String> values) {
		this.values = values;
	}
}

