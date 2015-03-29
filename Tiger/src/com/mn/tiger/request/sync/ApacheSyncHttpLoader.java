package com.mn.tiger.request.sync;

import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.mn.tiger.request.sync.apache.ApacheGetMethod;
import com.mn.tiger.request.sync.apache.ApacheHttpClient;
import com.mn.tiger.request.sync.apache.ApachePostMethod;
import com.mn.tiger.request.sync.method.TGHttpParams;
import com.mn.tiger.request.sync.receiver.TGHttpResult;

public class ApacheSyncHttpLoader extends AbstractSyncHttpLoader
{
	@Override
	public TGHttpResult loadByGetSync(Context context, String requestUrl, TGHttpParams parameters,
			Map<String, String> properties)
	{
		if (TextUtils.isEmpty(requestUrl))
		{
			return getHttpResultWhileUrlIsNULL(context);
		}
		
		ApacheHttpClient httpClient = new ApacheHttpClient(context);
		return httpClient.execute(new ApacheGetMethod());
	}

	@Override
	public TGHttpResult loadByPostSync(Context context, String requestUrl, TGHttpParams parameters,
			Map<String, String> properties)
	{
		if (TextUtils.isEmpty(requestUrl))
		{
			return getHttpResultWhileUrlIsNULL(context);
		}
		
		ApacheHttpClient httpClient = new ApacheHttpClient(context);
		return httpClient.execute(new ApachePostMethod());
	}

	@Override
	public TGHttpResult loadByPutSync(Context context, String requestUrl, TGHttpParams parameters,
			Map<String, String> properties)
	{
		if (TextUtils.isEmpty(requestUrl))
		{
			return getHttpResultWhileUrlIsNULL(context);
		}
		return null;
	}

	@Override
	public TGHttpResult loadByDeleteSync(Context context, String requestUrl, TGHttpParams parameters,
			Map<String, String> properties)
	{
		if (TextUtils.isEmpty(requestUrl))
		{
			return getHttpResultWhileUrlIsNULL(context);
		}
		return null;
	}

}
