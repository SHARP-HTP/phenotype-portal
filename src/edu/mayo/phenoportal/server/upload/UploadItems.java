package edu.mayo.phenoportal.server.upload;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import edu.mayo.phenoportal.shared.AlgorithmType;
import org.apache.commons.fileupload.FileItem;

// import org.apache.log4j.Logger;

public class UploadItems implements Serializable {
    // static final Logger logger =
    // Logger.getLogger(UploadItems.class.getName());

    private static final long serialVersionUID = -8987031921542979987L;
    private String id;
    private String name;
    private String user;
    private String version;
    private String status;
    private String institution;
    private Date createDate;
    private String description;
    private String comment;
    private File htmlFile;
    private File docFile;
    private File xlsFile;
    private File xmlFile;
    private File zipFile;
    private String assocLink;
    private String assocName;
    private Date uploadDate;
    private String dir = UUID.randomUUID().toString();
	private AlgorithmType type;
	private byte[] bytes;
    private final List<FileItem> inputFiles = new ArrayList<FileItem>(2);
    private final StringBuilder messages = new StringBuilder();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
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

    public File getHtmlFile() {
        return htmlFile;
    }

    public void setHtmlFile(File htmlFile) {
        this.htmlFile = htmlFile;
    }

    public File getDocFile() {
        return docFile;
    }

    public void setDocFile(File docFile) {
        this.docFile = docFile;
    }

    public File getXlsFile() {
        return xlsFile;
    }

    public void setXlsFile(File xlsFile) {
        this.xlsFile = xlsFile;
    }

    public File getXmlFile() {
        return xmlFile;
    }

    public void setXmlFile(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    public File getZipFile() {
        return zipFile;
    }

    public void setZipFile(File zipFile) {
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

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public String getMessages() {
        return messages.toString();
    }

    public void addMessage(String message) {
        messages.append(message + "<br/>");
    }

    public void addInputFileItem(FileItem fileItem) {
        if (!inputFiles.contains(fileItem)) {
            this.inputFiles.add(fileItem);
        }
    }

    public List<FileItem> getInputFileItems() {
        return this.inputFiles;
    }

	public AlgorithmType getType() {
		return type;
	}

	public void setType(AlgorithmType type) {
		this.type = type;
	}
}
