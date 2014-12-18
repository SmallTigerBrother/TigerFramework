package com.mn.tiger.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;

/**
 * QQ空间分享插件
 */
public class TGQQZoneSharePlugin extends TGQQSharePlugin
{
	/**
	 * 使用分享功能的界面
	 */
	private Activity activity;
	
	public TGQQZoneSharePlugin(Context context, String appID)
	{
		super(context, appID);
	}
	
	@Override
	protected void sendShareMsg(Activity activity, Bundle shareMsg)
	{
		LOG.d("[Method:sendShareMsg]" + shareMsg.toString());
		
		setActivity(activity);
		//QQ分享必须到指定的Activity执行，因此在这里启动TGQQZoneEntryActivity，在TGQQZoneEntryActivity中会调用share2QQ()方法执行分享功能
		Intent intent = new Intent(getContext(), TGQQZoneEntryActivity.class);
		getContext().startActivity(intent);
	}
	
	/**
	 * 分享到QQ空间
	 */
	@Override
	public void share2QQ()
	{
		LOG.d("[Method:share2QQZone]");
		
		getTencent().shareToQzone(activity, getShareMsg(), null);
		//清空actvity，避免内存泄露
		setActivity(null);
	}
	
	/**
	 * QQZone分享信息建造者
	 */
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
			//设置显示分享到QQZone
			params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
			return params;
		}
	}
}
