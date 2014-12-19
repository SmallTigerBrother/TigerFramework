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

/**
 *微信分享回调Activity
 */
public class TGWeiChatEntryActivity extends TGActionBarActivity implements IWXAPIEventHandler
{
	private static final Logger LOG = Logger.getLogger(TGWeiChatEntryActivity.class);
	
	private TGWeiChatSharePlugin sharePlugin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setVisible(false);
		
		initSharePlugin();
		if(null != sharePlugin)
		{
			sharePlugin.getIWXApi().handleIntent(getIntent(), this);
		}
	}
	
	private void initSharePlugin()
	{
		if(null != sharePlugin)
		{
			return;
		}
		
		sharePlugin = (TGWeiChatSharePlugin) TGSharePluginManager.getInstance().getPlugin(
				TGSharePluginManager.TAG_WEI_CHAT);
		if(null == sharePlugin)
		{
			sharePlugin = (TGWeiChatSharePlugin) TGSharePluginManager.getInstance().getPlugin(
					TGSharePluginManager.TAG_WEI_CHAT_TIME_LINE);
		}
		
		if(null == sharePlugin)
		{
			LOG.e("You had never register WeChatSharePlugin before");
		}
	}
	
	protected IWXAPI getIWXAPI()
	{
		TGWeiChatSharePlugin plugin = (TGWeiChatSharePlugin) TGSharePluginManager.getInstance().getPlugin(
				TGSharePluginManager.TAG_WEI_CHAT);
		return plugin.getIWXApi();
	}
	
	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		initSharePlugin();
		if(null != sharePlugin)
		{
			sharePlugin.getIWXApi().handleIntent(getIntent(), this);
		}
	}
	
	@Override
	public void onReq(BaseReq req)
	{
		TGWeiChatShareResult shareResult = new TGWeiChatShareResult(req);
		
		boolean postResult = TGSharePluginManager.getInstance().postShareResult(TGSharePluginManager.TAG_WEI_CHAT, 
				shareResult);
		if(!postResult)
		{
			TGSharePluginManager.getInstance().postShareResult(TGSharePluginManager.TAG_WEI_CHAT_TIME_LINE, 
					shareResult);
		}
	}

	@Override
	public void onResp(BaseResp req)
	{
		TGWeiChatShareResult shareResult = new TGWeiChatShareResult(req);
		
		boolean postResult = TGSharePluginManager.getInstance().postShareResult(TGSharePluginManager.TAG_WEI_CHAT, 
				shareResult);
		if(!postResult)
		{
			TGSharePluginManager.getInstance().postShareResult(TGSharePluginManager.TAG_WEI_CHAT_TIME_LINE, 
					shareResult);
		}
	}
}
