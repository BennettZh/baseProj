package bennett.base.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import bennett.base.redis.JedisTemplate;
import bennett.base.redis.RedisCacheManager;


public class WechatUtil {
	public final static String TOKEN_WX = "token_wx";
	public final static String JSTOKEN_WX = "jstoken_wx";
	public final static String APPUSER_WX = "appuser_wx";
	public final static String OPENID_WX = "openid_wx";
	private final static String APPID = "";
	private final static String SECRET = "";
	private final static String MCHID = "";
	private final static String KEY = "";

	private static WechatUtil wxConfig;
	private WebApplicationContext webApplicationContext;

	private WechatUtil(WebApplicationContext webApplicationContext) {
		this.webApplicationContext = webApplicationContext;
	}


	public static WechatUtil getWxConfigInstance(
			WebApplicationContext webApplicationContext) {
		if (wxConfig == null) {
			wxConfig = new WechatUtil(webApplicationContext);
		}
		return wxConfig;
	}

	public static void main(String args[]) {
		

	}
	
	//网页端调起支付API
	public Map<String,String> jsPayForOrder(String openid,String total_fee,String out_trade_no,String notify_url){
		return createUniteOrder(openid, total_fee, out_trade_no, notify_url);
		
	}
	//获取统一订单加网页端调起支付API
	public  Map<String,String> createUniteOrder(String openid,String total_fee,String out_trade_no,String notify_url){
		String url="https://api.mch.weixin.qq.com/pay/unifiedorder";
		String[] paramArr = new String[] { "appid="+APPID+"",
				"mch_id="+MCHID+"", "device_info=WEB",
				"body=","nonce_str="+create_nonce_str()+"",
				"notify_url="+notify_url+"","openid="+openid+"",
				"out_trade_no="+out_trade_no+"","spbill_create_ip=[IP]",
				"total_fee="+total_fee+"","trade_type=JSAPI"
		        };
		String sign=createSign(paramArr);
		StringBuilder buid= new StringBuilder();
		buid.append("<xml>");
		for(int i=0;i<paramArr.length;i++){
			String []str=paramArr[i].split("=");
			buid.append("<"+str[0]+">"+str[1]+"</"+str[0]+">");
		}
		buid.append("<sign>"+sign+"</sign>");
		buid.append("</xml>");
		String xmlString=HttpClientTool.postXml(url,buid.toString());
		Map<String,String> map=parseXml(xmlString);
		if(map.get("return_code").equals("SUCCESS")&&map.get("result_code").equals("SUCCESS")){
			
			String timeStamp=create_timestamp();
			String nonceStr=create_nonce_str();
			String packageValue="prepay_id="+map.get("prepay_id");
			String signType="MD5";
			
			String[] paramA = new String[] {
					"appId="+APPID+"","timeStamp="+timeStamp+"",
					"nonceStr="+nonceStr+"",
					"package="+packageValue+"","signType="+signType+""
			        };
			String paySign=createPaySign(paramA);
			Map<String,String> result= new HashMap<String,String>();
			result.put("appId", APPID);
			result.put("timeStamp", timeStamp);
			result.put("nonceStr", nonceStr);
			result.put("packageValue", packageValue);
			result.put("signType", signType);
			result.put("paySign", paySign);

			return result;
		}
		return null;
	}

	public static void createMenu() {

	}

	// 页面加载js-sdk
	public Map<String, String> JsoAuth(String url) {
		Map<String, String> ret = new HashMap<String, String>();
		String accstoken = getToken();
		if ("".equals(accstoken)) {
			return null;
		}
		String jsapiTicket = getJsapiTicket(accstoken);
		String timestamp = create_timestamp();
		String nonce_str = create_nonce_str();
		String signature = "";

		String[] paramArr = new String[] { "jsapi_ticket=" + jsapiTicket + "",
				"timestamp=" + timestamp + "", "noncestr=" + nonce_str + "",
				"url=" + url + "" };
		Arrays.sort(paramArr);

		String content = paramArr[0].concat("&" + paramArr[1])
				.concat("&" + paramArr[2]).concat("&" + paramArr[3]);
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			// 对拼接后的字符串进行 sha1 加密
			byte[] digest = md.digest(content.toString().getBytes());
			signature = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		ret.put("url", url);
		ret.put("jsapi_ticket", jsapiTicket);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);
		ret.put("appid", APPID);
		return ret;
	}

	// 获取jstoken
	public String getJsapiTicket(String token) {
		JedisTemplate jedisTemplate=((RedisCacheManager) webApplicationContext
				.getBean("redisCacheManager")).getJedisTemplate();
		String jstoken=jedisTemplate.get(JSTOKEN_WX);
		if(jstoken==null||"".equals(jstoken)){
			String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
					+ token + "&type=jsapi";
			// 获取openID
			String result = HttpClientTool.get(url);
			Gson gson = new Gson();
			Map<String, String> map = gson.fromJson(result,
					new TypeToken<Map<String, String>>() {
					}.getType());
			if (map != null) {
				jedisTemplate.setnxex(JSTOKEN_WX, map.get("ticket"), 7000);
				jstoken= map.get("ticket");
			}
		}
		return jstoken;

	}

	// 获取token
	public String getToken() {
		JedisTemplate jedisTemplate=((RedisCacheManager) webApplicationContext
				.getBean("redisCacheManager")).getJedisTemplate();
		String token=jedisTemplate.get(TOKEN_WX);
		if(token==null||"".equals(token)){
			
			String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
					+ APPID + "&secret=" + SECRET + "";
			// 获取openID
			String result = HttpClientTool.get(url);
			Gson gson = new Gson();
			Map<String, String> map = gson.fromJson(result,
					new TypeToken<Map<String, String>>() {
					}.getType());
			if (map != null) {
				jedisTemplate.setnxex(TOKEN_WX, map.get("access_token"), 7000);
				token= map.get("access_token");
			}
			
		}
		return token;
		
	}

	/**
	 * 网页oAuth2.0授权自动授权
	 * 
	 * @return
	 */
	public Map<String, String> oAuthTo(String code, String state) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ APPID + "&secret=" + SECRET + "&code=" + code
				+ "&grant_type=authorization_code";
		// 获取openID
		String result = HttpClientTool.get(url);
		Gson gson = new Gson();
		Map<String, String> map = gson.fromJson(result,
				new TypeToken<Map<String, String>>() {
				}.getType());
		if (map != null) {
			return map;
		}
		return null;
	}
	
	
/************************支付****************/
	private static String createSign(String[] paramArr) {
		Arrays.sort(paramArr);
		String stringA = paramArr[0].concat("&" + paramArr[1])
				.concat("&" + paramArr[2]).concat("&" + paramArr[3]).concat("&" + paramArr[4])
				.concat("&" + paramArr[5]).concat("&" + paramArr[6]).concat("&" + paramArr[7])
				.concat("&" + paramArr[8]).concat("&" + paramArr[9]).concat("&" + paramArr[10]);
		String stringSignTemp=stringA+"&key="+KEY;
		String sign=MD5(stringSignTemp).toUpperCase();
		return sign;
	}
	private static String createPaySign(String[] paramArr) {
		Arrays.sort(paramArr);
		String stringA = paramArr[0].concat("&" + paramArr[1])
				.concat("&" + paramArr[2]).concat("&" + paramArr[3]).concat("&" + paramArr[4]);
		String stringSignTemp=stringA+"&key="+KEY;
		String paySign=MD5(stringSignTemp).toUpperCase();
		//String sign =MD5Encode(stringSignTemp, "UTF-8").toUpperCase();
		return paySign;
	}
	 public  static String MD5(String s) {
	        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
	        try {
	            byte[] btInput = s.getBytes("UTF-8");
	            // 获得MD5摘要算法的 MessageDigest 对象
	            MessageDigest mdInst = MessageDigest.getInstance("MD5");
	            // 使用指定的字节更新摘要
	            mdInst.update(btInput);
	            // 获得密文
	            byte[] md = mdInst.digest();
	            // 把密文转换成十六进制的字符串形式
	            int j = md.length;
	            char str[] = new char[j * 2];
	            int k = 0;
	            for (int i = 0; i < j; i++) {
	                byte byte0 = md[i];
	                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	                str[k++] = hexDigits[byte0 & 0xf];
	            }
	            return new String(str);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	 

	/************************支付end****************/
	
	
	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param byteArray
	 * @return
	 */
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

	/**
	 * 将字节转换为十六进制字符串
	 * 
	 * @param mByte
	 * @return
	 */
	private static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
				'b', 'c', 'd', 'e', 'f' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];
		String s = new String(tempArr);
		return s;
	}

	private static String create_nonce_str() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	private static String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	public static Map<String,String> parseXml(String xmlString) {
		Map<String,String> map= new HashMap<String,String>();
		try {
			Document doc = DocumentHelper.parseText(xmlString);
			Element root = doc.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> elements = root.elements();
			for (Element element : elements) {
				map.put(element.getName(), element.getStringValue());
				//System.out.println(element.getText());
				//System.out.println(element.getName());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 获取微信用户的个人相关信息
	 * 
	 * @return
	 */
	public String  oAuthToUserImage(String openid) {
		String  userimage="";
		String  accesstocken=getToken();
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accesstocken+"&openid="+openid+"&lang=en";
		// 获取openID
		String result = HttpClientTool.get(url);
		
		Gson gson = new Gson();
		Map<String, Object> map = gson.fromJson(result,
				new TypeToken<Map<String, Object>>() {
				}.getType());
		if(map!=null){
		if(map.get("headimgurl")!=null){
			userimage=map.get("headimgurl").toString();
			return	userimage;	
		}
		return	userimage="head.png";
		}else{
			return	userimage="head.png";	
		}
		
		
	}
	/**
	 * 获取微信用户的个人相关信息
	 * 
	 * @return
	 */
	public String  oAuthToUserNickName(String openid,String phone) {
		String  nickname="";
		String  accesstocken=getToken();
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accesstocken+"&openid="+openid+"&lang=en";
		// 获取openID
		String result = HttpClientTool.get(url);
		
		Gson gson = new Gson();
		Map<String, Object> map = gson.fromJson(result,
				new TypeToken<Map<String, Object>>() {
				}.getType());
		if(map!=null){
		if(map.get("nickname")!=null){
			nickname=map.get("nickname").toString();
			return	nickname;	
		}
		return	nickname=phone;
		}else{
			return	nickname=phone;	
		}
		
		
	}
	/**
	 * 获取微信带参数的二维码ticket
	 * @return
	 */
	public String getTicket(String scene_str){
		String token=getToken();
		String param="{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \""+scene_str+"\"}}}";
		String url="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+token;
		String res=HttpClientTool.postJson(url, param);
		return res;
	}
  
}
