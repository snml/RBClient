package com.snml.inet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class InetUtils {
	
	private static String log;
	
	public static String getLog() {
		return log;
	}
	
	public static String sendPost(String urlParameters) throws UnknownHostException, IOException {
		// формируем заголовок
		String httpHeader = "POST / HTTP/1.1\n" +
			"Host: m.rbtaxi.ru\n" +
			"User-Agent: Mozilla/5.0 (Windows NT 5.1; rv:24.0) Gecko/20100101 Firefox/24.0\n" +
			"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
			"Accept-Language: ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3\n" +
			//"Accept-Encoding: gzip, deflate\n" +
			"Accept-Encoding:\n" +
			"Referer: http://m.rbtaxi.ru/\n" +
			"Connection: keep-alive\n" +
			"Content-Type: application/x-www-form-urlencoded\n" +
			"Content-Length: " + urlParameters.length() + "\n" +
			"\n" +
			urlParameters;
		// создаем сокет
		Socket socket = new Socket("m.rbtaxi.ru", 80);
		// отправляем заголовок
		socket.getOutputStream().write(httpHeader.getBytes());
		// читаем ответ
		InputStreamReader isr = new InputStreamReader(socket.getInputStream());
		BufferedReader bfr = new BufferedReader(isr);
		StringBuffer sbf = new StringBuffer();
		/*int ch = bfr.read();
		while (ch != -1) {
			sbf.append((char)ch);
			ch = bfr.read();
		}*/
		String line = null;
		while((line = bfr.readLine()) != null) {
			sbf.append(line);
			sbf.append('\n');
		}
		bfr.close();
		String res = sbf.toString();
		/*int pos = res.indexOf("\n\n");
		if(pos > -1) {
			return res.substring(pos + 2);
		}*/
		log = res;
		String find = "secret=";
		int pos = res.indexOf(find);
		if(pos > -1) {
			String resFind = res.substring(pos + find.length());
			find = ";";
			pos = resFind.indexOf(find);
			if(pos > -1) {
				return resFind.substring(0, pos);
			}
		}
		return null;
	}
	
	public static String sendGet(String targetURL, String secret) throws UnknownHostException, IOException {
		// формируем заголовок
		String httpHeader = "GET " + targetURL + " HTTP/1.1\n" +
			"Host: m.rbtaxi.ru\n" +
			"User-Agent: Mozilla/5.0 (Windows NT 5.1; rv:24.0) Gecko/20100101 Firefox/24.0\n" +
			"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
			"Accept-Language: ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3\n" +
			//"Accept-Encoding: gzip, deflate\n" +
			"Accept-Encoding:\n" +
			"Referer: http://m.rbtaxi.ru/\n" +
			"Cookie: a_rbt=1; id=27688; secret=" + secret + "\n" +
			"Connection: keep-alive\n" +
			"\n";
		// создаем сокет
		Socket socket = new Socket("m.rbtaxi.ru", 80);
		// отправляем заголовок
		socket.getOutputStream().write(httpHeader.getBytes());
		// читаем ответ
		InputStreamReader isr = new InputStreamReader(socket.getInputStream());
		BufferedReader bfr = new BufferedReader(isr);
		StringBuffer sbf = new StringBuffer();
		/*int ch = bfr.read();
		while (ch != -1) {
			sbf.append((char)ch);
			ch = bfr.read();
		}*/
		String line = null;
		while((line = bfr.readLine()) != null) {
			sbf.append(line);
			sbf.append('\n');
		}
		bfr.close();
		String res = sbf.toString();
		log = res;
		int pos = res.indexOf("\n\n");
		if(pos > -1) {
			return res.substring(pos + 2);
		}
		return null;
	}
	
	/*public static String getUrlContent(String urlStr) throws IOException {
		//String content = "";
		URL url = new URL(urlStr);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				urlConnection.getInputStream()));
		String line = null;
		StringBuffer response = new StringBuffer(); 
		while((line = reader.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}
		reader.close();
		return response.toString();
	}
	
	public static String doPost(String targetURL, String urlParameters) throws IOException {
	    URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      url = new URL(targetURL);
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("Content-Type", 
	           "application/x-www-form-urlencoded");
				
	      connection.setRequestProperty("Content-Length", "" + 
	               Integer.toString(urlParameters.getBytes().length));
	      connection.setRequestProperty("Content-Language", "en-US");  
				
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      //Send request
	      DataOutputStream wr = new DataOutputStream (
	                  connection.getOutputStream ());
	      wr.writeBytes (urlParameters);
	      wr.flush ();
	      wr.close ();

	      //Get Response	
	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      return response.toString();
	      
	    } catch (IOException e) {
	      e.printStackTrace();
	      //return null;
	      throw e;
	      
	    } finally {
	      if(connection != null) {
	        connection.disconnect();
	      }
	    }
	}*/
	
}
