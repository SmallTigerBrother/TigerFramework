package com.mn.tiger.request.method;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;

import com.mn.tiger.utility.LogTools;
import com.mn.tiger.utility.StringUtils;

/**
 * 该类作用及功能说明
 * Post请求方法类
 * @author l00220455
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2013-12-1
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public class TGPostMethod extends TGHttpMethod 
{
	/**
	 * @author l00220455
	 * @date 2013-12-1
	 * 构造函数
	 * @param context
	 * @param url
	 */
	public TGPostMethod(Context context, String url)
	{
		super(context, url);
	}
	
	/**
	 * @author l00220455
	 * @date 2013-12-1
	 * 构造函数
	 * @param context
	 * @param url
	 * @param params
	 */
	public TGPostMethod(Context context, String url, Object params)
	{
		super(context, url);
		setReqeustParams(params);
	}
	
	/**
	 * @throws BusinessException 
	 * @see TGHttpMethod#excute()
	 */
	@Override
	public int excute() throws IOException,KeyManagementException, NoSuchAlgorithmException
	{
		super.excute();
		//添加请求参数
		outputRequestParams();
		
		HttpURLConnection httpConnection = getHttpURLConnection();
		if(null != httpConnection)
		{
			httpConnection.connect();
			
			//只捕获EOF异常处理
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
	 * 该方法的作用:
	 * 将请求参数添加到请求中
	 * @author l00220455
	 * @throws BusinessException 
	 * @date 2014年1月7日
	 */
	protected void outputRequestParams() throws IOException
	{
		if (null != getRequestParams() && null != getHttpURLConnection()) 
		{
			byte[] parameters = convertParams2bytes(getRequestParams());
			
			getHttpURLConnection().setRequestProperty("Content-Length", "" + 
			    String.valueOf(parameters.length));

			DataOutputStream out = null;
			
			try 
			{
				out = new DataOutputStream(getHttpURLConnection().getOutputStream());
				out.write(parameters, 0, parameters.length);
				out.flush();
			}
			catch (IOException e) 
			{
				LogTools.e(LOG_TAG, "",e);
			}
			finally
			{
				if(null != out)
				{
					try 
					{
						out.close();
					} 
					catch (IOException e)
					{
						LogTools.e(LOG_TAG,"", e);
					}
				}
			}
		}
	}
	
	/**
	 * @see TGHttpMethod#setHttpURLConnectionParams(HttpURLConnection)
	 */
	@Override
	protected void setHttpURLConnectionParams(HttpURLConnection httpConnection) throws IOException 
	{
		super.setHttpURLConnectionParams(httpConnection);
		
		httpConnection.setRequestMethod("POST");
		httpConnection.setDoOutput(true);
		httpConnection.setUseCaches(false); //不使用缓存
	}
	
	/**
	 * 该方法的作用:
	 * 转换post提交的数据
	 * @author l00220455
	 * @date 2014年3月24日
	 * @param parameters
	 * @return
	 * @throws BusinessException 
	 */
	@SuppressWarnings("unchecked")
	private byte[] convertParams2bytes(Object parameters) 
	{
		// 参数为Map时，添加请求参数加密处理
		if(parameters instanceof Map<?, ?>)
		{
			return convertParams2bytes((Map<String, String>)parameters);
		}
		
		// 参数为非Map时，添加请求参数加密处理
		if(StringUtils.isEmptyOrNull(parameters.toString()))
		{
			return "".getBytes();
		}
		else
		{
			return parameters.toString().getBytes();
		}

	}
	
	/**
	 * 该方法的作用:post提交中的数据
	 * @author nKF50342
	 * @date 2012-6-11
	 * @param parameters
	 * @return
	 */
	private byte[] convertParams2bytes(Map<String, String> parameters) 
	{
		StringBuffer params = new StringBuffer();
		/**
		 * 数据utf-8格式化
		 */
		for (Iterator<Entry<String, String>> iteror = parameters.entrySet().iterator(); iteror.hasNext();) 
		{
			// 处理请求参数中的key
			Entry<String, String> element = (Entry<String, String>) iteror.next();
			try
			{
				params.append(URLEncoder.encode(element.getKey(), "UTF-8"));
				
				params.append("=");
				
				params.append(URLEncoder.encode(element.getValue(), "UTF-8"));
				// 请求参数间隔符
				params.append("&");
			}
			catch (UnsupportedEncodingException e)
			{
				LogTools.e(LOG_TAG, e.getMessage(), e);
			}			
		}
		
		if (params.length() > 0) 
		{
			params = params.deleteCharAt(params.length() - 1);
		}
		
		byte[] bytes = params.toString().getBytes();
		return bytes;
	}

	/**
	 * @see TGHttpMethod#disconnect()
	 */
	@Override
	public void disconnect()
	{
		HttpURLConnection httpConnection = getHttpURLConnection();
		if(null != httpConnection)
		{
			httpConnection.disconnect();
		}
	}
	
}
