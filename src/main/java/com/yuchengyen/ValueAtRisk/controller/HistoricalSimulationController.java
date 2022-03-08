package com.yuchengyen.ValueAtRisk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yuchengyen.ValueAtRisk.entity.RequestHis;
import com.yuchengyen.ValueAtRisk.service.HistoricalSimulationService;

@RestController
@RequestMapping(value = "/HistoricalSimulation", produces = MediaType.APPLICATION_JSON_VALUE)
public class HistoricalSimulationController {

	@Autowired
	private HistoricalSimulationService historicalSimulationService;

	@PostMapping
	public double getProducts(@RequestBody RequestHis request) {
		double var = historicalSimulationService.getVar(request);
		System.out.println("Var: " + var);
		return var;
	}
}
