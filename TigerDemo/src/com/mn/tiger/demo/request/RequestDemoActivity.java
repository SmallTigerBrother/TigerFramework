package com.mn.tiger.demo.request;

import android.os.Bundle;

import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.log.LogTools;
import com.mn.tiger.request.sync.HttpImplementionType;
import com.mn.tiger.request.sync.receiver.TGHttpResult;

public class RequestDemoActivity extends TGActionBarActivity
{ 
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		HttpLoader<String> httpLoader = new HttpLoader<String>();
		httpLoader.setHttpImplementationType(HttpImplementionType.ApacheHttpClient);
		httpLoader.addRequestParam("storeId", "tewps");
		httpLoader.loadByPost(this, "http://apigouxie.d3.com.cn/shopping/static", String.class, 
				new SimpleLoadCallback<String>(this)
				{
					@Override
					public void onLoadSuccess(String result, TGHttpResult httpResult)
					{
						LogTools.d(httpResult.getResult());
					}
				});
	}
}
