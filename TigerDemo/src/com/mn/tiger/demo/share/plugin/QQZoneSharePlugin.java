package com.mn.tiger.demo.share.plugin;

import android.content.Context;

import com.mn.tiger.share.TGQQZoneSharePlugin;
import com.mn.tiger.share.result.TGQQShareResult;

public class QQZoneSharePlugin extends TGQQZoneSharePlugin
{
	public QQZoneSharePlugin(Context context, String appID)
	{
		super(context, appID);
	}
	
	@Override
	public void onShareSuccess(TGQQShareResult result)
	{
		super.onShareSuccess(result);
		//TODO 添加数据统计代码，或其他与界面无关的操作
	}
	
	@Override
	public void onShareFailed(TGQQShareResult result)
	{
		super.onShareFailed(result);
		//TODO 添加数据统计代码，或其他与界面无关的操作
	}
	
	@Override
	public void onShareOver(TGQQShareResult result)
	{
		super.onShareOver(result);
		//TODO 添加数据统计代码，或其他与界面无关的操作
	}

}
