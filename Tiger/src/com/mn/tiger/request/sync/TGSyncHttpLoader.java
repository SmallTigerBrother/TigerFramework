package com.mn.tiger.request.sync;

import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.mn.tiger.request.sync.client.DefaultHttpClient;
import com.mn.tiger.request.sync.client.TGHttpClient;
import com.mn.tiger.request.sync.method.TGDeleteMethod;
import com.mn.tiger.request.sync.method.TGGetMethod;
import com.mn.tiger.request.sync.method.TGHttpMethod;
import com.mn.tiger.request.sync.method.TGHttpParams;
import com.mn.tiger.request.sync.method.TGPostMethod;
import com.mn.tiger.request.sync.method.TGPutMethod;
import com.mn.tiger.request.sync.receiver.DefaultHttpReceiver;
import com.mn.tiger.request.sync.receiver.TGHttpReceiver;
import com.mn.tiger.request.sync.receiver.TGHttpResult;

public class TGSyncHttpLoader extends AbstractSyncHttpLoader
{
	protected static final String LOG_TAG = TGSyncHttpLoader.class.getSimpleName();
	
	/**
	 * 该方法的作用:
	 * Get请求
	 * @date 2013-12-1
	 * @param context
	 * @param requestUrl
	 * @param parameters
	 * @param properties content-type等请求参数
	 * @return
	 */
	public TGHttpResult loadByGetSync(Context context, String requestUrl, 
			TGHttpParams parameters, Map<String, String> properties)
	{
		if(TextUtils.isEmpty(requestUrl))
		{
			return getHttpResultWhileUrlIsNULL(context);
		}
		
		TGHttpMethod getMethod = new TGGetMethod(context, requestUrl, parameters);
		TGHttpReceiver httpReceiver = new DefaultHttpReceiver(context);
		TGHttpClient httpClient = new DefaultHttpClient(context);
		
		if(properties != null && properties.size() > 0)
		{
			getMethod.setProperties(properties);
		}
		
		return httpClient.executeHttpMethod(getMethod, httpReceiver);
	}
	
	/**
	 * 该方法的作用:post请求，请求参数可以自定义设置
	 * @date 2014年5月23日
	 * @param context
	 * @param requestUrl
	 * @param parameters
	 * @param properties content-type等请求参数
	 * @return
	 */
	public TGHttpResult loadByPostSync(Context context, String requestUrl, 
			TGHttpParams parameters, Map<String, String> properties)
	{
		if(TextUtils.isEmpty(requestUrl))
		{
			return getHttpResultWhileUrlIsNULL(context);
		}
		
		TGHttpMethod postMethod = new TGPostMethod(context, requestUrl, parameters);
		TGHttpReceiver httpReceiver = new DefaultHttpReceiver(context);
		TGHttpClient httpClient = new DefaultHttpClient(context);
		
		if(properties != null && properties.size() > 0)
		{
			postMethod.setProperties(properties);
		}
		
		return httpClient.executeHttpMethod(postMethod, httpReceiver);
	}
	
	/**
	 * 该方法的作用: put请求，请求参数可以自定义设置
	 * @date 2014年5月23日
	 * @param context
	 * @param requestUrl
	 * @param parameters
	 * @param properties content-type等请求参数
	 * @return
	 */
	public TGHttpResult loadByPutSync(Context context, String requestUrl, 
			TGHttpParams parameters, Map<String, String> properties)
	{
		if(TextUtils.isEmpty(requestUrl))
		{
			return getHttpResultWhileUrlIsNULL(context);
		}
		
		TGHttpMethod putMethod = new TGPutMethod(context, requestUrl, parameters);
		TGHttpReceiver httpReceiver = new DefaultHttpReceiver(context);
		TGHttpClient httpClient = new DefaultHttpClient(context);
		
		if(properties != null && properties.size() > 0)
		{
			putMethod.setProperties(properties);
		}
		
		return httpClient.executeHttpMethod(putMethod, httpReceiver);
	}
	
	/**
	 * 该方法的作用: Delete请求
	 * @date 2014年5月19日
	 * @param context
	 * @param requestUrl
	 * @param parameters
	 * @param properties content-type等请求参数
	 * @return
	 */
	public TGHttpResult loadByDeleteSync(Context context, String requestUrl, 
			TGHttpParams parameters, Map<String, String> properties)
	{
		if(TextUtils.isEmpty(requestUrl))
		{
			return getHttpResultWhileUrlIsNULL(context);
		}
		
		TGHttpMethod delMethod = new TGDeleteMethod(context, requestUrl, parameters);
		TGHttpReceiver httpReceiver = new DefaultHttpReceiver(context);
		TGHttpClient httpClient = new DefaultHttpClient(context);
		
		if(properties != null && properties.size() > 0)
		{
			delMethod.setProperties(properties);
		}
		
		return httpClient.executeHttpMethod(delMethod, httpReceiver);
	}
	
}