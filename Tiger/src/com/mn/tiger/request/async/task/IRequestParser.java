package com.mn.tiger.request.async.task;

import com.mn.tiger.request.receiver.TGHttpResult;

public interface IRequestParser
{
	Object parseRequestResult(TGHttpResult httpResult, String resultClsName);
}
