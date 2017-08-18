package com;

import java.io.IOException;

import org.json.*;

import okhttp3.*;
import sun.rmi.runtime.Log;


public class AddCard {

	private JSONObject json = new JSONObject();
	private String responseString = null;
	public AddCard(String deckName,String modelName,JSONObject wordJSONInfo){
		this.confSet(deckName, modelName, wordJSONInfo);
	}
	public AddCard(String deckName,String modelName,JSONObject wordJSONInfo,String sentence,String translation){
		if(sentence!=null&&translation!=null) {
		wordJSONInfo.put("例句1", sentence);
		wordJSONInfo.put("例句翻译1", translation);
		this.confSet(deckName, modelName, wordJSONInfo);
		}else {
			this.confSet(deckName, modelName, wordJSONInfo);
		}
	}
	
	public void confSet(String deckName,String modelName,JSONObject wordJSONInfo) {
		JSONObject obj2 = new JSONObject();
		JSONObject obj3 = new JSONObject();
		JSONArray array = new JSONArray();
		array.put("2");
		obj3.put("fields", wordJSONInfo);
		obj3.put("deckName", deckName);
		obj3.put("modelName", modelName);
		obj3.put("tags", array);
		obj2.put("note", obj3);
		json.put("params", obj2);
		json.put("action","addNote");
		this.add();
	}
	public boolean add(){
		postJSON p = new postJSON("http://127.0.0.1:8765",json);
		if(p.getPostState()) {
			responseString = p.getResponseString();
			return true;
		}else {
			return false;
		}	
	}
	public String getResponseString() {
		return responseString;
	}
	 
}
