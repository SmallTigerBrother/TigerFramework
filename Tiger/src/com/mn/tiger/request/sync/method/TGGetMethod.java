package com.mn.tiger.request.sync.method;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.Map;

import android.content.Context;
import android.os.Build.VERSION;
import android.text.TextUtils;

import com.mn.tiger.log.LogTools;

/**
 * 该类作用及功能说明 Get请求方法类
 * 
 * @version V2.0
 * @see JDK1.6,android-8
 */
public class TGGetMethod extends TGHttpMethod
{
	/**
	 * @date 2013-12-1 构造函数
	 * @param context
	 * @param url
	 */
	public TGGetMethod(Context context, String url)
	{
		super(context, url);
	}
	
	/**
	 * @date 2013-12-1 构造函数
	 * @param context
	 * @param url
	 * @param params
	 */
	public TGGetMethod(Context context, String url, Object params)
	{
		super(context, url, params);
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
		httpConnection.setRequestMethod("GET");
	}
	
	@Override
	protected String appendParams2Url(String url, Object params)
	{
		String paramsString = null;
		if (params instanceof Map<?, ?>)
		{
			try
			{
				paramsString = ((TGHttpParams)params).mergeStringParams2KeyValuePair();
			}
			catch (UnsupportedEncodingException e)
			{
				LogTools.e(LOG_TAG, e);
			}
		}
		else if (params instanceof String)
		{
			paramsString = (String) params;
		}
		
		if (!TextUtils.isEmpty(paramsString))
		{
			if (url.indexOf("?") < 0)
			{
				url = url + "?";
			}
			url = url + paramsString;;
		}

		return url;
	}

}
