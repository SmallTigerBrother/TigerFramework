package com.mn.tiger.share;

import android.content.Intent;
import android.os.Bundle;

import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.log.Logger;
import com.mn.tiger.share.result.TGWeiboShareResult;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;

public class TGWBEntryActivity extends TGActionBarActivity implements IWeiboHandler.Response
{
	private static final Logger LOG = Logger.getLogger(TGWBEntryActivity.class);
	
	private IWeiboShareAPI weiboShareAPI;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setVisible(false);
		
		weiboShareAPI = getWeiboShareAPI();
		
		if(null != weiboShareAPI)
		{
			weiboShareAPI.handleWeiboResponse(getIntent(), this);
		}
		else
		{
			LOG.e("Your had not register weibo shareplugin ever");
		}
	}
	
	protected IWeiboShareAPI getWeiboShareAPI()
	{
		TGWeiboSharePlugin plugin = (TGWeiboSharePlugin) TGSharePluginManager.getInstance().getPlugin(
				TGSharePluginManager.TAG_WEI_BO);
		return plugin.getWeiboShareAPI();
	}
	
	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		setIntent(intent);
		if(null != weiboShareAPI)
		{
			weiboShareAPI.handleWeiboResponse(getIntent(), this);
		}
		else
		{
			LOG.e("Your had not register weibo shareplugin ever");
		}
	}

	@Override
	public void onResponse(BaseResponse response)
	{
		TGSharePluginManager.getInstance().postShareResult(TGSharePluginManager.TAG_WEI_BO, 
				new TGWeiboShareResult(response));
	}
}
