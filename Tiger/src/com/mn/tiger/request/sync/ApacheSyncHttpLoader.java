package com.mn.tiger.request.sync;

import java.util.Map;

import android.content.Context;

import com.mn.tiger.request.sync.receiver.TGHttpResult;

public class ApacheSyncHttpLoader implements ISyncHttpLoader
{
	@Override
	public TGHttpResult loadByGetSync(Context context, String requestUrl, Object parameters,
			Map<String, String> properties)
	{
		return null;
	}

	@Override
	public TGHttpResult loadByPostSync(Context context, String requestUrl, Object parameters,
			Map<String, String> properties)
	{
		return null;
	}

	@Override
	public TGHttpResult loadByPutSync(Context context, String requestUrl, Object parameters,
			Map<String, String> properties)
	{
		return null;
	}

	@Override
	public TGHttpResult loadByDeleteSync(Context context, String requestUrl, Object parameters,
			Map<String, String> properties)
	{
		return null;
	}

}
