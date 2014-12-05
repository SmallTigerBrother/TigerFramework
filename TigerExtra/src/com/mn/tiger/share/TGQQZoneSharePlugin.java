package com.mn.tiger.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;

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
	
	public static class TGQQZoneShareMsgBuilder extends TGQQShareMsgBuilder
	{
		public TGQQZoneShareMsgBuilder(int shareType)
		{
			super(shareType);
		}

		@Override
		public Bundle build()
		{
			Bundle params = super.build();
			params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
			return params;
		}
	}
}
