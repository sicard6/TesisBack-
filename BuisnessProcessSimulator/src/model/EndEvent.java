package model;

import java.util.ArrayList;

public class EndEvent extends Event {

	private String result;

	public EndEvent(String result) {
		super();
		this.result = result;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
