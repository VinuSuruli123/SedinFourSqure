package com.sedin.task.utils;

import java.io.BufferedInputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;

public class HttpConnection {
	public static String httpConnection(String url) {

		StringBuffer buffer_string = new StringBuffer(url);
		String replyString = "";
		Log.v("url", "========" + url);
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(buffer_string.toString());

		try {

			HttpResponse response = httpclient.execute(httpget);
			InputStream is = response.getEntity().getContent();

			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(20);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			replyString = new String(baf.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// trim the whitespaces
		return replyString.trim();
	}


}
