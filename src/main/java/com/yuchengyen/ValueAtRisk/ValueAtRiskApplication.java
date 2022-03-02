package com.yuchengyen.ValueAtRisk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ValueAtRiskApplication {

	//VaR takes two parameters, Confidence and Time Horizon. Suppose we take the confidence level 
	//This means we are 99% sure that we won’t lose more than 
	//our estimate of Value at Risk, within our Time Horizon, which is usually one day
	//There are three methods that are often used: Variance-Covariance Method, Historical Simulation Method, Monte Carlo Simulation Method
	public static void main(String[] args) {
		SpringApplication.run(ValueAtRiskApplication.class, args);
		System.out.println("API Start");
		
	}

}
