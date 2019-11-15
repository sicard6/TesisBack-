package model;

public class RedefinableHeader {

	private String author;
	private String version;
	private String countryKey;
	
	public RedefinableHeader(String author, String version, String countryKey) {
		super();
		this.author = author;
		this.version = version;
		this.countryKey = countryKey;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCountryKey() {
		return countryKey;
	}

	public void setCountryKey(String countryKey) {
		this.countryKey = countryKey;
	}
	
	

}
