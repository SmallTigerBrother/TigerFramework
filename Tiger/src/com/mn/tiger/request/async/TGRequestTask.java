package com.mn.tiger.request.async;

import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.mn.tiger.core.parser.json.TGJsonUtils;
import com.mn.tiger.request.async.task.IRequestParser;
import com.mn.tiger.request.error.IHttpErrorHandler;
import com.mn.tiger.request.receiver.TGHttpResult;
import com.mn.tiger.task.invoke.TGTaskParams;
import com.mn.tiger.utility.StringUtils;

/**
 * 数据请求异步任务 <br />
 * 请求数据后可将数据解析成Class<T>类型的类<br />
 * 增加当出现errorinfo异常时的回调
 * 
 * @date 2014-6-9
 */
public class TGRequestTask implements IRequestParser
{
	private Class<?> mCls;

	private InternalAsyncTask asyncTask;
	
	@SuppressWarnings("unused")
	private TGRequestTask()
	{
		super();
	}

	public TGRequestTask(Context context, String requestUrl, RequestListener listener, Class<?> cls,
			int requestType, IHttpErrorHandler httpErrorHandler)
	{
		asyncTask = new InternalAsyncTask(context, requestUrl, httpErrorHandler, 
				new RequestHandler(listener), requestType);
		mCls = cls;
	}
	
	/**
	 * 该方法的作用:
	 * 执行任务
	 * @date 2014年8月23日
	 * @param params
	 */
	public void execute(Object... params)
	{
		asyncTask.execute(params);
	}
	
	@Override
	public Object parseRequestResult(TGHttpResult httpResult)
	{
		String jsonStr = (httpResult.getJSONResult() == null) ? null : httpResult.getJSONResult().toString();

		if (!StringUtils.isEmptyOrNull(jsonStr) && !"{}".equals(jsonStr))
		{
			return TGJsonUtils.parseJson2Object(jsonStr, mCls);
		}
		
		return null;
	}
	
	public void cancel()
	{
		asyncTask.cancel(true);
	}
	
	/**
	 * 该方法的作用:
	 * 设置message的What值
	 * @date 2013年12月8日
	 * @param messageWhat
	 */
	public void setMessageWhat(int messageWhat) 
	{
		this.asyncTask.setMessageWhat(messageWhat);
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
	
	public static class RequestHandler extends Handler
	{
		private RequestListener mListener;

		public RequestHandler(RequestListener listener)
		{
			mListener = listener;
		}

		@Override
		public void handleMessage(Message msg)
		{
			mListener.onRequestResult((TGHttpResult) msg.obj, msg.what);
		}
	}
	
	/**
	 * 请求结果的回调
	 * 
	 * @date 2014-6-10
	 */
	public static interface RequestListener
	{
		public void onRequestResult(TGHttpResult httpResult, int what);
	}

	/**
	 * 任务异常的时候的回调
	 * 
	 * @date 2014-6-10
	 */
	public static interface OnTaskCancelListener
	{
		public void onTaskCancel();
	}

	private class InternalAsyncTask extends TGAsyncTask<TGHttpResult>
	{
		public InternalAsyncTask(Context context, String requestUrl,
				IHttpErrorHandler errorInterFace, Handler handler, int requestType)
		{
			super(context, requestUrl, errorInterFace, handler, requestType);
		}
		
		@Override
		protected TGHttpResult parseRequestResult(TGHttpResult httpResult, JSONObject jsonResult)
		{
			return httpResult;
		}
		
		public void execute(Object... params)
		{
			Context context = getContext();

			if (context instanceof Activity && ((Activity) context).isFinishing())
			{
				return;
			}

			super.execute(params);
		}
		
		@Override
		protected TGTaskParams initHttpParams(Object... params)
		{
			TGTaskParams taskParams = super.initHttpParams(params);
			//增加解析器方法
			((Bundle)taskParams.getParams()).putString("parserClsName", TGRequestTask.this.getClass().getName());
			
			return taskParams;
		}
	}
}
