package com.cof.model;

import java.util.function.*;


public class Expense  {
	private double spent=0.0;
	private double income=0.0;
	private long count=0;
	
	public Expense(){
		new Expense(0.0,0.0);
	}
	
	public void accept(Txn src)
	{
	  if (src.getAmount() > 0 ) {
		  income += src.getAmount();
	  }else {
		  spent += src.getAmount();
	  }
	  ++count;
	}

	public Expense combine(Expense tgt)
	{
	  spent += tgt.getSpent();
	  income += tgt.getIncome();
	  return this;
	}
	
	public Expense(Expense e1, Expense e2) {
	    this.income = e1.income + e2.income;
	    this.spent = e1.spent + e2.spent;
	    count = e1.count+e2.count ; 
	  }

	  public void add(Txn t) {
		  if (t.getAmount() > 0 ) {
			  income += t.getAmount();
		  }else {
			  spent += t.getAmount();
		  }
		  ++count;
	  }
	  
	public Expense(double thisIncome, double thisSpent){
		income=thisIncome; 
		spent=thisSpent;
		count=0;
	}
	
	public double averageSpent() {
        return count > 0 ? ((double) spent)/count : 0.0;
    }
	
	public double averageIncome() {
        return count > 0 ? ((double) income)/count : 0.0;
    }
	
	public double getSpent() {
		return spent;
	}

	public void setSpent(double spent) {
		this.spent = spent;
	}

	public double getIncome() {
		return income;
	}

	public void setIncome(double income) {
		this.income = income;
	}

		
}
