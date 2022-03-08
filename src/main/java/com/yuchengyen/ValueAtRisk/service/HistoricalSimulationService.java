package com.yuchengyen.ValueAtRisk.service;

import yahoofinance.Stock;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.yuchengyen.ValueAtRisk.PercentageChange;
import com.yuchengyen.ValueAtRisk.entity.RequestHis;
import com.yuchengyen.ValueAtRisk.model.StockData;

@Service
public class HistoricalSimulationService {
    public double getVar(RequestHis request) {
		StockData data = new StockData();
		data.getData(Integer.parseInt(request.getHistoricalYears()),request.getSymbol());
        double Confidence = Double.parseDouble(request.getConfidenceValue());
        double TimeHorizon = Math.sqrt(Integer.parseInt(request.getTimeHorizonValue()));

        //ArrayList<Double> tomorrowPortfolio = new ArrayList<>(Collections.nCopies(size, 0.0));
		int countAsset = data.countAsset;
		int size = data.size;
        double[] tomorrowPortfolio = new double[size];

        /** Predict Tomorrow's Portfolio Prices **/
        try {
            for (String sym : data.strSymbols) {
                Stock stock = data.stockHashMap.get(sym);
                int stockDeltas = data.hashStockDeltas.get(sym);
                double currentPrice = stock.getQuote().getPreviousClose().doubleValue();
                double[] percentageChanges = PercentageChange.getArray(stock.getHistory());
                for(int i = 0; i < percentageChanges.length; i++)
                    tomorrowPortfolio[i] += (percentageChanges[i] + 1) * currentPrice * stockDeltas;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Compute VaR
        Arrays.sort(tomorrowPortfolio);
        double index = (1 - Confidence) * size;
        double VaR = (data.currentPortfolio - tomorrowPortfolio[(int) index]) * TimeHorizon;
        return VaR;
//    	return 1.11;
    }
}
