package com.mn.tiger.request.async;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.mn.tiger.request.TGHttpRequest;
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
public class TGHttpAsyncTask<Result>
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
	private int requestType = TGHttpRequest.REQUEST_GET;
	
	/**
	 * 网络请求参数
	 */
	private HashMap<String, String> properties = null;
	
	private boolean isCancel = false;
	
	/**
	 * 任务ID
	 */
	private int taskID = -1;
	
	/**
	 * 执行任务类名
	 */
	private String taskClsName = null;
	
	private String parserClsName = "";
	
	private String resultClsName = "";
	
	@SuppressWarnings("rawtypes")
	private TGHttpAsyncRequester.RequestListener listener;
	
	private TGHttpResultHandler resultHandler = new TGHttpResultHandler()
	{
		@SuppressWarnings("unchecked")
		@Override
		protected void onSuccess(TGHttpResult httpResult)
		{
			LogTools.i(LOG_TAG, "[Method:onSuccess]");
			//解析请求结果
			if(!isCancelled())
			{
				listener.onRequestSuccess(httpResult.getObjectResult());
			}
		}
		
		protected void onError(TGHttpResult httpResult) 
		{
			LogTools.i(LOG_TAG, "[Method:onError]");
			//解析请求结果
			if(!isCancelled())
			{
				listener.onRequestError(httpResult.getResponseCode(), httpResult.getResult());
			}
		}
	};
	
	/**
	 * @param context
	 * @param requestUrl
	 * @param requestType
	 * @param listener
	 */
	@SuppressWarnings("rawtypes")
	public TGHttpAsyncTask(Context context, String requestUrl, int requestType, 
			TGHttpAsyncRequester.RequestListener listener) 
	{
		this.context = context;
		this.requestUrl = requestUrl;
		this.requestType = requestType;
		this.listener = listener;
		
		properties = new HashMap<String, String>();
	}
	
	/**
	 * 该方法的作用:
	 * 执行任务
	 * @date 2014年8月22日
	 * @param params
	 */
	public void execute(final Object params)
	{
		if (context instanceof Activity && ((Activity) context).isFinishing())
		{
			return;
		}
		
		onPreExecute();
		
		doInBackground(params);
	}
	
	protected void onPreExecute()
	{
		if(null != listener)
		{
			listener.onRequestStart();
		}
	}
	
	/**
	 * 该方法的作用:
	 * 后台执行
	 * @date 2014年8月22日
	 * @param params
	 * @return
	 */
	protected TGHttpResult doInBackground(Object params) 
	{
		LogTools.p(LOG_TAG, "[Method: doInBackground]  " + "start request.");
		
		if(isCancelled())
		{
			return null;
		}
		
		TGTaskParams taskParams = initHttpParams(params);
		
		taskID = TGTaskManager.getInstance(context).startTask(taskParams);
		return null;
	}
	
	/**
	 * 该方法的作用:
	 * 初始化Http请求参数
	 * @date 2014年8月22日
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected TGTaskParams initHttpParams(Object params)
	{
		if(requestType > TGHttpRequest.REQUEST_PUT || 
				requestType < TGHttpRequest.REQUEST_POST)
		{
			throw new RuntimeException("Your requestType is invalid!");
		}
		
		// 设置请求参数
		Bundle data = new Bundle();
		data.putString(TGHttpTask.PARAM_URL, requestUrl);
		data.putSerializable(TGHttpTask.PARAM_PROPERTIES, (HashMap<String, String>)properties);
		if(null != params)
		{
    		if(params instanceof HashMap<?, ?>)
    		{
    			data.putSerializable(TGHttpTask.PARAM_PARAMS, (HashMap<String, String>) params);
    		}
    		else if(params instanceof String)
    		{
    			data.putString(TGHttpTask.PARAM_PARAMS, (String) params);
    		}
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
				case TGHttpRequest.REQUEST_GET:
					taskClsName = TGGetTask.class.getName();
					
					break;
				case TGHttpRequest.REQUEST_POST:
					taskClsName = TGPostTask.class.getName();
					
					break;
				case TGHttpRequest.REQUEST_PUT:
					taskClsName = TGPutTask.class.getName();
					
					break;
				case TGHttpRequest.REQUEST_DELETE:
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
		TGTaskManager.getInstance(context).cancelTask(taskID, TGTask.TASK_TYPE_HTTP);
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
	 * 该方法的作用: 批量设置请求参数
	 * @date 2014年5月23日
	 * @param properties
	 */
	public void setProperties(Map<String, String> properties)
	{
		this.properties.putAll(properties);
	}
	
	public void setParserClsName(String parserClsName)
	{
		this.parserClsName = parserClsName;
	}
	
	public void setResultClsName(String resultClsName)
	{
		this.resultClsName = resultClsName;
	}
	
	public void setRequestType(int requestType)
	{
		this.requestType = requestType;
	}
	
	public void setRequestUrl(String requestUrl)
	{
		this.requestUrl = requestUrl;
	}
	
	@SuppressWarnings("rawtypes")
	public void setListener(TGHttpAsyncRequester.RequestListener listener)
	{
		this.listener = listener;
	}
}
