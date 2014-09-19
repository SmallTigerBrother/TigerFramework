package com.mn.tiger.request.method;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * 该类作用及功能说明
 * Http请求方法类，封装请求初始化，请求连接、请求中断的方法
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2013-12-1
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public abstract class TGHttpMethod 
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 请求Url
	 */
	private String url;
	
	/**
	 * 业务请求参数
	 */
	private Object params = null;
	
	/**
	 * 请求连接类
	 */
	private HttpURLConnection httpConnection = null;
	
	/**
	 * 网络请求参数
	 */
	private HashMap<String, String> properties = null;
	
	/**
	 * 连接超时时间
	 */
	private int connectTimeout = 60000;
	
	/**
	 * 连接读取超时时间
	 */
	private int readTimeout = 60000;
	
	/**
	 * 主机的信任列表
	 */
	static TrustManager[] xtmArray = new TGTmArray[] { new TGTmArray() };  

	/**
	 * 不做任何验证
	 */
	static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() 
	{
		@Override
		public boolean verify(String hostname, SSLSession session) 
		{
			return true;
		}
	};
	
	/**
	 * 运行环境
	 */
	private Context context;
	
	/**
	 * @date 2013-12-1
	 * 构造函数
	 * @param context
	 * @param url
	 */
	public TGHttpMethod(Context context, String url)
	{
		this.url = url;
		this.context = context;
		this.properties = new HashMap<String, String>();
	}
	
	/**
	 * 该方法的作用:
	 * 设置请求Url
	 * @date 2013-12-1
	 * @param url
	 */
	public void setUrl(String url) 
	{
		this.url = url;
	}
	
	/**
	 * 该方法的作用:
	 * 获取请求Url
	 * @date 2013-12-1
	 * @return
	 */
	public String getUrl() 
	{
		return url;
	}
	
	/**
	 * 该方法的作用:
	 * 获取网络连接
	 * @date 2013-12-1
	 * @return
	 */
	protected HttpURLConnection getHttpURLConnection() 
	{
		return httpConnection;
	}
	
	protected void setHttpConnection(HttpURLConnection httpConnection)
	{
		this.httpConnection = httpConnection;
	}

	/**
	 * 该方法的作用:
	 * 获取Context
	 * @date 2013-12-1
	 * @return
	 */
	protected Context getContext() 
	{
		return context;
	}
	
	/**
	 * 该方法的作用:
	 * 设置请求参数
	 * @date 2013-12-1
	 * @param params
	 */
	public void setReqeustParams(Object params)
	{
		this.params = params;
	}
	
	/**
	 * 该方法的作用:
	 * 设置请求参数
	 * @date 2013-12-1
	 * @return
	 */
	public Object getRequestParams() 
	{
		return params;
	}
	
	/**
	 * 
	 * 该方法的作用:设置Connection请求的属性
	 * @date 2014年4月23日
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, String value)
	{
		this.properties.put(key, value);
	}
	
	/**
	 * 
	 * 该方法的作用: 批量设置请求参数
	 * @date 2014年5月23日
	 * @param properties
	 */
	public void setProperties(Map<String, String> properties)
	{
		this.properties.putAll(properties);
	}
	
	/**
	 * 该方法的作用:
	 * 中断连接
	 * @date 2013-12-1
	 */
	public abstract void disconnect();
	
	/**
	 * 该方法的作用:
	 * 获取输入流
	 * @date 2013-12-1
	 * @return
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException
	{
		if(null != httpConnection)
		{
			return httpConnection.getInputStream();
		}
		
		return null;
	}
	
	/**
	 * 该方法的作用:
	 * 执行网络连接
	 * @date 2013-12-1
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public int excute() throws IOException, KeyManagementException, NoSuchAlgorithmException
	{
		httpConnection = initHttpURLConnection(url);
		return 0;
	}
	
	/**
	 * 该方法的作用:
	 * 初始化网络连接类,共两个步骤
	 * 1、打开网络连接，openHttpUrlConnection(String url)
	 * 2、设置网络连接参数，setHttpURLConnectionParams(HttpURLConnection httpConnection)
	 * @date 2013-12-1
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws ProtocolException
	 */
	protected final HttpURLConnection initHttpURLConnection(String url) throws 
	    IOException, KeyManagementException, NoSuchAlgorithmException, ProtocolException
	{
		httpConnection = openHttpUrlConnection(url);
		setHttpURLConnectionParams(httpConnection);
		setProperties();
		
		return httpConnection;
	}
	
	/**
	 * 
	 * 该方法的作用: 设置Connection请求的属性
	 * @date 2014年4月23日
	 */
	private void setProperties()
	{
		if(null != httpConnection)
		{
			for(Entry<String, String> entry : properties.entrySet()){
				String key = entry.getKey();
				String value = entry.getValue();
				if(null != key && null != value){
					httpConnection.setRequestProperty(key, value);
				}
			}
		}
	}
	
	/**
	 * 该方法的作用:
	 * 打开网络连接
	 * @date 2013-12-1
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	@SuppressLint("DefaultLocale")
	@SuppressWarnings("deprecation")
	protected HttpURLConnection openHttpUrlConnection(String url) throws 
	    IOException, KeyManagementException, NoSuchAlgorithmException
	{
		HttpURLConnection httpConnection = null;
		URL requestUrl = new URL(url);
		
		//处理中国移动cmwap网络代理
		NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(
				Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		java.net.Proxy javaProxy = null;
		if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && 
				"cmwap".equals(networkInfo.getExtraInfo()))
		{
			String host = android.net.Proxy.getDefaultHost();

			int port = android.net.Proxy.getDefaultPort();
			if (host != null && port != -1) 
			{
				InetSocketAddress inetAddress = new InetSocketAddress(host,port);
				java.net.Proxy.Type proxyType = java.net.Proxy.Type.valueOf(
						requestUrl.getProtocol().toUpperCase());
				javaProxy = new java.net.Proxy(proxyType, inetAddress);
			}
		}
		
		// 判断是http请求还是https请求
		if (requestUrl.getProtocol().toLowerCase().equals("https"))
		{
			trustAllHosts();
			httpConnection = (HttpsURLConnection) getHttpURLConnection(requestUrl, javaProxy);
			((HttpsURLConnection) httpConnection).setHostnameVerifier(DO_NOT_VERIFY);// 不进行主机名确认
		}
		else 
		{
			httpConnection = (HttpURLConnection) getHttpURLConnection(requestUrl, javaProxy);
		}
		
		return httpConnection;
	}
	
	/**
	 * 该方法的作用:
	 * 获取请求UrlConnection
	 * @date 2014年2月10日
	 * @param requestUrl
	 * @param javaProxy
	 * @return
	 * @throws IOException
	 */
	private HttpURLConnection getHttpURLConnection(URL requestUrl, java.net.Proxy javaProxy) 
			throws IOException
	{
		if(requestUrl == null) 
		{
			return null;
		}
		
		//打开网络连接
		if (null != javaProxy) 
		{
			return (HttpURLConnection) requestUrl.openConnection(javaProxy);
		} 
		else 
		{
			return (HttpURLConnection) requestUrl.openConnection();
		}
	}
	
	/**
	 * 该方法的作用:
	 * 设置网络连接参数
	 * @date 2013-12-1
	 * @param httpConnection
	 * @throws ProtocolException
	 * @throws IOException
	 */
	protected void setHttpURLConnectionParams(HttpURLConnection httpConnection) throws 
	    ProtocolException, IOException
	{
		if (null != httpConnection) 
		{
			httpConnection.setDoInput(true);
			
			// 设置连接超时时间
			httpConnection.setConnectTimeout(getConnectTimeout());
			// 设置读取超时时间
		    httpConnection.setReadTimeout(getReadTimeout());
		    //防止socketTimeout
		    httpConnection.setRequestProperty("http.socket.timeout", "60000");
		}
	}
	
	/**
	 * 该方法的作用:
	 * 获取请求头的属性key
	 * @date 2013-12-1
	 * @param posn
	 * @return
	 */
	public String getHeaderFieldKey(int posn)
	{
		if(null != httpConnection)
		{
			return httpConnection.getHeaderFieldKey(posn);
		}
		return null;
	}
	
	/**
	 * 该方法的作用:
	 * 获取请求头的属性
	 * @date 2013-12-1
	 * @param pos
	 * @return
	 */
	public String getHeaderField(int pos)
	{
		if(null != httpConnection)
		{
			return httpConnection.getHeaderField(pos);
		}
		return null;
	}
	
	/**
	 * 该方法的作用:
	 * 获取请求头信息
	 * @date 2013-12-1
	 * @return
	 */
	public Map<String, List<String>> getHeaderFields()
	{
		if(null != httpConnection)
		{
			return httpConnection.getHeaderFields();
		}
		return null;
	}
	
	/**
	 * 该方法的作用:
	 * 信任所有主机-对于任何证书都不做检查
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 * @date 2014年2月10日
	 */
	@SuppressLint("TrulyRandom")
	private static void trustAllHosts() throws NoSuchAlgorithmException, KeyManagementException
	{
		// Create a trust manager that does not validate certificate chains
		// Android 采用X509的证书信息机制
		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, xtmArray, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		// HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);//
		// 不进行主机名确认
	}

	public int getConnectTimeout()
	{
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout)
	{
		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout()
	{
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout)
	{
		this.readTimeout = readTimeout;
	}
}
