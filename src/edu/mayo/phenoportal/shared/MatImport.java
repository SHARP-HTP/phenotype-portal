package edu.mayo.phenoportal.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MatImport implements IsSerializable {

	public String id;
	public String user;
	public String password;
	public String title;
	public String date;
	public String version;
	public String description;
	public String institution;
	public String nqfName;
	public String nqfLink;
	public byte[] filearr;
	public String filePath;

	public MatImport(String id, String user, String password, String title, String date, String version,
	                 String description, String institution, String nqfName, String nqfLink, byte[] filearr,
	                 String filePath) {
		this.id = id;
		this.user = user;
		this.password = password;
		this.title = title;
		this.date = date;
		this.version = version;
		this.description = description;
		this.institution = institution;
		this.nqfName = nqfName;
		this.nqfLink = nqfLink;
		this.filearr = filearr;
		this.filePath = filePath;
	}

	public MatImport() {
	}
}
