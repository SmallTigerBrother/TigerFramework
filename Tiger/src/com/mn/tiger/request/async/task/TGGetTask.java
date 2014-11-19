package com.mn.tiger.request.async.task;

import java.net.HttpURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import com.mn.tiger.request.TGHttpRequester;
import com.mn.tiger.request.receiver.TGHttpResult;

/**
 * 该类作用及功能说明
 * Get请求任务类
 * @date 2014年3月18日
 */
public class TGGetTask extends TGHttpTask
{
	@Override
	protected TGHttpResult executeHttpRequest()
	{
		TGHttpResult httpResult = new TGHttpResult();
		httpResult.setResponseCode(HttpURLConnection.HTTP_OK);
		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("Test", "AAA0");
			jsonObject.put("count", 120);
			httpResult.setResult(jsonObject.toString());
			
			return httpResult;
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		return httpResult;
		
//		return TGHttpRequester.requestGet(getContext(), getRequestUrl(), 
//				getRequestParams(), getRequestProperties());
	}
}
