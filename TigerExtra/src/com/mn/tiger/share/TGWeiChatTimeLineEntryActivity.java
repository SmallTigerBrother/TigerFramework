package com.mn.tiger.share;

import com.tencent.mm.sdk.openapi.IWXAPI;

/**
 * 微信朋友圈分享回调Activity
 */
public class TGWeiChatTimeLineEntryActivity extends TGWeiChatEntryActivity
{
	@Override
	protected IWXAPI getIWXAPI()
	{
		TGWeiChatSharePlugin plugin = (TGWeiChatSharePlugin) TGSharePluginManager.getInstance().getPlugin(
				TGSharePluginManager.TAG_WEI_CHAT_TIME_LINE);
		return plugin.getIWXApi();
	}
}
