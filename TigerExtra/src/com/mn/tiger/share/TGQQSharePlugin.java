package com.mn.tiger.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mn.tiger.share.result.TGQQShareResult;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.Tencent;

/**
 * QQ分享插件
 */
public class TGQQSharePlugin extends TGSharePlugin<Bundle, TGQQShareResult>
{
	/**
	 * 默认的分享信息键值（QQ的分享结果不能区分多个信息）
	 */
	private static final String indicatorKey = "indicator";
	
	/**
	 * Tecent分享类
	 */
	private Tencent tencent;
	
	/**
	 * 使用分享功能的界面
	 */
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
		LOG.d("[Method:sendShareMsg]" + shareMsg.toString());
		
		setActivity(activity);
		//QQ分享必须到指定的Activity执行，因此在这里启动TGQQEntryActivity，在TGQQEntryActivity中会调用share2QQ()方法执行分享功能
		Intent intent = new Intent(getContext(), TGQQEntryActivity.class);
		getContext().startActivity(intent);
	}
	
	/**
	 * 分享到QQ
	 */
	public void share2QQ()
	{
		LOG.d("[Method:share2QQ]");
		
		if(null != getShareMsg())
		{
			tencent.shareToQQ(activity, getShareMsg(), null);
		}
		//清空actvity，避免内存泄露
		setActivity(null);
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
	
	/**
	 * 获取Tencent类
	 * @return
	 */
	protected Tencent getTencent()
	{
		return tencent;
	}
	
	protected void setActivity(Activity activity)
	{
		this.activity = activity;
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
	
	@Override
	protected void onRemoveResultHandler(TGQQShareResult result)
	{
		//QQ不支持多任务，不移除handler
	}

	/**
	 * QQ分享信息建造者
	 * @author Dalang
	 *
	 */
	public static class TGQQShareMsgBuilder extends TGShareMsgBuilder<Bundle>
	{
		/**
		 * 分享数据
		 */
		private Bundle params;
		
		public TGQQShareMsgBuilder(int shareType)
		{
			super(shareType);
			params = new Bundle();
		}

		@Override
		public Bundle build()
		{
			//设置不显示分享到QQZone
			params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
			return params;
		}
		
		public void setQQKeyType(int qqKeyType)
		{
			params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, qqKeyType);
		}
		
		public void setQQTargetUrl(String targetUrl)
		{
			params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
		}
		
		public void setQQTitle(String title)
		{
			params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
		}
		
		public void setQQSummary(String summary)
		{
			params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
		}
		
		public void setQQImageUrl(String imageUrl)
		{
			params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
		}
		
		public void setQQAppName(String appName)
		{
			params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName);
		}
	}
}
