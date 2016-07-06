package bennett.base.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class HttpClientTool {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(HttpClientTool.class);

	public static String Post(String url, List<NameValuePair> formparams) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String json = "";
		HttpPost httppost = new HttpPost(url);

		// List<namevaluepair> formparams = new ArrayList<namevaluepair>();
		// formparams.add(new BasicNameValuePair("username", "admin"));
		// formparams.add(new BasicNameValuePair("password", "123456"));
		// formparams.add(new BasicNameValuePair("type", "house"));
		UrlEncodedFormEntity uefEntity;
		try {
			if (formparams != null) {
				uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
				httppost.setEntity(uefEntity);
			}
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return EntityUtils.toString(entity);
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	public static String get(String url) {
		String json = "";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// 创建httpget.
			HttpGet httpget = new HttpGet(url);
			// 执行get请求.
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return EntityUtils.toString(entity, "utf-8");
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	public static InputStream getImg(String url) {
		InputStream is = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// 创建httpget.
			HttpGet httpget = new HttpGet(url);
			// 执行get请求.
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					if (entity.getContentType().getValue().contains("image")) {
						is = entity.getContent();
					}

				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return is;
	}

	/**
	 * 发送postxml
	 * 
	 * @param strUrl
	 * @param xml
	 * @return
	 */
	public static String postXml(String strUrl, String xml) {
		try {
			LOGGER.debug("in post xml");
			// String url="https://api.mch.weixin.qq.com/pay/unifiedorder"
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(30000); // 设置连接主机超时（单位：毫秒)
			conn.setReadTimeout(30000); // 设置从主机读取数据超时（单位：毫秒)
			conn.setDoOutput(true); // post请求参数要放在http正文内，顾设置成true，默认是false
			conn.setDoInput(true); // 设置是否从httpUrlConnection读入，默认情况下是true
			conn.setUseCaches(false); // Post 请求不能使用缓存
			// 设定传送的内容类型是可序列化的java对象(如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestMethod("POST");// 设定请求的方法为"POST"，默认是GET
			conn.setRequestProperty("Content-Length", xml.length() + "");
			String encode = "utf-8";
			OutputStreamWriter out = new OutputStreamWriter(
					conn.getOutputStream(), encode);
			out.write(xml);
			out.flush();
			out.close();
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				LOGGER.debug(String.valueOf(conn.getResponseCode()));
				return null;
			}
			// 获取响应内容体
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line = "";
			StringBuffer strBuf = new StringBuffer();
			while ((line = in.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			in.close();
			return strBuf.toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void postImage(String strUrl, MultipartFile file) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setChunkedStreamingMode(1024 * 1024);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Charsert", "UTF-8");
	
			conn.setRequestProperty("Content-Type", "multipart/form-data;media="
					+ file.getName());
			conn.setRequestProperty("filename", file.getName());
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			DataInputStream in = new DataInputStream(file.getInputStream());
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		}

	}

	public static String postJson(String url, String json) {
		LOGGER.info(url);
		LOGGER.info(json);
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		try {
			StringEntity s = new StringEntity(json, "UTF-8");
			s.setContentType("application/json;charset=UTF-8");
			s.setContentEncoding("UTF-8");
			post.setEntity(s);

			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = res.getEntity();
				if (entity != null) {
					return EntityUtils.toString(entity, "utf-8");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return "";
	}

	public static void main(String asg[]) {

	}
}
