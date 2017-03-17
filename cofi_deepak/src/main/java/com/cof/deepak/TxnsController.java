package com.cof.deepak;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Map;
import java.util.function.Function;

import com.cof.model.Txns;

import com.cof.model.Txn;
import com.cof.model.Expense;

public class TxnsController {

	Function<Txn, Expense> getExpense = new Function<Txn, Expense>() {
	    public Expense apply(Txn t) {
	    	return new Expense(t.getIncome(),t.getSpent(), t.getTransaction_time());}
	    };
 
	public void generate() {
		try {
			TxnJSONParser tmp=new TxnJSONParser();
			FileInputStream fis= new FileInputStream(TxnJSONParser.FILE_NAME);
			Txns txns=tmp.parse(fis);
			List<Txn> income=txns.getTxns().stream()
							.filter(t->t.getAmount()>=0)
							.collect(Collectors.toList());
			List<Txn> spent=txns.getTxns().stream()
					.filter(t->t.getAmount()<0)
					.collect(Collectors.toList());
					  
			// reduce stream to list of objects, for yearMonth with sum amounts for spend and income
			// reduce stream to one, for average of all 
			Map<String, Double> spentMap=
					spent.stream().collect(
							Collectors.groupingBy(Txn::getYearMonth, 
							Collectors.reducing(0.0,Txn::getAmount,Double::sum))); 
			
			Map<String, Double> incomeMap=
					income.stream().map(getExpense)
							.collect(
							Collectors.groupingBy(Expense::getYearMonth, 
							Collectors.reducing(0.0,Expense::getIncome,Double::sum))); 
			
			System.out.println(spentMap);
			System.out.println(incomeMap);
		    /*List<Expense> myLocations = txns.getTxns().stream()
		            .map(getExpense)
		            .collect(Collectors.<Expense> toList());
		    
			Map<String, Double> groupByYearMonthAvg=
					txns.getTxns().stream().collect(Collectors.groupingBy(
							Txn::getYearMonth, 
							Collectors.averagingDouble(Txn::getAmount))); 

			*/
	/*		Double avg = txns.getTxns().stream()
			//.map(t-> t.getTransaction_time().format(fmtYearMonth))
			
			.forEach(System.out::println);*/
			// from input stream
			// given Txn,
			// get stream of positive and negative values 
			// aggregate positive and negative amounts on year&month into income and spend
			
			// reduce to generate the average ?
			// classic impl
			// forEach Txn t
			//  if not exists node(t.year, t.month)  nodeList.addNode (t.year, t.month) 
			// 	 if t.amount > 0 (credit) 
			//    node.income += t.amount 
			//   else
			//    node.spent -= t.amount
			//  
			// Render Nodelist as JSON markup
	/*		txns.sort();
			double total=txns.getTxns().stream()
							  .collect(Collectors.summingDouble(Txn::getAmount));
			System.out.println(total);
			System.out.println(txns);*/
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static void main(String[] args) {
		TxnsController tc=new TxnsController();
		tc.generate();
	}
	
}
