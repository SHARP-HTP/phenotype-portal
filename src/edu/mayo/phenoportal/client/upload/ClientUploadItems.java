package edu.mayo.phenoportal.client.upload;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.mayo.phenoportal.shared.AlgorithmType;

public class ClientUploadItems implements IsSerializable {

	private int id;
    private String parentId;
    private String name;
    private String user;
    private String version;
    private String status;
    private String institution;
    private String createDate;
    private String description;
    private String comment;
    private String htmlFile;
    private String docFile;
    private String xlsFile;
    private String xmlFile;
    private String zipFile;
    private String assocLink;
    private String assocName;
    private String uploadDate;
    private String prefix;
	private AlgorithmType type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHtmlFile() {
        return htmlFile;
    }

    public void setHtmlFile(String htmlFile) {
        this.htmlFile = htmlFile;
    }

    public String getDocFile() {
        return docFile;
    }

    public void setDocFile(String docFile) {
        this.docFile = docFile;
    }

    public String getXlsFile() {
        return xlsFile;
    }

    public void setXlsFile(String xlsFile) {
        this.xlsFile = xlsFile;
    }

    public String getXmlFile() {
        return xmlFile;
    }

    public void setXmlFile(String xmlFile) {
        this.xmlFile = xmlFile;
    }

    public String getZipFile() {
        return zipFile;
    }

    public void setZipFile(String zipFile) {
        this.zipFile = zipFile;
    }

    public String getAssocLink() {
        return assocLink;
    }

    public void setAssocLink(String assocLink) {
        this.assocLink = assocLink;
    }

    public String getAssocName() {
        return assocName;
    }

    public void setAssocName(String assocName) {
        this.assocName = assocName;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getPrefix() {
        return name + "_" + parentId + "_" + version;
    }

	public AlgorithmType getType() {
		return type;
	}

	public void setType(AlgorithmType type) {
		this.type = type;
	}
}
