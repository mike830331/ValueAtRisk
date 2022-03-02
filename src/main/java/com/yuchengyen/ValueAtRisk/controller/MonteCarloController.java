package com.yuchengyen.ValueAtRisk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yuchengyen.ValueAtRisk.entity.RequestVar;
import com.yuchengyen.ValueAtRisk.service.MonteCarloService;

@RestController
@RequestMapping(value = "/MonteCarlo ", produces = MediaType.APPLICATION_JSON_VALUE)
public class MonteCarloController {

	@Autowired
	private MonteCarloService monteCarloService;

	@PostMapping
	public String getProducts(@RequestBody RequestVar request) {
		double a = monteCarloService.getVar();
		System.out.println("Var: " + a);
		return "AAAA";
	}
}
