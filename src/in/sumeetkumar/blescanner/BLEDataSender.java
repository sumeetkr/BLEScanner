package in.sumeetkumar.blescanner;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class BLEDataSender {

	public static void sendData(WebRequestData data) {

		final WebRequestData dataCopy = data;
		Thread thread = new Thread(new Runnable(){
		    @Override
		    public void run() {
		        try {
		    		StringBuilder baseURL = new StringBuilder("http://10.69.6.38:1337/new?");
		    		//StringBuilder baseURL = new StringBuilder("http://oscarsandoval-datacol.nodejitsu.com/new?");
		    		
		    		baseURL.append("timestamp=" + dataCopy.timeStamp);
		    		baseURL.append("&ticket=" + URLEncoder.encode( dataCopy.tagUniqueName));
		    		baseURL.append("&phoneid=" + URLEncoder.encode(dataCopy.phoneId));
		    		baseURL.append("&tagid=" +URLEncoder.encode( dataCopy.tagId));
		    		baseURL.append("&tagname=" + URLEncoder.encode( dataCopy.tagUniqueName));
		    		baseURL.append("&rssi=" + dataCopy.tagSignalStrength);
		    		
		    		connect( baseURL.toString());

		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		});

		thread.start(); 
		
	}

	public static void connect(String url) {

		HttpClient httpclient = new DefaultHttpClient();

		System.out.println(url);
		// Prepare a request object
		HttpGet httpget = new HttpGet(url);

		// Execute the request
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			// Examine the response status
			//Log.i("Http response", response.getStatusLine().toString());
			System.out.println(response.toString());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
