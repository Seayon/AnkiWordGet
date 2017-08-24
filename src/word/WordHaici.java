package word;

/*
	该类传入一个英文单词作为参数，在构造时即调用抓取信息函数抓取信息，组装成JSON格式，使用对外私有的JSONwordJSONInfoect对象存储抓取到的JSON格式数据并返回
	考虑优化正则表达式，充分利用正则表达式中 以xxx开头但是不包括
*/
import java.io.IOException;
import org.json.*;
import okhttp3.Request;

public class WordHaici extends Word {
	private final static String dictURL = "http://dict.cn/";// 目标网址

	public WordHaici(String word) {
		if (word.replaceAll(" ", "").equals("")) {
			wordJSONInfo.put("error", "请勿传入空值");
		} else {
			super.dictURL = dictURL;
			this.word = word;
			try {
				this.wordJSONInfo = wordInfoCapture();
			} catch (Exception e) {
				e.printStackTrace();
				wordJSONInfo = null;
			}
		}
	}

	// 抓取单词信息函数
	protected JSONObject wordInfoCapture() {
		Request request = new Request.Builder().url(WordHaici.dictURL + this.word).build();// 根据传入的单词建立请求参数
		try {
			response = okHttpClient.newCall(request).execute();// 执行请求
			responseHTML = response.body().string();
		} catch (IOException e1) {
			e1.printStackTrace();
			wordJSONInfo.put("error", "连接到海词网站异常");
			return wordJSONInfo;
		}

		if (response.code() != 200) {
			wordJSONInfo.put("error", "页面连接异常");
			return wordJSONInfo;
			// 如果某个单词没有，目前发现会有两种情况，1：响应头404错误，2：提示要"寻找的是不是"
		} else if (response.code() == 404) { // 判断第一种情况404错误
			wordJSONInfo.put("error", 404);
		} else if (responseHTML.contains("ifufind") && responseHTML.contains("您要查找的是不是")) {// 判断第二种找不到的情况
			wordJSONInfo.put("error", "单词可能拼写错误!");
		} else {
			wordJSONInfo.put("单词", word);
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
			// 以下是匹配单词词性和释义并添加到wordJSONInfo JSON对象里面

			// 匹配音标
			String soundMarkPat = "[美|英]\\n.*EN-US.*</bdo>";
			soundMark = regexWith(responseHTML, soundMarkPat);
			int i = 1;
			while (soundMark[i] != null && i < 8) {
				startIndex = soundMark[i].indexOf(">");
				endIndex = soundMark[i].lastIndexOf("<");
				wordJSONInfo.put("音标" + i, soundMark[i].substring(startIndex + 1, endIndex));
				i++;
			}
			// 匹配单词词性和释义
			String wordPropertyPat = "<li>[\\n]*[\\t]*[\\s]*<span>\\w*\\.<";
			String wordValuePat = "</span>[\\n]*[\\t]*[\\s]*<strong>.*</strong>";
			wordProperty = regexWith(responseHTML, wordPropertyPat);
			wordValue = regexWith(responseHTML, wordValuePat);
			i = 1;
			while (wordProperty[i] != null && wordValue[i] != null && i < 8) {
				startIndex = wordProperty[i].indexOf("n>");
				endIndex = wordProperty[i].lastIndexOf("<");
				wordJSONInfo.put("词性" + i, wordProperty[i].substring(startIndex + 2, endIndex));

				startIndex = wordValue[i].indexOf("g>");
				endIndex = wordValue[i].lastIndexOf("<");
				wordJSONInfo.put("释义" + i, wordValue[i].substring(startIndex + 2, endIndex));
				i++;
			}

			// 匹配例句
			String sentenceENPat = "<li>.*<br/>";
			String sentenceCNPat = "<br/>.*</li>";
			sentenceEN = regexWith(responseHTML, sentenceENPat);
			sentenceCN = regexWith(responseHTML, sentenceCNPat);
			i = 1;
			while (sentenceEN[i] != null && sentenceCN[i] != null && i < 8) {
				endIndex = sentenceEN[i].lastIndexOf("<");
				wordJSONInfo.put("例句" + i, sentenceEN[i].substring(4, endIndex));
				endIndex = sentenceCN[i].lastIndexOf("<");
				wordJSONInfo.put("例句翻译" + i, sentenceCN[i].substring(5, endIndex));
				i++;
			}
		}
		return wordJSONInfo;
	}

}
