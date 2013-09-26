package edu.mayo.phenoportal.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

public class Execution implements IsSerializable {

	private String id;
	private String url;
	private String user;
	private String algorithmName;
	private String algorithmVersion;
	private String algorithmCategoryPath;
	private String algorithmCategoryId;
	private String startDate;
	private String endDate;
	private String elapsedTime;
	private String status;
	private String bpmnPath;
	private String xmlPath;
	private String rulesPath;
	private String dateRangeFrom;
	private String dateRangeTo;
	private Image image;
	private List<Demographic> demographics;
	private boolean error = false;

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public String getAlgorithmVersion() {
		return algorithmVersion;
	}

	public void setAlgorithmVersion(String algorithmVersion) {
		this.algorithmVersion = algorithmVersion;
	}

	public String getAlgorithmCategoryPath() {
		return algorithmCategoryPath;
	}

	public void setAlgorithmCategoryPath(String algorithmCategoryPath) {
		this.algorithmCategoryPath = algorithmCategoryPath;
	}

	public String getAlgorithmCategoryId() {
		return algorithmCategoryId;
	}

	public void setAlgorithmCategoryId(String algorithmCategoryId) {
		this.algorithmCategoryId = algorithmCategoryId;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public String getDateRangeFrom() {
		return dateRangeFrom;
	}

	public void setDateRangeFrom(String dateRangeFrom) {
		this.dateRangeFrom = dateRangeFrom;
	}

	public String getDateRangeTo() {
		return dateRangeTo;
	}

	public void setDateRangeTo(String dateRangeTo) {
		this.dateRangeTo = dateRangeTo;
	}

	public String getXmlPath() {
		return xmlPath;
	}

	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}

	public String getBpmnPath() {
		return bpmnPath;
	}

	public void setBpmnPath(String bpmnPath) {
		this.bpmnPath = bpmnPath;
	}

	public String getRulesPath() {
		return rulesPath;
	}

	public void setRulesPath(String rulesPath) {
		this.rulesPath = rulesPath;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public List<Demographic> getDemographics() {
		return demographics;
	}

	public void setDemographics(List<Demographic> demographics) {
		this.demographics = demographics;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Execution...\n");
		sb.append("id: " + this.id + "\n");
		sb.append("user: " + user + "\n");
		sb.append("algoName: " + algorithmName + "\n");
		sb.append("algoVersion: " + algorithmVersion + "\n");
		sb.append("stateDate: " + startDate + "\n");
		sb.append("elapsedTime: " + elapsedTime + "\n");
		sb.append("dateRangeFrom: " + dateRangeFrom + "\n");
		sb.append("dateRangeTo: " + dateRangeTo + "\n");
		sb.append("xmlPath: " + xmlPath + "\n");
		sb.append("bpmnPath: " + bpmnPath + "\n");
		sb.append("imagePath: " + (image != null ? image.getImagePath() : null) + "\n");
		sb.append("rulesPath: " + rulesPath + "\n");
		return sb.toString();
	}
}
