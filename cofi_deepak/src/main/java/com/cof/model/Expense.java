package com.cof.model;

import java.time.LocalDate;

public class Expense  {
	private double spent;
	private double income;
	private LocalDate transaction_time;
	
	public Expense(double thisIncome, double thisSpent, LocalDate thisTime){
		income=thisIncome; 
		spent=thisSpent;
		transaction_time = thisTime;
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

	public LocalDate getTransaction_time() {
		return transaction_time;
	}

	public void setTransaction_time(LocalDate transaction_time) {
		this.transaction_time = transaction_time;
	}
	
	public String getYearMonth() {
		return transaction_time.format(Txn.fmtYearMonth);
	}
	
	public Expense sum(Expense e1, Expense e2){
		if (e1.transaction_time == e2.transaction_time)
		 return new Expense(e1.income+e2.income, e1.spent+ e2.spent, e1.transaction_time);
		else
		 return null;
	}

}
