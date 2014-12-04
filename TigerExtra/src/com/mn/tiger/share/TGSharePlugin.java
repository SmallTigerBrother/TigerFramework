package com.mn.tiger.share;

import android.content.Context;

import com.mn.tiger.share.result.TGWeiChatShareResult;
import com.tencent.mm.sdk.openapi.BaseResp;

/**
 * 分享插件
 * @param <T> 分享数据类型
 */
public abstract class TGSharePlugin<T>
{
	private TGShareMsgBuilder<T> msgBuilder;
	
	private T shareMsg;
	
	private Context context;
	
	public TGSharePlugin(Context context)
	{
		this.context = context;
	}
	
	public <E extends TGShareMsgBuilder<T>> void share(E msgBuilder)
	{
		sendShareMsg(getShareMsg());
	}
	
	protected abstract void sendShareMsg(T shareMsg);
	
	protected T getShareMsg()
	{
		if(null == shareMsg && null != msgBuilder)
		{
			shareMsg = msgBuilder.build();
		}
		
		return shareMsg;
	}
	
	public int getShareType()
	{
		if(null != msgBuilder)
		{
			return msgBuilder.getShareType();
		}
		
		return -1;
	}
	
	/**
	 * 分享结束
	 * @param result 分享结果
	 */
	final void shareOver(TGWeiChatShareResult result)
	{
		if(result.getResultCode() == BaseResp.ErrCode.ERR_OK)
		{
			onShareSuccess(result);
		}
		else
		{
			onShareFailed(result);
		}
		
		onShareOver();
	}
	
	/**
	 * 分享成功回调（不可添加操作界面相关的代码）
	 * @param result 分享结果
	 */
	public abstract void onShareSuccess(TGWeiChatShareResult result);
	
	/**
	 * 分享失败回调（不可添加操作界面相关的代码）
	 * @param result 分享结果
	 */
	public abstract void onShareFailed(TGWeiChatShareResult result);
	
	/**
	 * 分享完成，不论分享成功或失败都会回调
	 */
	public abstract void onShareOver();
	
	public Context getContext()
	{
		return context;
	}
	
	public TGShareMsgBuilder<T> getMsgBuilder()
	{
		return msgBuilder;
	}
	
	public static abstract class TGShareMsgBuilder<T>
	{
		/**
		 * 分享类型
		 */
		private int shareType;
		
		/**
		 * 分享信息建造者
		 * @param shareType 分享类型，为确保相同分享渠道的不同分享信息之间无冲突，
		 *                  不同界面的相同分享方法，请定义不同的shareType
		 */
		public TGShareMsgBuilder(int shareType)
		{
			this.shareType = shareType;
		}
		
		public int getShareType()
		{
			return shareType;
		}
		
		public abstract T build();
	}
}
