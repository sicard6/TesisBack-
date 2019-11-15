package model;

public class NodeGraphicsInfo {

	private String toolId;
	private int height;
	private int width;
	private int borderColor;
	private int fillColor;
	private boolean borderVisible;
	private Formatting formatting;
	private Coordinate coordinate;

	public NodeGraphicsInfo(String toolId, int height, int width, int borderColor, int fillColor, boolean borderVisible,
			Formatting formatting, Coordinate coordinate) {
		super();
		this.toolId = toolId;
		this.height = height;
		this.width = width;
		this.borderColor = borderColor;
		this.fillColor = fillColor;
		this.borderVisible = borderVisible;
		this.formatting = formatting;
		this.coordinate = coordinate;
	}

	/**
	 * Consturctor for NodeGraphicsInfo objects that don't rely on coordinates
	 **/
	
	public NodeGraphicsInfo(String toolId, int height, int width, int borderColor, int fillColor, boolean borderVisible,
			Formatting formatting) {
		super();
		this.toolId = toolId;
		this.height = height;
		this.width = width;
		this.borderColor = borderColor;
		this.fillColor = fillColor;
		this.borderVisible = borderVisible;
		this.formatting = formatting;
	}

	public String getToolId() {
		return toolId;
	}

	public void setToolId(String toolId) {
		this.toolId = toolId;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
	}

	public int getFillColor() {
		return fillColor;
	}

	public void setFillColor(int fillColor) {
		this.fillColor = fillColor;
	}

	public boolean isBorderVisible() {
		return borderVisible;
	}

	public void setBorderVisible(boolean borderVisible) {
		this.borderVisible = borderVisible;
	}

	public Formatting getFormatting() {
		return formatting;
	}

	public void setFormatting(Formatting formatting) {
		this.formatting = formatting;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}
	
}
