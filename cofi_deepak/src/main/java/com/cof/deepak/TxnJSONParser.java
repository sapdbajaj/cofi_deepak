package com.cof.deepak;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import com.cof.model.Txn;
import com.cof.model.Txns;
import com.cof.model.State;

public class TxnJSONParser {

	private static final String DEFAULT_FILENAME = "txn.json";
	
	public void parse(InputStream is, Txns txns){
	JsonParser jsonParser = Json.createParser(is);
	Txn t=null;
	String keyName = null;		
	State state =State.INIT;
	
	while (jsonParser.hasNext()) {
		Event event = jsonParser.next();
		switch (event) {
		
		case START_OBJECT:
			if (state==State.TXN ) {
				t = new Txn();
				txns.getTxns().add(t);
			}
			else{
				state = State.TXNS;				
			}
			
			break;
			
		case END_OBJECT:
			if (state!=State.TXN && (txns==null || txns.getTxns()==null||txns.getTxns().isEmpty())) throw new TxnException("No Transactions found");
			break;
			
		case START_ARRAY:
			if(txns.getTxns()==null)txns.setTxns(new ArrayList<Txn> ());
			state = State.TXN ;
			break;
			
		case END_ARRAY:
			state = State.TXNS;	
			break;
			
		case KEY_NAME:
			keyName = jsonParser.getString();
			break;
			
		case VALUE_STRING:
			setStringValues(t, keyName, jsonParser.getString());
			break;
			
		case VALUE_NUMBER:
			setNumberValues(t, keyName, jsonParser.getLong());
			break;
			
		case VALUE_FALSE:
			setBooleanValues(t, keyName, false);
			break;
			
		case VALUE_TRUE:
			setBooleanValues(t, keyName, true);
			break;
			
		case VALUE_NULL:

			break;
		default:

		}
	}
	jsonParser.close();

	}
	
	public static void main(String[] args) throws IOException {
		TxnJSONParser tmp=new TxnJSONParser();
		FileInputStream fis=new FileInputStream(DEFAULT_FILENAME);
		Txns txns=new Txns();
		tmp.parse(fis, txns);
		System.out.println(txns);
		
		//close resources
		fis.close();
		txns=null;
		tmp=null;
	}

	private void setNumberValues(Txn t,
			String keyName, long value) {
		switch(keyName){
		case "amount":
			t.setAmount(value);
			break;
		case "aggregation-time":
			t.setAggregation_time(value);
			break;
		case "clear-date":
			t.setClear_date(value);
			break;
		default:
			System.out.println("Unknown element with key="+keyName);	
		}
	}

	private void setBooleanValues(Txn t, 
			String key, boolean value) {
		if("is-pending".equals(key)){
			t.setPending(value);
		}else{
			System.out.println("Unknown element with key="+key);
		}
	}

	private void setStringValues(Txn t, String key, String value) {
		switch(key){
		case "error":
			if(value.compareToIgnoreCase("no-error")!=0) throw new TxnException("Unable to get txn data");
			break;
		case "transaction":
			break;
		case "account-id":
			t.setAccount_id(value);
			break;
		case "transaction-id":
			t.setTransaction_id(value);
			break;
		case "raw-merchant":
			t.setRaw_merchant(value);
			break;
		case "categorization":
			t.setCategorization(value);
			break;
		case "merchant":
			t.setMerchant(value);
			break;
		case "transaction-time":
			String tmp[]=value.split(Pattern.quote("."));
			t.setTransaction_time(LocalDateTime.parse(tmp[0],DateTimeFormatter.ISO_LOCAL_DATE_TIME));
			break;
		default:
			//System.out.println("Unkonwn Key="+key);
			break;	
		}
	}

}
