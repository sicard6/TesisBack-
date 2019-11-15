package model;

public class TriggerResultMessage {

	private String catchThrow;
	private String id;
	
	public TriggerResultMessage(String catchThrow, String id) {
		super();
		this.catchThrow = catchThrow;
		this.id = id;
	}

	public String getCatchThrow() {
		return catchThrow;
	}

	public void setCatchThrow(String catchThrow) {
		this.catchThrow = catchThrow;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
