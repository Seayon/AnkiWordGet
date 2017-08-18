package com;

import java.io.IOException;

import org.json.*;

import okhttp3.*;
import sun.rmi.runtime.Log;


public class AddCard {
	private static final MediaType JSON =MediaType.parse("application/json;charset=utf-8");
	private JSONObject json = null;
	private String jsonStr = null;
	public AddCard(String deckName,String modelName,JSONObject wordJSONInfo){
		JSONObject obj1 = new JSONObject();
		JSONObject obj2 = new JSONObject();
		JSONObject obj3 = new JSONObject();
		JSONArray array = new JSONArray();
		array.put("2");
		obj3.put("fields", wordJSONInfo);
		obj3.put("deckName", deckName);
		obj3.put("modelName", modelName);
		obj3.put("tags", array);
		obj2.put("note", obj3);
		obj1.put("params", obj2);
		obj1.put("action","addNote");
		System.out.println(obj1);
		this.jsonStr = obj1.toString();
		this.add();
	}
	public boolean add(){
		OkHttpClient otc = new OkHttpClient();
		RequestBody requestBody = RequestBody.create(JSON, jsonStr);
		Request request =new Request.Builder()
				.url("http://127.0.0.1:8765")
				.post(requestBody)
				.build();
		Response response = null;
		 try {
			response=otc.newCall(request).execute();
			System.out.println(response.body().string());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		 return true;
	}
	 
}
