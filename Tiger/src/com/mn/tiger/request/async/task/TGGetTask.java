package com.mn.tiger.request.async.task;

import com.mn.tiger.request.TGHttpLoader;
import com.mn.tiger.request.receiver.TGHttpResult;

/**
 * 该类作用及功能说明
 * Get请求任务类
 * @date 2014年3月18日
 */
public class TGGetTask extends TGHttpTask
{
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected TGHttpResult executeHttpRequest()
	{
		return new TGHttpLoader().loadByGetSync(getContext(), getRequestUrl(), 
				getRequestParams(), getRequestProperties());
	}
}
