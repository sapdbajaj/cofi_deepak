package com.cof.deepak;

import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import com.cof.model.Args;
import com.cof.model.User;

public class UserJSONWriter {

	public JsonObject createTxns(){
		 	JsonObjectBuilder userBuilder = Json.createObjectBuilder();
			userBuilder.add("args", createCore());
			return userBuilder.build();
	   }
	
   public JsonObject createLogin(){
	   User user = createUser();
	   JsonObjectBuilder userBuilder = Json.createObjectBuilder();
		
		userBuilder.add("email", user.getEmail())
					.add("password", user.getPassword());
		userBuilder.add("args", createCore());
		JsonObject userJsonObject = userBuilder.build();	
		return userJsonObject ; 
   }

	public  JsonObjectBuilder createCore()  {
		User user = createUser();
		Args a = user.getArgs();

		JsonObjectBuilder argsBuilder = Json.createObjectBuilder();
		
		argsBuilder.add("uid",a.getUid().longValue())
				  .add("token",a.getToken())
				  .add("api-token",a.getApi_token())
				  .add("json-strict-mode",a.isJson_strict_mode())
				  .add("json-verbose-response",a.isJson_verbose_response());	
		return argsBuilder;
	}
	
	
	public static void main(String[] args) throws Exception {
		UserJSONWriter w=new UserJSONWriter();	
		//write to file
		OutputStream os = new FileOutputStream("c:/users/deepa/Downloads/user.txt");
		JsonObject jo=w.createLogin();
		Controller ctrl=new Controller();
		ctrl.createReq(jo, os);
		System.out.println("User JSON String\n"+jo);		
	}
	

	public static User createUser() {

		User user = new User();
		user.setEmail("interview@levelmoney.com");
		user.setPassword("password2");
		Args a = new Args();
		a.setUid(1110590645);
		a.setToken("242F0630905A27E08CFF0B363E215C4C");
		a.setApi_token("AppTokenForInterview");
		a.setJson_strict_mode(false);
		a.setJson_verbose_response(false);
		user.setArgs(a);
		return user;
	}

}
