package com.mn.tiger.request.async.task;

import com.mn.tiger.request.TGHttpRequest;
import com.mn.tiger.request.receiver.TGHttpResult;

/**
 * 该类作用及功能说明
 * Put请求任务类
 * @date 2014年8月22日
 */
public class TGPutTask extends TGHttpTask
{
	protected TGHttpResult executeHttpRequest() 
	{
		return TGHttpRequest.requestPut(getContext(), getRequestUrl(), 
				getRequestParams(), null, getRequestProperties());
	}
}
