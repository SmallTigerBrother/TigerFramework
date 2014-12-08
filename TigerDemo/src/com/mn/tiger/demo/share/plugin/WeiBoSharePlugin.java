package com.mn.tiger.demo.share.plugin;

import android.content.Context;

import com.mn.tiger.share.TGWeiBoSharePlugin;
import com.mn.tiger.share.result.TGWeiboShareResult;

public class WeiBoSharePlugin extends TGWeiBoSharePlugin
{
	public WeiBoSharePlugin(Context context, String appID)
	{
		super(context, appID);
	}
	
	@Override
	public void onShareSuccess(TGWeiboShareResult result)
	{
		super.onShareSuccess(result);
		//TODO 添加数据统计代码，或其他与界面无关的操作
	}
	
	@Override
	public void onShareFailed(TGWeiboShareResult result)
	{
		super.onShareFailed(result);
		//TODO 添加数据统计代码，或其他与界面无关的操作
	}
	
	@Override
	public void onShareOver(TGWeiboShareResult result)
	{
		super.onShareOver(result);
		//TODO 添加数据统计代码，或其他与界面无关的操作
	}

}
