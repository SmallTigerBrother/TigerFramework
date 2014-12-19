package com.mn.tiger.demo.share.plugin;

import android.content.Context;

import com.mn.tiger.share.TGWeChatSharePlugin;
import com.mn.tiger.share.result.TGWeChatShareResult;

public class WeiChatSharePlugin extends TGWeChatSharePlugin
{
	public WeiChatSharePlugin(Context context, String appID)
	{
		super(context, appID);
	}
	
	@Override
	public void onShareSuccess(TGWeChatShareResult result)
	{
		super.onShareSuccess(result);
		//TODO 添加数据统计代码，或其他与界面无关的操作
	}
	
	@Override
	public void onShareFailed(TGWeChatShareResult result)
	{
		super.onShareFailed(result);
		//TODO 添加数据统计代码，或其他与界面无关的操作
	}
	
	@Override
	public void onShareOver(TGWeChatShareResult result)
	{
		super.onShareOver(result);
		//TODO 添加数据统计代码，或其他与界面无关的操作
	}
	
	
}
