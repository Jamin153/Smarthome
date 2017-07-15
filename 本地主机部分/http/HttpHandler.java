/**
 * HttpHandler类的作用是向服务器上传数据
 */
package com.zhenxuyang.smarthome.http;

import java.io.IOException;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @author Administrator
 * @Date 2017年7月9日
 */
public class HttpHandler {
	
	/**
	 * Http代理
	 */
	CloseableHttpClient httpClient;
	/**
	 * 发送请求的API
	 */
	String url=null;
	/**
	 * API页面
	 */
	static String hostAddr="http://120.24.186.82:8081/db.php";
	
	public HttpHandler() {
		httpClient=HttpClients.createDefault();
	}
	
	/**
	 * 以GET方式发送数据
	 * @param temp
	 * @param humi
	 */
	public void sendData(float temp,float humi) {
		
		url=hostAddr+"?"+"temp="+temp+"&humi="+humi;
		HttpGet httpGet=new HttpGet(url);
		
		try {
			CloseableHttpResponse httpResponse=httpClient.execute(httpGet);
			if(httpResponse.getStatusLine().getStatusCode()==200)
				System.out.println("请求成功");
			else
				System.out.println("请求失败");
			httpResponse.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
