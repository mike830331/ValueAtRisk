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
    public String getProducts(@RequestBody RequestVar request) {
    	try {
//			double a =varianceCovarianceService.getVar(request.getConfidenceValue(), request.getTimeHorizonValue(),request.getRiskMeasureValue());
			double a =varianceCovarianceService.getVar(request);
			System.out.println("Var: "+ a);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "AAAA";
    }
}
