package com.mn.tiger.demo;

import com.mn.tiger.app.TGApplication;
import com.mn.tiger.demo.share.plugin.QQSharePlugin;
import com.mn.tiger.demo.share.plugin.QQZoneSharePlugin;
import com.mn.tiger.demo.share.plugin.WeiBoSharePlugin;
import com.mn.tiger.demo.share.plugin.WeiChatSharePlugin;
import com.mn.tiger.demo.share.plugin.WeiChatTimeLineSharePlugin;
import com.mn.tiger.share.TGSharePluginManager;

public class App extends TGApplication
{
	@Override
	protected void initSharePluginManager()
	{
		TGSharePluginManager.getInstance().registerPlugin(
				TGSharePluginManager.TAG_WEI_CHAT, new WeiChatSharePlugin(this, ""));
		
		TGSharePluginManager.getInstance().registerPlugin(
				TGSharePluginManager.TAG_WEI_CHAT_TIME_LINE, new WeiChatTimeLineSharePlugin(this, ""));
		
		TGSharePluginManager.getInstance().registerPlugin(
				TGSharePluginManager.TAG_QQ, new QQSharePlugin(this, "101009937"));
		
		TGSharePluginManager.getInstance().registerPlugin(
				TGSharePluginManager.TAG_QQ_ZONE, new QQZoneSharePlugin(this, "101009937"));
		
		TGSharePluginManager.getInstance().registerPlugin(
				TGSharePluginManager.TAG_WEI_BO, new WeiBoSharePlugin(this, ""));
	}
	
}
