package com;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.*;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Word {
	private final static String dictURL = "http://dict.cn/";//目标网址
	private String word = null;//目标单词
	private JSONObject obj = new JSONObject();//创建一个JSON对象来存储获取到的信息
	
	public Word(String word) {
		this.word = word;
		try {
			obj = this.getInfo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			obj = null;
		}
	}
	public JSONObject getJSON() {
		return obj;
	}
	public JSONObject getInfo(){
		long endTime, runTime;

		OkHttpClient otc = new OkHttpClient();// 建立Http客户端
		Request request = new Request.Builder().url(Word.dictURL+this.word).build();// 根据传入的单词建立请求参数
		Response response = null;
		try {
			response = otc.newCall(request).execute();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			obj.put("error", "客户端连接异常");
		} // 执行请求
		
		// 如果某个单词没有，目前发现会有两种情况，1：404错误，2：提示要"寻找的是不是"
		if (response.code() == 404) { // 判断第一种情况404错误
			obj.put("error", 404);
			return obj; // 暂时注释
			// System.out.println(response.code());
		}

		String responseHTML = null;
		try {
			responseHTML = response.body().string();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 获取返回的页面字符串
		// System.out.println(responseHTML);
		if (responseHTML.contains("ifufind") || responseHTML.contains("没有找到")) {// 判断第二种找不到的情况
			obj.put("erroe", "Can't find the word");
			return obj;
		} else {
			// 创建一个JSON对象来存储获取到的信息
			obj.put("单词", word);
			// 使用正则表达式来匹配寻找

			/*
			 * //匹配单词本身,先留着,可以考虑用来校验输入的和页面返回的是否一样 String keywordPat = "keyword.*<"; Pattern
			 * patter = Pattern.compile(keywordPat); Matcher matcher =
			 * patter.matcher(responseHTML); String keywordFirst = null; while
			 * (matcher.find()) { keywordFirst = matcher.group(); } int start =
			 * keywordFirst.lastIndexOf(">"); int end = keywordFirst.lastIndexOf("<");
			 * String keyword = keywordFirst.substring(start + 1, end);
			 * System.out.println(keyword);
			 */
			int startIndex, endIndex;// 字符串截取的开始和结束位置
			// 以下是匹配单词词性和释义并添加到obj JSON对象里面
			String wordProperty, wordValue;
			Pattern patter, patter2 = null;
			Matcher matcher, matcher2 = null;
			String wordPropertyPat = "(<span>.*\\.)";
			String wordValuePat = "(\\s<strong>.*</strong>)";
			patter = Pattern.compile(wordPropertyPat);
			patter2 = Pattern.compile(wordValuePat);
			matcher = patter.matcher(responseHTML);
			matcher2 = patter2.matcher(responseHTML);
			int i = 1;
			while (matcher.find() && matcher2.find() && i <= 4) {
				wordProperty = matcher.group(0).substring(6);
				endIndex = matcher2.group(0).lastIndexOf("<");
				wordValue = matcher2.group(0).substring(9, endIndex);
				obj.put("词性" + i, wordProperty);
				obj.put("释义" + i, wordValue);
				i++;
			}

			// 匹配音标
			String soundMarkPat = "美\\n.*EN-US.*</bdo>";
			String soundMark = null;
			patter = Pattern.compile(soundMarkPat);
			matcher = patter.matcher(responseHTML);
			while (matcher.find()) {
				startIndex = matcher.group(0).indexOf(">");
				endIndex = matcher.group(0).lastIndexOf("<");
				soundMark = matcher.group(0).substring(startIndex + 1, endIndex);
				obj.put("音标", soundMark);
			}

			// 匹配例句
			String sentenceENPat = "<li>.*<br/>";
			String sentenceCNPat = "<br/>.*</li>";
			String sentenceEN = null;
			String sentenceCN = null;
			patter = Pattern.compile(sentenceENPat);
			patter2 = Pattern.compile(sentenceCNPat);
			matcher = patter.matcher(responseHTML);
			matcher2 = patter2.matcher(responseHTML);
			i = 1;
			while (matcher.find() && matcher2.find() && i <= 2) {
				endIndex = matcher.group().lastIndexOf("<");
				sentenceEN = matcher.group().substring(4, endIndex);
				endIndex = matcher2.group().lastIndexOf("<");
				sentenceCN = matcher2.group().substring(5, endIndex);
				obj.put("例句" + i, sentenceEN);
				obj.put("例句翻译" + i, sentenceCN);
				i++;
			}
			return obj;
		}
	}

}