package com.mn.tiger.request.async;

import org.json.JSONObject;

import com.mn.tiger.request.error.IHttpErrorHandler;
import com.mn.tiger.request.error.TGHttpErrorHandler;
import com.mn.tiger.request.receiver.TGHttpResult;
import com.mn.tiger.task.result.TGTaskResult;
import com.mn.tiger.task.result.TGTaskResultHandler;
import com.mn.tiger.utility.LogTools;

/**
 * 该类作用及功能说明
 * Http结果接收类
 * @author l00220455
 * @date 2014年3月18日
 */
public class TGHttpResultHandler extends TGTaskResultHandler
{
	/**
	 * Http错误处理类
	 */
	private IHttpErrorHandler httpErrorHandler;
	
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
		
		if (null != httpResult)
		{
			if(handleHttpError(httpResult))
			{
				httpResult.setJSONResult(new JSONObject());
			}
			handleHttpResult(httpResult);
		}
	}
	
	/**
	 * 该方法的作用:
	 * 处理Http结果
	 * @author l00220455
	 * @date 2014年3月18日
	 * @param httResult
	 */
	protected void handleHttpResult(TGHttpResult httpResult)
	{
		
	}
	
	/**
	 * 该方法的作用:
	 * 处理Http异常
	 * @author l00220455
	 * @date 2014年3月18日
	 * @param result
	 * @return
	 */
	protected boolean handleHttpError(TGHttpResult result)
	{
		if (null != httpErrorHandler && TGHttpErrorHandler.hasHttpError(result))
		{
			return httpErrorHandler.handleErrorInfo(result);
		}
		return false;
	}

	/**
	 * 该方法的作用:
	 * 获取Http异常处理接口
	 * @author l00220455
	 * @date 2014年3月18日
	 * @return
	 */
	public IHttpErrorHandler getHttpErrorHandler()
	{
		return httpErrorHandler;
	}

	/**
	 * 该方法的作用:
	 * 设置Http异常处理接口
	 * @author l00220455
	 * @date 2014年3月18日
	 * @param httpErrorHandler
	 */
	public void setHttpErrorHandler(IHttpErrorHandler httpErrorHandler)
	{
		this.httpErrorHandler = httpErrorHandler;
	}
}
