package com.mn.tiger.request.async;

import org.json.JSONObject;

import com.mn.tiger.request.receiver.TGHttpResult;
import com.mn.tiger.task.result.TGTaskResult;
import com.mn.tiger.task.result.TGTaskResultHandler;
import com.mn.tiger.utility.LogTools;

/**
 * 该类作用及功能说明
 * Http结果接收类
 * @date 2014年3月18日
 */
public class TGHttpResultHandler extends TGTaskResultHandler
{
	@Override
	public void handleTaskResult(TGTaskResult result)
	{
		TGHttpResult httpResult = (TGHttpResult)result.getResult();

		try
		{
			httpResult.setJSONResult(new JSONObject(httpResult.getResult()));
		}
		catch (Exception e)
		{
			LogTools.d(LOG_TAG, e.getMessage(), e);
		}
		
		if(hasError(httpResult))
		{
			onError(httpResult);
		}
		else
		{
			onSuccess(httpResult);
		}
		
		onRequestOver();
	}
	
	/**
	 * 该方法的作用:
	 * 处理Http结果
	 * @date 2014年3月18日
	 * @param httResult
	 */
	protected void onSuccess(TGHttpResult httpResult)
	{
		
	}
	
	protected void onError(TGHttpResult httpResult)
	{
		
	}
	
	protected void onReturnCachedResult(TGHttpResult httpResult)
	{
		
	}
	
	protected void onRequestOver()
	{
		
	}
	
	/**
	 * 该方法的作用:
	 * 处理Http异常
	 * @date 2014年3月18日
	 * @param result
	 * @return
	 */
	protected boolean hasError(TGHttpResult result)
	{
		return false;
	}
}
