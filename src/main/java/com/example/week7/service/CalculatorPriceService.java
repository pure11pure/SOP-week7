package com.example.week7.service;


import org.springframework.stereotype.Service;

@Service
public class CalculatorPriceService {
    public double getPrice(Double cost, Double profit)
    {
        return cost + profit;
    }
}
