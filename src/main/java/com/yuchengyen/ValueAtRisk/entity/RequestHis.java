package com.yuchengyen.ValueAtRisk.entity;

public class RequestHis {

	private String confidenceValue;
	private String timeHorizonValue;
	private String riskMeasureValue;
	private String historicalYears;
	private String symbol;

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

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

}
