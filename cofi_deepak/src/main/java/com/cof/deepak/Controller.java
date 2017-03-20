package com.cof.deepak;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import javax.net.ssl.HttpsURLConnection;

import com.cof.model.Activity;
import com.cof.model.Txn;
import com.cof.model.Txns;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

public class Controller {

	protected Txns to;
	List<Txn> dTxns;
	OptionSet options;

	public void createReq(JsonObject jo, OutputStream os) {
		JsonWriter jsonWriter = Json.createWriter(os);
		jsonWriter.writeObject(jo);
		jsonWriter.close();
	}
	
	public HttpsURLConnection doReq(String myURL, JsonObject jo) {
		//StringBuilder sb=new StringBuilder(1024);
		HttpsURLConnection httpConnection =null;
		try {

			URL targetUrl = new URL(myURL);

			httpConnection= (HttpsURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");

			OutputStream os = httpConnection.getOutputStream();
			createReq(jo,os);
			os.flush();

			if (httpConnection.getResponseCode() != 200) {
				throw new TxnException("Fatal Error in getting response from " + targetUrl + " HTTP ERROR :"
					+ httpConnection.getResponseCode());
			}

			
			/*BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
					(httpConnection.getInputStream())));
			
			String output;
			System.out.println("Output from Server:\n");
			while ((output = responseBuffer.readLine()) != null) {
				sb.append(output);
				System.out.println(output);
			}
			httpConnection.disconnect();		*/	
		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		 }
		return httpConnection ; 
	}
	
	public void writeRsp(){
		
	}
	
	public void createRsp(HttpsURLConnection httpConnection){
	 StringBuilder sb=new StringBuilder();
	 	try{
		BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
				(httpConnection.getInputStream())));
		
		String output;
		System.out.println("Output from Server:\n");
		while ((output = responseBuffer.readLine()) != null) {
			sb.append(output);
			System.out.println(output);
		}
		httpConnection.disconnect();
	 	}
	 	catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		 }
	}
	
	public void addTxns(HttpsURLConnection httpConnection){
		 	try{
		 		TxnJSONParser tmp=new TxnJSONParser();
				 tmp.parse(httpConnection.getInputStream(), to);								
				tmp=null;
				httpConnection.disconnect();
		 	}
		 	catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			 }
		}
	
	// login the user 
	public void login() { 
		ServiceJSONWriter w=new ServiceJSONWriter();	
		JsonObject uo=w.createLogin();
		createRsp(doReq("https://2016.api.levelmoney.com/api/v2/core/login",uo));

		System.out.println("User JSON String\n"+uo);
	}
	
	public void initTxns(){
		to = new Txns();
		ServiceJSONWriter w=new ServiceJSONWriter();	
		JsonObject tjo=w.getTxns(null);
		System.out.println("Txns Req JSON String\n"+tjo);
		//createRsp(doReq("https://2016.api.levelmoney.com/api/v2/core/get-all-transactions",to));
		addTxns(doReq("https://2016.api.levelmoney.com/api/v2/core/get-all-transactions",tjo));	
		System.out.println (" ALL Count"+to.getTxns().size());
		if (options.has("crystal-ball")) {
			tjo=null;
			tjo=w.getTxns(options);
			addTxns(doReq("https://2016.api.levelmoney.com/api/v2/core/projected-transactions-for-month",tjo));
		}
		System.out.println (" Total Count"+to.getTxns().size());
	}
	
	Predicate<Txn> evalDonut=(t)->{
		return options.has("ignore-donuts") ? !(t.getMerchant().matches("(?i:.*(Donuts|DUNKIN).*)")): true ; 
		};

	// we can use the filter, if inline dup removal is needed
	Predicate<Txn> evalExist=(t)->{
		return options.has("ignore-cc-payments") && dTxns != null ? !(dTxns.contains(t)): true ; 
		};
		
	public void process(String filename ){
		 List<Txn> tmpTxns= to.getTxns();
		 // process txns
		 // TODO: Move processing of txns to separate thread, fetching separate stream processing incoming txns concurrently
		
		 if (options.has("ignore-cc-payments")) { 
			 Map<Boolean, List<Txn>> splitTxns = to.getTxns().stream().collect(Collectors.partitioningBy(t-> Collections.frequency(to.getTxns(),t)>1));
			 System.out.println("Dup COUNT " + splitTxns.get(true).size());
			 System.out.println("Non Dup COUNT " + splitTxns.get(false).size());
			 dTxns = splitTxns.get(true);
			 tmpTxns = splitTxns.get(false);
			 System.out.println(" DUPLICATES "); 
			 splitTxns.get(true).forEach(System.out::println);
		 }
		 
		Map<String,Activity> myMap =   tmpTxns.stream().filter(evalDonut).collect(
				 Collectors.groupingBy(Txn::getYearMonth, Collector.of(Activity::new, Activity::accept, Activity::combine))
						 	);
		// calculate the average node and add to the map 
		 Activity avg = new Activity(
				 myMap.values().stream().collect(Collectors.averagingDouble(Activity::getSpent)), 
				 myMap.values().stream().collect(Collectors.averagingDouble(Activity::getIncome)));
		 myMap.put("Average", avg);

		// output the process results to file
	    try {
			OutputStream fos = new FileOutputStream(filename);
			OutputGenerator og=new OutputGenerator(fos);

			og.start();
			 myMap.entrySet().stream().sorted(Map.Entry.<String, Activity>comparingByKey()).forEach(entry -> {
				 //System.out.println(entry.getKey()+": { spent: "+currencyFormatter.format(entry.getValue().getSpent())+", income: " + currencyFormatter.format(entry.getValue().getIncome())+"},");
				 try {
					og.write(entry);
				} catch (IOException e) {
					e.printStackTrace();
				}
			 });
			 og.end();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	
	public static void main(String[] args) throws Exception {
		OptionParser parser = new OptionParser();
        parser.accepts( "ignore-donuts" );
        parser.accepts( "crystal-ball" );
        parser.accepts( "ignore-cc-payments" );

		Controller ctrl=new Controller();
        ctrl.options = parser.parse( args);
		ctrl.initTxns();
		ctrl.process("activity_out.txt");
	}
}
