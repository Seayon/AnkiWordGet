/*
 * 海词迷你词典抓取
 * 由于海词使用JS动态请求生成，故需要两个步骤，第一部，先从dictURL页面上获取token值，经过计算后带着此参数发送到dictcontent.php页面
 * 
 */

package word;

import java.io.IOException;
import org.json.*;

import com.Utility;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WordHaiciMini extends Word {
	private final static String dictURL = "http://apii.dict.cn/mini.php";// 目标网址
	private final static String dictURLReal = "http://apii.dict.cn/ajax/dictcontent.php";// 海词迷你

	public WordHaiciMini(String word) {
		super.dictURL = dictURL;
		this.word = word;
		try {
			this.wordJSONInfo = wordInfoCapture();
		} catch (Exception e) {
			e.printStackTrace();
			wordJSONInfo = null;
		}
	}

	protected JSONObject wordInfoCapture() {
		Request request = new Request.Builder().url(WordHaiciMini.dictURL + "?q=" + this.word)
				.build();
		try {
			response = okHttpClient.newCall(request).execute();

			byte[] b = response.body().bytes();// 由于海词网站页面是gb2312,故需要一个字节数组来存储转码
			String str = new String(b, "gb2312");// 再用gb2312格式读取到一个字符串中
			this.responseHTML = new String(str.getBytes(), "UTF-8");// 拿到页面HTML字符串
		} catch (IOException e) {
			e.printStackTrace();
			wordJSONInfo.put("error", "连接到海词网站异常");
			return wordJSONInfo;
		}
		String dict_pagetoken[] = regexWith(responseHTML, "(?<=pagetoken=\").*(?=\")");
		RequestBody requestBody = new FormBody.Builder()
				.add("q", this.word)
				.add("s", "2")
				.add("t",Utility.md5(this.word+"dictcn"+dict_pagetoken[1]))
				.build();
		Request requestContent = new Request.Builder()
				.url(WordHaiciMini.dictURLReal)
				.header("Referer", WordHaiciMini.dictURL + "?q=" + this.word)
				.header("ISAJAX","yes")
				.header("Origin","http://apii.dict.cn")
				.header("Connection","keep-live")
				.header("Accept","*/*")
				.header("DNT","1")
				.header("HOST","apii.dict.cn")
				.header("Accept-Encoding","gzip,deflate")
				.header("Content-type","application/x-www-form-urlencoded")
				.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36")
				.post(requestBody)
				.build();
		Response responseContent;
		try {
			responseContent = okHttpClient.newCall(requestContent).execute();
			System.out.println(Utility.decodeUnicode(responseContent.body().string()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
