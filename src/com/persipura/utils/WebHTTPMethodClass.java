package com.persipura.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class WebHTTPMethodClass {
	static int mTimeoutConnection = 90000;

	public static String httpGetService(String serviceName, String param) {
		String result = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet getMethod = new HttpGet(AppConstants.BASE_URL + serviceName
					+ "?" + param);
			Log.e(serviceName + " GetURL = ", AppConstants.BASE_URL
					+ serviceName + "?" + param);
			BufferedReader in = null;
			BasicHttpResponse httpResponse = (BasicHttpResponse) httpclient
					.execute(getMethod);
			if (httpResponse.getStatusLine().getStatusCode() == 401) {
				Log.i("Response Json Failure: 401",
						"" + httpResponse.toString());
				AppConstants.ERROR401 = httpResponse.getStatusLine()
						.getStatusCode() + "";
			} else
				Log.i("Response Json 401 not fount", ""
						+ "httpResponse.toString()");

			in = new BufferedReader(new InputStreamReader(httpResponse
					.getEntity().getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
			result = sb.toString();
			System.out.println(serviceName + " result = " + result);
			// result = checkFor401Error(httpResponse, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String httpGetServiceWithoutparam(String serviceName) {
		String result = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet getMethod = new HttpGet(AppConstants.BASE_URL + serviceName);
			Log.e(serviceName + " GetURL = ", AppConstants.BASE_URL
					+ serviceName);
			BufferedReader in = null;
			BasicHttpResponse httpResponse = (BasicHttpResponse) httpclient
					.execute(getMethod);
			if (httpResponse.getStatusLine().getStatusCode() == 401) {
				Log.i("Response Json Failure: 401",
						"" + httpResponse.toString());
				AppConstants.ERROR401 = httpResponse.getStatusLine()
						.getStatusCode() + "";
			} else
				Log.i("Response Json 401 not fount", ""
						+ "httpResponse.toString()");

			in = new BufferedReader(new InputStreamReader(httpResponse
					.getEntity().getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
			result = sb.toString();
			System.out.println(serviceName + " result = " + result);
			// result = checkFor401Error(httpResponse, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String httpDeleteService(String serviceName, String param) {
		String result = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpDelete getMethod = new HttpDelete(AppConstants.BASE_URL
					+ serviceName + "?" + param);
			Log.e(serviceName + " deleteURL = ", AppConstants.BASE_URL
					+ serviceName + "?" + param);
			BufferedReader in = null;
			BasicHttpResponse httpResponse = (BasicHttpResponse) httpclient
					.execute(getMethod);
			if (httpResponse.getStatusLine().getStatusCode() == 401) {
				Log.i("Response Json Failure:401 ",
						"" + httpResponse.toString());
				AppConstants.ERROR401 = httpResponse.getStatusLine()
						.getStatusCode() + "";
			} else
				Log.i("Response Json 401 not fount", ""
						+ "httpResponse.toString()");
			in = new BufferedReader(new InputStreamReader(httpResponse
					.getEntity().getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
			result = sb.toString();
			System.out.println(serviceName + " result = " + result);
			// result = checkFor401Error(httpResponse, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String executeHttPost(String serviceName,
			List<NameValuePair> mParams) {
		String responseData = null;

		HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				mTimeoutConnection);

		DefaultHttpClient client = new DefaultHttpClient(httpParameters);

		SchemeRegistry registry = new SchemeRegistry();
		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		socketFactory
				.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		registry.register(new Scheme("https", socketFactory, 443));
		SingleClientConnManager mgr = new SingleClientConnManager(
				client.getParams(), registry);
		DefaultHttpClient httpClient = new DefaultHttpClient(mgr,
				client.getParams());

		// Set verifier
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

		// DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		HttpPost post = new HttpPost(serviceName);
		
		try {
			post.setEntity(new UrlEncodedFormEntity(mParams/* , HTTP.UTF_8 */));
			HttpResponse response = httpClient.execute(post);
			responseData = EntityUtils.toString(response.getEntity());
			if (response.getStatusLine().getStatusCode() == 401) {
				Log.i("Response Json Failure:401 ",
						"" + responseData.toString());
				AppConstants.ERROR401 = response.getStatusLine()
						.getStatusCode() + "";
			} else
				Log.i("Response Json 401 not fount", ""
						+ "httpResponse.toString()");
			responseData = responseData.trim();
			// responseData = checkFor401Error(response, responseData);
		} catch (Exception e) {
			responseData = "UnsupportedEncodingException";
			if (e != null)
				e.printStackTrace();
		}
		System.out.println(serviceName + " result = " + responseData);
		return responseData;
	}

	public static String postAppJsonStringToServer(String jsonString, String URL) {
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(),
				mTimeoutConnection);
		HttpResponse httpresponse;
		String response = "";
		try {
			HttpPost post = new HttpPost(URL);
			Log.e("jsonRequest:", jsonString);
			Log.e("URL:", URL);
			StringEntity se = new StringEntity(jsonString);
			post.setHeader("Content-type", "application/json");

			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			post.setEntity(se);
			httpresponse = client.execute(post);
			response = EntityUtils.toString(httpresponse.getEntity()).trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(URL + " result = " + response);
		return response;
	}
}
