package com.cof.deepak;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Map;

import com.cof.model.Txns;

import com.cof.model.Txn;
import com.cof.model.Activity;

public class TxnsController {
	
	public void test1() { 
		List <Txn> txns = getTxns();
		Map<String, Map<Boolean, List<Txn>> > a =
			    txns
			        .stream()
			        .collect(
			            Collectors.groupingBy(
			                Txn::getYearMonth, 
			                Collectors.partitioningBy(t-> t.getAmount()>=0)));	
		collectingAndThen(summarizingDouble(Person::getAge), 
                dss -> new Data((long)dss.getAverage(), (long)dss.getSum()))));
		System.out.println(a);
	}
	
	public List<Txn> getTxns() { 
		try{
		TxnJSONParser tmp=new TxnJSONParser();
		FileInputStream fis= new FileInputStream(TxnJSONParser.FILE_NAME);
		Txns txns=tmp.parse(fis);
		return txns.getTxns();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void process(){
		 TxnsController tc=new TxnsController();
		 List <Txn> txns = tc.getTxns();

		 Map<String,Activity> myMap =   txns.stream().collect(
				 Collectors.groupingBy(Txn::getYearMonth, Collector.of(Activity::new, Activity::accept, Activity::combine))
						 	);
		// calculate the average node and add to the map 
		 Activity avg = new Activity(myMap.values().stream().collect(Collectors.averagingDouble(Activity::getSpent)), myMap.values().stream().collect(Collectors.averagingDouble(Activity::getIncome)));
		 myMap.put("Average", avg);

	    try {
			OutputStream fos = new FileOutputStream("activity_out_stream.txt");
			OutputGenerator og=new OutputGenerator(fos);

			og.start();
			 myMap.entrySet().stream().sorted(Map.Entry.<String, Activity>comparingByKey()).forEach(entry -> {
				 //System.out.println(entry.getKey()+": { spent: "+currencyFormatter.format(entry.getValue().getSpent())+", income: " + currencyFormatter.format(entry.getValue().getIncome())+"},");
				 try {
					og.write(entry);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 });
			 og.end();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	
	public static void main(String[] args) {
		TxnsController tc=new TxnsController();
		tc.process();
	}
	
}
