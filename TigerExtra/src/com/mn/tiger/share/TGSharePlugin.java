package com.mn.tiger.share;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import android.app.Activity;
import android.content.Context;

import com.mn.tiger.share.result.TGShareResult;

/**
 * 分享插件
 * @param <T> 分享数据类型
 */
public abstract class TGSharePlugin<T, H extends TGShareResult>
{
	private TGShareMsgFactory<T> msgFactory;
	
	private T shareMsg;
	
	private Context context;
	
	private ConcurrentHashMap<String, IShareResultHandler<H>> resultHandlderMap;
	
	private ConcurrentHashMap<String, Integer> shareTypeMap;
	
	private String appID;
	
	public TGSharePlugin(Context context, String appID)
	{
		this.context = context;
		this.appID = appID;
		this.resultHandlderMap = new ConcurrentHashMap<String, IShareResultHandler<H>>();
		this.shareTypeMap = new ConcurrentHashMap<String, Integer>();
		
		registerApp();
	}
	
	protected abstract void registerApp();
	
	public final synchronized <E extends TGShareMsgFactory<T>> void share(Activity activity,
			E msgFactory, IShareResultHandler<H> handler)
	{
		shareTypeMap.put(getMsgIndicator(getShareMsg()), msgFactory.getShareType());
		resultHandlderMap.put(getMsgIndicator(getShareMsg()), handler);
		
		sendShareMsg(activity, getShareMsg());
	}
	
	protected abstract void sendShareMsg(Activity activity, T shareMsg);
	
	protected final T getShareMsg()
	{
		if(null == shareMsg && null != msgFactory)
		{
			shareMsg = msgFactory.createShareMsg();
		}
		
		return shareMsg;
	}
	
	protected abstract String getMsgIndicator(T shareMsg);
	
	protected abstract String getMsgIndicator(H shareResult);

	/**
	 * 接收分享结果
	 * @param result 分享结果
	 */
	protected final void handleShareResult(H result)
	{
		result.setShareType(shareTypeMap.get(getMsgIndicator(result)));
		IShareResultHandler<H> handler = resultHandlderMap.get(getMsgIndicator(result));
		if(null != handler)
		{
			handler.handleShareResult(result);
		}
		
		if(result.isSuccess())
		{
			onShareSuccess(result);
		}
		else
		{
			onShareFailed(result);
		}
		
		onShareOver(result);
	}
	
	/**
	 * 分享成功回调（不可添加操作界面相关的代码）
	 * @param result 分享结果
	 */
	public abstract void onShareSuccess(H result);
	
	/**
	 * 分享失败回调（不可添加操作界面相关的代码）
	 * @param result 分享结果
	 */
	public abstract void onShareFailed(H result);
	
	/**
	 * 分享完成，不论分享成功或失败都会回调
	 */
	public abstract void onShareOver(H result);
	
	public void removeResultHandler(IShareResultHandler<H> resultHandler)
	{
		Iterator<Entry<String, IShareResultHandler<H>>> iterator = 
				resultHandlderMap.entrySet().iterator();
		while (iterator.hasNext())
		{
			if(resultHandler.equals(iterator.next().getValue()))
			{
				iterator.remove();
				break;
			}
		}
	}
	
	public Context getContext()
	{
		return context;
	}
	
	public TGShareMsgFactory<T> getMsgFactory()
	{
		return msgFactory;
	}
	
	public String getAppID()
	{
		return appID;
	}
	
	public static abstract class TGShareMsgFactory<T>
	{
		/**
		 * 分享类型
		 */
		private int shareType;
		
		/**
		 * 分享信息工厂
		 * @param shareType 分享类型，为确保相同分享渠道的不同分享信息之间无冲突，
		 *                  不同界面的相同分享方法，请定义不同的shareType
		 */
		public TGShareMsgFactory(int shareType)
		{
			this.shareType = shareType;
		}
		
		public int getShareType()
		{
			return shareType;
		}
		
		public abstract T createShareMsg();
	}
}
