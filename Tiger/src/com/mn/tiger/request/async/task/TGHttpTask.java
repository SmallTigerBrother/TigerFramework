package com.mn.tiger.request.async.task;

import java.util.HashMap;

import android.os.Bundle;
import android.text.TextUtils;

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
	/**
	 * 参数名--url
	 */
	public static final String PARAM_URL = "url";
	
	/**
	 * 参数名--params
	 */
	public static final String PARAM_PARAMS = "params";
	
	/**
	 * 参数名--properties
	 */
	public static final String PARAM_PROPERTIES = "properties";
	
	/**
	 * 参数名--parserClsName
	 */
	public static final String PARAM_PARSERCLSNAME = "parserClsName";
	
	/**
	 * 参数名--resultClsName
	 */
	public static final String PARAM_RESLUTCLSNAME = "resultClsName";

	
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
		String resultClsName = getResultClsName();
		if(!TextUtils.isEmpty(parserClsName) && !TextUtils.isEmpty(resultClsName))
		{
			try
			{
				IRequestParser parser = (IRequestParser) Class.forName(parserClsName).newInstance();
				httpResult.setObjectResult(parser.parseRequestResult(httpResult, resultClsName));
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
			//发送执行结果
			super.sendTaskResult(result);
			//设置任务执行状态
			setTaskState(MPTaskState.FINISHED);
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
		return httpParams.get(PARAM_PARAMS);
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
		return httpParams.getString(PARAM_URL);
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
		return (HashMap<String, String>)httpParams.get(PARAM_PROPERTIES);
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
		return httpParams.getString(PARAM_PARSERCLSNAME);
	}
	
	/**
	 * 该方法的作用:
	 * 获取解析类的名字
	 * @date 2014年8月15日
	 * @return
	 */
	public String getResultClsName()
	{
		Bundle httpParams = (Bundle)getParams();
		return httpParams.getString(PARAM_RESLUTCLSNAME);
	}
}
