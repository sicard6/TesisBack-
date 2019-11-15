package model;

import java.util.ArrayList;

public class Gateway{

	private boolean markerVisible;
	private String gatewayDirection;
	private String gatewayType;
	private String exclusiveType;

	public Gateway(String gatewayDirection) {
		super();
		this.gatewayDirection = gatewayDirection;
	}

	public boolean isMarkerVisible() {
		return markerVisible;
	}

	public void setMarkerVisible(boolean markerVisible) {
		this.markerVisible = markerVisible;
	}

	public String getGatewayDirection() {
		return gatewayDirection;
	}

	public void setGatewayDirection(String gatewayDirection) {
		this.gatewayDirection = gatewayDirection;
	}

	public String getGatewayType() {
		return gatewayType;
	}

	public void setGatewayType(String gatewayType) {
		this.gatewayType = gatewayType;
	}

	public String getExclusiveType() {
		return exclusiveType;
	}

	public void setExclusiveType(String exclusiveType) {
		this.exclusiveType = exclusiveType;
	}
	
}
