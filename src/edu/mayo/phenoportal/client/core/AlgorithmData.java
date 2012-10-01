package edu.mayo.phenoportal.client.core;

/**
 * Class to encapsulate a selected algorithm.
 *
 */
public class AlgorithmData {

	private String i_categoryId;
	private String i_parentId;
	private String i_algorithmVersion;
	private String i_algorithmUser;
	private String i_algorithmDescription;
	private String i_algorithmName;
	
	public AlgorithmData() {

		i_categoryId = "";
		i_parentId = "";
		i_algorithmVersion = "";
		i_algorithmUser = "";
		i_algorithmDescription = "";
		i_algorithmName = "";
	}
	
	public AlgorithmData(String categoryId, String parentId,
			String algorithmVersion, String algorithmUser,
			String algorithmDescription, String algorithmName) {

		i_categoryId = categoryId;
		i_parentId = parentId;
		i_algorithmVersion = algorithmVersion;
		i_algorithmUser = algorithmUser;
		i_algorithmDescription = algorithmDescription;
		i_algorithmName = algorithmName;
	}


	/**
	 * @return the categoryId
	 */
	public String getCategoryId() {
		return i_categoryId;
	}


	/**
	 * @param the categoryId to set
	 */
	public void setCategoryId(String categoryId) {
		i_categoryId = categoryId;
	}


	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return i_parentId;
	}


	/**
	 * @param the i_parentId to set
	 */
	public void setParentId(String parentId) {
		i_parentId = parentId;
	}


	/**
	 * @return the i_algorithmVersion
	 */
	public String getAlgorithmVersion() {
		return i_algorithmVersion;
	}


	/**
	 * @param i_algorithmVersion the i_algorithmVersion to set
	 */
	public void setAlgorithmVersion(String algorithmVersion) {
		i_algorithmVersion = algorithmVersion;
	}


	/**
	 * @return the i_algorithmUser
	 */
	public String getAlgorithmUser() {
		return i_algorithmUser;
	}


	/**
	 * @param the i_algorithmUser to set
	 */
	public void setAlgorithmUser(String algorithmUser) {
		i_algorithmUser = algorithmUser;
	}


	/**
	 * @return the i_algorithmDescription
	 */
	public String getAlgorithmDescription() {
		return i_algorithmDescription;
	}


	/**
	 * @param i_algorithmDescription the i_algorithmDescription to set
	 */
	public void setAlgorithmDescription(String algorithmDescription) {
		i_algorithmDescription = algorithmDescription;
	}


	public String getAlgorithmName() {
		return i_algorithmName;
	}


	public void setAlgorithmName(String algorithmName) {
		i_algorithmName = algorithmName;
	}
	
	
}
