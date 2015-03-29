package com.mn.tiger.request.async.task;

import com.mn.tiger.request.sync.TGSyncHttpLoaderFactory;
import com.mn.tiger.request.sync.receiver.TGHttpResult;

/**
 * 该类作用及功能说明
 * Post请求任务
 * @date 2014年3月18日
 */
public class TGPostTask extends TGHttpTask
{
	@Override
	protected TGHttpResult executeHttpRequest()
	{
		return TGSyncHttpLoaderFactory.createSyncHttpLoader(getHttpImplementionType()).loadByPostSync(getContext(), getRequestUrl(), 
				getRequestParams(), getRequestProperties());
	}
}
