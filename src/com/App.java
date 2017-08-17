package com;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class App {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		OkHttpClient otc = new OkHttpClient();
		Request request = new Request.Builder().url("https://www.baidu.com").build();
		Response response = otc.newCall(request).execute();
		System.out.println(response.body().string());
	}

}
