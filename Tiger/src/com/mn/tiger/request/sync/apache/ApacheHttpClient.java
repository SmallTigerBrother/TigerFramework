package com.mn.tiger.request.sync.apache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;

import android.R.integer;
import android.content.Context;

import com.mn.tiger.request.error.TGHttpError;
import com.mn.tiger.request.sync.receiver.TGHttpResult;
import com.mn.tiger.utility.CR;

public class ApacheHttpClient
{
	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8 * 1024; // 8KB
	private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	private static final String ENCODING_GZIP = "gzip";

	private static int MAX_CONNECTIONS = 10; // http请求最大并发连接数
	private static int SOCKET_TIME_OUT = 30 * 1000; // 超时时间，默认10秒
	private static int MAX_RETRIES = 5;// 错误尝试次数，错误异常表请在RetryHandler添加
	
	private String charset = "utf-8";
	
	private HttpContext httpContext;

	private DefaultHttpClient httpClient;
	
	private int executionCount = 0;
	
	private Context context;
	
	public ApacheHttpClient(Context context)
	{
		this.context = context;
		
		BasicHttpParams httpParams = initHttpParams();

		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
		
		httpClient = new DefaultHttpClient(cm, httpParams);
		httpClient.addRequestInterceptor(new HttpRequestInterceptor()
		{
			public void process(HttpRequest request, HttpContext context)
			{
				addGZIPProperty(request);
			}
		});

		httpClient.addResponseInterceptor(new HttpResponseInterceptor()
		{
			public void process(HttpResponse response, HttpContext context)
			{
				decodingGZIP(response);
			}
		});

		httpClient.setHttpRequestRetryHandler(new RetryHandler(MAX_RETRIES));
		
		httpContext = new SyncBasicHttpContext(new BasicHttpContext());
	}

	private BasicHttpParams initHttpParams()
	{
		BasicHttpParams httpParams = new BasicHttpParams();

		ConnManagerParams.setTimeout(httpParams, SOCKET_TIME_OUT);
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(MAX_CONNECTIONS));
		ConnManagerParams.setMaxTotalConnections(httpParams, MAX_CONNECTIONS);

		HttpConnectionParams.setSoTimeout(httpParams, SOCKET_TIME_OUT);
		HttpConnectionParams.setConnectionTimeout(httpParams, SOCKET_TIME_OUT);
		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);
		return httpParams;
	}
	
	public TGHttpResult execute(ApacheHttpMethod httpMethod)
	{
		TGHttpResult httpResult = initHttpResult();
		boolean retry = true;
		IOException cause = null;
		HttpRequestRetryHandler retryHandler = httpClient.getHttpRequestRetryHandler();
		while (retry)
		{
			try
			{
				HttpResponse response = httpClient.execute(httpMethod.createHttpRequest(), httpContext);
				return handleEntity(response.getStatusLine().getStatusCode(), response.getEntity(), charset);
			}
			catch (UnknownHostException e)
			{
				httpResult.setResult(TGHttpError.getDefaultErrorMsg(context, TGHttpError.ERROR_URL));
				retry = retryHandler.retryRequest(cause, ++executionCount, httpContext);
			}
			catch (IOException e)
			{
				httpResult.setResult(TGHttpError.getDefaultErrorMsg(context, TGHttpError.IOEXCEPTION));
				retry = retryHandler.retryRequest(cause, ++executionCount, httpContext);
			}
			catch (NullPointerException e)
			{
				// HttpClient 4.0.x 之前的一个bug
				// http://code.google.com/p/android/issues/detail?id=5255
				httpResult.setResult(TGHttpError.getDefaultErrorMsg(context, TGHttpError.UNKNOWN_EXCEPTION));
				retry = retryHandler.retryRequest(cause, ++executionCount, httpContext);
			}
			catch (Exception e)
			{
				httpResult.setResult(TGHttpError.getDefaultErrorMsg(context, TGHttpError.UNKNOWN_EXCEPTION));
				retry = retryHandler.retryRequest(cause, ++executionCount, httpContext);
			}
		}
		
		return httpResult;
	}
	
	public TGHttpResult handleEntity(int responseCode, HttpEntity entity, String charset) throws IOException
	{
		TGHttpResult httpResult = initHttpResult();
		if (null != entity)
		{
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];

			int length = -1;
			InputStream inputStream = entity.getContent();
			while ((length = inputStream.read(buffer)) != -1)
			{
				outStream.write(buffer, 0, length);
			}
			byte[] data = outStream.toByteArray();
			outStream.close();
			inputStream.close();
			
			httpResult.setResult(new String(data, charset));
			return httpResult;
		}
		
		return httpResult;
	}
	
	/**
	 * 该方法的作用:
	 * 初始化网络请求结果
	 * @date 2013-12-1
	 * @return
	 */
	protected TGHttpResult initHttpResult()
	{
		TGHttpResult httpResult = new TGHttpResult();
		httpResult.setResponseCode(Integer.valueOf(4936));
		httpResult.setResult(getContext().getText(CR.getStringsId(getContext(), "tiger_try_later")).toString());
		return httpResult;
	}
	
	private void addGZIPProperty(HttpRequest request)
	{
		if (!request.containsHeader(HEADER_ACCEPT_ENCODING))
		{
			request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
		}
	}

	private void decodingGZIP(HttpResponse response)
	{
		final HttpEntity entity = response.getEntity();
		if (entity == null)
		{
			return;
		}
		final Header encoding = entity.getContentEncoding();
		if (encoding != null)
		{
			for (HeaderElement element : encoding.getElements())
			{
				if (element.getName().equalsIgnoreCase(ENCODING_GZIP))
				{
					response.setEntity(new InflatingEntity(response.getEntity()));
					break;
				}
			}
		}
	}
	
	public Context getContext()
	{
		return context;
	}

	private static class InflatingEntity extends HttpEntityWrapper
	{
		public InflatingEntity(HttpEntity wrapped)
		{
			super(wrapped);
		}

		@Override
		public InputStream getContent() throws IOException
		{
			return new GZIPInputStream(wrappedEntity.getContent());
		}

		@Override
		public long getContentLength()
		{
			return -1;
		}
	}
}
