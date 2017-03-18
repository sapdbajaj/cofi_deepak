package com.cof.deepak;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonGenerator;

import com.cof.model.Activity;


public class OutputGenerator {
	final public static NumberFormat currencyFormatter = 
	        NumberFormat.getCurrencyInstance(Locale.getDefault());

	JsonGenerator jsonGenerator;
	
	OutputGenerator(OutputStream os) { 
	 jsonGenerator = Json.createGenerator(os);	
	}
	
	public void start() {
		jsonGenerator.writeStartObject(); // {	
	}
	
	public void end () {
		jsonGenerator.writeEnd(); // }		
		jsonGenerator.close();
	}
	public void write(Map.Entry<String, Activity> entry) throws IOException {
		
		//myMap.entrySet().stream().sorted(Map.Entry.<String, Activity>comparingByKey()).forEach(entry -> {
			jsonGenerator.writeStartObject(entry.getKey()); // { for key 
			jsonGenerator.write("spent :", currencyFormatter.format(entry.getValue().getSpent())); 
			jsonGenerator.write("income :", currencyFormatter.format(entry.getValue().getIncome())); 
			jsonGenerator.writeEnd(); // }	
		 //}); 
		
	}
	
	public static void main(String[] args) throws IOException {
		OutputStream fos = new FileOutputStream("activity_out_stream.txt");
		OutputGenerator og=new OutputGenerator(fos);

		fos.close();
	}	

}
