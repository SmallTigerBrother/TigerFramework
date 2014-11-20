package com.mn.tiger.request.async;

import android.app.Activity;
import android.content.Context;

import com.mn.tiger.request.error.TGHttpErrorHandler;
import com.mn.tiger.request.receiver.TGHttpResult;
import com.mn.tiger.task.result.TGTaskResult;
import com.mn.tiger.task.result.TGTaskResultHandler;

/**
 * 该类作用及功能说明
 * Http结果接收类
 * @date 2014年3月18日
 */
public abstract class TGHttpResultHandler extends TGTaskResultHandler
{
	private Context context;
	
	public TGHttpResultHandler(Context context)
	{
		this.context = context;
	}
	
	@Override
	public void handleTaskResult(TGTaskResult result)
	{
		if(null != context)
		{
			if(context instanceof Activity && ((Activity)context).isDestroyed() || 
					((Activity)context).isFinishing())
			{
				return;
			}
			
			TGHttpResult httpResult = (TGHttpResult)result.getResult();

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
		
	}
	
	/**
	 * 该方法的作用:
	 * 处理Http结果
	 * @date 2014年3月18日
	 * @param httResult
	 */
	protected abstract void onSuccess(TGHttpResult httpResult);
	
	protected abstract void onError(TGHttpResult httpResult);
	
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
		return TGHttpErrorHandler.hasHttpError(result);
	}
}
