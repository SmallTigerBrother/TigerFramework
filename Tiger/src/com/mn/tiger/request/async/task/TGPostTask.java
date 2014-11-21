package com.mn.tiger.request.async.task;

import com.mn.tiger.request.TGHttpLoader;
import com.mn.tiger.request.receiver.TGHttpResult;

/**
 * 该类作用及功能说明
 * Post请求任务
 * @date 2014年3月18日
 */
public class TGPostTask extends TGHttpTask
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected TGHttpResult executeHttpRequest()
	{
		return new TGHttpLoader().loadByPostSync(getContext(), getRequestUrl(), 
				getRequestParams(), getRequestProperties());
	}
}
