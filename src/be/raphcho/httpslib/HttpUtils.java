package be.raphcho.httpslib;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ParseException;
import be.raphcho.httpslib.classes.EasySSLSocketFactory;
import be.raphcho.httpslib.classes.HttpsParameter;

public class HttpUtils {

	public static HttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new EasySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 30000);
			HttpConnectionParams.setSoTimeout(params, 30000);
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	public static String getResponseBodyToString(HttpResponse response) {

		String response_text = null;
		HttpEntity entity = null;

		try {
			entity = response.getEntity();
			response_text = _getResponseBody(entity);
		} catch (ParseException e) {
			e.printStackTrace();

		} catch (IOException e) {

			if (entity != null) {
				try {
					entity.consumeContent();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return response_text;

	}

	private static String _getResponseBody(final HttpEntity entity) throws IOException, ParseException {

		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}

		InputStream instream = entity.getContent();

		if (instream == null) {
			return "";
		}

		if (entity.getContentLength() > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(

			"HTTP entity too large to be buffered in memory");
		}

		String charset = getContentCharSet(entity);

		if (charset == null) {
			charset = HTTP.DEFAULT_CONTENT_CHARSET;
		}

		Reader reader = new InputStreamReader(instream, charset);

		StringBuilder buffer = new StringBuilder();

		try {

			char[] tmp = new char[1024];

			int l;

			while ((l = reader.read(tmp)) != -1) {
				buffer.append(tmp, 0, l);
			}

		} finally {
			reader.close();
		}

		return buffer.toString();

	}

	public static String getContentCharSet(final HttpEntity entity) throws ParseException {

		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}

		String charset = null;

		if (entity.getContentType() != null) {

			HeaderElement values[] = entity.getContentType().getElements();

			if (values.length > 0) {

				NameValuePair param = values[0].getParameterByName("charset");

				if (param != null) {
					charset = param.getValue();
				}

			}

		}

		return charset;

	}

	public static void addPart(MultipartEntity reqEntity, HttpsParameter param) {
	    Object value= param.getValue();
	    String name = param.getName();
	    String extraName = param.getExtraName();
	    
		if (value instanceof File) {
			reqEntity.addPart(name, new FileBody((File) value));
		}
		if (value instanceof Bitmap) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			((Bitmap) value).compress(CompressFormat.JPEG, 100, bos);
			byte[] data = bos.toByteArray();
			ByteArrayBody bab = new ByteArrayBody(data, extraName + ".jpg");
			reqEntity.addPart(name, bab);
		}
		else{
		    try {
                reqEntity.addPart(name, new StringBody(String.valueOf(value), Charset.forName(HTTP.UTF_8)));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
		}

	}

	public static HttpsParameter addObjectParameter(String objectName, String valueField, Object value, String extraName) {
		String paramStart = "[";
		String paramEnd = "]";
		return new HttpsParameter(objectName + paramStart + valueField + paramEnd, value, extraName);
	}
	
	public static HttpsParameter addObjectParameter(String objectName, String valueField, Object value) {
		String paramStart = "[";
		String paramEnd = "]";
		return new HttpsParameter(objectName + paramStart + valueField + paramEnd, value);
	}
	
	public static ArrayList<HttpsParameter> getParametersForObject(String name, Object obj) {
		ArrayList<HttpsParameter> parameters = new ArrayList<HttpsParameter>();
		
		String paramStart = "[";
		String paramEnd = "]";
		
		Field[] fields = obj.getClass().getFields();
		for (Field field : fields) {
			try {
				if (field.get(obj) instanceof String || field.get(obj) instanceof String)
					parameters.add(new HttpsParameter(name + paramStart + field.getName() + paramEnd, field.get(obj)));
				
				if (field.get(obj) instanceof List) {
					List<?> liste = (List<?>) field.get(obj);
					for (int i = 0; i < liste.size(); i++) {
						parameters.add(new HttpsParameter(name + paramStart + field.getName() + paramEnd + paramStart + i + paramEnd, liste.get(i)));
					}
				}
				else {
					parameters.add(new HttpsParameter(name + paramStart + field.getName() + paramEnd, field.get(obj)));
				}
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return parameters;
	}
}