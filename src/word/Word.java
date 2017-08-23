package word;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.MyCookieJar;

import okhttp3.OkHttpClient;
import okhttp3.Response;

abstract class Word {
	protected String dictURL;//抓取单词的目标页面地址
	protected String word;//要抓取的单词
	protected String[] soundMark;//单词音标
	protected String[] wordProperty;
	protected String[] wordValue;
	protected String[] sentenceEN;
	protected String[] sentenceCN;
	protected JSONObject wordJSONInfo = new JSONObject();//用来存储单词信息的JSON对象
	protected String responseHTML;//请求的时候用来存储目标网页响应HTML字符串
	protected Response response;//响应Response对象
	OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(new MyCookieJar()).build();//Cookie可以持久化的HTTP客户端
	private Pattern patter ;
	private Matcher matcher;
	public String getDictURL() {
		return this.dictURL;
	}
	public JSONObject getWordJSONInfo() {
		return this.wordJSONInfo;
	}
	//封装的正则提取函数
	protected String[] regexWith(String source,String regexPat) {
		String[] arries =new String[10];
		patter = Pattern.compile(regexPat);
		matcher = patter.matcher(source);
		int i = 1;
		while(matcher.find() && i < 10) {
			arries[i] = matcher.group();			
			 i++;
		}
		return	arries;
	}
	protected abstract JSONObject wordInfoCapture();
	
}
