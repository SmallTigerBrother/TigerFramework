package com.mn.tiger.request.method;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import com.mn.tiger.log.LogTools;
import com.mn.tiger.utility.Commons;


/**
 * http请求方法基类
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2013-12-1
 */
public abstract class TGHttpMethod 
{
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	private String url;
	
	/**
	 * 请求参数
	 */
	private Object params = null;
	
	private HttpURLConnection httpConnection = null;
	
	/**
	 * 消息头里的属性参数
	 */
	private HashMap<String, String> properties = null;
	
	/**
	 * 连接超时时间
	 */
	private int connectTimeout = 60000;
	
	/**
	 * 读取超时时间
	 */
	private int readTimeout = 60000;
	
	/**
	 * 证书
	 */
	static TrustManager[] xtmArray = new TGTmArray[] { new TGTmArray() };  

	/**
	 * 主机认证
	 */
	static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() 
	{
		@Override
		public boolean verify(String hostname, SSLSession session) 
		{
			return true;
		}
	};
	
	private Context context;
	
	/**
	 * @date 2013-12-1
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
	 * @date 2013-12-1 
	 * @param context
	 * @param url
	 * @param params
	 */
	public TGHttpMethod(Context context, String url, Object params)
	{
		this(context, url);
		this.params = params;
	}
	
	/**
	 * @date 2013-12-1
	 * @param url
	 */
	public void setUrl(String url) 
	{
		this.url = url;
	}
	
	/**
	 * @date 2013-12-1
	 * @return
	 */
	public String getUrl() 
	{
		return url;
	}
	
	/**
	 * @date 2013-12-1
	 * @return
	 */
	protected HttpURLConnection getHttpURLConnection() 
	{
		return httpConnection;
	}
	
	protected Context getContext() 
	{
		return context;
	}
	
	/**
	 * 设置请求参数
	 * @date 2013-12-1
	 * @param params
	 */
	public void setReqeustParams(Object params)
	{
		this.params = params;
	}
	
	/**
	 * 获取请求参数
	 * @date 2013-12-1
	 * @return
	 */
	public Object getRequestParams() 
	{
		return params;
	}
	
	/**
	 * 设置消息头里的属性参数
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, String value)
	{
		this.properties.put(key, value);
	}
	
	/**
	 * 设置消息头里的属性参数
	 * @param properties
	 */
	public void setProperties(Map<String, String> properties)
	{
		this.properties.putAll(properties);
	}
	
	/**
	 * 断开连接
	 * @date 2013-12-1
	 */
	public void disconnect()
	{
		if (null != httpConnection)
		{
			httpConnection.disconnect();
		}
	}
	
	/**
	 * 获取请求结果输入流
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
	 * 执行http请求
	 * @date 2013-12-1
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public final int excute() throws IOException, KeyManagementException, NoSuchAlgorithmException
	{
		//1、向url中加入参数
		url = appendParams2Url(url, params);
		
		//2、打开HttpUrlConnection
		httpConnection = openHttpUrlConnection(url);
		
		if(null != httpConnection)
		{
			//3、设置HttpURLConnection控制参数
			setHttpURLConnectionParams(httpConnection);
			
			//4、设置属性参数
			setProperties();
			
			//5、向HttpUrlConnection的输出流中加入参数
			appendParams2OutputStream(params);
			
			//6、执行连接，返回ResponseCode
			return startConnect();
		}
		else
		{
			LogTools.e(LOG_TAG, "[Method:excute] There may be something error when openHttpUrlConnection");
		}
		
		return 0;
	}
	
	/**
	 * 执行连接，返回ResponseCode
	 * @return
	 * @throws IOException
	 */
	protected int startConnect() throws IOException
	{
		if (null != httpConnection)
		{
			httpConnection.connect();
			try
			{
				return httpConnection.getResponseCode();
			}
			catch (EOFException e)
			{
				LogTools.i(LOG_TAG, e);
				return httpConnection.getResponseCode();
			}
		}
		
		return 0;
	}
	
	/**
	 * 向url中加入参数
	 * @param url
	 * @param params
	 * @return
	 */
	protected String appendParams2Url(String url, Object params)
	{
		return url;
	}
	
	/**
	 * 向HttpUrlConnection的输出流中加入参数
	 * @throws BusinessException 
	 */
	protected final void appendParams2OutputStream(Object params) 
			throws IOException
	{
		byte[] parameters = convertParams2bytes(params);
		if (null != parameters && parameters.length > 0)
		{
			getHttpURLConnection().setRequestProperty("Content-Length",
					"" + String.valueOf(parameters.length));
			OutputStream outputStream = getHttpURLConnection().getOutputStream();
			if (null != outputStream)
			{
				try
				{
					outputStream.write(parameters, 0, parameters.length);
					outputStream.flush();
				}
				catch (IOException e)
				{
					LogTools.e(LOG_TAG, "", e);
				}
				finally
				{
					Commons.closeOutputStream(outputStream);
				}
			}
		}
	}
	
	/**
	 * 将参数转换为byte数组
	 * @param parameters
	 * @return
	 * @throws BusinessException 
	 */
	protected byte[] convertParams2bytes(Object parameters) 
	{
		return null;
	}
	
	/**
	 * 设置Connection请求的属性
	 * @date 2014年4月23日
	 */
	private void setProperties()
	{
		if(null != httpConnection)
		{
			for (Entry<String, String> entry : properties.entrySet())
			{
				String key = entry.getKey();
				String value = entry.getValue();
				if (null != key && null != value)
				{
					httpConnection.setRequestProperty(key, value);
				}
			}
		}
	}
	
	/**
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
			httpConnection.setConnectTimeout(connectTimeout);
			// 设置读取超时时间
		    httpConnection.setReadTimeout(readTimeout);
		    //防止socketTimeout
		    httpConnection.setRequestProperty("http.socket.timeout", "60000");
		}
	}
	
	/**
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
}
