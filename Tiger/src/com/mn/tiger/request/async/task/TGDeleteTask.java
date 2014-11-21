package com.mn.tiger.request.async.task;

import com.mn.tiger.request.TGHttpLoader;
import com.mn.tiger.request.receiver.TGHttpResult;

/**
 * 该类作用及功能说明
 * Delete请求任务
 * @date 2014年8月22日
 */
public class TGDeleteTask extends TGHttpTask
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected TGHttpResult executeHttpRequest() 
	{
		return new TGHttpLoader().loadByDeleteSync(getContext(), getRequestUrl(), 
				getRequestParams(), getRequestProperties());
	}
}
