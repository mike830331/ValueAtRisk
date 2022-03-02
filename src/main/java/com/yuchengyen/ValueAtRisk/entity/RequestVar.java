package com.yuchengyen.ValueAtRisk.entity;

public class RequestVar {

	private String confidenceValue;
	private String timeHorizonValue;
	private String riskMeasureValue;
	private String historicalYears;
	
	public String getConfidenceValue() {
		return confidenceValue;
	}

	public void setConfidenceValue(String confidenceValue) {
		this.confidenceValue = confidenceValue;
	}

	public String getTimeHorizonValue() {
		return timeHorizonValue;
	}

	public void setTimeHorizonValue(String timeHorizonValue) {
		this.timeHorizonValue = timeHorizonValue;
	}

	public String getRiskMeasureValue() {
		return riskMeasureValue;
	}

	public void setRiskMeasureValue(String riskMeasureValue) {
		this.riskMeasureValue = riskMeasureValue;
	}

	public String getHistoricalYears() {
		return historicalYears;
	}

	public void setHistoricalYears(String historicalYears) {
		this.historicalYears = historicalYears;
	}

}
