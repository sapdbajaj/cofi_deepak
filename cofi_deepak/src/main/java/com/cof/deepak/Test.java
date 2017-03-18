package com.cof.deepak;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.cof.model.Activity;
import com.cof.model.Expense;
import com.cof.model.Txn;

public class Test {
	
	
	 static public void testTxn(){
		 TxnsController tc=new TxnsController();
		 List <Txn> txns = tc.getTxns();

		 Map<String,Activity> myMap = txns.stream().collect(
					Collectors.groupingBy(Txn::getYearMonth, Collector.of(Activity::new, Activity::accept, Activity::combine)));
			myMap.entrySet().forEach(entry -> {System.out.println("Key : " + entry.getKey() + 
					" I : " + entry.getValue().getSpent() + ", J : " + entry.getValue().getYear_month() + ", count :" + entry.getValue().getCount());
			});
			
			myMap.entrySet().forEach(entry -> {
			    System.out.println("Key : " + entry.getKey() + " Income : " + entry.getValue().getIncome()+ " Spent : " + entry.getValue().getSpent());
			}); 
	 }
	public static void main(String[] args) {
		testTxn();
	}
}
