package com.phonecompany.application;

import com.phonecompany.billing.TelephoneBillCalculator;
import com.phonecompany.billing.impl.TelephoneBillCalculatorImpl;

public class Application {
	
	public static void main(String[] args) {
      TelephoneBillCalculator billCalculate = new TelephoneBillCalculatorImpl();
      
      String phoneLog = "420774577453,13-01-2020 18:10:15,13-01-2020 18:12:57\n"
      		+ "420776562353,18-01-2020 08:59:20,18-01-2020 09:10:00\n";
      System.out.println(billCalculate.calculate(phoneLog));
      
    }
}
