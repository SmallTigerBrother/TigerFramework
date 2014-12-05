package com.mn.tiger.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class TGQQZoneSharePlugin extends TGQQSharePlugin
{
	private Activity activity;
	
	public TGQQZoneSharePlugin(Context context, String appID)
	{
		super(context, appID);
	}
	
	@Override
	protected void sendShareMsg(Activity activity, Bundle shareMsg)
	{
		this.activity = activity;
		Intent intent = new Intent(activity, TGQQZoneEntryActivity.class);
		activity.startActivity(intent);
	}
	
	@Override
	public void share2QQ()
	{
		getTencent().shareToQzone(activity, getShareMsg(), null);
		//清空actvity，避免内存泄露
		this.activity = null;
	}
}
