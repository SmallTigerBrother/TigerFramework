package com.mn.tiger.request.sync.method;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.Map;

import com.mn.tiger.log.LogTools;

import android.content.Context;
import android.text.TextUtils;

/**
 * 该类作用及功能说明 Put请求方法类
 * 
 * @version V2.0
 * @see JDK1.6,android-8
 */
public class TGPutMethod extends TGHttpMethod
{
	/**
	 * @date 2014年5月20日
	 * @param context
	 * @param url
	 */
	public TGPutMethod(Context context, String url)
	{
		super(context, url);
	}
	
	/**
	 * @date 2014年5月20日
	 * @param context
	 * @param url
	 * @param params
	 */
	public TGPutMethod(Context context, String url, TGHttpParams params)
	{
		super(context, url, params);
	}
	
	/**
	 * @see TGHttpMethod#setHttpURLConnectionParams(HttpURLConnection)
	 */
	@Override
	protected void setHttpURLConnectionParams(HttpURLConnection httpConnection) throws IOException 
	{
		super.setHttpURLConnectionParams(httpConnection);
		
		httpConnection.setRequestMethod("PUT");
		httpConnection.setDoOutput(true);
		httpConnection.setUseCaches(false); //不使用缓存
	}
	
	/**
	 * 该方法的作用:
	 * 转换put提交的数据
	 * @date 2014年3月24日
	 * @param parameters
	 * @return
	 */
	@Override
	protected byte[] convertParams2bytes(Object parameters) 
	{
		// 参数为Map时，添加请求参数加密处理
		if(parameters instanceof Map<?, ?>)
		{
			if(null == ((TGHttpParams)parameters).getFileParams())
			{
				try
				{
					return ((TGHttpParams)parameters).mergeStringParams2KeyValuePair().getBytes();
				}
				catch (UnsupportedEncodingException e)
				{
					LogTools.e(LOG_TAG, e);
				}
			}
			else
			{
				return ((TGHttpParams)parameters).toByteArray();
			}
		}
		
		// 参数为非Map时
		if(TextUtils.isEmpty(parameters.toString()))
		{
			return "".getBytes();
		}
		else
		{
			return parameters.toString().getBytes();
		}
	}
}
