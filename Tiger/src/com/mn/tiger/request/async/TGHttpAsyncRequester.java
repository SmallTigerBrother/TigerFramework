package com.mn.tiger.request.async;

import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.mn.tiger.core.parser.json.TGJsonUtils;
import com.mn.tiger.request.TGHttpRequest;
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
	
	private OnCancelListener cancelListener;
	
	/**
	 * 为反射提供的类
	 */
	@SuppressWarnings("unused")
	private TGHttpAsyncRequester()
	{
		
	}
	
	/**
	 * @param context
	 */
	public TGHttpAsyncRequester(Context context)
	{
		asyncTask = new InternalAsyncTask(context, "", 
				TGHttpRequest.REQUEST_UNKNOWN, null);
	}
	
	/**
	 * 执行get请求
	 * @param requestUrl 请求url
	 * @param resultClsName 解析结果类名
	 * @param params 请求参数
	 * @param listener 请求回调方法
	 */
	public void get(String requestUrl, String resultClsName, Object params, 
			RequestListener<T> listener)
	{
		execute(TGHttpRequest.REQUEST_GET, requestUrl, resultClsName, params, listener);
	}
	
	/**
	 * 执行post请求
	 * @param requestUrl 请求url
	 * @param resultClsName 解析结果类名
	 * @param params 请求参数
	 * @param listener 请求回调方法
	 */
	public void post( String requestUrl, String resultClsName, Object params, 
			RequestListener<T> listener)
	{
		execute(TGHttpRequest.REQUEST_POST, requestUrl, resultClsName, params, listener);
	}
	
	/**
	 * 执行put请求
	 * @param requestUrl 请求url
	 * @param resultClsName 解析结果类名
	 * @param params 请求参数
	 * @param listener 请求回调方法
	 */
	public void put(String requestUrl, String resultClsName, Object params, 
			RequestListener<T> listener)
	{
		execute(TGHttpRequest.REQUEST_PUT, requestUrl, resultClsName, params, listener);
	}
	
	/**
	 * 执行delete请求
	 * @param requestUrl 请求url
	 * @param resultClsName 解析结果类名
	 * @param params 请求参数
	 * @param listener 请求回调方法
	 */
	public void delete(String requestUrl, String resultClsName, Object params, 
			RequestListener<T> listener)
	{
		execute(TGHttpRequest.REQUEST_DELETE, requestUrl, resultClsName, params, listener);
	}
	
	/**
	 * 执行异步任务
	 * @param requestType 请求类型
	 * @param requestUrl 请求url
	 * @param resultClsName 解析结果类名
	 * @param params 请求参数
	 * @param listener 请求回调方法
	 */
	protected void execute(int requestType, String requestUrl, String resultClsName, 
			Object params, RequestListener<T> listener)
	{
		asyncTask.setRequestType(requestType);
		asyncTask.setRequestUrl(requestUrl);
		asyncTask.setResultClsName(resultClsName);
		asyncTask.setListener(listener);
		
		asyncTask.execute(params);
	}
	
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
		if(null != cancelListener)
		{
			cancelListener.onCancel();
		}
	}
	
	/**
	 * 该方法的作用: 批量设置请求参数
	 * @date 2014年5月23日
	 * @param properties
	 */
	public void setProperties(Map<String, String> properties)
	{
		this.asyncTask.setProperties(properties);
	}
	
	/**
	 * 设置任务取消监听器
	 * @param cancelListener
	 */
	public void setCancelListener(OnCancelListener cancelListener)
	{
		this.cancelListener = cancelListener;
	}

	/**
	 * 请求结果的回调
	 * 
	 * @date 2014-6-10
	 */
	public static interface RequestListener<T>
	{
		/**
		 * 开始请求
		 */
		public static final int MESSAGE_START = 0x000001;
		
		/**
		 * 请求成功
		 */
		public static final int MESSAGE_SUCCESS = 0x000010;
		
		/**
		 * 请求异常
		 */
		public static final int MESSAGE_ERROR = 0x000100;
		
		/**
		 * 启动任务时回调
		 */
		public void onStart();
		
		/**
		 * 请求成功时回调
		 * @param result 请求结果
		 */
		public void onSuccess(T result);
		
		/**
		 * 请求出现异常时回调
		 * @param code 错误码
		 * @param message 异常信息
		 */
		public void onError(int code, String message);
	}

	/**
	 * 任务异常的时候的回调
	 * 
	 * @date 2014-6-10
	 */
	public static interface OnCancelListener
	{
		public void onCancel();
	}

	/**
	 * 执行异步任务的内部类
	 */
	private class InternalAsyncTask extends TGHttpAsyncTask<TGHttpResult>
	{
		public InternalAsyncTask(Context context, String requestUrl, 
				int requestType, TGHttpAsyncRequester.RequestListener<T> listener)
		{
			super(context, requestUrl,requestType, listener);
			//设置解析结果类名
			setParserClsName(TGHttpAsyncRequester.this.getClass().getName());
		}
	}
}
