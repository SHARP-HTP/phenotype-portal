package edu.mayo.phenoportal.server.upload;

import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static org.junit.Assert.fail;

public class ImportServletTestIT {

	@Test
	public void importTest() throws Exception {
		String url = "http://127.0.0.1:8888/htp/algorithmimport";
		String charset = "UTF-8";
		String query = String.format("gwt.codesvr=%s&userId=%s&password=%s",
		  URLEncoder.encode("127.0.0.1:9997", charset),
		  URLEncoder.encode("admin", charset),
		  URLEncoder.encode("admin", charset));

		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setConnectTimeout(0);
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Accept-Charset", charset);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("charset", charset);
		connection.setRequestProperty("Content-Length", Integer.toString(query.length()));
		connection.setUseCaches(false);
		connection.connect();
		OutputStream output = connection.getOutputStream();
		output.write(query.getBytes(charset));
		output.flush();

		InputStream response;
		if (connection.getResponseCode() == 200) {
			response = connection.getInputStream();
			System.out.println(response.toString());

		}
		else {
			fail();
		}
	}

}
