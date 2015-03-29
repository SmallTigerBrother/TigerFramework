package com.mn.tiger.request.sync;

import java.util.Map;

import android.content.Context;

import com.mn.tiger.request.sync.receiver.TGHttpResult;

public interface ISyncHttpLoader
{
	TGHttpResult loadByGetSync(Context context, String requestUrl, 
			Object parameters, Map<String, String> properties);
	
	TGHttpResult loadByPostSync(Context context, String requestUrl, 
			Object parameters, Map<String, String> properties);
	
	TGHttpResult loadByPutSync(Context context, String requestUrl, 
			Object parameters, Map<String, String> properties);
	
	TGHttpResult loadByDeleteSync(Context context, String requestUrl, 
			Object parameters, Map<String, String> properties);
	
}
