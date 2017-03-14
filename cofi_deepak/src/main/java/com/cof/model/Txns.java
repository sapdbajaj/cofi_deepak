package com.cof.model;

public class Txns {
	@Override
	public String toString() {
		return "Txns [txns=" + txns + "]";
	}

	public Txn getTxns() {
		return txns;
	}

	public void setTxns(Txn txns) {
		this.txns = txns;
	}

	private Txn txns;
}
