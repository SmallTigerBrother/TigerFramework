package com.mn.tiger.request.async.task;

import com.mn.tiger.request.TGHttpRequest;
import com.mn.tiger.request.receiver.TGHttpResult;

/**
 * 该类作用及功能说明
 * Delete请求任务
 * @author l00220455
 * @date 2014年8月22日
 */
public class TGDeleteTask extends TGHttpTask
{
	protected TGHttpResult executeHttpRequest() 
	{
		return TGHttpRequest.requestDelete(getContext(), getRequestUrl(), 
				getRequestParams(), null, getRequestProperties());
	}
}
