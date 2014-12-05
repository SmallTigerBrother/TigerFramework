package com.mn.tiger.share;

import com.tencent.tauth.Tencent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class TGQQZoneSharePlugin extends TGQQSharePlugin
{
	public TGQQZoneSharePlugin(Context context, String appID)
	{
		super(context, appID);
	}
	
	@Override
	protected void sendShareMsg(Activity activity, Bundle shareMsg)
	{
		shareMsg.putInt(Tencent.SHARE_TO_QQ_EXT_INT, Tencent.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
		
		Tencent tencent = Tencent.createInstance(getAppID(), getContext());
		tencent.shareToQzone(activity, shareMsg, this);
	}
}
