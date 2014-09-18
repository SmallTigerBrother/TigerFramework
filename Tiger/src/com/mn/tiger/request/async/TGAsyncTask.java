package com.mn.tiger.request.async;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.mn.tiger.request.async.task.TGDeleteTask;
import com.mn.tiger.request.async.task.TGGetTask;
import com.mn.tiger.request.async.task.TGPostTask;
import com.mn.tiger.request.async.task.TGPutTask;
import com.mn.tiger.request.error.IHttpErrorHandler;
import com.mn.tiger.request.receiver.TGHttpResult;
import com.mn.tiger.task.TGTask;
import com.mn.tiger.task.TGTaskManager;
import com.mn.tiger.task.invoke.TGTaskParams;
import com.mn.tiger.utility.LogTools;

/**
 * 该类作用及功能说明
 * MJet框架异步请求基类
 * @author l00220455
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2014年2月10日
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public class TGAsyncTask<Result>
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
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
	 * 回调控制器
	 */
	private Handler handler;
	
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
	private int requestType = REQUEST_GET;
	
	/**
	 * 设置message传递的数据
	 */
	private int messageWhat = 0;
	
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
	 * 请求结果
	 */
	private Result result;
	
	/**
	 * 执行任务类名
	 */
	private String taskClsName = null;
	
	private TGHttpResultHandler resultHandler = new TGHttpResultHandler()
	{
		@Override
		protected void handleHttpResult(TGHttpResult httpResult)
		{
			LogTools.i(LOG_TAG, "[Method:handleHttpResult]");
			//解析请求结果
			result = parseRequestResult(httpResult, httpResult.getJSONResult());
			onPostExecute(result);
		}
	};
	
	/**
	 * @author l00220455
	 * @date 2013年12月8日
	 * 构造函数
	 * @param context
	 * @param requestUrl
	 * @param errorInterFace
	 * @param handler
	 * @param requestType
	 */
	public TGAsyncTask(Context context, String requestUrl, 
			IHttpErrorHandler errorInterFace, Handler handler ,int requestType) 
	{
		this.context = context;
		this.requestUrl = requestUrl;
		setHttpErrorHandler(errorInterFace);
		this.handler = handler;
		this.requestType = requestType;
	}
	
	/**
	 * 该方法的作用:
	 * 执行任务
	 * @author l00220455
	 * @date 2014年8月22日
	 * @param params
	 */
	public void execute(final Object... params)
	{
		doInBackground(params);
	}
	
	/**
	 * 该方法的作用:
	 * 后台执行
	 * @author l00220455
	 * @date 2014年8月22日
	 * @param params
	 * @return
	 */
	protected TGHttpResult doInBackground(Object... params) 
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
	 * @author l00220455
	 * @date 2014年8月22日
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected TGTaskParams initHttpParams(Object... params)
	{
		if(getRequestType() > REQUEST_PUT || getRequestType() < REQUEST_POST)
		{
			throw new RuntimeException("Your requestType is invalid!");
		}
		
		// 设置请求参数
		Bundle data = new Bundle();
		data.putString("url", getRequestUrl());
		data.putSerializable("properties", (HashMap<String, String>)getProperties());
		if(null != params && params.length > 0)
		{
    		if(params[0] instanceof HashMap<?, ?>)
    		{
    			data.putSerializable("params", (HashMap<String, String>) params[0]);
    		}
    		else if(params[0] instanceof String)
    		{
    			data.putString("params", (String) params[0]);
    		}
		}
		data.putString("parserClsName", "");
		
		TGTaskParams taskParams = TGTaskManager.createTaskParams(data, getTaskClsName(getRequestType()), getResultHandler());
		taskParams.setTaskType(TGTask.TASK_TYPE_HTTP);
		
		return taskParams;
	}
	
	/**
	 * 该方法的作用:
	 * 获取执行任务的类名
	 * @author l00220455
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
				case REQUEST_GET:
					taskClsName = TGGetTask.class.getName();
					
					break;
				case REQUEST_POST:
					taskClsName = TGPostTask.class.getName();
					
					break;
				case REQUEST_PUT:
					taskClsName = TGPutTask.class.getName();
					
					break;
				case REQUEST_DELETE:
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
	 * @author l00220455
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
	 * @author l00220455
	 * @date 2014年8月22日
	 * @param cancelIfRunning
	 */
	public void cancel(boolean cancelIfRunning)
	{
		this.isCancel = true;
		TGTaskManager.getInstance(context).cancelTask(taskID, TGTask.TASK_TYPE_HTTP);
	}
	
	/**
	 * 该方法的作用:
	 * 打印进度
	 * @author l00220455
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
	 * @author l00220455
	 * @date 2014年8月22日
	 * @param value
	 */
	protected void onProgressUpdate(Integer value) 
	{
	}
	
	/**
	 * 该方法的作用:
	 * 任务执行后的回调方法
	 * @author l00220455
	 * @date 2014年8月22日
	 * @param result
	 */
	protected void onPostExecute(Result result)
	{
		LogTools.p(LOG_TAG, "[Method:onPostExecute]" + "-->requestOver");
		
		if(!isCancelled() && result != null)
		{
			//通知界面更新
			sendMessage(result);
		}
	}

	/**
	 * 该方法的作用: 解析请求结果JSON
	 * @author l00220455
	 * @date 2012-12-25
	 * @param jsonResult  请求结果JSON
	 * @return 请求结果
	 */
	protected Result parseRequestResult(TGHttpResult httpResult, JSONObject jsonResult)
	{
		return null;
	}
	
	/**
	 * 该方法的作用:
	 * 发送通知消息
	 * @author l00220455
	 * @date 2013-6-4
	 */
	protected void sendMessage(Result result)
	{
		if(null != handler)
		{
			Message message = new Message();
			message.what = messageWhat;
			message.obj = this.result;
			
			this.handler.sendMessage(message);
		}
	}
	
	/**
	 * 该方法的作用:
	 * 设置请求Url
	 * @author l00220455
	 * @date 2013年12月8日
	 * @param requestUrl
	 */
	public void setRequestUrl(String requestUrl) 
	{
		this.requestUrl = requestUrl;
	}

	/**
	 * 该方法的作用:
	 * 获取请求Url
	 * @author l00220455
	 * @date 2013年12月8日
	 * @return
	 */
	public String getRequestUrl() 
	{
		return requestUrl;
	}

	/**
	 * 该方法的作用:
	 * 获取错误处理接口
	 * @author l00220455
	 * @date 2014年1月15日
	 * @param httpErrorHandler
	 */
	public void setHttpErrorHandler(IHttpErrorHandler httpErrorHandler)
	{
		getResultHandler().setHttpErrorHandler(httpErrorHandler);
	}
	
	/**
	 * 该方法的作用:
	 * 设置请求类型
	 * @author l00220455
	 * @date 2013年12月8日
	 * @param requestType
	 */
	public void setRequestType(int requestType) 
	{
		this.requestType = requestType;
	}

	/**
	 * 该方法的作用:
	 * 获取网络请求类型
	 * @author l00220455
	 * @date 2013年12月8日
	 * @return
	 */
	public int getRequestType() 
	{
		return requestType;
	}

	/**
	 * 该方法的作用:
	 * 设置更新Handler
	 * @author l00220455
	 * @date 2013年12月8日
	 * @param handler
	 */
	public void setHandler(Handler handler)
	{
		this.handler = handler;
	}

	/**
	 * 该方法的作用:
	 * 获取Handler
	 * @author l00220455
	 * @date 2013年12月8日
	 * @return
	 */
	protected Handler getHandler()
	{
		return handler;
	}
	
	/**
	 * 该方法的作用:
	 * 获取请求结果
	 * @author l00220455
	 * @date 2013年12月8日
	 * @return
	 */
	public Result getRequestResult() 
	{
		return result;
	}
	
	/**
	 * 该方法的作用:
	 * 设置message的What值
	 * @author l00220455
	 * @date 2013年12月8日
	 * @param messageWhat
	 */
	public void setMessageWhat(int messageWhat) 
	{
		this.messageWhat = messageWhat;
	}
	
	/**
	 * 该方法的作用:
	 * 获取Message的What值
	 * @author l00220455
	 * @date 2014年2月10日
	 * @return
	 */
	protected int getMessageWhat()
	{
		return messageWhat;
	}

	public boolean isCancelled()
	{
		return isCancel;
	}
	
	/**
	 * 该方法的作用:
	 * 获取结果回调类
	 * @author l00220455
	 * @date 2014年8月22日
	 * @return
	 */
	public TGHttpResultHandler getResultHandler()
	{
		return resultHandler;
	}

	/**
	 * 该方法的作用:
	 * 设置结果回调类
	 * @author l00220455
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
	 * @author pWX197040
	 * @date 2014年5月23日
	 * @param properties
	 */
	public void setProperties(Map<String, String> properties)
	{
		this.properties = new HashMap<String, String>();
		this.properties.putAll(properties);
	}
	
	/**
	 * 该方法的作用:
	 * 获取消息头properites
	 * @author l00220455
	 * @date 2014年8月22日
	 * @return
	 */
	public HashMap<String, String> getProperties()
	{
		return properties;
	}
}
