package com.mn.tiger.request.method;

import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import android.content.Context;
import android.os.Build.VERSION;

import com.mn.tiger.utility.LogTools;

/**
 * 该类作用及功能说明 Delete请求方法类
 * 
 * @author pwx197040
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2014-05-19 Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights
 *       reserved.
 */
public class TGDeleteMethod extends TGHttpMethod
{
	/**
	 * @author pwx197040
	 * @date 2014-05-19 构造函数
	 * @param context
	 * @param url
	 */
	public TGDeleteMethod(Context context, String url)
	{
		super(context, url);
	}

	/**
	 * @author pwx197040
	 * @date 2014-05-19 构造函数
	 * @param context
	 * @param url
	 * @param params
	 */
	public TGDeleteMethod(Context context, String url, Object params)
	{
		super(context, url);
		setReqeustParams(params);
	}

	/**
	 * @see MPGetMothod#excute()
	 */
	@Override
	public int excute() throws IOException, KeyManagementException, NoSuchAlgorithmException
	{
		setHttpConnection(initHttpURLConnection(getUrl()));
		
		HttpURLConnection httpConnection = getHttpURLConnection();
		if (null != httpConnection)
		{
			httpConnection.connect();

			// 只捕获EOF异常处理
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
	 * @see MPGetMothod#openHttpUrlConnection(String)
	 */
	@Override
	protected HttpURLConnection openHttpUrlConnection(String url) throws IOException,
			KeyManagementException, NoSuchAlgorithmException
	{
		// 拼装Url
		if (null != getRequestParams())
		{
			try
			{
				url = getRequestDelUrl(url, getRequestParams());
			}
			catch (UnsupportedEncodingException e)
			{
				LogTools.e(LOG_TAG, "", e);
			}
		}
		
		return super.openHttpUrlConnection(url);
	}

	/**
	 * @see MPGetMothod#setHttpURLConnectionParams(HttpURLConnection)
	 */
	@Override
	protected void setHttpURLConnectionParams(HttpURLConnection httpConnection)
			throws ProtocolException, IOException
	{
		super.setHttpURLConnectionParams(httpConnection);

		if (VERSION.SDK_INT <= 13)
		{
			httpConnection.setDoOutput(true);
		}
		httpConnection.setRequestMethod("DELETE");
	}

	/**
	 * 
	 * 该方法的作用: 拼装Delete请求Url
	 * @author pWX197040
	 * @date 2014年5月19日
	 * @param baseUrl
	 * @param parameters
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	protected String getRequestDelUrl(String baseUrl, Object parameters)
			throws UnsupportedEncodingException
	{
		if (parameters instanceof Map<?, ?>)
		{
			return getRequestGetUrl(baseUrl, (Map<String, String>) parameters);
		}
		else if (parameters instanceof String)
		{
			return getRequestGetUrl(baseUrl, (String) parameters);
		}

		return baseUrl;
	}

	/**
	 * 
	 * 该方法的作用: 获去Delete请求Url，将参数与Url进行拼装
	 * @author pWX197040
	 * @date 2014年5月19日
	 * @param baseUrl
	 * @param parameters
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	protected String getRequestGetUrl(String baseUrl, Map<String, String> parameters)
			throws UnsupportedEncodingException
	{
		StringBuffer urlBuffer = new StringBuffer(baseUrl);
		if (null != parameters)
		{
			if (urlBuffer.indexOf("?") < 0)
			{
				urlBuffer.append("?");
			}

			Object[] paraKeys = parameters.keySet().toArray();
			int parameterLength = paraKeys.length;

			for (int i = 0; i < parameterLength; i++)
			{
				urlBuffer.append(URLEncoder.encode((String) paraKeys[i], "UTF-8"));
				urlBuffer.append("=");
				urlBuffer.append(URLEncoder.encode(parameters.get(paraKeys[i]), "utf-8"));
				if (i != (parameterLength - 1))
				{
					urlBuffer.append("&");
				}
			}
		}

		return urlBuffer.toString();
	}

	/**
	 * 
	 * 该方法的作用: 拼装Delete请求Url
	 * @author pWX197040
	 * @date 2014年5月19日
	 * @param baseUrl
	 * @param parameters
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	protected String getRequestGetUrl(String baseUrl, String parameters)
			throws UnsupportedEncodingException
	{
		StringBuffer urlBuffer = new StringBuffer(baseUrl);
		if (null != parameters)
		{
			if (urlBuffer.indexOf("?") < 0)
			{
				urlBuffer.append("?");
			}
			urlBuffer.append(parameters);
		}

		return urlBuffer.toString();
	}

	/**
	 * @see TGHttpMethod#disconnect()
	 */
	@Override
	public void disconnect()
	{
		HttpURLConnection httpConnection = getHttpURLConnection();
		if (null != httpConnection)
		{
			httpConnection.disconnect();
		}
	}

}
