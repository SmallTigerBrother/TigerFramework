package com.mn.tiger.request.async;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.mn.tiger.request.TGHttpLoader;
import com.mn.tiger.request.TGHttpLoader.OnLoadCallback;
import com.mn.tiger.request.async.task.TGDeleteTask;
import com.mn.tiger.request.async.task.TGGetTask;
import com.mn.tiger.request.async.task.TGHttpTask;
import com.mn.tiger.request.async.task.TGPostTask;
import com.mn.tiger.request.async.task.TGPutTask;
import com.mn.tiger.request.error.TGHttpErrorHandler;
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
public class TGHttpAsyncTask
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
	private int requestType = TGHttpLoader.REQUEST_GET;
	
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
	private OnLoadCallback loadCallback;
	
	/**
	 * @param context
	 * @param requestUrl
	 * @param requestType
	 * @param callback
	 */
	@SuppressWarnings("rawtypes")
	public TGHttpAsyncTask(String requestUrl, int requestType, 
			OnLoadCallback callback) 
	{
		this.requestUrl = requestUrl;
		this.requestType = requestType;
		this.loadCallback = callback;
		
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
		
		if(null != context)
		{
			onPreExecute();
			
			doInBackground(params);
		}
	}
	
	/**
	 * 请求之前执行方法（MainThread）
	 */
	protected void onPreExecute()
	{
		if(null != loadCallback)
		{
			loadCallback.onLoadStart();
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
		if(requestType > TGHttpLoader.REQUEST_PUT || 
				requestType < TGHttpLoader.REQUEST_POST)
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
				getTaskClsName(requestType), initHttpResultHandler());
		taskParams.setTaskType(TGTask.TASK_TYPE_HTTP);
		
		return taskParams;
	}
	
	protected final TGHttpResultHandler initHttpResultHandler()
	{
		TGHttpResultHandler resultHandler = new TGHttpResultHandler(context)
		{
			@SuppressWarnings("unchecked")
			@Override
			protected void onSuccess(TGHttpResult httpResult)
			{
				LogTools.i(LOG_TAG, "[Method:onSuccess]");
				//解析请求结果
				if(!isCancelled() && null != loadCallback)
				{
					loadCallback.onLoadSuccess(httpResult.getObjectResult(), httpResult);
				}
			}
			
			@Override
			protected void onError(TGHttpResult httpResult) 
			{
				LogTools.i(LOG_TAG, "[Method:onError]");
				//解析请求结果
				if(!isCancelled() && null != loadCallback)
				{
					loadCallback.onLoadError(httpResult.getResponseCode(),
							httpResult.getResult(), httpResult);
				}
			}
			
			@Override
			@SuppressWarnings("unchecked")
			protected void onReturnCachedResult(TGHttpResult httpResult)
			{
				LogTools.i(LOG_TAG, "[Method:onReturnCachedResult]");
				//解析请求结果
				if(!isCancelled() && null != loadCallback)
				{
					loadCallback.onLoadCache(httpResult.getObjectResult(), httpResult);
				}
			}
			
			@Override
			protected void onRequestOver() 
			{
				if(null != loadCallback)
				{
					loadCallback.onLoadOver();
				}
			}
			
			@Override
			protected boolean hasError(TGHttpResult result)
			{
				return TGHttpAsyncTask.this.hasError(result);
			}
		};
		
		return resultHandler;
	}
	
	/**
	 * 判断网络请求结果是否有异常
	 * @param httpResult
	 * @return
	 */
	protected boolean hasError(TGHttpResult httpResult)
	{
		return TGHttpErrorHandler.hasHttpError(httpResult);
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
				case TGHttpLoader.REQUEST_GET:
					taskClsName = TGGetTask.class.getName();
					
					break;
				case TGHttpLoader.REQUEST_POST:
					taskClsName = TGPostTask.class.getName();
					
					break;
				case TGHttpLoader.REQUEST_PUT:
					taskClsName = TGPutTask.class.getName();
					
					break;
				case TGHttpLoader.REQUEST_DELETE:
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
		if(null != loadCallback)
		{
			this.loadCallback.onLoadOver();
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
	 * @param callback
	 */
	@SuppressWarnings("rawtypes")
	public void setLoadCallback(OnLoadCallback callback)
	{
		this.loadCallback = callback;
	}
}
