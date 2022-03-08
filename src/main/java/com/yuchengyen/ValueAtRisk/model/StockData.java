package com.yuchengyen.ValueAtRisk.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;

public class StockData {
	public static String[] strSymbols;
	public static HashMap<String, Stock> stockHashMap;
	public static HashMap<String, Integer> hashStockDeltas = new HashMap<>();
	public static HashMap<String, Integer> hashOptionDeltas = new HashMap<>();
	public static double currentPortfolio;
	public static int countAsset;
	public static int size;

	public void getData(int historicalYears, String symbols) {
		String[] strSymbol = symbols.split(",");
		strSymbols = strSymbol;
		countAsset = strSymbols.length;
		stockHashMap = getStock(historicalYears);
		for (String sym : strSymbols) {

			hashStockDeltas.put(sym, 100);
			hashOptionDeltas.put(sym, 0);
		}

		currentPortfolio = valuePortfolio();
		size = getSize();
	}

	private static HashMap<String, Stock> getStock(int historicalYears) {
		int years = historicalYears;

		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.add(Calendar.YEAR, -years); // from 5 years ago
		HashMap<String, Stock> stockArrayList = new HashMap<>();

		try {
			for (String sym : strSymbols) {
				Stock stock = YahooFinance.get(sym, from, to, Interval.DAILY);
				stockArrayList.put(sym, stock);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stockArrayList;
	}

	private static double valuePortfolio() {
		double currentPortfolio = 0.0;
		try {
			for (String sym : strSymbols) {
				Stock stock = stockHashMap.get(sym);
				double currentPrice = stock.getQuote().getPreviousClose().doubleValue();
				// add to current portfolio
				currentPortfolio += currentPrice * hashStockDeltas.get(sym);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return currentPortfolio;
	}

	public static int getSize() {
		Stock stck = stockHashMap.get(strSymbols[0]);
		int size = 0;
		try {
			size = stck.getHistory().size() - 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return size;
	}

}
