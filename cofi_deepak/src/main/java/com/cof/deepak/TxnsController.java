package com.cof.deepak;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.cof.model.Txns;
import com.cof.model.Txn;

public class TxnsController {

	public static void main(String[] args) {
		try {
		TxnJSONParser tmp=new TxnJSONParser();
		FileInputStream fis= new FileInputStream(TxnJSONParser.FILE_NAME);
		Txns txns=tmp.parse(fis);
		Stream<Txn> dis=txns.getTxns().stream()
						.distinct();
		// forEach Txn t
		//  if not exists node(t.year, t.month)  nodeList.addNode (t.year, t.month) 
		// 	 if t.amount > 0 (credit) 
		//    node.income += t.amount 
		//   else
		//    node.spent -= t.amount
		//  
		// Render Nodelist as JSON markup
		txns.sort();
		double total=txns.getTxns().stream()
						  .collect(Collectors.summingDouble(Txn::getAmount));
		System.out.println(total);
		System.out.println(txns);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
