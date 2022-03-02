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

	public void getData(int historicalYears) {
		strSymbols = readTxt("symbol.txt");
		countAsset = strSymbols.length;
		stockHashMap = getStock(historicalYears);
		readDeltas();
		currentPortfolio = valuePortfolio();
		size = getSize();
	}

	private static void readDeltas() {
		for (String sym : strSymbols) {
			StringBuilder stringBuilder = new StringBuilder();
			String filename = stringBuilder.append("Deltas").append(File.separator).append(sym).append(".txt")
					.toString();
			String[] strDeltas = readTxt(filename);
			try {
				hashStockDeltas.put(sym, Integer.parseInt(strDeltas[0]));
				hashOptionDeltas.put(sym, Integer.parseInt(strDeltas[1]));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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

	public static String[] readTxt(String filename) {
		try {
			Scanner inFile = new Scanner(new FileReader(filename));
			ArrayList<String> stringArrayList = new ArrayList<>();
			while (inFile.hasNextLine()) {
				String strLine = inFile.nextLine();
				stringArrayList.add(strLine);
			}
			String[] stringArray = stringArrayList.toArray(new String[stringArrayList.size()]);
			return stringArray;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
