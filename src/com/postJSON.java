package com;

import java.io.IOException;

import org.json.JSONObject;

import okhttp3.*;

public class postJSON {
	private String postURL = null;
	private String jsonStr = null;
	private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
	private Response response;
	private String responseString;
	private boolean postState;
	public postJSON(String postURL, JSONObject json) {
		this.postURL = postURL;
		this.jsonStr = json.toString();
		this.postJSONInfo();
		}

	public boolean postJSONInfo() {
		OkHttpClient otc = new OkHttpClient();
		RequestBody requestBody = RequestBody.create(JSON, jsonStr);
		Request request = new Request.Builder().url(postURL).post(requestBody).build();
		response = null;
		
		try {
			response = otc.newCall(request).execute();
			if(response.code()!=200) {
				postState = false;
			}
			responseString = response.body().string();
			postState = true;
			return true;
		} catch (IOException e) {
			responseString = "连接异常";
			postState = false;
			return false;
		}
	}
	
	public String getResponseString() {
		return responseString;
	}
	public boolean getPostState() {
		return postState;
	}
}
