package com.cof.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Txn  {
	final static long HOURS24 = 24 * 60 * 60 ;
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Txn other = (Txn) obj;
		if (transaction_time == null) {
			if (other.transaction_time != null)
				return false;
		} else if (Math.abs(Duration.between(transaction_time, other.transaction_time).getSeconds()) > HOURS24 || Math.abs(amount)!=Math.abs(other.amount))
			return false;
		return true;
	}

	public boolean isEqual(Txn t){
		return equals(t);
	}
	@Override
	public String toString() {
		return "Txn [amount=" + amount + ", isPending=" + isPending + ", aggregation_time=" + aggregation_time
				+ ", account_id=" + account_id + ", clear_date=" + clear_date + ", transaction_id=" + transaction_id
				+ ", raw_merchant=" + raw_merchant + ", categorization=" + categorization + ", merchant=" + merchant
				+ ", transaction_time=" + transaction_time + "]";
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
		if (amount >=0) in=amount; else out=amount ;
	}


	public boolean isPending() {
		return isPending;
	}


	public void setPending(boolean isPending) {
		this.isPending = isPending;
	}


	public long getAggregation_time() {
		return aggregation_time;
	}


	public void setAggregation_time(long aggregation_time) {
		this.aggregation_time = aggregation_time;
	}


	public String getAccount_id() {
		return account_id;
	}


	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}


	public long getClear_date() {
		return clear_date;
	}


	public void setClear_date(long clear_date) {
		this.clear_date = clear_date;
	}


	public String getTransaction_id() {
		return transaction_id;
	}


	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}


	public String getRaw_merchant() {
		return raw_merchant;
	}


	public void setRaw_merchant(String raw_merchant) {
		this.raw_merchant = raw_merchant;
	}


	public String getCategorization() {
		return categorization;
	}


	public void setCategorization(String categorization) {
		this.categorization = categorization;
	}


	public String getMerchant() {
		return merchant;
	}


	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	final public static DateTimeFormatter fmtYearMonth = DateTimeFormatter.ofPattern("yyyy-MM");
	
	public double getOut() {
		return out;
	}


	public void setOut(double out) {
		this.out = out;
	}


	public double getIn() {
		return in;
	}


	public void setIn(double in) {
		this.in = in;
	}
	private double amount ; 
	private double in;
	private double out;
	private boolean isPending;
	private long aggregation_time;
	private String account_id;
	private long clear_date;
	private String transaction_id;
	private String raw_merchant;
	private String categorization;
	private String merchant;
	private LocalDateTime transaction_time;
	
	public String getYearMonth() {
		return transaction_time.format(fmtYearMonth);
	}
	public LocalDateTime getTransaction_time() {
		return transaction_time;
	}

	public void setTransaction_time(LocalDateTime transaction_time) {
		this.transaction_time = transaction_time;
	}
	
	
}
