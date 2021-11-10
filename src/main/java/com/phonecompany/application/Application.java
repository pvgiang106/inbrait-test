package com.phonecompany.application;

import com.phonecompany.billing.TelephoneBillCalculator;
import com.phonecompany.billing.impl.TelephoneBillCalculatorImpl;

public class Application {
	
	public static void main(String[] args) {
      TelephoneBillCalculator billCalculate = new TelephoneBillCalculatorImpl();
      
      String phoneLog = "";
      System.out.println(billCalculate.calculate(phoneLog));
      
    }
}
