package com.yuchengyen.ValueAtRisk.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yuchengyen.ValueAtRisk.entity.RequestVar;
import com.yuchengyen.ValueAtRisk.service.VarianceCovarianceService;

@RestController
@RequestMapping(value = "/VarianceCovariance", produces = MediaType.APPLICATION_JSON_VALUE)
public class VarianceCovarianceController {
	@Autowired
	private VarianceCovarianceService varianceCovarianceService;

	@PostMapping
	public double getProducts(@RequestBody RequestVar request) throws IOException {
		double var = varianceCovarianceService.getVar(request);
		System.out.println("Var: " + var);
		return var;
	}
}
