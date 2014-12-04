package com.mn.tiger.share;

import android.content.Intent;
import android.os.Bundle;

import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.log.Logger;
import com.mn.tiger.share.result.TGWeiChatShareResult;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class TGWXEntryActivity extends TGActionBarActivity implements IWXAPIEventHandler
{
	private static final Logger LOG = Logger.getLogger(TGWXEntryActivity.class);
	
	private IWXAPI api;
	
	private TGWeiChatSharePlugin wxSharePlugin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setVisible(false);
		
		wxSharePlugin = getWeiChatSharePlugin();
		
		if(null != wxSharePlugin)
		{
			api = WXAPIFactory.createWXAPI(this, wxSharePlugin.getAppID());
			api.registerApp(wxSharePlugin.getAppID());
			api.handleIntent(getIntent(), this);
		}
		else
		{
			LOG.e("Your had not register weichat shareplugin ever");
		}
	}
	
	protected TGWeiChatSharePlugin getWeiChatSharePlugin()
	{
		return (TGWeiChatSharePlugin) TGSharePluginManager.getInstance().getPlugin(
				TGSharePluginManager.TAG_WEI_CHAT);
	}
	
	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		setIntent(intent);
		if(null != wxSharePlugin)
		{
			api.handleIntent(getIntent(), this);
		}
		else
		{
			LOG.e("Your had not register weichat shareplugin ever");
		}
	}
	
	@Override
	public void onReq(BaseReq req)
	{
		TGWeiChatShareResult shareResult = new TGWeiChatShareResult(req);
		TGSharePluginManager.getInstance().postShareResult(TGSharePluginManager.TAG_WEI_CHAT, 
				shareResult);
	}

	@Override
	public void onResp(BaseResp req)
	{
		TGSharePluginManager.getInstance().postShareResult(TGSharePluginManager.TAG_WEI_CHAT, 
				new TGWeiChatShareResult(req));
	}
}
