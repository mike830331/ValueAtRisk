package com.yuchengyen.ValueAtRisk.service;

import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.springframework.stereotype.Service;

import com.yuchengyen.ValueAtRisk.PercentageChange;
import com.yuchengyen.ValueAtRisk.VolatilityFactory;
import com.yuchengyen.ValueAtRisk.entity.RequestVar;
import com.yuchengyen.ValueAtRisk.model.StockData;

import yahoofinance.Stock;

@Service
public class VarianceCovarianceService {

	public String volatilityMeasure;

//	public Analytical(String volatilityMeasure) {
//		this.volatilityMeasure = volatilityMeasure;
//	}

	public double getVar(RequestVar request) throws IOException {
//	public double getVar() throws IOException {

		StockData data = new StockData();
		data.getData(Integer.parseInt(request.getHistoricalYears()));
		String[] strSymbols = data.strSymbols;
//		HashMap<String, String> hashParam = data.hashParam;
//		String[] riskMeasures = data.riskMeasures;
		String riskMeasureValue = request.getRiskMeasureValue();
		HashMap<String, Stock> stockHashMap = data.stockHashMap;
		HashMap<String, Integer> hashStockDeltas = data.hashStockDeltas;
		HashMap<String, Integer> hashOptionDeltas = data.hashOptionDeltas;
		double currentPortfolio = data.currentPortfolio;
		int countAsset = data.countAsset;
		int size = data.size;

		NormalDistribution distribution = new NormalDistribution(0, 1);
		System.out.println("ConfidenceValue: " + request.getConfidenceValue());
		System.out.println("TimeHorizonValue: " + request.getTimeHorizonValue());
		double Confidence = Double.parseDouble(request.getConfidenceValue());
		double TimeHorizon = Math.sqrt(Double.parseDouble(request.getTimeHorizonValue()));
		double riskPercentile = -distribution.inverseCumulativeProbability(1 - Confidence);

		double[] currentPrices = new double[countAsset];
		double[] stockDelta = new double[countAsset];
		double[][] matrixPcntChanges = new double[countAsset][size];

		// test
//		if (riskMeasureValue.equals("Historical Simulation"))
//			volatilityMeasure = "Historical Simulation";
//		System.out.println("Historical Simulation");

		if (riskMeasureValue.equals("Analytical EW")) {
			volatilityMeasure = "EW";
			System.out.println("HAnalytical EW");
		} else if (riskMeasureValue.equals("Analytical EWMA")) {
			volatilityMeasure = "EWMA";
			System.out.println("Analytical EWMA");
		} else if (riskMeasureValue.equals("MonteCarlo EW")) {
			volatilityMeasure = "EW";
			System.out.println("MonteCarlo EW");
		} else if (riskMeasureValue.equals("MonteCarlo EWMA")) {
			volatilityMeasure = "EWMA";
			System.out.println("MonteCarlo EWMA");
		} else {
			volatilityMeasure = "";
		}
		// test
		try {
			for (int i = 0; i < countAsset; i++) {
				String sym = strSymbols[i];
				Stock stock = stockHashMap.get(sym);
				currentPrices[i] = stock.getQuote().getPreviousClose().doubleValue();
				stockDelta[i] = new Double(hashStockDeltas.get(sym));
				double[] percentageChanges = PercentageChange.getArray(stock.getHistory());
				matrixPcntChanges[i] = percentageChanges;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		double[][] covarianceMatrix = new VolatilityFactory().getType(volatilityMeasure)
				.getCovarianceMatrix(matrixPcntChanges);

		// Compute linear combination
		double sum = 0.0;
		for (int i = 0; i < countAsset; i++)
			for (int j = 0; j < countAsset; j++)
				sum += stockDelta[i] * stockDelta[j] * currentPrices[i] * currentPrices[j] * covarianceMatrix[i][j];
		// Computer VaR
		double VaR = Math.sqrt(TimeHorizon) * riskPercentile * Math.sqrt(sum);
		return VaR;
	}

}
