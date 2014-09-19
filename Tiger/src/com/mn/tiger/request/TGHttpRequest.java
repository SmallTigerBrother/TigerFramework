package com.mn.tiger.request;

import java.util.Map;

import android.content.Context;

import com.mn.tiger.request.client.DefaultHttpClient;
import com.mn.tiger.request.client.TGHttpClient;
import com.mn.tiger.request.error.IHttpErrorHandler;
import com.mn.tiger.request.method.TGDeleteMethod;
import com.mn.tiger.request.method.TGGetMethod;
import com.mn.tiger.request.method.TGHttpMethod;
import com.mn.tiger.request.method.TGPostMethod;
import com.mn.tiger.request.method.TGPutMethod;
import com.mn.tiger.request.receiver.DefaultHttpReceiver;
import com.mn.tiger.request.receiver.TGHttpReceiver;
import com.mn.tiger.request.receiver.TGHttpResult;

public class TGHttpRequest 
{
	public static final int REQUEST_UNKNOWN = -1;
	
	/**
	 * Post请求类型
	 */
	public static final int REQUEST_POST = 0;
	/**
	 * Get请求类型
	 */
	public static final int REQUEST_GET = 1;
	/**
	 * DELETE请求类型
	 */
	public static final int REQUEST_DELETE = 2;
	/**
	 * Put请求类型
	 */
	public static final int REQUEST_PUT = 3;
	
	/**
	 * 日志标签
	 */
	protected static final String LOG_TAG = TGHttpRequest.class.getSimpleName();
	
	/**
	 * 该方法的作用:
	 * Post请求
	 * @date 2013-12-1
	 * @param context
	 * @param requestUrl
	 * @param parameters
	 * @param httpErrorHandler
	 * @return
	 */
	public static TGHttpResult requestPost(Context context, String requestUrl, 
			Object parameters, IHttpErrorHandler httpErrorHandler)
	{
		return requestPost(context, requestUrl, parameters, httpErrorHandler, null);
	}
	
	/**
	 * 
	 * 该方法的作用:post请求，请求参数可以自定义设置
	 * @date 2014年5月23日
	 * @param context
	 * @param requestUrl
	 * @param parameters
	 * @param httpErrorHandler
	 * @param properties content-type等请求参数
	 * @param isEncryptRequest 是否加密请求
	 * @return
	 */
	public static TGHttpResult requestPost(Context context, String requestUrl, 
			Object parameters, IHttpErrorHandler httpErrorHandler, Map<String, String> properties)
	{
		TGHttpMethod postMethod = new TGPostMethod(context, requestUrl, parameters);
		TGHttpReceiver httpReceiver = new DefaultHttpReceiver(context, httpErrorHandler);
		TGHttpClient httpClient = new DefaultHttpClient(context);
		
		if(properties != null && properties.size() > 0)
		{
			postMethod.setProperties(properties);
		}
		
		return httpClient.executeHttpMethod(postMethod, httpReceiver);
	}
	
	/**
	 * 该方法的作用:
	 * Get请求
	 * @date 2013-12-1
	 * @param context
	 * @param requestUrl
	 * @param parameters
	 * @param error
	 * @return
	 */
	public static TGHttpResult requestGet(Context context, String requestUrl, 
			Object parameters, IHttpErrorHandler httpErrorHandler)
	{
		return requestGet(context, requestUrl, parameters, httpErrorHandler, null);
	}
	
	/**
	 * 该方法的作用:
	 * Get请求
	 * @date 2013-12-1
	 * @param context
	 * @param requestUrl
	 * @param parameters
	 * @param error
	 * @param properties content-type等请求参数
	 * @param isEncryptRequest 是否加密请求
	 * @return
	 */
	public static TGHttpResult requestGet(Context context, String requestUrl, 
			Object parameters, IHttpErrorHandler httpErrorHandler, Map<String, String> properties)
	{
		TGHttpMethod getMethod = new TGGetMethod(context, requestUrl, parameters);
		TGHttpReceiver httpReceiver = new DefaultHttpReceiver(context, httpErrorHandler);
		TGHttpClient httpClient = new DefaultHttpClient(context);
		
		if(properties != null && properties.size() > 0)
		{
			getMethod.setProperties(properties);
		}
		
		return httpClient.executeHttpMethod(getMethod, httpReceiver);
	}
	
	/**
	 * 
	 * 该方法的作用: Delete请求
	 * @date 2014年5月19日
	 * @param context
	 * @param requestUrl
	 * @param parameters
	 * @param httpErrorHandler
	 * @return
	 */
	public static TGHttpResult requestDelete(Context context, String requestUrl, 
			Object parameters, IHttpErrorHandler httpErrorHandler)
	{
		return requestDelete(context, requestUrl, parameters, httpErrorHandler, null);
	}
	
	/**
	 * 
	 * 该方法的作用: Delete请求
	 * @date 2014年5月19日
	 * @param context
	 * @param requestUrl
	 * @param parameters
	 * @param httpErrorHandler
	 * @param properties content-type等请求参数
	 * @param isEncryptRequest 是否加密请求
	 * @return
	 */
	public static TGHttpResult requestDelete(Context context, String requestUrl, 
			Object parameters, IHttpErrorHandler httpErrorHandler, Map<String, String> properties)
	{
		TGHttpMethod delMethod = new TGDeleteMethod(context, requestUrl, parameters);
		TGHttpReceiver httpReceiver = new DefaultHttpReceiver(context, httpErrorHandler);
		TGHttpClient httpClient = new DefaultHttpClient(context);
		
		if(properties != null && properties.size() > 0)
		{
			delMethod.setProperties(properties);
		}
		
		return httpClient.executeHttpMethod(delMethod, httpReceiver);
	}
	
	/**
	 * 
	 * 该方法的作用: Put请求
	 * @date 2014年5月19日
	 * @param context
	 * @param requestUrl
	 * @param parameters
	 * @param httpErrorHandler
	 * @return
	 */
	public static TGHttpResult requestPut(Context context, String requestUrl, 
			Object parameters, IHttpErrorHandler httpErrorHandler)
	{
		return requestPut(context, requestUrl, parameters, httpErrorHandler, null);
	}
	
	/**
	 * 
	 * 该方法的作用: put请求，请求参数可以自定义设置
	 * @date 2014年5月23日
	 * @param context
	 * @param requestUrl
	 * @param parameters
	 * @param httpErrorHandler
	 * @param properties content-type等请求参数
	 * @return
	 */
	public static TGHttpResult requestPut(Context context, String requestUrl, 
			Object parameters, IHttpErrorHandler httpErrorHandler, Map<String, String> properties)
	{
		TGHttpMethod putMethod = new TGPutMethod(context, requestUrl, parameters);
		TGHttpReceiver httpReceiver = new DefaultHttpReceiver(context, httpErrorHandler);
		TGHttpClient httpClient = new DefaultHttpClient(context);
		
		if(properties != null && properties.size() > 0)
		{
			putMethod.setProperties(properties);
		}
		
		return httpClient.executeHttpMethod(putMethod, httpReceiver);
	}
}
