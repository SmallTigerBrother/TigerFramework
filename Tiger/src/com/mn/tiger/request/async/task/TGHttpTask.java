package com.mn.tiger.request.async.task;

import java.util.HashMap;

import android.os.Bundle;
import android.text.TextUtils;

import com.mn.tiger.request.error.TGHttpErrorHandler;
import com.mn.tiger.request.receiver.TGHttpResult;
import com.mn.tiger.task.TGTask;
import com.mn.tiger.utility.LogTools;

/**
 * 该类作用及功能说明 Http请求任务类
 * 
 * @date 2014年3月18日
 */
public abstract class TGHttpTask extends TGTask
{
	@Override
	protected MPTaskState executeOnSubThread()
	{
		// 执行网络访问（不带异常处理）；异常处理下放到Activity中执行,
		TGHttpResult result = executeHttpRequest();
		if (getTaskState() == MPTaskState.RUNNING)
		{
			// 返回网络访问结果
			sendTaskResult(parseRequestResult(result));
		}

		return getTaskState();
	}

	/**
	 * 该方法的作用: 执行Http请求
	 * 
	 * @date 2014年3月18日
	 * @return
	 */
	protected abstract TGHttpResult executeHttpRequest();
	
	/**
	 * 该方法的作用:
	 * 解析请求结果
	 * @date 2014年8月22日
	 * @param httpResult
	 * @return
	 */
	protected TGHttpResult parseRequestResult(TGHttpResult httpResult)
	{
		String parserClsName = getParserClsName();
		if(!TextUtils.isEmpty(parserClsName))
		{
			try
			{
				IRequestParser parser = (IRequestParser) Class.forName(parserClsName).newInstance();
				httpResult.setObjectResult(parser.parseRequestResult(httpResult));
			}
			catch (Exception e)
			{
				LogTools.e(LOG_TAG, e.getMessage(), e);
			}
		}
		
		return httpResult;
	}

	@Override
	protected void sendTaskResult(Object result)
	{
		if(getTaskState() == MPTaskState.RUNNING)
		{
			LogTools.d(LOG_TAG, "[Method:sendTaskResult]");
			if(!TGHttpErrorHandler.checkLoginError((TGHttpResult) result))
			{
				//设置执行结果
				super.sendTaskResult(result);
				setTaskState(MPTaskState.FINISHED);
			}
			else
			{
				setTaskError(TaskError.LOCK_DISPATER_CODE, "");
				setTaskState(MPTaskState.ERROR);
			}
		}
	}
	
	/**
	 * 该方法的作用:
	 * 获取请求参数
	 * @date 2014年8月15日
	 * @return
	 */
	public Object getRequestParams()
	{
		Bundle httpParams = (Bundle)getParams();
		return httpParams.get("params");
	}
	
	/**
	 * 该方法的作用:
	 * 获取请求Url
	 * @date 2014年8月15日
	 * @return
	 */
	public String getRequestUrl()
	{
		Bundle httpParams = (Bundle)getParams();
		return httpParams.getString("url");
	}
	
	/**
	 * 该方法的作用:
	 * 获取请求header中的Properties
	 * @date 2014年8月15日
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getRequestProperties()
	{
		Bundle httpParams = (Bundle)getParams();
		return (HashMap<String, String>)httpParams.get("properties");
	}
	
	/**
	 * 该方法的作用:
	 * 获取解析类的名字
	 * @date 2014年8月15日
	 * @return
	 */
	public String getParserClsName()
	{
		Bundle httpParams = (Bundle)getParams();
		return httpParams.getString("parserClsName");
	}
}
