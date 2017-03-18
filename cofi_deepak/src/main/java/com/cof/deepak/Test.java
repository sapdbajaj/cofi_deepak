package com.cof.deepak;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.cof.model.Activity;
import com.cof.model.Txn;

public class Test {
	
	
	 static public void testTxn(){
		 TxnsController tc=new TxnsController();
		 List <Txn> txns = tc.getTxns();

		 Map<String,Activity> myMap =   txns.stream().collect(
				 Collectors.groupingBy(Txn::getYearMonth, Collector.of(Activity::new, Activity::accept, Activity::combine))
						 	);
		// calculate the average node and add to the map 
		 Activity avg = new Activity(myMap.values().stream().collect(Collectors.averagingDouble(Activity::getSpent)), myMap.values().stream().collect(Collectors.averagingDouble(Activity::getIncome)));
		 myMap.put("Average", avg);

		    NumberFormat currencyFormatter = 
		        NumberFormat.getCurrencyInstance(Locale.getDefault());

		 myMap.entrySet().stream().sorted(Map.Entry.<String, Activity>comparingByKey()).forEach(entry -> {
			 System.out.println(entry.getKey()+": { spent: "+currencyFormatter.format(entry.getValue().getSpent())+", income: " + currencyFormatter.format(entry.getValue().getIncome())+"},");
		 }); 
	 }
	public static void main(String[] args) {
		testTxn();
	}
}
