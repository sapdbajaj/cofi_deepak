package com.cof.deepak;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import javax.net.ssl.HttpsURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

public class Controller {
	
	public void createReq(JsonObject jo, OutputStream os) {
		JsonWriter jsonWriter = Json.createWriter(os);
		jsonWriter.writeObject(jo);
		jsonWriter.close();
	}
	
	public HttpsURLConnection doReq(String myURL, JsonObject jo) {
		StringBuilder sb=new StringBuilder(1024);
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
				throw new RuntimeException("Failed : HTTP error code : "
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
	
	
	// login the user 
	public void login() { 
		UserJSONWriter w=new UserJSONWriter();	
		JsonObject uo=w.createLogin();
		createRsp(doReq("https://2016.api.levelmoney.com/api/v2/core/login",uo));

		System.out.println("User JSON String\n"+uo);
	}
	
	public void getTxns(){
		UserJSONWriter w=new UserJSONWriter();	
		JsonObject to=w.createTxns();
		System.out.println("Txns Req JSON String\n"+to);
		createRsp(doReq("https://2016.api.levelmoney.com/api/v2/core/get-all-transactions",to));
		
	}
	public static void main(String[] args) throws Exception {
		Controller ctrl=new Controller();
		ctrl.getTxns();
	}
}
