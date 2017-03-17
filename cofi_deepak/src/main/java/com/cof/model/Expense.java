package com.cof.model;

import java.time.LocalDate;
import java.util.function.*;


public class Expense  {
	private double spent=0.0;
	private double income=0.0;
	private long count=0;
	
	public Expense(){
		new Expense(0.0,0.0);
	}
	
	public Expense(Expense e1, Expense e2) {
	    this.income = e1.income + e2.income;
	    this.spent = e1.spent + e2.spent;
	  }

	  public void add(Txn t) {
	    this.income = t.getAmount() > 0 ? t.getAmount() : 0;
	    this.spent = t.getAmount() < 0 ? t.getAmount() : 0;
	  }
	  
	public Expense(double thisIncome, double thisSpent){
		income=thisIncome; 
		spent=thisSpent;
		count=0;
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
	
	
	final public static Supplier<Expense> iden= ()->new Expense(0.0,0.0);

	public double average() {
        return count > 0 ? ((double) spent)/count : 0;
    }
	
		
}
