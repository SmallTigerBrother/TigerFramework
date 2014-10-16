package com.mn.tiger.request.async;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.mn.tiger.request.TGHttpRequester;
import com.mn.tiger.request.async.task.TGDeleteTask;
import com.mn.tiger.request.async.task.TGGetTask;
import com.mn.tiger.request.async.task.TGHttpTask;
import com.mn.tiger.request.async.task.TGPostTask;
import com.mn.tiger.request.async.task.TGPutTask;
import com.mn.tiger.request.receiver.TGHttpResult;
import com.mn.tiger.task.TGTask;
import com.mn.tiger.task.TGTaskManager;
import com.mn.tiger.task.invoke.TGTaskParams;
import com.mn.tiger.utility.LogTools;

/**
 * 该类作用及功能说明
 * tiger框架异步请求基类
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2014年2月10日
 */
class TGHttpAsyncTask<Result>
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 运行环境
	 */
	private Context context;
	/**
	 * 请求URL
	 */
	private String requestUrl;
	
	/**
	 * 请求类型，默认为Post类型
	 */
	private int requestType = TGHttpRequester.REQUEST_GET;
	
	/**
	 * 网络请求headers参数
	 */
	private HashMap<String, String> properties = null;
	
	/**
	 * 网络请求参数
	 */
	private HashMap<String, String> params = null;
	
	/**
	 * 任务是否已取消
	 */
	private boolean isCancel = false;
	
	/**
	 * 任务ID
	 */
	private int taskID = -1;
	
	/**
	 * 执行任务类名
	 */
	private String taskClsName = null;
	
	/**
	 * 解析类的类名
	 */
	private String parserClsName = "";
	
	/**
	 * 结果类的类名
	 */
	private String resultClsName = "";
	
	/**
	 * 请求结果回调类
	 */
	@SuppressWarnings("rawtypes")
	private TGHttpAsyncRequester.TGRequestHandler requestHandler;
	
	/**
	 * http结果接收类
	 */
	private TGHttpResultHandler resultHandler = new TGHttpResultHandler()
	{
		@SuppressWarnings("unchecked")
		@Override
		protected void onSuccess(TGHttpResult httpResult)
		{
			LogTools.i(LOG_TAG, "[Method:onSuccess]");
			//解析请求结果
			if(!isCancelled() && null != requestHandler)
			{
				requestHandler.onRequestSuccess(httpResult.getObjectResult());
			}
		}
		
		protected void onError(TGHttpResult httpResult) 
		{
			LogTools.i(LOG_TAG, "[Method:onError]");
			//解析请求结果
			if(!isCancelled() && null != requestHandler)
			{
				requestHandler.onRequestError(httpResult.getResponseCode(), httpResult.getResult());
			}
		}
		
		@SuppressWarnings("unchecked")
		protected void onReturnCachedResult(TGHttpResult httpResult)
		{
			LogTools.i(LOG_TAG, "[Method:onReturnCachedResult]");
			//解析请求结果
			if(!isCancelled() && null != requestHandler)
			{
				requestHandler.onReturnCachedResult(httpResult.getObjectResult());
			}
		}
	};
	
	/**
	 * @param context
	 * @param requestUrl
	 * @param requestType
	 * @param handler
	 */
	@SuppressWarnings("rawtypes")
	public TGHttpAsyncTask(Context context, String requestUrl, int requestType, 
			TGHttpAsyncRequester.TGRequestHandler handler) 
	{
		this.context = context;
		this.requestUrl = requestUrl;
		this.requestType = requestType;
		this.requestHandler = handler;
		
		params = new HashMap<String, String>();
		properties = new HashMap<String, String>();
	}
	
	/**
	 * 该方法的作用:
	 * 执行任务
	 * @date 2014年8月22日
	 */
	public void execute()
	{
		if (context instanceof Activity && ((Activity) context).isFinishing())
		{
			return;
		}
		
		onPreExecute();
		
		doInBackground(params);
	}
	
	/**
	 * 请求之前执行方法（MainThread）
	 */
	protected void onPreExecute()
	{
		if(null != requestHandler)
		{
			requestHandler.onRequestStart();
		}
	}
	
	/**
	 * 该方法的作用:
	 * 后台执行
	 * @date 2014年8月22日
	 * @param params
	 * @return
	 */
	protected int doInBackground(HashMap<String, String> params) 
	{
		LogTools.p(LOG_TAG, "[Method: doInBackground]  " + "start request.");
		
		if(isCancelled())
		{
			return -1;
		}
		
		TGTaskParams taskParams = initHttpParams(params);
		
		taskID = TGTaskManager.getInstance().startTask(context, taskParams);
		return taskID;
	}
	
	/**
	 * 该方法的作用:
	 * 初始化Http请求参数（MainTread）
	 * @date 2014年8月22日
	 * @param params
	 * @return
	 */
	protected TGTaskParams initHttpParams(HashMap<String, String> params)
	{
		if(requestType > TGHttpRequester.REQUEST_PUT || 
				requestType < TGHttpRequester.REQUEST_POST)
		{
			throw new RuntimeException("Your requestType is invalid!");
		}
		
		// 设置请求参数
		Bundle data = new Bundle();
		data.putString(TGHttpTask.PARAM_URL, requestUrl);
		data.putSerializable(TGHttpTask.PARAM_PROPERTIES, (HashMap<String, String>)properties);
		if(null != params)
		{
			data.putSerializable(TGHttpTask.PARAM_PARAMS, (HashMap<String, String>) params);
		}
		
		data.putString(TGHttpTask.PARAM_PARSERCLSNAME, parserClsName);
		data.putString(TGHttpTask.PARAM_RESLUTCLSNAME, resultClsName);
		
		TGTaskParams taskParams = TGTaskManager.createTaskParams(data, 
				getTaskClsName(requestType), resultHandler);
		taskParams.setTaskType(TGTask.TASK_TYPE_HTTP);
		
		return taskParams;
	}
	
	/**
	 * 该方法的作用:
	 * 获取执行任务的类名
	 * @date 2014年8月22日
	 * @param requestType
	 * @return
	 */
	protected String getTaskClsName(int requestType)
	{
		if(TextUtils.isEmpty(taskClsName))
		{
			switch (requestType)
			{
				case TGHttpRequester.REQUEST_GET:
					taskClsName = TGGetTask.class.getName();
					
					break;
				case TGHttpRequester.REQUEST_POST:
					taskClsName = TGPostTask.class.getName();
					
					break;
				case TGHttpRequester.REQUEST_PUT:
					taskClsName = TGPutTask.class.getName();
					
					break;
				case TGHttpRequester.REQUEST_DELETE:
					taskClsName = TGDeleteTask.class.getName();
					
					break;
				default:
					taskClsName = "";
					
					break;
			}
		}
		
		return taskClsName;
	}
	
	/**
	 * 该方法的作用:
	 * 设置执行任务的类名
	 * @date 2014年8月22日
	 * @param taskClsName
	 */
	public void setTaskClsName(String taskClsName)
	{
		this.taskClsName = taskClsName;
	}
	
	/**
	 * 该方法的作用:
	 * 取消任务
	 * @date 2014年8月22日
	 */
	public void cancel()
	{
		this.isCancel = true;
		TGTaskManager.getInstance().cancelTask(taskID, TGTask.TASK_TYPE_HTTP);
		if(null != requestHandler)
		{
			this.requestHandler.onRequestCancel();
		}
	}
	
	/**
	 * 该方法的作用:
	 * 打印进度
	 * @date 2014年8月22日
	 * @param value
	 */
	public void publishProgress(Integer value)
	{
		onProgressUpdate(value);
	}
	
	/**
	 * 该方法的作用:
	 * 更新进度
	 * @date 2014年8月22日
	 * @param value
	 */
	protected void onProgressUpdate(Integer value) 
	{
	}

	/**
	 * 是否已取消
	 * @return
	 */
	public boolean isCancelled()
	{
		return isCancel;
	}
	
	/**
	 * 该方法的作用:
	 * 设置结果回调类
	 * @date 2014年8月22日
	 * @param resultHandler
	 */
	public void setResultHandler(TGHttpResultHandler resultHandler)
	{
		this.resultHandler = resultHandler;
	}
	
	public void setContext(Context context)
	{
		this.context = context;
	}
	
	public Context getContext()
	{
		return context;
	}
	
	/**
	 * 该方法的作用: 设置Headers请求参数(会清空原有参数)
	 * @date 2014年5月23日
	 * @param properties
	 */
	public void setProperties(Map<String, String> properties)
	{
		this.properties.clear();
		this.properties.putAll(properties);
	}
	
	/**
	 * 添加Headers请求参数
	 * @param key
	 * @param value
	 */
	public void addProperty(String key, String value)
	{
		this.properties.put(key, value);
	}
	
	/**
	 * 设置请求参数（会清空原有参数）
	 * @param params
	 */
	public void setRequestParams(Map<String, String> params)
	{
		this.params.clear();
		this.params.putAll(params);
	}
	
	/**
	 * 添加请求参数
	 * @param key
	 * @param value
	 */
	public void addRequestParam(String key, String value)
	{
		this.params.put(key, value);
	}
	
	/**
	 * 设置解析类的类名
	 * @param parserClsName
	 */
	public void setParserClsName(String parserClsName)
	{
		this.parserClsName = parserClsName;
	}
	
	/**
	 * 设置结果类的类名
	 * @param resultClsName
	 */
	public void setResultClsName(String resultClsName)
	{
		this.resultClsName = resultClsName;
	}
	
	/**
	 * 设置请求类型
	 * @param requestType
	 */
	public void setRequestType(int requestType)
	{
		this.requestType = requestType;
	}
	
	/**
	 * 设置请求Url
	 * @param requestUrl
	 */
	public void setRequestUrl(String requestUrl)
	{
		this.requestUrl = requestUrl;
	}
	
	/**
	 * 设置请求回调类
	 * @param handler
	 */
	@SuppressWarnings("rawtypes")
	public void setRequestHandler(TGHttpAsyncRequester.TGRequestHandler handler)
	{
		this.requestHandler = handler;
	}
}
