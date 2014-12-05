package com.mn.tiger.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mn.tiger.share.result.TGQQShareResult;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.Tencent;

public class TGQQSharePlugin extends TGSharePlugin<Bundle, TGQQShareResult>
{
	private static final String indicatorKey = "indicator";
	
	private Tencent tencent;
	
	private Activity activity;
	
	public TGQQSharePlugin(Context context, String appID)
	{
		super(context, appID);
	}
	
	@Override
	protected void registerApp()
	{
		tencent = Tencent.createInstance(getAppID(), getContext());
	}
	
	@Override
	protected void sendShareMsg(Activity activity, Bundle shareMsg)
	{
		this.activity = activity;
		Intent intent = new Intent(activity, TGQQEntryActivity.class);
		activity.startActivity(intent);
	}
	
	public void share2QQ()
	{
		if(null != getShareMsg())
		{
			tencent.shareToQQ(activity, getShareMsg(), null);
		}
		//清空actvity，避免内存泄露
		this.activity = null;
	}

	@Override
	protected String getMsgIndicator(Bundle shareMsg)
	{
		return indicatorKey;
	}

	@Override
	protected String getMsgIndicator(TGQQShareResult shareResult)
	{
		return indicatorKey;
	}
	
	protected Tencent getTencent()
	{
		return tencent;
	}

	@Override
	public void onShareSuccess(TGQQShareResult result)
	{
		
	}

	@Override
	public void onShareFailed(TGQQShareResult result)
	{
		
	}

	@Override
	public void onShareOver(TGQQShareResult result)
	{
		
	}

	public static class TGQQShareMsgBuilder extends TGShareMsgBuilder<Bundle>
	{
		private Bundle params;
		
		public TGQQShareMsgBuilder(int shareType)
		{
			super(shareType);
			params = new Bundle();
		}

		@Override
		public Bundle build()
		{
			params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
			return params;
		}
		
		public void setQQKeyType(int qqKeyType)
		{
			params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, qqKeyType);
		}
		
		public void setQQTargetUrl(int targetUrl)
		{
			params.putInt(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
		}
		
		public void setQQTitle(int title)
		{
			params.putInt(QQShare.SHARE_TO_QQ_TITLE, title);
		}
		
		public void setQQSummary(int summary)
		{
			params.putInt(QQShare.SHARE_TO_QQ_SUMMARY, summary);
		}
		
		public void setQQImageUrl(int imageUrl)
		{
			params.putInt(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
		}
		
		public void setQQAppName(int appName)
		{
			params.putInt(QQShare.SHARE_TO_QQ_APP_NAME, appName);
		}
	}
}
