package model;

public class ScenarioParameter {

	private String baseTimeUnit;
	private String baseCurrencyUnit;
	private String durationParameterValue;
	
	public ScenarioParameter(String baseTimeUnit, String baseCurrencyUnit, String durationParameterValue) {
		super();
		this.baseTimeUnit = baseTimeUnit;
		this.baseCurrencyUnit = baseCurrencyUnit;
		this.durationParameterValue = durationParameterValue;
	}

	public String getBaseTimeUnit() {
		return baseTimeUnit;
	}

	public void setBaseTimeUnit(String baseTimeUnit) {
		this.baseTimeUnit = baseTimeUnit;
	}

	public String getBaseCurrencyUnit() {
		return baseCurrencyUnit;
	}

	public void setBaseCurrencyUnit(String baseCurrencyUnit) {
		this.baseCurrencyUnit = baseCurrencyUnit;
	}

	public String getDurationParameterValue() {
		return durationParameterValue;
	}

	public void setDurationParameterValue(String durationParameterValue) {
		this.durationParameterValue = durationParameterValue;
	}
	
}
