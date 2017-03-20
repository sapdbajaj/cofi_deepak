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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

public class Controller {
	final static public String endpoint="https://2016.api.levelmoney.com/api/v2/core/";
	
	final static protected String DEFAULT_FILENAME="activity_out.txt";
	final static protected String DEFAULT_DUPFILENAME="duplicates.txt";
	final static protected String OPT_IGNORE_DONUTS = "ignore-donuts";
	final static protected String OPT_CRYSTAL_BALL = "crystal-ball";
	final static protected String OPT_IGNORE_CC_PAYMENTS ="ignore-cc-payments" ;
	final static protected String OPT_OUT="out";
	final static protected String OPT_DUPSOUT="dups";
	
	final static protected String[] opts= {OPT_IGNORE_DONUTS,OPT_CRYSTAL_BALL,OPT_IGNORE_CC_PAYMENTS};
	final static protected int DEFAULT_YEAR=2017;
	final static protected int DEFAULT_MONTH=3; 
	private String outFilename;
	private String dupFilename;
	private String expMerchant;
	private Map<String, Integer> crystalParams = new HashMap(2); 

	
	static OptionParser parser = new OptionParser();
	{
		parser.accepts(OPT_IGNORE_DONUTS).withOptionalArg().ofType(String.class).describedAs( "Ignore donut txns" ).defaultsTo("Donuts|DUNKIN");
		
		parser.accepts(OPT_CRYSTAL_BALL).withOptionalArg().describedAs( "Include projected txns" );
		parser.accepts( "year" ).requiredIf( OPT_CRYSTAL_BALL).withRequiredArg().ofType(Integer.class).describedAs( "year for projected txns" ).defaultsTo(DEFAULT_YEAR);
		parser.accepts( "month" ).requiredIf( OPT_CRYSTAL_BALL).withRequiredArg().ofType(Integer.class).describedAs( "month for projected txns" ).defaultsTo(DEFAULT_MONTH);
		
		parser.accepts(OPT_IGNORE_CC_PAYMENTS).withOptionalArg().describedAs( "Ignore reversed(24hrs) CreditCard txns" );	
		parser.accepts(OPT_DUPSOUT ).requiredIf( OPT_IGNORE_CC_PAYMENTS).withRequiredArg().ofType(String.class).describedAs( "Output for duplicate txns" ).defaultsTo(DEFAULT_DUPFILENAME);

		parser.accepts( OPT_OUT ).withOptionalArg().ofType(String.class).describedAs( "Outputfile for txns" ).defaultsTo(DEFAULT_FILENAME);
	}
	
	
	protected Txns to;
	List<Txn> dTxns;
	OptionSet options;

	public Controller(String[] args) {
		 options = parser.parse( args);	
		 System.out.println("Recieved args = "+Arrays.toString(args));
		// get the DONUTS Pattern from command line, if available 
		 if (options.has(OPT_IGNORE_DONUTS)) { 
			 expMerchant=(String) options.valueOf(OPT_IGNORE_DONUTS) ;
		 }
		 
		 // for crystal ball, get the year and month from command line, if possible 
		 if (options.has(OPT_CRYSTAL_BALL) ) { 
			 crystalParams.put("year", (Integer) options.valueOf("year"));
			 crystalParams.put("month", (Integer) options.valueOf("month"));
		 }
		 
		 // get the outfilename from command line 
		 if (options.has(OPT_OUT) ) outFilename = (String) options.valueOf(OPT_OUT) ;		 
		 // get the duplicates out filename from cmd line 
		 if (options.has(OPT_IGNORE_CC_PAYMENTS)) { 
			 dupFilename=(String) options.valueOf(OPT_DUPSOUT) ;
		 }
	}
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
		createRsp(doReq(endpoint+"login",uo));

		System.out.println("User JSON String\n"+uo);
	}
	
	public void initTxns(){
		to = new Txns();
		ServiceJSONWriter w=new ServiceJSONWriter();	
		JsonObject tjo=w.getTxns(null);
		System.out.println("Txns Req JSON String\n"+tjo);
		//createRsp(doReq("https://2016.api.levelmoney.com/api/v2/core/get-all-transactions",to));
		addTxns(doReq(endpoint+"get-all-transactions",tjo));	
		System.out.println (" ALL Count"+to.getTxns().size());
		if (options.has(OPT_CRYSTAL_BALL)) {
			tjo=null;
			tjo=w.getTxns(crystalParams);
			addTxns(doReq(endpoint+"projected-transactions-for-month",tjo));
		}
		System.out.println (" Total Count"+to.getTxns().size());
	}
	
	Predicate<Txn> evalDonut=(t)->{
		return options.has(OPT_IGNORE_DONUTS) ? !(t.getMerchant().matches("(?i:.*("+expMerchant+").*)")): true ; 
		};

	// we can use the filter, if inline dup removal is needed
	Predicate<Txn> evalExist=(t)->{
		return options.has(OPT_IGNORE_CC_PAYMENTS) && dTxns != null ? !(dTxns.contains(t)): true ; 
		};
		
	public void process(){
		 List<Txn> tmpTxns= to.getTxns();
		 // process txns		
		 if (options.has(OPT_IGNORE_CC_PAYMENTS)) { 
			 Map<Boolean, List<Txn>> splitTxns = to.getTxns().stream().collect(Collectors.partitioningBy(t-> Collections.frequency(to.getTxns(),t)>1));
			 System.out.println("Dup COUNT " + splitTxns.get(true).size());
			 System.out.println("Non Dup COUNT " + splitTxns.get(false).size());
			 
			 dTxns = splitTxns.get(true);
			 try {
				Files.write(Paths.get(dupFilename), (Iterable<String>)dTxns.stream().map(String::valueOf)::iterator);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 tmpTxns = splitTxns.get(false);
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
			OutputStream fos = new FileOutputStream(outFilename);
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
	
	public static void main(String[] args) throws Exception {
		Controller ctrl=new Controller(args);
		parser.printHelpOn(System.out);
		ctrl.initTxns();
		ctrl.process();
		}
}
