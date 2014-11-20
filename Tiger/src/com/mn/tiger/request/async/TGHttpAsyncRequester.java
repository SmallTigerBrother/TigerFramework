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
	private TGHttpAsyncTask asyncTask;
	
	public TGHttpAsyncRequester()
	{
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
			IRequestHandler<T> handler)
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
			IRequestHandler<T> handler)
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
			IRequestHandler<T> handler)
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
			IRequestHandler<T> handler)
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
			IRequestHandler<T> handler)
	{
		getAsyncTask().setContext(context);
		getAsyncTask().setRequestType(requestType);
		getAsyncTask().setRequestUrl(requestUrl);
		getAsyncTask().setResultClsName(resultClsName);
		getAsyncTask().setRequestHandler(handler);
		getAsyncTask().setParserClsName(TGHttpAsyncRequester.this.getClass().getName());
		
		getAsyncTask().execute();
	}
	
	/**
	 * 解析请求结果
	 */
	@Override
	public Object parseRequestResult(TGHttpResult httpResult, String resultClsName)
	{
		String jsonStr = httpResult.getResult();

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
		getAsyncTask().cancel();
	}
	
	/**
	 * 该方法的作用: 批量设置Headers请求参数
	 * @date 2014年5月23日
	 * @param properties
	 */
	public void setProperties(Map<String, String> properties)
	{
		this.getAsyncTask().setProperties(properties);
	}
	
	/**
	 * 添加Headers请求参数
	 * @param key
	 * @param value
	 */
	public void addProperty(String key, String value)
	{
		this.getAsyncTask().addProperty(key, value);
	}
	
	/**
	 * 添加请求参数
	 * @param key
	 * @param value
	 */
	public void addRequestParam(String key, String value)
	{
		this.getAsyncTask().addRequestParam(key, value);
	}
	
	/**
	 * 设置请求参数（会将已添加的参数替换）
	 * @param params
	 */
	public void setRequestParams(Map<String, String> params)
	{
		this.getAsyncTask().setRequestParams(params);
	}
	
	/**
	 * 获取异步任务
	 * @return
	 */
	protected final TGHttpAsyncTask getAsyncTask()
	{
		if(null == asyncTask)
		{
			asyncTask = initAsyncTask();
		}
		
		return asyncTask;
	}
	
	/**
	 * 初始化异步任务（可Override）
	 * @return
	 */
	protected TGHttpAsyncTask initAsyncTask()
	{
		return new TGHttpAsyncTask("", 
				TGHttpRequester.REQUEST_UNKNOWN, null);
	}
	
	/**
	 * 请求结果的回调
	 * 
	 * @date 2014-6-10
	 */
	public static interface IRequestHandler<T>
	{
		/**
		 * 启动任务时回调
		 */
		public void onRequestStart();
		
		/**
		 * 请求成功时回调
		 * @param result 请求结果
		 */
		void onRequestSuccess(T result, TGHttpResult httpResult);
		
		/**
		 * 请求出现异常时回调
		 * @param code 错误码
		 * @param message 异常信息
		 */
		void onRequestError(int code, String message, TGHttpResult httpResult);
		
		void onReturnCachedResult(T result, TGHttpResult httpResult);
		
		void onRequestOver();
	}
}
