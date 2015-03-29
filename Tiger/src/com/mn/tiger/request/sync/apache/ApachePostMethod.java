package com.mn.tiger.request.sync.apache;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import com.mn.tiger.request.sync.method.TGHttpParams;

public class ApachePostMethod extends ApacheHttpMethod
{
	@Override
	protected HttpUriRequest initHttpRequest(String url, TGHttpParams params)
	{
		HttpPost request = new HttpPost(url);
		request.setEntity(paramsToEntity(params));
		return request;
	}
	
	private HttpEntity paramsToEntity(TGHttpParams params)
	{
		HttpEntity entity = null;

		if (params != null)
		{
			entity = params.getEntity();
		}

		return entity;
	}
}
