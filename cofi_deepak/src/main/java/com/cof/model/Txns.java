package com.cof.model;

import java.util.ArrayList;
import java.util.List;

public class Txns {

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

	List<Txn> txns ;
}
