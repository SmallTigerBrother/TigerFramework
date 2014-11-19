package com.mn.tiger.request.async;

import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.mn.tiger.parser.json.TGJsonUtils;
import com.mn.tiger.request.TGHttpRequester;
import com.mn.tiger.request.async.task.IRequestParser;
import com.mn.tiger.request.receiver.TGHttpResult;
import com.mn.tiger.utility.LogTools;

/**
 * 数据请求异步任务 <br />
 * 请求数据后可将数据解析成Class<T>类型的类<br />
 * 
 * @date 2014-6-9
 */
public class TGHttpAsyncRequester<T> implements IRequestParser
{
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 执行异步任务的类
	 */
	private InternalAsyncTask asyncTask;
	
	public TGHttpAsyncRequester()
	{
		asyncTask = new InternalAsyncTask("", 
				TGHttpRequester.REQUEST_UNKNOWN, null);
	}
	
	/**
	 * 执行get请求
	 * @param requestUrl 请求url
	 * @param clazz 解析结果类名
	 * @param params 请求参数
	 * @param handler 请求回调方法
	 */
	@SuppressWarnings("rawtypes")
	public void get(Context context, String requestUrl, Class clazz, 
			TGRequestHandler<T> handler)
	{
		execute(context, TGHttpRequester.REQUEST_GET, requestUrl, clazz.getName(), handler);
	}
	
	/**
	 * 执行post请求
	 * @param requestUrl 请求url
	 * @param resultClsName 解析结果类名
	 * @param params 请求参数
	 * @param handler 请求回调方法
	 */
	@SuppressWarnings("rawtypes")
	public void post(Context context, String requestUrl, Class clazz,
			TGRequestHandler<T> handler)
	{
		execute(context, TGHttpRequester.REQUEST_POST, requestUrl, clazz.getName(), handler);
	}
	
	/**
	 * 执行put请求
	 * @param requestUrl 请求url
	 * @param resultClsName 解析结果类名
	 * @param params 请求参数
	 * @param handler 请求回调方法
	 */
	@SuppressWarnings("rawtypes")
	public void put(Context context, String requestUrl, Class clazz,
			TGRequestHandler<T> handler)
	{
		execute(context, TGHttpRequester.REQUEST_PUT, requestUrl, clazz.getName(), handler);
	}
	
	/**
	 * 执行delete请求
	 * @param requestUrl 请求url
	 * @param resultClsName 解析结果类名
	 * @param params 请求参数
	 * @param handler 请求回调方法
	 */
	@SuppressWarnings("rawtypes")
	public void delete(Context context, String requestUrl, Class clazz,
			TGRequestHandler<T> handler)
	{
		execute(context, TGHttpRequester.REQUEST_DELETE, requestUrl, clazz.getName(),  handler);
	}
	
	/**
	 * 执行异步任务
	 * @param requestType 请求类型
	 * @param requestUrl 请求url
	 * @param resultClsName 解析结果类名
	 * @param params 请求参数
	 * @param handler 请求回调方法
	 */
	protected void execute(Context context, int requestType, String requestUrl, String resultClsName, 
			TGRequestHandler<T> handler)
	{
		asyncTask.setContext(context);
		asyncTask.setRequestType(requestType);
		asyncTask.setRequestUrl(requestUrl);
		asyncTask.setResultClsName(resultClsName);
		asyncTask.setRequestHandler(handler);
		
		asyncTask.execute();
	}
	
	/**
	 * 解析请求结果
	 */
	@Override
	public Object parseRequestResult(TGHttpResult httpResult, String resultClsName)
	{
		String jsonStr = (httpResult.getJSONResult() == null) ? null : httpResult.getJSONResult().toString();

		if (!TextUtils.isEmpty(jsonStr) && !"{}".equals(jsonStr))
		{
			try
			{
				return TGJsonUtils.parseJson2Object(jsonStr, Class.forName(resultClsName));
			}
			catch (ClassNotFoundException e)
			{
				LogTools.e(LOG_TAG, e.getMessage(), e);
			}
		}
		
		return null;
	}
	
	/**
	 * 取消任务
	 */
	public void cancel()
	{
		asyncTask.cancel();
	}
	
	/**
	 * 该方法的作用: 批量设置Headers请求参数
	 * @date 2014年5月23日
	 * @param properties
	 */
	public void setProperties(Map<String, String> properties)
	{
		this.asyncTask.setProperties(properties);
	}
	
	/**
	 * 添加Headers请求参数
	 * @param key
	 * @param value
	 */
	public void addProperty(String key, String value)
	{
		this.asyncTask.addProperty(key, value);
	}
	
	/**
	 * 添加请求参数
	 * @param key
	 * @param value
	 */
	public void addRequestParam(String key, String value)
	{
		this.asyncTask.addRequestParam(key, value);
	}
	
	/**
	 * 设置请求参数（会将已添加的参数替换）
	 * @param params
	 */
	public void setRequestParams(Map<String, String> params)
	{
		this.asyncTask.setRequestParams(params);
	}
	
	/**
	 * 请求结果的回调
	 * 
	 * @date 2014-6-10
	 */
	public static abstract class TGRequestHandler<T>
	{
		/**
		 * 启动任务时回调
		 */
		public void onRequestStart()
		{
			
		}
		
		/**
		 * 请求成功时回调
		 * @param result 请求结果
		 */
		public abstract void onRequestSuccess(T result, TGHttpResult httpResult);
		
		/**
		 * 请求出现异常时回调
		 * @param code 错误码
		 * @param message 异常信息
		 */
		public abstract void onRequestError(int code, String message, TGHttpResult httpResult);
		
		public void onReturnCachedResult(T result, TGHttpResult httpResult)
		{
			
		}
		
		public void onRequestOver()
		{
			
		}
	}

	/**
	 * 执行异步任务的内部类
	 */
	private class InternalAsyncTask extends TGHttpAsyncTask<TGHttpResult>
	{
		public InternalAsyncTask(String requestUrl, int requestType, 
				TGHttpAsyncRequester.TGRequestHandler<T> listener)
		{
			super(requestUrl,requestType, listener);
			//设置解析结果类名
			setParserClsName(TGHttpAsyncRequester.this.getClass().getName());
		}
	}
}
