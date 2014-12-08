package com.mn.tiger.demo.share.plugin;

import android.content.Context;

import com.mn.tiger.share.TGWeiChatTimeLineSharePlugin;
import com.mn.tiger.share.result.TGWeiChatShareResult;

public class WeiChatTimeLineSharePlugin extends TGWeiChatTimeLineSharePlugin
{
	public WeiChatTimeLineSharePlugin(Context context, String appID)
	{
		super(context, appID);
	}
	
	@Override
	public void onShareSuccess(TGWeiChatShareResult result)
	{
		super.onShareSuccess(result);
		//TODO 添加数据统计代码，或其他与界面无关的操作
	}

	@Override
	public void onShareFailed(TGWeiChatShareResult result)
	{
		super.onShareFailed(result);
		//TODO 添加数据统计代码，或其他与界面无关的操作
	}
	
	@Override
	public void onShareOver(TGWeiChatShareResult result)
	{
		super.onShareOver(result);
		//TODO 添加数据统计代码，或其他与界面无关的操作
	}
	
}
