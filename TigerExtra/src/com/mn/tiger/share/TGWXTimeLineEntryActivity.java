package com.mn.tiger.share;

import com.tencent.mm.sdk.openapi.IWXAPI;

public class TGWXTimeLineEntryActivity extends TGWXEntryActivity
{
	@Override
	protected IWXAPI getIWXAPI()
	{
		TGWeiChatSharePlugin plugin = (TGWeiChatSharePlugin) TGSharePluginManager.getInstance().getPlugin(
				TGSharePluginManager.TAG_WEI_CHAT_TIME_LINE);
		return plugin.getIWXApi();
	}
}
