package be.raphcho.httpslib;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.net.Uri;
import android.net.Uri.Builder;
import be.raphcho.httpslib.classes.HttpsParameter;

public class HttpsWorker {
	
	
	/*
	 * SIMPLIFIED METHODS
	 */
	public static String doDeleteToString(String URL, ArrayList<HttpsParameter> parameters) {
		return HttpUtils.getResponseBodyToString(doDelete(URL, parameters));
	}
	
	public static String doDeleteToString(String URL) {
		return HttpUtils.getResponseBodyToString(doDelete(URL));
	}
	public static String doPutToString(String URL, ArrayList<HttpsParameter> parameters) {
		return HttpUtils.getResponseBodyToString(doPut(URL, parameters));
	}
	
	public static String doPutToString(String URL) {
		return HttpUtils.getResponseBodyToString(doPut(URL));
	}
	
	public static String doPostToString(String URL, ArrayList<HttpsParameter> parameters) {
		return HttpUtils.getResponseBodyToString(doPost(URL, parameters));
	}
	
	public static String doPostToString(String URL) {
		return HttpUtils.getResponseBodyToString(doPost(URL));
	}
	
	public static String doGetToString(String URL) {
		return HttpUtils.getResponseBodyToString(doGet(URL));
	}
	
	public static String doGetToString(String URL, ArrayList<HttpsParameter> parameters) {
		return HttpUtils.getResponseBodyToString(doGet(URL, parameters));
	}
	
	/*
	 * PUT
	 */
	protected static HttpResponse doPut(String URL, ArrayList<HttpsParameter> parameters) {
		HttpClient httpClient = HttpUtils.getNewHttpClient();
		
		HttpPut posteur = new HttpPut(URL);
		
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.STRICT, null, Charset.forName("UTF-8"));
		if (parameters != null) {
			for (HttpsParameter param : parameters) {
				HttpUtils.addPart(reqEntity, param);
			}
		}
		posteur.setEntity(reqEntity);
		
		HttpResponse response = null;
		try {
			response = httpClient.execute(posteur);
		}
		catch (ClientProtocolException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	protected static HttpResponse doPut(String URL) {
		return doPut(URL, null);
	}
	
	
	/*
	 * 		DELETE
	 */
	
	protected static HttpResponse doDelete(String URL, ArrayList<HttpsParameter> parameters) {
		HttpClient httpClient = HttpUtils.getNewHttpClient();
		
		Builder uriBuilder = new Uri.Builder().encodedPath(URL);
		if (parameters != null) {
			for (HttpsParameter param : parameters)
				uriBuilder.appendQueryParameter(param.getName(), (String) param.getValue());
		}
		HttpDelete posteur = new HttpDelete(uriBuilder.build().toString());
		
		HttpResponse response = null;
		try {
			response = httpClient.execute(posteur);
		}
		catch (ClientProtocolException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	protected static HttpResponse doDelete(String URL) {
		return doDelete(URL,null);
	}
	
	/*
	 * POST
	 */
	protected static HttpResponse doPost(String URL, ArrayList<HttpsParameter> parameters) {
		HttpClient httpClient = HttpUtils.getNewHttpClient();
		
		HttpPost posteur = new HttpPost(URL);
		
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.STRICT, null, Charset.forName("UTF-8"));
		if (parameters != null) {
			for (HttpsParameter param : parameters) {
				HttpUtils.addPart(reqEntity, param);
			}
		}
		posteur.setEntity(reqEntity);
		
		HttpResponse response = null;
		try {
			response = httpClient.execute(posteur);
		}
		catch (ClientProtocolException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	protected static HttpResponse doPost(String URL) {
		return doPost(URL, null);
	}
	
	/*
	 * GET
	 */
	
	protected static HttpResponse doGet(String URL, ArrayList<HttpsParameter> parameters) {
		Builder uriBuilder = new Uri.Builder().encodedPath(URL);
		if (parameters != null) {
			for (HttpsParameter param : parameters)
				uriBuilder.appendQueryParameter(param.getName(), (String) param.getValue());
		}
		HttpClient httpClient = HttpUtils.getNewHttpClient();
		
		HttpGet getteur = new HttpGet(uriBuilder.build().toString());
		HttpResponse response = null;
		
		try {
			response = httpClient.execute(getteur);
		}
		catch (ClientProtocolException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	protected static HttpResponse doGet(String URL) {
		return doGet(URL, null);
	}
	
	
	public static String executePostUTF8(String url, List<NameValuePair> nvps) {
		AbstractHttpEntity ent;
		String response= "error";
		try {
			ent = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
			
			ent.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
			ent.setContentEncoding("UTF-8");
			HttpPost post = new HttpPost();
			post.setEntity(ent);
			post.setURI(new URI(url));
			HttpClient client = new DefaultHttpClient();
			HttpResponse res = client.execute(post);
			response = HttpUtils.getResponseBodyToString(res);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
	        e.printStackTrace();
		} catch (ClientProtocolException e) {
	        e.printStackTrace();
        } catch (IOException e) {
	        e.printStackTrace();
        }
		
		return response;
	}
}
