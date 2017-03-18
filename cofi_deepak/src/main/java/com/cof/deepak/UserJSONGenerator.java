package com.cof.deepak;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.json.Json;
import javax.json.stream.JsonGenerator;

import com.cof.model.Args;
import com.cof.model.User;

public class UserJSONGenerator {

	public static void main(String[] args) throws IOException {
		OutputStream fos = new FileOutputStream("C:/Users/deepa/downloads/user_stream.txt");
		JsonGenerator jsonGenerator = Json.createGenerator(fos);
		
		User u = ServiceJSONWriter.createUser();
		Args a = u.getArgs();
		jsonGenerator.writeStartObject(); // {
		jsonGenerator.write("email", u.getEmail()); 
		jsonGenerator.write("password", u.getPassword());
		
		jsonGenerator.writeStartObject("args") //start of args object
			.write("uid", a.getUid().longValue())
			.write("token",a.getToken())
			.write("api-token",a.getApi_token())
			.write("json-strict-mode",a.isJson_strict_mode())
			.write("json-verbose-response",a.isJson_verbose_response())
			.writeEnd(); //end of args object
	 
		jsonGenerator.writeEnd(); // }
		
		jsonGenerator.close();
		fos.close();
	}

}
