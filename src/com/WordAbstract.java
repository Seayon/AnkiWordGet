package com;

import org.json.JSONObject;

public abstract class WordAbstract {
	protected String dictURL;//抓取单词的目标页面地址
	protected String word;//要抓取的单词
	protected JSONObject wordJSONInfo;//用来存储单词信息的JSON对象
	
	public String getDictURL() {
		return this.dictURL;
	}
	public JSONObject getWordJSONInfo() {
		return this.wordJSONInfo;
	}
	protected abstract JSONObject wordInfoCapture();
	
}
