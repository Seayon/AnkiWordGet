package com;

import org.json.*;

import word.WordHaici;

public class AddCard {

	private JSONObject json = new JSONObject();// 存储添加单词需要POST的数据的JSON对象
	private JSONObject wordJSONInfo = new JSONObject();// 存储添加的单词的信息(可能会被重载函数修改,非原始单词信息)
	private String responseString = null;// 页面响应信息
	private boolean addState = false;// 页面响应信息
	// 构造函数，根据传入参数立即调用抓取函数获得信息

//	public AddCard(String deckName, String modelName, String word) {
//		wordJSONInfo = (new WordHaici(word).getWordJSONInfo());// 根据传入的单词获取单词JSON格式的信息
//		this.confSet(deckName, modelName, wordJSONInfo);
//	}

	// 重载构造函数，如果用户有自定义例句1和例句翻译1就执行这个
	public AddCard(String deckName, String modelName, String word, String sentence, String translation) {
		this.wordJSONInfo = (new WordHaici(word).getWordJSONInfo());
		if (wordJSONInfo == null || wordJSONInfo.has("error")) {
			System.out.println(wordJSONInfo);
			return;
		}
		System.out.println(wordJSONInfo);
		if (sentence != null && translation != null) {// 有可能传入空值
			wordJSONInfo.put("例句翻译1", translation);
			wordJSONInfo.put("例句1", sentence);
			wordJSONInfo.put("例句1",	wordJSONInfo.getString("例句1").replaceAll(word, "<font color=\"#ff5500\">" + word + "</font>"));
			wordJSONInfo.put("音标", wordJSONInfo.getString("音标2"));
		}
		if(wordJSONInfo.has("例句2")){
			wordJSONInfo.put("例句2",	wordJSONInfo.getString("例句2").replaceAll(word, "<font color=\"#ff5500\">" + word + "</font>"));
		}
		if(wordJSONInfo.has("例句1")) {
			wordJSONInfo.put("例句1",	wordJSONInfo.getString("例句1").replaceAll(word, "<font color=\"#ff5500\">" + word + "</font>"));
		}
		if(wordJSONInfo.has("音标2")) {
			wordJSONInfo.put("音标", wordJSONInfo.get("音标2"));
		}
		this.confSet(deckName, modelName, wordJSONInfo);

	}

	// 封装的把添加卡片封装成JSON对象的函数
	public void confSet(String deckName, String modelName, JSONObject wordJSONInfo) {
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
		json.put("action", "addNote");
		this.add();
	}

	// 执行添加操作,根据是否成功改变响应信息字符串
	public boolean add() {
		postJSON p = new postJSON("http://127.0.0.1:8765", json);
		if (p.getPostState()) {

			responseString = p.getResponseString();
			if (responseString.equals("null")) {
				addState = false;
				return false;
			} else {
				addState = true;
				return true;
			}
		} else {
			responseString = p.getResponseString();
			addState = false;
			return false;
		}
	}

	// 获取响应信息字符串
	public String getResponseString() {
		return responseString;
	}

	// 获取添加成功状态
	public boolean getAddState() {
		return addState;
	}

	public JSONObject getWordJSONInfo() {
		return wordJSONInfo;
	}

}
