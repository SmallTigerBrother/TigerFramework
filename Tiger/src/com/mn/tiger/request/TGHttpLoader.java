package com.mn.tiger.request;

import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.mn.tiger.log.LogTools;
import com.mn.tiger.request.async.TGHttpAsyncTask;
import com.mn.tiger.request.async.task.IRequestParser;
import com.mn.tiger.request.sync.HttpImplementionType;
import com.mn.tiger.request.sync.receiver.TGHttpResult;

/**
 * Http请求类（包含异步、同步方法）
 */
public class TGHttpLoader<T> implements IRequestParser
{
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 未知请求类型
	 */
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
	 * 执行异步任务的类
	 */
	private TGHttpAsyncTask<T> asyncTask;
	
	public TGHttpLoader()
	{
	}
	
	/**
	 * 执行get请求
	 * @param requestUrl 请求url
	 * @param clazz 解析结果类名
	 * @param params 请求参数
	 * @param callback 请求回调方法
	 */
	public void loadByGet(Context context, String requestUrl, Class<T> clazz, 
			OnLoadCallback<T> callback)
	{
		execute(context, REQUEST_GET, requestUrl, clazz.getName(), callback);
	}
	
	/**
	 * 执行post请求
	 * @param requestUrl 请求url
	 * @param resultClsName 解析结果类名
	 * @param params 请求参数
	 * @param callback 请求回调方法
	 */
	public void loadByPost(Context context, String requestUrl, Class<T> clazz,
			OnLoadCallback<T> callback)
	{
		execute(context, REQUEST_POST, requestUrl, clazz.getName(), callback);
	}
	
	/**
	 * 执行put请求
	 * @param requestUrl 请求url
	 * @param resultClsName 解析结果类名
	 * @param params 请求参数
	 * @param callback 请求回调方法
	 */
	public void loadByPut(Context context, String requestUrl, Class<T> clazz,
			OnLoadCallback<T> callback)
	{
		execute(context, REQUEST_PUT, requestUrl, clazz.getName(), callback);
	}
	
	/**
	 * 执行delete请求
	 * @param requestUrl 请求url
	 * @param resultClsName 解析结果类名
	 * @param params 请求参数
	 * @param callback 请求回调方法
	 */
	public void loadByDelete(Context context, String requestUrl, Class<T> clazz,
			OnLoadCallback<T> callback)
	{
		execute(context, REQUEST_DELETE, requestUrl, clazz.getName(),  callback);
	}
	
	/**
	 * 执行异步任务
	 * @param requestType 请求类型
	 * @param requestUrl 请求url
	 * @param resultClsName 解析结果类名
	 * @param params 请求参数
	 * @param callback 请求回调方法
	 */
	protected void execute(Context context, int requestType, String requestUrl,
			String resultClsName,  OnLoadCallback<T> callback)
	{
		getAsyncTask().setContext(context);
		getAsyncTask().setRequestType(requestType);
		getAsyncTask().setRequestUrl(requestUrl);
		getAsyncTask().setResultClsName(resultClsName);
		getAsyncTask().setLoadCallback(callback);
		getAsyncTask().setParserClsName(TGHttpLoader.this.getClass().getName());
		
		getAsyncTask().execute();
	}
	
	/**
	 * 解析请求结果
	 */
	@Override
	public Object parseRequestResult(TGHttpResult httpResult, String resultClsName)
	{
		//判断解析结果的className是否为Void
		if(!TextUtils.isEmpty(resultClsName) && !resultClsName.equals(Void.class.getName()))
		{
			String jsonStr = httpResult.getResult();
			//判断结果是否为空
			if (!TextUtils.isEmpty(jsonStr))
			{
				try
				{
					Gson gson = new Gson();
					return gson.fromJson(jsonStr, Class.forName(resultClsName));
				}
				catch (ClassNotFoundException e)
				{
					LogTools.e(LOG_TAG, e.getMessage(), e);
				}
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
	 * @param key 参数键
	 * @param value 参数值
	 */
	public void addRequestParam(String key, String value)
	{
		this.getAsyncTask().addRequestParam(key, value);
	}
	
	/**
	 * 添加请求参数
	 * @param key 参数键
	 * @param filePath 文件路径
	 */
	public void addFileParam(String key, String filePath)
	{
		this.getAsyncTask().addRequestParam(key, filePath);
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
	 * 设置http请求实现方式
	 * @param httpImplementationType
	 */
	public void setHttpImplementationType(HttpImplementionType httpImplementationType)
	{
		this.getAsyncTask().setHttpImplementationType(httpImplementationType);
	}
	
	/**
	 * 获取异步任务
	 * @return
	 */
	protected final TGHttpAsyncTask<T> getAsyncTask()
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
	protected TGHttpAsyncTask<T> initAsyncTask()
	{
		return new TGHttpAsyncTask<T>("", REQUEST_UNKNOWN, null);
	}
	
	/**
	 * 请求结果的回调
	 * 
	 * @date 2014-6-10
	 */
	public static interface OnLoadCallback<T>
	{
		/**
		 * 启动加载时回调
		 */
		public void onLoadStart();
		
		/**
		 * 请求成功时回调
		 * @param result 请求结果
		 */
		void onLoadSuccess(T result, TGHttpResult httpResult);
		
		/**
		 * 请求出现异常时回调
		 * @param code 错误码
		 * @param message 异常信息
		 */
		void onLoadError(int code, String message, TGHttpResult httpResult);
		
		/**
		 * 加载缓存中数据
		 * @param result
		 * @param httpResult
		 */
		void onLoadCache(T result, TGHttpResult httpResult);
		
		/**
		 * 加载结束
		 */
		void onLoadOver();
	}
}
