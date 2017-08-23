package word;

import org.json.JSONObject;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Word91 extends Word {
	private final String dictURL = "http://www.91dict.com/words?w=";// 91人人词典的链接
	private String responseHTML = null;
	private Response response = null;
	public Word91(String word) {
		super.dictURL = dictURL;
		this.word = word;
		try {
			this.wordJSONInfo = wordInfoCapture();
		} catch (Exception e) {
			e.printStackTrace();
			wordJSONInfo = null;
		}
	}

	// 实现
	protected JSONObject wordInfoCapture() {
		
		OkHttpClient okHttpClient = new OkHttpClient();
		Request request = new Request.Builder().url(dictURL + word).build();
		try {
			response = okHttpClient.newCall(request).execute();
			ResponseBody responseBody = response.body();// 执行这一句的时候可能报错
			byte[] b = responseBody.bytes();// 由于海词网站页面是gb2312,故需要一个字节数组来存储转码
			String str = new String(b, "UTF-8");// 再用gb2312格式读取到一个字符串中
			this.responseHTML = new String(str.getBytes(), "UTF-8");// 拿到页面HTML字符串

			System.out.println(responseHTML);
		} catch (IOException e) {
			this.wordJSONInfo.put("error", "请求海词页面出错");
			e.printStackTrace();
			return wordJSONInfo;
		}

		if (response.code() != 200) {// 判断连接是否正常
			wordJSONInfo.put("error", "连接到海词网站异常");
		} else if (responseHTML.contains("要查找的是不是")) {// 如果某个单词没有，在使用Mini海词版本的话，由于单词输入错误的模糊匹配是前端js动态请求服务器计算返回的，故此处只会提示“您要查找的是不是”
			wordJSONInfo.put("error", "没有找到该单词");
		} else {
//			System.out.println(responseHTML);
			System.out.println(regexWith(responseHTML, "上文"));
		}
		return wordJSONInfo;

	}

}
