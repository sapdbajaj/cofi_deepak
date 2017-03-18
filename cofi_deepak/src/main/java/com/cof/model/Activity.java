package com.cof.model;

public class Activity {
	protected double spent, income;
	protected String year_month;
	protected int count=0 ;

	public Activity()
	{
	  spent = 0;income=0.0;
	  year_month = "" ; 
	  count=0;
	}

	public void accept(Txn src)
	{
	  if (src.getAmount() > 0 ) {
		  income += src.getAmount();
	  }else {
		  spent += src.getAmount();
	  }
	  year_month=src.getYearMonth();
	  ++count;
	}

	public Activity combine(Activity tgt)
	{
	  spent += tgt.getSpent();
	  income += tgt.getIncome();
	  return this;
	}

	public double getSpent()
	{
	  return spent;
	}
	
	public double getIncome()
	{
	  return income;
	}
	
	public String getYear_month()
	{
	  return year_month;
	}
	
	public int getCount()
	{
	  return count;
	}
}
