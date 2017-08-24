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
	private JSONObject HaiciMiniJSON = new JSONObject();

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
		Request request = new Request.Builder().url(WordHaiciMini.dictURL + "?q=" + this.word).build();
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
		RequestBody requestBody = new FormBody.Builder().add("q", this.word).add("s", "2")
				.add("t", Utility.md5(this.word + "dictcn" + dict_pagetoken[1])).build();
		Request requestContent = new Request.Builder().url(WordHaiciMini.dictURLReal)
				.header("Referer", WordHaiciMini.dictURL + "?q=" + this.word).header("ISAJAX", "yes")
				.header("Origin", "http://apii.dict.cn").header("Connection", "keep-live").header("Accept", "*/*")
				.header("DNT", "1").header("HOST", "apii.dict.cn").header("Accept-Encoding", "gzip,deflate")
				.header("Content-type", "application/x-www-form-urlencoded")
				.header("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36")
				.post(requestBody).build();
		Response responseContent;
		try {
			responseContent = okHttpClient.newCall(requestContent).execute();
			HaiciMiniJSON = new JSONObject(Utility.decodeUnicode(responseContent.body().string()));// 返回的Unicode字符需要解码
		} catch (IOException e) {
			e.printStackTrace();
		}
		String e = HaiciMiniJSON.getString("e");
		String t = HaiciMiniJSON.getString("t");
		String s = HaiciMiniJSON.getString("s");
		//匹配词性和释义，这里由于写不出来正则表达式，故使用了indexOf曲线救国
//		wordProperty = regexWith(e, "(?<=>??)[a-z]{1,6}\\.");
//		wordValue = regexWith(e, "(?<=[>??][a-z]{1,6}\\.).*[<br]{0,1}");//这里的正则表达式写不出来
		String wordArray[] = e.split("<br />+");
		int i = 1;
		for(String a:wordArray) {
			int indexDot = a.indexOf(".");
			wordProperty[i] = a.substring(0, indexDot+1);
			wordValue[i] = a.substring(indexDot+1);
			i++;
		}
		i = 1 ;
		while (wordProperty[i] != null && wordValue[i] != null && i < 9) {
			System.out.println(wordProperty[i]);
			System.out.println(wordValue[i]);
			wordJSONInfo.put("词性" + i, wordProperty[i]);
			wordJSONInfo.put("释义" + i, wordValue[i]);
			i++;
		}
		//匹配
		
		
		System.out.println(HaiciMiniJSON);

		return wordJSONInfo;
	}

}
