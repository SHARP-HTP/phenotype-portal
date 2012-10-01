package edu.mayo.phenoportal.shared;

public class Drools {
    private String id;
	private String guvnorId;
	private String parentId;
    private String bpmnPath;
    private String imagePath;
    private String rulesPath;
    private String title;
    private String comment;
    private String username;
    private boolean editable;
	private String createdDate;
	private int categoryNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public String getGuvnorId() {
		return guvnorId;
	}

	public void setGuvnorId(String guvnorId) {
		this.guvnorId = guvnorId;
	}

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getBpmnPath() {
        return bpmnPath;
    }

    public void setBpmnPath(String bpmnPath) {
        this.bpmnPath = bpmnPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getRulesPath() {
        return rulesPath;
    }

    public void setRulesPath(String rulesPath) {
        this.rulesPath = rulesPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

	public int getCategoryNum() {
		return categoryNum;
	}

	public void setCategoryNum(int categoryNum) {
		this.categoryNum = categoryNum;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	/* Creates a clone of the current DroolsMetadata instance MINUS the id and createdDate. */
    @Override
    public Drools clone() {
        Drools clone = new Drools();
        clone.parentId = this.parentId;
        clone.bpmnPath = this.bpmnPath;
        clone.imagePath = this.imagePath;
        clone.rulesPath = this.rulesPath;
        clone.title = this.title;
        clone.comment = this.comment;
        clone.username = this.username;
        clone.editable = this.editable;
	    clone.categoryNum = this.categoryNum;
        return clone;
    }

}
