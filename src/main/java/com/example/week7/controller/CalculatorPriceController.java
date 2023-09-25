package com.example.week7.controller;


import com.example.week7.service.CalculatorPriceService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorPriceController {
    protected CalculatorPriceService calculatorPriceService;

    public CalculatorPriceController(){
        this.calculatorPriceService = new CalculatorPriceService();
    }

    @RequestMapping(value = "/getPrice/{cost}/{profit}", method = RequestMethod.GET)
    public double serviceGetProducts(@PathVariable("cost") double cost, @PathVariable("profit") double profit){
        return this.calculatorPriceService.getPrice(cost, profit);
    }
}
