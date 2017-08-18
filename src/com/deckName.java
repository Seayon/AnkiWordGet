package com;

import org.json.JSONObject;

import okhttp3.*;

public class deckName {
	private String str[] = null;
	private JSONObject json = new JSONObject();
	public deckName(){
		json.put("action", "deckNames");
	}
	public String getDeckName(){
		postJSON p = new postJSON("http://127.0.0.1:8765", json);
	    return p.getResponseString();
	}
}
