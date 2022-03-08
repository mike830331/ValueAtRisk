package com.yuchengyen.ValueAtRisk.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

import com.yuchengyen.ValueAtRisk.PercentageChange;
import com.yuchengyen.ValueAtRisk.VolatilityAbstract;
import com.yuchengyen.ValueAtRisk.VolatilityFactory;
import com.yuchengyen.ValueAtRisk.entity.RequestMon;
import com.yuchengyen.ValueAtRisk.model.StockData;

import yahoofinance.Stock;

@Service
public class MonteCarloService {

	private String volatilityMeasure;

	public void MonteCarlo(String volatilityMeasure) {
		this.volatilityMeasure = volatilityMeasure;
	}

	private double[] sampleCorrelatedVariables(double[][] choleskyDecomposition, int countAsset) {
		// Generate a vector of random variables, sampling from random Gaussian of mean
		// 0 and sd 1
		Random epsilon = new Random();
		double[] correlatedRandomVariables = new double[countAsset];
		for (int i = 0; i < countAsset; i++)
			for (int j = 0; j < countAsset; j++)
				// multiply the Cholesky Decomposition by a random variable sampled from the
				// standard gaussian
				correlatedRandomVariables[i] += choleskyDecomposition[i][j] * epsilon.nextGaussian();
		// our random variables are now correlated
		return correlatedRandomVariables;
	}

	private double[] randomWalk(double[] currentStockPrices, double[][] choleskyDecomposition, int countAsset,
			int steps) {
		double[] previousStockPrices = currentStockPrices;
		double[] predictedStockPrices = new double[countAsset];
		double dt = 1.0 / steps;
		for (int i = 0; i < steps; i++) {
			double[] correlatedRandomVariables = sampleCorrelatedVariables(choleskyDecomposition, countAsset);
			for (int j = 0; j < countAsset; j++)
				predictedStockPrices[j] = (correlatedRandomVariables[j] * previousStockPrices[j] * Math.sqrt(dt))
						+ previousStockPrices[j];
			previousStockPrices = predictedStockPrices;
		}
		return predictedStockPrices;
	}

	public double getVar(RequestMon request) {
		int steps = Integer.parseInt(request.getSteps());
		int paths = Integer.parseInt(request.getPaths());
		double dt = 1.0 / steps;
		StockData data = new StockData();
		data.getData(Integer.parseInt(request.getHistoricalYears()), request.getSymbol());
		double Confidence = Double.parseDouble(request.getConfidenceValue());
		double TimeHorizon = Math.sqrt(Double.parseDouble(request.getTimeHorizonValue()));
		int countAsset = data.countAsset;
		int size = data.size;
		ArrayList<Future<double[]>> list = new ArrayList<>();
		ExecutorService executor = Executors.newCachedThreadPool();

		double[] currentPrices = new double[countAsset];
		double[] stockDelta = new double[countAsset];

		double[][] matrixPcntChanges = new double[countAsset][size];
		try {
			for (int i = 0; i < countAsset; i++) {
				String sym = data.strSymbols[i];
				Stock stock = data.stockHashMap.get(sym);
				currentPrices[i] = stock.getQuote().getPreviousClose().doubleValue();
				stockDelta[i] = data.hashStockDeltas.get(sym);
				// get percentage changes of stock
				double[] percentageChanges = PercentageChange.getArray(stock.getHistory());
				matrixPcntChanges[i] = percentageChanges;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (request.getRiskMeasureValue().equals("MonteCarlo EW")) {
			volatilityMeasure = "EW";
			System.out.println("MonteCarlo EW");
		} else if (request.getRiskMeasureValue().equals("MonteCarlo EWMA")) {
			volatilityMeasure = "EWMA";
			System.out.println("MonteCarlo EWMA");
		} else {
			volatilityMeasure = "";
		}
		// get cholesky decomposition (square root of covariance matrix)
		VolatilityFactory volatilityFactory = new VolatilityFactory();
		VolatilityAbstract volatilty = volatilityFactory.getType(volatilityMeasure);
		double[][] choleskyMatrix = volatilty.getCholeskyDecomposition(matrixPcntChanges);

		// Callable Future - run random walks in parallel
		ArrayList<double[]> simulatedPrices = new ArrayList<>();
		for (int j = 0; j < paths; j++) {
			// System.out.println(j);
			Callable<double[]> callable = () -> {
				double[] rw = randomWalk(currentPrices, choleskyMatrix, data.countAsset,
						Integer.parseInt(request.getSteps()));
				return rw;
			};
			Future<double[]> future = executor.submit(callable);
			list.add(future);
		}
		// Get Futures when ready
		try {
			for (Future<double[]> fut : list) {
				// because Future.get() waits for task to get completed
				double[] randomWalk = fut.get();
				simulatedPrices.add(randomWalk);
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} finally {
			// shut down the executor service
			executor.shutdown();
		}

		ArrayList<Double> tomorrowPi = new ArrayList<>();
		for (double[] bd : simulatedPrices) {
			double sum = 0.0;
			for (int k = 0; k < countAsset; k++)
				sum += bd[k] * (stockDelta[k]);
			tomorrowPi.add(sum);
		}

		// Compute VaR
		Collections.sort(tomorrowPi);
		double index = (1 - Confidence) * paths;
		double VaR = data.currentPortfolio - tomorrowPi.get((int) index) * TimeHorizon;
		return VaR;
	}
}
