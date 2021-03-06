package com.sparrow.opinion.utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("rawtypes")
public class HttpRequestProxy {
    /**
     * 连接超时
     */
    private static int connectTimeOut = 10000;

    /**
     * 读取数据超时
     */
    private static int readTimeOut = 60000;
    
    /**
     * 请求编码
     */
    private static String requestEncoding = "UTF-8";

    private static Logger logger = Logger.getLogger(HttpRequestProxy.class);
    
	public static String doGet(String reqUrl, Map parameters, String recvEncoding) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try {
            StringBuffer params = new StringBuffer();
            for (Iterator iter = parameters.entrySet().iterator(); iter.hasNext();) {
                Entry element = (Entry) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(), HttpRequestProxy.requestEncoding));
                params.append("&");
            }

            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            url_con.setConnectTimeout(HttpRequestProxy.connectTimeOut);
            url_con.setReadTimeout(HttpRequestProxy.readTimeOut);
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
            String crlf=System.getProperty("line.separator");
            while (tempLine != null) {
                temp.append(tempLine);
                temp.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = temp.toString();
            rd.close();
            in.close();
        }catch (IOException e) {
            logger.error("网络故障", e);
        }finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }

        return responseContent;
    }
	
	 public static String doGet(String reqUrl, String recvEncoding) {
		 HttpURLConnection url_con = null;
		 String responseContent = null;
		 try {
			 URL url = new URL(reqUrl);
			 url_con = (HttpURLConnection) url.openConnection();
			 url_con.setRequestMethod("GET");
			 url_con.setConnectTimeout(HttpRequestProxy.connectTimeOut);
			 url_con.setReadTimeout(HttpRequestProxy.readTimeOut);

			 InputStream in = url_con.getInputStream();
			 BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
			 String tempLine = rd.readLine();
			 StringBuffer temp = new StringBuffer();
			 String crlf=System.getProperty("line.separator");
			 while (tempLine != null) {
				 temp.append(tempLine);
				 temp.append(crlf);
				 tempLine = rd.readLine();
			 }
			 responseContent = temp.toString();
			 rd.close();
			 in.close();	
		 }catch (IOException e) {
			 logger.error("网络故障", e);
		 }finally {
			 if (url_con != null) {
				 url_con.disconnect();
			 }
		 }

		 return responseContent;
	 }
	 
	 public static String doGet(String reqUrl) {
		 return doGet(reqUrl, "UTF-8");
	 }
	 
	 public static String doGet(String reqUrl, Map parameters){
		 return doGet(reqUrl, parameters, "UTF-8");
	 }
	 
	 public static String doPost(String reqUrl, Map parameters, String recvEncoding) throws IOException {
		 HttpURLConnection url_con = null;
		 String responseContent = null;
		 try {
			 StringBuffer params = new StringBuffer();
			 for (Iterator iter = parameters.entrySet().iterator(); iter.hasNext();) {
				 Entry element = (Entry) iter.next();
				 params.append(element.getKey().toString());
				 params.append("=");
				 params.append(URLEncoder.encode(element.getValue().toString(), HttpRequestProxy.requestEncoding));
				 params.append("&");
			 }

			 if (params.length() > 0) {
				 params = params.deleteCharAt(params.length() - 1);
			 }

			 URL url = new URL(reqUrl);
			 url_con = (HttpURLConnection) url.openConnection();
			 url_con.setRequestMethod("POST");
			 url_con.setConnectTimeout(HttpRequestProxy.connectTimeOut);
//			 url_con.setReadTimeout(HttpRequestProxy.readTimeOut);
	         url_con.setDoOutput(true);
	         byte[] b = params.toString().getBytes();
	         url_con.getOutputStream().write(b, 0, b.length);
	         url_con.getOutputStream().flush();
	         url_con.getOutputStream().close();

	         InputStream in = url_con.getInputStream();
	         BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
	         String tempLine = rd.readLine();
	         StringBuffer tempStr = new StringBuffer();
	         String crlf=System.getProperty("line.separator");
	         while (tempLine != null){
	        	 tempStr.append(tempLine);
	             tempStr.append(crlf);
	             tempLine = rd.readLine();
	         }
	         responseContent = tempStr.toString();
	         rd.close();
	         in.close();
		 }catch (IOException e) {
//			 logger.error("网络故障", e);
			 throw e;
		 }finally {
			 if (url_con != null){
				 url_con.disconnect();
			 }
		 }
		 return responseContent;
	 }

	 public static int getConnectTimeOut() {
		 return connectTimeOut;
	 }
	
	 public static void setConnectTimeOut(int connectTimeOut) {
		 HttpRequestProxy.connectTimeOut = connectTimeOut;
	 }
	
	 public static int getReadTimeOut() {
		 return readTimeOut;
	 }
	
	 public static void setReadTimeOut(int readTimeOut) {
		 HttpRequestProxy.readTimeOut = readTimeOut;
	 }
	
	 public static String getRequestEncoding() {
		 return requestEncoding;
	 }
	
	 public static void setRequestEncoding(String requestEncoding) {
		 HttpRequestProxy.requestEncoding = requestEncoding;
	 }
}
