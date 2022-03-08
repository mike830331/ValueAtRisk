package com.yuchengyen.ValueAtRisk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yuchengyen.ValueAtRisk.entity.RequestMon;
import com.yuchengyen.ValueAtRisk.service.MonteCarloService;

@RestController
@RequestMapping(value = "/MonteCarlo", produces = MediaType.APPLICATION_JSON_VALUE)
public class MonteCarloController {

	@Autowired
	private MonteCarloService monteCarloService;

	@PostMapping
	public double getProducts(@RequestBody RequestMon request) {
		double var = monteCarloService.getVar(request);
		System.out.println("Var: " + var);
		return var;
	}
}
