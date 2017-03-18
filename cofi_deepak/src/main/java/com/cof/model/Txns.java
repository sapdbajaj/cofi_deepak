package com.cof.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Txns {

	public Txns() { 
		txns = new ArrayList<Txn> ();
	}
	@Override
	public String toString() {
		return "transactions" + txns ;
	}

	public List<Txn> getTxns() {
		return txns;
	}

	public void setTxns(List<Txn> liTxn) {
		this.txns = liTxn;
	}

	public void sort(){
		// sort the txns on transaction time
		Collections.sort(txns,(a,b)->a.getTransaction_time().compareTo(b.getTransaction_time()));	
		}
	
	List<Txn> txns ;
}
