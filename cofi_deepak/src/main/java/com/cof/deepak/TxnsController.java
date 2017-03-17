package com.cof.deepak;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.Collections.*;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Map;
import java.util.function.*;

import com.cof.model.Txns;

import com.cof.model.Txn;
import com.cof.model.Expense;

public class TxnsController {

	Function<Txn, Expense> getExpense = new Function<Txn, Expense>() {
	    public Expense apply(Txn t) {
	    	return new Expense(t.getIncome(),t.getSpent());}
	    };
 
	public void test() { 
		List <Txn> txns = getTxns();
		Map<String, List<Expense>> a =
			    txns
			        .stream()
			        .collect(
			            Collectors.groupingBy(
			                Txn::getYearMonth, 
			                Collectors.mapping(
			                    getExpense,
			                    Collectors.toList())));	
		System.out.println(a);
	}
	
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
	

	public void test2() { 
		List <Txn> txns = getTxns();
		Map<String, Expense > a =
			    txns
			        .stream()
			        .collect(
			            Collectors.groupingBy(
			                Txn::getYearMonth, 
			                mapping(getExpense,
			                		Collector.of(
			                				  Expense::new,
			                				  (Expense e, Txn t) -> e.add(t),
			                				  (e1, e2) -> new Expense(e1, e2)
			                				   )))
			            	);
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
	
	public void generate() {
			List <Txn> txns = getTxns();
			List<Txn> income=txns.stream()
							.filter(t->t.getAmount()>=0)
							.collect(Collectors.toList());
			List<Txn> spent=txns.stream()
					.filter(t->t.getAmount()<0)
					.collect(Collectors.toList());
					  
			// reduce stream to list of objects, for yearMonth with sum amounts for spend and income
			// reduce stream to one, for average of all 
			Map<String, Double> spentMap=
					txns.stream().collect(
							Collectors.groupingBy(Txn::getYearMonth, 
							Collectors.reducing(0.0,Txn::getAmount,Double::sum))); 

			
/*			Map<String, Expense> expenseMap=
					txns.stream().collect(
							Collectors.groupingBy(Txn::getYearMonth, getExpense,
							Collectors.reducing(0.0,Expense::getIncome,Double::sum))); */
			
			System.out.println(spentMap);
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
			
	}
	
	public static void main(String[] args) {
		TxnsController tc=new TxnsController();
		tc.test();
	}
	
}
