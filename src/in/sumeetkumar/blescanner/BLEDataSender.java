package in.sumeetkumar.blescanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class BLEDataSender {

	private final static String BASE_URL = "http://10.15.2.15:1337/";
	private final static String STATUS_URL = BASE_URL + "latest?ticket=";
	private final static String OPEN_SENSE_URL = "http://api.sen.se/events/?sense_key=RMSHIf10oCKD22_NINkGs";
	
	public static String getData(String ticketNo){
		String result = "Status not available";
		try {
			
			String url = STATUS_URL + ticketNo;
			System.out.println("Get status request url " + url);
			result = connect( url);
			System.out.println("Get status response " + result);
	 } catch (Exception e) {
         e.printStackTrace();
     }
		return result;
	}
	public static void sendData(WebRequestData data) {

		final WebRequestData dataCopy = data;
		final TagToTicketMap tagTicketMap = new TagToTicketMap();
		
		Thread thread = new Thread(new Runnable(){
		    @Override
		    public void run() {
		        try {
		    		StringBuilder baseURL = new StringBuilder( BASE_URL +"new?");
		    		//StringBuilder baseURL = new StringBuilder("http://oscarsandoval-datacol.nodejitsu.com/new?");
		    		
		    		baseURL.append("timestamp=" + dataCopy.timeStamp);
		    		baseURL.append("&ticket=" + URLEncoder.encode( tagTicketMap.getTicket(dataCopy.tagId), "UTF-8"));
		    		baseURL.append("&phoneid=" + URLEncoder.encode(dataCopy.phoneId, "UTF-8"));
		    		baseURL.append("&tagid=" +URLEncoder.encode( dataCopy.tagId, "UTF-8"));
		    		baseURL.append("&tagname=" + URLEncoder.encode( dataCopy.tagUniqueName, "UTF-8"));
		    		baseURL.append("&rssi=" + dataCopy.tagSignalStrength);
		    		
		    		connect( baseURL.toString());

		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		});

		thread.start(); 
		
	}

	public static String connect(String url) {

		HttpClient httpclient = new DefaultHttpClient();

		System.out.println(url);
		// Prepare a request object
		HttpGet httpget = new HttpGet(url);

		// Execute the request
		HttpResponse response;
		String returnDataString="";
		String result = "";
		try {
			response = httpclient.execute(httpget);
			returnDataString = response.getStatusLine().toString();
			// Get hold of the response entity
	        HttpEntity entity = response.getEntity();
	        // If the response does not enclose an entity, there is no need
	        // to worry about connection release

	        if (entity != null) {

	            // A Simple JSON Response Read
	            InputStream instream = entity.getContent();
	            result= convertStreamToString(instream);
	            // now you have the string representation of the HTML request
	            instream.close();
	            System.out.println("responce received " + result);
	        }
			

		} catch (Exception e) {
			System.out.println("Exception" + e.getMessage());
		}
		
		return result;
	}
	
	private static String convertStreamToString(InputStream is) {
	    /*
	     * To convert the InputStream to String we use the BufferedReader.readLine()
	     * method. We iterate until the BufferedReader return null which means
	     * there's no more data to read. Each line will appended to a StringBuilder
	     * and returned as String.
	     */
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}

}
