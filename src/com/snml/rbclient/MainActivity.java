package com.snml.rbclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.snml.inet.InetUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void refresh1(View view) {
		new AsyncTask<String, Void, Void>() {
			@Override
			protected Void doInBackground(String... params) {
				//refreshAsync();
				return null;
			}
		}.execute("");
	}
	
	private Activity self = this;
	
	public void refresh(View view) {
		//TextView textViewMain = (TextView) findViewById(R.id.textViewMain);
		//textViewMain.setText("Привет!");
		LinearLayout llRoutes = (LinearLayout) findViewById(R.id.LinearLayoutRoutes);
		/*for(int i = 0; i < 10; i++) {
			LinearLayout llRoute = new LinearLayout(this);
			//linearLayoutRoute.setOrientation(0);
			llRoutes.addView(llRoute);
			TextView twRoute = new TextView(this);
			twRoute.setText("Маршрут № " + i);
			llRoute.addView(twRoute);
			Button bRoute = new Button(this);
			bRoute.setText("Взять");
			llRoute.addView(bRoute);
		}*/
		int i = 1;
		int step = 0;
		// лог - begin
		LinearLayout llRoute = new LinearLayout(this);
		llRoutes.addView(llRoute);
		TextView twRoute = new TextView(this);
		twRoute.setText("START");
		llRoute.addView(twRoute);
		// лог - end
		String content = "";
		String urlStr = "http://m.rbtaxi.ru/order/";
		HttpURLConnection urlConnection = null;
		try {
			step = 1;
			/*URL url = new URL(urlStr);
			step = 2;
			urlConnection = (HttpURLConnection) url.openConnection();
			step = 3;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			step = 4;
			String line = null;
			while ((line = reader.readLine()) != null) {
				step = 5;
				content += line;
			}
			step = 6;
			*/
			new AsyncTask<String, Void, String>() {
				@Override
				protected String doInBackground(String... params) {
					String urlStr = params[0];
					String content = "";
					try {
						/*URL url = new URL(urlStr);
						HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
						BufferedReader reader = new BufferedReader(new InputStreamReader(
								urlConnection.getInputStream()));
						String line = null;
						while ((line = reader.readLine()) != null) {
							content += line;
						}*/
						content = InetUtils.getUrlContent(urlStr);
						if(content.indexOf("<table id=\"auth\" class=\"form\">") > -1) {
							// нужна авторизация
							//content = InetUtils.getUrlContent("http://m.rbtaxi.ru/order/?auth_login=27688&auth_password=89854652386");
							//String urlParameters = "fName=" + URLEncoder.encode("???", "UTF-8") + "&lName=" + URLEncoder.encode("???", "UTF-8");
							content = InetUtils.doPost("http://m.rbtaxi.ru/", "auth_login=27688&auth_password=89854652386");
							if(content.indexOf("<table id=\"auth\" class=\"form\">") == -1) {
								content = InetUtils.getUrlContent(urlStr);
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return e.toString();
					}
					return content;
				}
				@Override
				protected void onPostExecute(String result) {
					if((result == null) || (result.isEmpty())) {
						return;
					}
					String content = result;
					LinearLayout llRoutes = (LinearLayout) findViewById(R.id.LinearLayoutRoutes);
					// лог - begin
					LinearLayout llRoute = new LinearLayout(self);
					llRoutes.addView(llRoute);
					TextView twRoute = new TextView(self);
					//twRoute.setText("content = " + content.substring(0, 100));
					twRoute.setText("content = " + content);
					llRoute.addView(twRoute);
					// лог - end
					String tableDelimeter = "<table id=\"terminal\">";
					int tablePos = content.indexOf(tableDelimeter);
					if(tablePos > -1) {
						String tableContent = content.substring(tablePos + tableDelimeter.length());
						// лог - begin
						llRoute = new LinearLayout(self);
						llRoutes.addView(llRoute);
						twRoute = new TextView(self);
						twRoute.setText("tableContent = " + tableContent.substring(0, 100));
						llRoute.addView(twRoute);
						// лог - end
						/*String tableEndDelimeter = "</table>";
						int tableEndPos = tableContent.indexOf(tableEndDelimeter);
						if(tableEndPos > -1) {
							String routes = content.substring(0, tableEndPos - 1);
							// парсим маршруты
							String pattern = "<tbody class=\"order\" id=\"order-(\\d*)\">(.*)</tbody>";
							Pattern r = Pattern.compile(pattern);
							Matcher m = r.matcher(routes);
							while (m.find()) {
								String route_id = m.group(1);
								String route = m.group(2);
								if((route != null) && (!route.isEmpty())) {
									// время
									String time = null;
									String patternTime = "<td class=\"time\">.*<a href=\"/order/view/\\d*\">(.*)</a>";
									Pattern rTime = Pattern.compile(patternTime);
									Matcher mTime = rTime.matcher(route);
									if(mTime.find()) {
										time = mTime.group(1);
									}
									// адрес
									String address = null;
									String patternAddress = "<td class=\"address\">.*<a href=\"/order/view/\\d*\">(.*)</a>";
									Pattern rAddress = Pattern.compile(patternAddress);
									Matcher mAddress = rAddress.matcher(route);
									if(mAddress.find()) {
										address = mAddress.group(1);
									}
									// добавляем
									LinearLayout llRoute = new LinearLayout(this);
									llRoutes.addView(llRoute);
									TextView twRoute = new TextView(this);
									twRoute.setText("!!! - " + i + " - " + time + " - " + address);
									llRoute.addView(twRoute);
									Button bRoute = new Button(this);
									bRoute.setText("Взять");
									llRoute.addView(bRoute);
									i++;
								}
							}
						}*/
					}
				}
			}.execute(urlStr);
			//String[] table = content.split("<table id=\"terminal\">");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// лог - begin
			llRoute = new LinearLayout(this);
			llRoutes.addView(llRoute);
			twRoute = new TextView(this);
			twRoute.setText("Exception, toString = " + e.toString() + ", getCause = " + e.getCause() + ", step = " + step);
			llRoute.addView(twRoute);
			// лог - end
		}
	}
	
}
