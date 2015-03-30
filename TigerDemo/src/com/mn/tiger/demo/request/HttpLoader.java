package com.mn.tiger.demo.request;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Build;

import com.google.gson.Gson;
import com.mn.tiger.log.LogTools;
import com.mn.tiger.request.TGHttpLoader;
import com.mn.tiger.request.async.TGHttpAsyncTask;
import com.mn.tiger.request.sync.receiver.TGHttpResult;
import com.mn.tiger.utility.PackageUtils;

/**
 * 网络请求类
 * @param <T>
 */
public class HttpLoader<T> extends TGHttpLoader<T>
{
	public HttpLoader()
	{
	}
	
	@Override
	protected void execute(Context context, int requestType, String requestUrl,
			String resultClsName, com.mn.tiger.request.TGHttpLoader.OnLoadCallback<T> callback)
	{
		//加入默认参数
		this.addRequestParam("platform", "android");
		this.addRequestParam("appVersion", PackageUtils.getPackageInfoByName(
				context, context.getPackageName()).versionName);
		this.addRequestParam("systemVersion", Build.VERSION.RELEASE);
		
		super.execute(context, requestType, requestUrl, resultClsName, callback);
	}
	
	@Override
	protected TGHttpAsyncTask<T> initAsyncTask()
	{
		return new InternalAsyncTask();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object parseRequestResult(TGHttpResult httpResult, String resultClsName)
	{
		//解析数据
		try
		{
			Response<T> response = new Response<T>();
			JSONObject jsonObject = new JSONObject(httpResult.getResult());
			if(jsonObject.has("result"))
			{
				response.result = jsonObject.getInt("result");
			}
			
			if(jsonObject.has("message"))
			{
				response.message = jsonObject.getString("message");
			}
		
			if(jsonObject.has("data"))
			{
				response.rawData = jsonObject.get("data").toString();
				Gson gson = new Gson();
				try
				{
					response.data = (T) gson.fromJson(jsonObject.get("data").toString(), 
							Class.forName(resultClsName));
				}
				catch (Exception e)
				{
					LogTools.e(LOG_TAG, e);
				}
			}
			
			return response;
		}
		catch (JSONException e)
		{
			LogTools.e(LOG_TAG, e);
		}
		
		return null;
	}
	
	/**
	 * 内部使用的异步任务类
	 */
	private class InternalAsyncTask extends TGHttpAsyncTask<T>
	{
		public InternalAsyncTask()
		{
			super("", TGHttpLoader.REQUEST_UNKNOWN, null);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected boolean hasError(TGHttpResult httpResult)
		{
			//检测自定义异常
			Response<T> response = (Response<T>) httpResult.getObjectResult();
			if(null != response)
			{
				return response.result == Response.RESULT_FAILED;
			}
			
			return true;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected T parseResult(Object originalResultObject)
		{
			//返回T
			if(null != originalResultObject)
			{
				return ((Response<T>)originalResultObject).data;
			}
			
			return null;
		}
	}
}
