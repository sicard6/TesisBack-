package model;

import java.util.Date;

public class Modification {

	private Date date;
	private String userName;
	
	public Modification(Date date, String userName) {
		super();
		this.date = date;
		this.userName = userName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
	
}
