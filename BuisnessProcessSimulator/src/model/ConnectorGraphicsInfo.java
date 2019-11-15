package model;

import java.util.ArrayList;

public class ConnectorGraphicsInfo {

	private String toolId;
	private int borderColor;
	private String textX;
	private String textY;
	private int textWidth;
	private int textHeight;
	private Formatting formatting;
	private ArrayList<Coordinate> coordinates;

	public ConnectorGraphicsInfo(String toolId, int borderColor, String textX, String textY, int textWidth,
			int textHeight, Formatting formatting, ArrayList<Coordinate> coordinates) {
		super();
		this.toolId = toolId;
		this.borderColor = borderColor;
		this.textX = textX;
		this.textY = textY;
		this.textWidth = textWidth;
		this.textHeight = textHeight;
		this.formatting = formatting;
		this.coordinates = coordinates;
	}

	public String getToolId() {
		return toolId;
	}

	public void setToolId(String toolId) {
		this.toolId = toolId;
	}

	public int getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
	}

	public String getTextX() {
		return textX;
	}

	public void setTextX(String textX) {
		this.textX = textX;
	}

	public String getTextY() {
		return textY;
	}

	public void setTextY(String textY) {
		this.textY = textY;
	}

	public int getTextWidth() {
		return textWidth;
	}

	public void setTextWidth(int textWidth) {
		this.textWidth = textWidth;
	}

	public int getTextHeight() {
		return textHeight;
	}

	public void setTextHeight(int textHeight) {
		this.textHeight = textHeight;
	}

	public Formatting getFormatting() {
		return formatting;
	}

	public void setFormatting(Formatting formatting) {
		this.formatting = formatting;
	}

	public ArrayList<Coordinate> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(ArrayList<Coordinate> coordinates) {
		this.coordinates = coordinates;
	}

}
