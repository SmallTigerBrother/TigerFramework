package com.mn.tiger.request.async.task;

import com.mn.tiger.request.receiver.TGHttpResult;
import com.mn.tiger.request.sync.TGSyncHttpLoaderFactory;

/**
 * 该类作用及功能说明 Delete请求任务
 * 
 * @date 2014年8月22日
 */
public class TGDeleteTask extends TGHttpTask
{
	protected TGHttpResult executeHttpRequest()
	{
		return TGSyncHttpLoaderFactory.createSyncHttpLoader(getHttpImplementionType()).loadByDeleteSync(getContext(),
				getRequestUrl(), getRequestParams(), getRequestProperties());
	}
}
