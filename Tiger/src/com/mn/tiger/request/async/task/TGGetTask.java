package com.mn.tiger.request.async.task;

import com.mn.tiger.request.TGHttpRequest;
import com.mn.tiger.request.receiver.TGHttpResult;

/**
 * 该类作用及功能说明
 * Get请求任务类
 * @date 2014年3月18日
 */
public class TGGetTask extends TGHttpTask
{
	@Override
	protected TGHttpResult executeHttpRequest()
	{
		return TGHttpRequest.requestGet(getContext(), getRequestUrl(), 
				getRequestParams(), null, getRequestProperties());
	}
}
