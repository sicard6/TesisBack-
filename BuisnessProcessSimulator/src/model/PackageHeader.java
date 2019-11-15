package model;

import java.util.ArrayList;
import java.util.Date;

public class PackageHeader {
	
	private String XPDLVersion;
	private String vendor;
	private Date created;
	private Date modificationDate;
	private String description;
	private String documentation;
	private String creationVersion;
	private String version;
	private ArrayList<Modification> modifications;

	public PackageHeader(String xPDLVersion, String vendor, Date created, Date modificationDate, String description,
			String documentation, String creationVersion, String version, ArrayList<Modification> modifications) {
		super();
		XPDLVersion = xPDLVersion;
		this.vendor = vendor;
		this.created = created;
		this.modificationDate = modificationDate;
		this.description = description;
		this.documentation = documentation;
		this.creationVersion = creationVersion;
		this.version = version;
		this.modifications = modifications;
	}

	public String getXPDLVersion() {
		return XPDLVersion;
	}

	public void setXPDLVersion(String xPDLVersion) {
		XPDLVersion = xPDLVersion;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	public String getCreationVersion() {
		return creationVersion;
	}

	public void setCreationVersion(String creationVersion) {
		this.creationVersion = creationVersion;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public ArrayList<Modification> getModifications() {
		return modifications;
	}

	public void setModifications(ArrayList<Modification> modifications) {
		this.modifications = modifications;
	}
	
}
